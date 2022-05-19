package com.groupsix.project3_cst438.repository;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.groupsix.project3_cst438.retrofit.RetrofitClient;
import com.groupsix.project3_cst438.retrofit.StoryLikesResponse;
import com.groupsix.project3_cst438.roomDB.AppDatabase;
import com.groupsix.project3_cst438.roomDB.DAO.StoryLikesDAO;
import com.groupsix.project3_cst438.roomDB.entities.StoryLikes;

import java.util.List;
import java.util.concurrent.Future;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StoryLikesRepository {
    public static StoryLikesRepository repoInstance;
    private AppDatabase mRoomDb;
    private StoryLikesDAO mStoryLikesDao;

    private RetrofitClient mRetrofitClient;

    private Observer<List<StoryLikesResponse>> storyLikesObserver;
    public MutableLiveData<StoryLikesResponse> storyLikesResponseMutableLiveData;
    public MutableLiveData<List<StoryLikesResponse>> storyLikesResponseListMutableLiveData;

    public StoryLikesRepository(Context context) {
        mRetrofitClient = RetrofitClient.getInstance(context);
        mRoomDb = AppDatabase.getInstance(context);
        mStoryLikesDao = mRoomDb.getStoryLikesDAO();

        storyLikesResponseMutableLiveData = new MutableLiveData<>();
        storyLikesResponseListMutableLiveData = new MutableLiveData<>();
    }

    public static StoryLikesRepository getRepoInstance(Context context) {
        if (repoInstance == null) {
            repoInstance = new StoryLikesRepository(context);
        }
        return repoInstance;
    }

    public LiveData<List<StoryLikes>> getAllLocalStoryLikesLiveData() { return mStoryLikesDao.getAll(); }

    // API response live data
    public LiveData<List<StoryLikesResponse>> getStoryLikesListResponseLiveData() { return storyLikesResponseListMutableLiveData; }
    public LiveData<StoryLikesResponse> getStoryLikesResponseLiveData() { return storyLikesResponseMutableLiveData; }

    public void updateRoomDBFromExternal() {
        getAllLikesEntry();

        storyLikesObserver = storyLikesResponses -> {
            for(StoryLikesResponse storyLikesResponse : storyLikesResponses) {
                insertLocalLikesEntry(storyLikesResponse.getStoryLikesObject());
            }
            getStoryLikesListResponseLiveData().removeObserver(storyLikesObserver);
        };

        getStoryLikesListResponseLiveData().observeForever(storyLikesObserver);
        System.out.println("Updated StoryLikes Table with REST API data");
    }

    // =========== ROOM DB OPERATIONS ============================

    public void insertLocalLikesEntry(StoryLikes storyLikes) {
        AppDatabase.databaseWriteExecutor.execute(() ->{
            mStoryLikesDao.insert(storyLikes);
            mStoryLikesDao = mRoomDb.getStoryLikesDAO();
        });
    }

    public void updateLocalLikesEntry(StoryLikes storyLikes) {
        AppDatabase.databaseWriteExecutor.execute(() ->{
            mStoryLikesDao.update(storyLikes);
            mStoryLikesDao = mRoomDb.getStoryLikesDAO();
        });
    }

    public void deleteLocalLikesEntry(StoryLikes storyLikes) {
        AppDatabase.databaseWriteExecutor.execute(() ->{
            mStoryLikesDao.delete(storyLikes);
            mStoryLikesDao = mRoomDb.getStoryLikesDAO();
        });
    }

    public StoryLikes getLocalLikesByStoryIdAndUserId(int storyId, int userId) {
        Future<StoryLikes> storyLikesFuture = AppDatabase.databaseWriteExecutor.submit(() -> mStoryLikesDao.getByStoryIdAndUserId(storyId, userId));
        StoryLikes storyLikes = null;

        try {
            storyLikes = storyLikesFuture.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return storyLikes;
    }

    // ========================== REST API OPERATIONS ===========================================

    public void insertLikesEntry(StoryLikes storyLikes) {
        mRetrofitClient.apiInterface.insertLikesEntry(storyLikes.getStoryId(), storyLikes.getUserId(), storyLikes.isLiked(), storyLikes.isDisliked()).enqueue(new Callback<StoryLikesResponse>() {
            @Override
            public void onResponse(@NonNull Call<StoryLikesResponse> call, @NonNull Response<StoryLikesResponse> response) {
                if (response.isSuccessful()) {
                    System.out.println("Story likes entry inserted using API");
                    storyLikesResponseMutableLiveData.postValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<StoryLikesResponse> call, @NonNull Throwable t) {
                storyLikesResponseMutableLiveData.postValue(null);
                System.out.println("Error" + t.getMessage());
            }
        });
    }

    public void getLikesByStoryIdAndUserId(StoryLikes storyLikes) {
        mRetrofitClient.apiInterface.getLikesByStoryIdAndUserId(storyLikes.getStoryId(), storyLikes.getUserId()).enqueue(new Callback<StoryLikesResponse>() {
            @Override
            public void onResponse(@NonNull Call<StoryLikesResponse> call, @NonNull Response<StoryLikesResponse> response) {
                if (response.isSuccessful()) {
                    System.out.println("Story likes retrieved");
                    storyLikesResponseMutableLiveData.postValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<StoryLikesResponse> call, @NonNull Throwable t) {
                storyLikesResponseMutableLiveData.postValue(null);
                System.out.println("Error" + t.getMessage());
            }
        });
    }

    public void getAllLikesEntry() {
        mRetrofitClient.apiInterface.getAllStoryLikes().enqueue(new Callback<List<StoryLikesResponse>>() {
            @Override
            public void onResponse(@NonNull Call<List<StoryLikesResponse>> call, @NonNull Response<List<StoryLikesResponse>> response) {
                if (response.isSuccessful()) {
                    System.out.println("Retrieved all story like entries");
                    storyLikesResponseListMutableLiveData.postValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<StoryLikesResponse>> call, @NonNull Throwable t) {
                storyLikesResponseListMutableLiveData.postValue(null);
                System.out.println("Error" + t.getMessage());
            }
        });
    }

    public void updateStoryLikesIsLiked(StoryLikes storyLikes) {
        mRetrofitClient.apiInterface.updateStoryIsLiked(storyLikes.getLikesId(), storyLikes.isLiked()).enqueue(new Callback<StoryLikesResponse>() {
            @Override
            public void onResponse(@NonNull Call<StoryLikesResponse> call, @NonNull Response<StoryLikesResponse> response) {
                if(response.isSuccessful()) {
                    System.out.println("Story is liked check updated!");
                    storyLikesResponseMutableLiveData.postValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<StoryLikesResponse> call, @NonNull Throwable t) {
                storyLikesResponseMutableLiveData.postValue(null);
                System.out.println("Error" + t.getMessage());
            }
        });
    }

    public void updateStoryLikesIsDisliked(StoryLikes storyLikes) {
        mRetrofitClient.apiInterface.updateStoryIsDisliked(storyLikes.getLikesId(), storyLikes.isDisliked()).enqueue(new Callback<StoryLikesResponse>() {
            @Override
            public void onResponse(@NonNull Call<StoryLikesResponse> call, @NonNull Response<StoryLikesResponse> response) {
                if(response.isSuccessful()) {
                    System.out.println("Story is disliked check updated!");
                    storyLikesResponseMutableLiveData.postValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<StoryLikesResponse> call, @NonNull Throwable t) {
                storyLikesResponseMutableLiveData.postValue(null);
                System.out.println("Error" + t.getMessage());
            }
        });
    }
}
