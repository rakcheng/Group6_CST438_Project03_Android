package com.groupsix.project3_cst438.repository;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.google.gson.Gson;
import com.groupsix.project3_cst438.retrofit.RetrofitClient;
import com.groupsix.project3_cst438.retrofit.StoryResponse;
import com.groupsix.project3_cst438.roomDB.AppDatabase;
import com.groupsix.project3_cst438.roomDB.DAO.StoryDAO;
import com.groupsix.project3_cst438.roomDB.entities.Story;

import java.util.List;
import java.util.concurrent.Future;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StoryRepository {
    private static StoryRepository repoInstance;
    private final AppDatabase mRoomDb;
    private StoryDAO mStoryDao;

    private final RetrofitClient mRetrofitClient;

    Observer<List<StoryResponse>> storyObserver;

    private StoryRepository(Context context) {
        mRetrofitClient = RetrofitClient.getInstance(context);
        mRoomDb = AppDatabase.getInstance(context);
        mStoryDao = mRoomDb.getStoryDAO();
        updateRoomDBFromExternal();
    }

    public static StoryRepository getRepoInstance(Context context) {
        if (repoInstance == null) {
            repoInstance = new StoryRepository(context);
        }
        return repoInstance;
    }

    // Livedata to check if story table was changed in room database
    public LiveData<List<Story>> getAllStoryLocal() { return mStoryDao.getAll(); }

    // Livedata to check response from API
    public LiveData<StoryResponse> getStoryResponse() { return mRetrofitClient.storyResponse; }
    public LiveData<List<StoryResponse>> getStoryListResponse() { return mRetrofitClient.storyResponseList; }
    public LiveData<StoryResponse> getStoryUpdatedResponse() { return mRetrofitClient.storyUpdatedResponse; }


    // USE THIS TO UPDATE FROM EXTERNAL API WHEN APP STARTS
    public void updateRoomDBFromExternal() {
        getAllStory();

        // Create an observer that when changed inserts story into room database
        storyObserver = storyResponseList -> {
            for (StoryResponse storyResponse : storyResponseList) {
                insertLocalStory(storyResponse.getStory());
            }
            getStoryListResponse().removeObserver(storyObserver);
        };

        getStoryListResponse().observeForever(storyObserver);
        System.out.println("Updated Story Table with REST API data");
    }

    public void insertLocalStory(Story story) {
        AppDatabase.databaseWriteExecutor.execute(() ->{
            mStoryDao.insert(story);
            mStoryDao = mRoomDb.getStoryDAO();
        });
    }

    public void updateLocalStory(Story story) {
        AppDatabase.databaseWriteExecutor.execute(() ->{
            mStoryDao.update(story);
            mStoryDao = mRoomDb.getStoryDAO();
        });
    }

    public void deleteLocalStory(Story story) {
        AppDatabase.databaseWriteExecutor.execute(() ->{
            mStoryDao.delete(story);
            mStoryDao = mRoomDb.getStoryDAO();
        });
    }

    public Story getLocalStoryById(int storyId) {
        Story story = null;
        Future<Story> storyFuture = AppDatabase.databaseWriteExecutor.submit(() -> mStoryDao.getStoryById(storyId));

        try {
            story = storyFuture.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return story;
    }

    public Story getLocalStoryByUserId(int userId) {
        Story story = null;
        Future<Story> storyFuture = AppDatabase.databaseWriteExecutor.submit(() -> mStoryDao.getStoryByUserId(userId));

        try {
            story = storyFuture.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return story;
    }

    public Story getLocalStoryByName(String storyName) {
        Story story = null;
        Future<Story> storyFuture = AppDatabase.databaseWriteExecutor.submit(() -> mStoryDao.getStoryByName(storyName));

        try {
            story = storyFuture.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return story;
    }

    //===================================== REST OPERATIONS

    public void insertStory(Story story) {
        // List of stories body
        Gson gson = new Gson();
        String strList = gson.toJson(story.getStoryList());
        RequestBody body = RequestBody.create(strList, MediaType.parse("application/json"));

        mRetrofitClient.apiInterface.insertStory(story.getUserId(), story.getStoryName(), body).enqueue(new Callback<StoryResponse>() {
            @Override
            public void onResponse(@NonNull Call<StoryResponse> call, @NonNull Response<StoryResponse> response) {
                if (response.isSuccessful()) {
                    System.out.println("Story created successfully");
                    mRetrofitClient.storyResponse.postValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<StoryResponse> call, @NonNull Throwable t) {
                mRetrofitClient.storyResponse.postValue(null);
                System.out.println("Error" + t.getMessage());
            }
        });
    }

    public void updateStoryList(Story story) {
        // List of stories body
        Gson gson = new Gson();
        String strList = gson.toJson(story.getStoryList());
        RequestBody body = RequestBody.create(strList, MediaType.parse("application/json"));

        mRetrofitClient.apiInterface.updateStoryList(story.getStoryId(), body).enqueue(new Callback<StoryResponse>() {
            @Override
            public void onResponse(@NonNull Call<StoryResponse> call, @NonNull Response<StoryResponse> response) {
                if(response.isSuccessful()) {
                    System.out.println("Story stories list updated successfully");
                    mRetrofitClient.storyUpdatedResponse.postValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<StoryResponse> call, @NonNull Throwable t) {
                mRetrofitClient.storyUpdatedResponse.postValue(null);
                System.out.println("Error" + t.getMessage());
            }
        });
    }

    public void updateStoryLikesCount(Story story) {
        mRetrofitClient.apiInterface.updateStoryLikes(story.getStoryId(), story.getLikes()).enqueue(new Callback<StoryResponse>() {
            @Override
            public void onResponse(@NonNull Call<StoryResponse> call, @NonNull Response<StoryResponse> response) {
                if(response.isSuccessful()) {
                    System.out.println("Story likes updated!");
                    mRetrofitClient.storyResponse.postValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<StoryResponse> call, @NonNull Throwable t) {
                mRetrofitClient.storyResponse.postValue(null);
                System.out.println("Error" + t.getMessage());
            }
        });
    }
    public void updateStoryDislikesCount(Story story) {
        mRetrofitClient.apiInterface.updateStoryDislikes(story.getStoryId(), story.getDislikes()).enqueue(new Callback<StoryResponse>() {
            @Override
            public void onResponse(@NonNull Call<StoryResponse> call, @NonNull Response<StoryResponse> response) {
                if(response.isSuccessful()) {
                    System.out.println("Story dislikes updated!");
                    mRetrofitClient.storyResponse.postValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<StoryResponse> call, @NonNull Throwable t) {
                mRetrofitClient.storyResponse.postValue(null);
                System.out.println("Error" + t.getMessage());
            }
        });
    }

    public void finishStory(Story story) {
        mRetrofitClient.apiInterface.updateStoryIsOpen(story.getStoryId(), story.getIsOpen()).enqueue(new Callback<StoryResponse>() {
            @Override
            public void onResponse(@NonNull Call<StoryResponse> call, @NonNull Response<StoryResponse> response) {
                if(response.isSuccessful()) {
                    System.out.println("Story closed successfully");
                    mRetrofitClient.storyResponse.postValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<StoryResponse> call, @NonNull Throwable t) {
                mRetrofitClient.storyResponse.postValue(null);
                System.out.println("Error" + t.getMessage());
            }
        });
    }

    public void getAllStory() {
        mRetrofitClient.apiInterface.getAllStory().enqueue(new Callback<List<StoryResponse>>() {
            @Override
            public void onResponse(@NonNull Call<List<StoryResponse>> call, @NonNull Response<List<StoryResponse>> response) {
                if(response.isSuccessful()) {
                    System.out.println("Retrieved all story!");
                    mRetrofitClient.storyResponseList.postValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<StoryResponse>> call, @NonNull Throwable t) {
                mRetrofitClient.storyResponseList.postValue(null);
                System.out.println("Error" + t.getMessage());
            }
        });
    }

    public void getAllOpenStory() {
        mRetrofitClient.apiInterface.getAllOpenStory().enqueue(new Callback<List<StoryResponse>>() {
            @Override
            public void onResponse(@NonNull Call<List<StoryResponse>> call, @NonNull Response<List<StoryResponse>> response) {
                if(response.isSuccessful()) {
                    System.out.println("Retrieved all open story!");
                    mRetrofitClient.storyResponseList.postValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<StoryResponse>> call, @NonNull Throwable t) {
                mRetrofitClient.storyResponseList.postValue(null);
                System.out.println("Error" + t.getMessage());
            }
        });
    }

    public void getAllClosedStory() {
        mRetrofitClient.apiInterface.getAllClosedStory().enqueue(new Callback<List<StoryResponse>>() {
            @Override
            public void onResponse(@NonNull Call<List<StoryResponse>> call, @NonNull Response<List<StoryResponse>> response) {
                if(response.isSuccessful()) {
                    System.out.println("Retrieved all closed story!");
                    mRetrofitClient.storyResponseList.postValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<StoryResponse>> call, @NonNull Throwable t) {
                mRetrofitClient.storyResponseList.postValue(null);
                System.out.println("Error" + t.getMessage());
            }
        });
    }

    public void getStoryById(int storyId) {
        mRetrofitClient.apiInterface.getStoryById(storyId).enqueue(new Callback<StoryResponse>() {
            @Override
            public void onResponse(@NonNull Call<StoryResponse> call, @NonNull Response<StoryResponse> response) {
                if(response.isSuccessful()) {
                    System.out.println("Story retrieved by story id successfully");
                    mRetrofitClient.storyResponse.postValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<StoryResponse> call, @NonNull Throwable t) {
                mRetrofitClient.storyResponse.postValue(null);
                System.out.println("Error" + t.getMessage());
            }
        });
    }

    public void getAllStoryByUserId(int userId) {
        mRetrofitClient.apiInterface.getAllStoryByUserId(userId).enqueue(new Callback<List<StoryResponse>>() {
            @Override
            public void onResponse(@NonNull Call<List<StoryResponse>> call, @NonNull Response<List<StoryResponse>> response) {
                if(response.isSuccessful()) {
                    System.out.println("All story retrieved by user id successfully");
                    mRetrofitClient.storyResponseList.postValue(response.body()); // May need to change this
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<StoryResponse>> call, @NonNull Throwable t) {
                mRetrofitClient.storyResponseList.postValue(null);
                System.out.println("Error" + t.getMessage());
            }
        });
    }

    public void getStoryByName(String storyName) {
        mRetrofitClient.apiInterface.getStoryByName(storyName).enqueue(new Callback<StoryResponse>() {
            @Override
            public void onResponse(@NonNull Call<StoryResponse> call, @NonNull Response<StoryResponse> response) {
                if(response.isSuccessful()) {
                    System.out.println("Story retrieved by name successfully");
                    mRetrofitClient.storyResponse.postValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<StoryResponse> call, @NonNull Throwable t) {
                mRetrofitClient.storyResponse.postValue(null);
                System.out.println("Error" + t.getMessage());
            }
        });
    }

}
