package com.groupsix.project3_cst438.repository;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.groupsix.project3_cst438.retrofit.RetrofitClient;
import com.groupsix.project3_cst438.retrofit.StoriesResponse;
import com.groupsix.project3_cst438.retrofit.UserResponse;
import com.groupsix.project3_cst438.roomDB.AppDatabase;
import com.groupsix.project3_cst438.roomDB.DAO.StoriesDAO;
import com.groupsix.project3_cst438.roomDB.DAO.UserDAO;
import com.groupsix.project3_cst438.roomDB.entities.Stories;

import java.util.List;
import java.util.concurrent.Future;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StoriesRepository {
    public static StoriesRepository repoInstance;
    private AppDatabase mRoomDb;
    private StoriesDAO mStoriesDao;

    private final RetrofitClient mRetrofitClient;

    public MutableLiveData<StoriesResponse> storiesResponseMutableLiveData;
    public MutableLiveData<List<StoriesResponse>> storiesListResponseMutableLiveData;

    public StoriesRepository(Context context) {
        mRetrofitClient = RetrofitClient.getInstance(context);
        mRoomDb = AppDatabase.getInstance(context);
        mStoriesDao = mRoomDb.getStoriesDAO();

        storiesResponseMutableLiveData = new MutableLiveData<>();
        storiesListResponseMutableLiveData = new MutableLiveData<>();
    }

    public static StoriesRepository getRepoInstance(Context context) {
        if (repoInstance == null) {
            repoInstance = new StoriesRepository(context);
        }
        return repoInstance;
    }

    // Livedata to check if stories table was changed in room database
    public LiveData<List<Stories>> getAllLocalStoriesLiveData() { return mStoriesDao.getAll(); }

    public LiveData<StoriesResponse> getStoriesResponseLiveData() { return storiesResponseMutableLiveData; }
    public LiveData<List<StoriesResponse>> getStoriesListResponseLiveData() { return storiesListResponseMutableLiveData; }

    public void insertLocalStories(Stories stories) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            mStoriesDao.insert(stories);
            mStoriesDao = mRoomDb.getStoriesDAO();
        });
    }

    public void updateLocalStories(Stories stories) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            mStoriesDao.update(stories);
            mStoriesDao = mRoomDb.getStoriesDAO();
        });
    }

    public void deleteLocalStories(Stories stories) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            mStoriesDao.delete(stories);
            mStoriesDao = mRoomDb.getStoriesDAO();
        });
    }

    public Stories getLocalStoriesById(int storiesId) {
        Future<Stories> storiesFuture = AppDatabase.databaseWriteExecutor.submit(() -> mStoriesDao.getStoriesById(storiesId));
        Stories stories = null;

        try {
            stories = storiesFuture.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stories;
    }

    public Stories getLocalStoriesByUserId(int userId) {
        Stories stories = null;
        Future<Stories> storiesFuture = AppDatabase.databaseWriteExecutor.submit(() -> mStoriesDao.getStoriesByUserId(userId));

        try {
            stories = storiesFuture.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stories;
    }

    // ======================= REST API OPERATIONS ===========

    public void insertStories(Stories stories) {
        mRetrofitClient.apiInterface.insertStories(stories.getUserId(), stories.getStory()).enqueue(new Callback<StoriesResponse>() {
            @Override
            public void onResponse(@NonNull Call<StoriesResponse> call, @NonNull Response<StoriesResponse> response) {
                if (response.isSuccessful()) {
                    System.out.println("Stories created successfully");
                    storiesResponseMutableLiveData.postValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<StoriesResponse> call, @NonNull Throwable t) {
                storiesResponseMutableLiveData.postValue(null);
                System.out.println("Error" + t.getMessage());
            }
        });
    }

    public void getStoriesByUserIdAndStory(int userId, String story) {
        mRetrofitClient.apiInterface.getStoriesByUserIdAndStory(userId, story).enqueue(new Callback<List<StoriesResponse>>() {
            @Override
            public void onResponse(@NonNull Call<List<StoriesResponse>> call, @NonNull Response<List<StoriesResponse>> response) {
                if(response.isSuccessful()) {
                    System.out.println("Stories retrieved by userId and story successfully");
                    storiesListResponseMutableLiveData.postValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<StoriesResponse>> call, @NonNull Throwable t) {
                storiesListResponseMutableLiveData.postValue(null);
                System.out.println("Error" + t.getMessage());
            }
        });
    }
}
