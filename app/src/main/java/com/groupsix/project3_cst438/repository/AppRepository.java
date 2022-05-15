package com.groupsix.project3_cst438.repository;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.groupsix.project3_cst438.retrofit.RetrofitClient;
import com.groupsix.project3_cst438.retrofit.StoriesResponse;
import com.groupsix.project3_cst438.retrofit.StoryResponse;
import com.groupsix.project3_cst438.retrofit.UserResponse;
import com.groupsix.project3_cst438.roomDB.AppDatabase;
import com.groupsix.project3_cst438.roomDB.DAO.StoriesDAO;
import com.groupsix.project3_cst438.roomDB.DAO.StoryDAO;
import com.groupsix.project3_cst438.roomDB.DAO.UserDAO;
import com.groupsix.project3_cst438.roomDB.entities.Stories;
import com.groupsix.project3_cst438.roomDB.entities.Story;
import com.groupsix.project3_cst438.roomDB.entities.User;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.Future;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 *  Determines from where to retrieve or update/store data
 *  (local room database or Backend REST API)
 *  (Try to use the local room database cache instead of always calling api)
 */

public class AppRepository {
    public static AppRepository repoInstance;
    private AppDatabase mRoomDb;
    private UserDAO mUserDao;
    private StoryDAO mStoryDao;
    private StoriesDAO mStoriesDao;

    // Retrofit client instance
    public static final String BASE_URL = "http://10.0.2.2:8080/"; // Using ip of host for android emulator
    RetrofitClient retrofitClient = new RetrofitClient(BASE_URL);

    public AppRepository(Context context) {
        mRoomDb = AppDatabase.getInstance(context);
        mUserDao = mRoomDb.getUserDAO();
        mStoryDao = mRoomDb.getStoryDAO();
        mStoriesDao = mRoomDb.getStoriesDAO();
    }

    public static AppRepository getRepoInstance(Context context) {
        if (repoInstance == null) {
           repoInstance = new AppRepository(context);
        }
        return repoInstance;
    }

    //================================ Room Database Operations ====================================

    public LiveData<List<User>> getAllLocalUsers() { return mUserDao.getAllUsers(); }
    public LiveData<List<Story>> getAllLocalStoryLiveData() { return mStoryDao.getAll(); }
    public LiveData<List<Stories>> getAllLocalStories() { return mStoriesDao.getAll(); }

    public void insertLocalUser(User user) {
        AppDatabase.databaseWriteExecutor.execute(() ->{
            mUserDao.insert(user);
            mUserDao = mRoomDb.getUserDAO();
        });
    }

    public void updateLocalUser(User user) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            mUserDao.update(user);
            mUserDao = mRoomDb.getUserDAO();
        });
    }

    public void deleteLocalUser(User user) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            mUserDao.delete(user);
            mUserDao = mRoomDb.getUserDAO();
        });
    }

    public User getLocalUserById(int userId) {
        User user = null;
        Future<User> userFuture = AppDatabase.databaseWriteExecutor.submit(() -> mUserDao.getUserByUserId(userId));

        try {
            user = userFuture.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    public User getLocalUserByUsername(String username) {
        User user = null;
        Future<User> userFuture = AppDatabase.databaseWriteExecutor.submit(() -> mUserDao.getUserByUsername(username));

        try {
            user = userFuture.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
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
        Stories stories = null;
        Future<Stories> storiesFuture = AppDatabase.databaseWriteExecutor.submit(() -> mStoriesDao.getStoriesById(storiesId));

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

    //================================ REST API Operations =========================================

    public void insertStory(Story story) {
        // List of stories body
        Gson gson = new Gson();
        String strList = gson.toJson(story.getStoryList());
        RequestBody body = RequestBody.create(strList, MediaType.parse("application/json"));

        retrofitClient.apiInterface.insertStory(story.getUserId(), story.getStoryName(), body).enqueue(new Callback<StoryResponse>() {
            @Override
            public void onResponse(@NonNull Call<StoryResponse> call, @NonNull Response<StoryResponse> response) {
                if(response.isSuccessful()) {
                    System.out.println("Story created successfully");
                    retrofitClient.storyResponseMutableLiveData.postValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<StoryResponse> call, @NonNull Throwable t) {
                System.out.println("Failed");
                retrofitClient.storyResponseMutableLiveData.postValue(null);
                System.out.println("Error" + t.getMessage());
            }
        });
    }

    public void finishStory(Story story) {
        retrofitClient.apiInterface.updateStoryIsOpen(story.getStoryId(), story.getOpen()).enqueue(new Callback<StoryResponse>() {
            @Override
            public void onResponse(@NonNull Call<StoryResponse> call, @NonNull Response<StoryResponse> response) {
                if(response.isSuccessful()) {
                    System.out.println("Story closed successfully");
                    retrofitClient.storyResponseMutableLiveData.postValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<StoryResponse> call, @NonNull Throwable t) {
                System.out.println("Failed");
                retrofitClient.storyResponseMutableLiveData.postValue(null);
                System.out.println("Error" + t.getMessage());
            }
        });
    }

    public void getAllStory() {
        retrofitClient.apiInterface.getAllStory().enqueue(new Callback<List<StoryResponse>>() {
            @Override
            public void onResponse(@NonNull Call<List<StoryResponse>> call, @NonNull Response<List<StoryResponse>> response) {
                if(response.isSuccessful()) {
                    System.out.println("Retrieved all story!");
                    retrofitClient.storyListResponseMutableLiveData.postValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<StoryResponse>> call, @NonNull Throwable t) {
                System.out.println("Failed");
                retrofitClient.storyResponseMutableLiveData.postValue(null);
                System.out.println("Error" + t.getMessage());
            }
        });
    }

    public void getAllOpenStory() {
        retrofitClient.apiInterface.getAllOpenStory().enqueue(new Callback<List<StoryResponse>>() {
            @Override
            public void onResponse(@NonNull Call<List<StoryResponse>> call, @NonNull Response<List<StoryResponse>> response) {
                if(response.isSuccessful()) {
                    System.out.println("Retrieved all open story!");
                    retrofitClient.storyListResponseMutableLiveData.postValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<StoryResponse>> call, @NonNull Throwable t) {
                System.out.println("Failed");
                retrofitClient.storyResponseMutableLiveData.postValue(null);
                System.out.println("Error" + t.getMessage());
            }
        });
    }

    public void getAllClosedStory() {
        retrofitClient.apiInterface.getAllClosedStory().enqueue(new Callback<List<StoryResponse>>() {
            @Override
            public void onResponse(@NonNull Call<List<StoryResponse>> call, @NonNull Response<List<StoryResponse>> response) {
                if(response.isSuccessful()) {
                    System.out.println("Retrieved all closed story!");
                    retrofitClient.storyListResponseMutableLiveData.postValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<StoryResponse>> call, @NonNull Throwable t) {
                System.out.println("Failed");
                retrofitClient.storyResponseMutableLiveData.postValue(null);
                System.out.println("Error" + t.getMessage());
            }
        });
    }

    public void getStoryById(int storyId) {
        retrofitClient.apiInterface.getStoryById(storyId).enqueue(new Callback<StoryResponse>() {
            @Override
            public void onResponse(@NonNull Call<StoryResponse> call, @NonNull Response<StoryResponse> response) {
                if(response.isSuccessful()) {
                    System.out.println("Story retrieved by story id successfully");
                    retrofitClient.storyResponseMutableLiveData.postValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<StoryResponse> call, @NonNull Throwable t) {
                System.out.println("Failed");
                retrofitClient.storyResponseMutableLiveData.postValue(null);
                System.out.println("Error" + t.getMessage());
            }
        });
    }

    public void getAllStoryByUserId(int userId) {
        retrofitClient.apiInterface.getAllStoryByUserId(userId).enqueue(new Callback<List<StoryResponse>>() {
            @Override
            public void onResponse(@NonNull Call<List<StoryResponse>> call, @NonNull Response<List<StoryResponse>> response) {
                if(response.isSuccessful()) {
                    System.out.println("All story retrieved by user id successfully");
                    retrofitClient.storyListResponseMutableLiveData.postValue(response.body()); // May need to change this
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<StoryResponse>> call, @NonNull Throwable t) {
                System.out.println("Failed");
                retrofitClient.storyListResponseMutableLiveData.postValue(null);
                System.out.println("Error" + t.getMessage());
            }
        });
    }

    public void getStoryByName(String storyName) {
        retrofitClient.apiInterface.getStoryByName(storyName).enqueue(new Callback<StoryResponse>() {
            @Override
            public void onResponse(@NonNull Call<StoryResponse> call, @NonNull Response<StoryResponse> response) {
                if(response.isSuccessful()) {
                    System.out.println("Story retrieved by name successfully");
                    retrofitClient.storyResponseMutableLiveData.postValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<StoryResponse> call, @NonNull Throwable t) {
                System.out.println("Failed");
                retrofitClient.storyResponseMutableLiveData.postValue(null);
                System.out.println("Error" + t.getMessage());
            }
        });
    }

    public void insertStories(Stories stories) {
        retrofitClient.apiInterface.insertStories(stories.getUserId(), stories.getStory()).enqueue(new Callback<StoriesResponse>() {
            @Override
            public void onResponse(@NonNull Call<StoriesResponse> call, @NonNull Response<StoriesResponse> response) {
                if (response.isSuccessful()) {
                    System.out.println("Stories created successfully");
                    retrofitClient.storiesResponseMutableLiveData.postValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<StoriesResponse> call, @NonNull Throwable t) {
                System.out.println("Failed");
                retrofitClient.storyResponseMutableLiveData.postValue(null);
                System.out.println("Error" + t.getMessage());
            }
        });
    }

    public void getStoriesByUserIdAndStory(int userId, String story) {
        retrofitClient.apiInterface.getStoriesByUserIdAndStory(userId, story).enqueue(new Callback<List<StoriesResponse>>() {
            @Override
            public void onResponse(@NonNull Call<List<StoriesResponse>> call, @NonNull Response<List<StoriesResponse>> response) {
                if(response.isSuccessful()) {
                    System.out.println("Stories retrieved by userId and story successfully");
                    retrofitClient.storiesListResponseMutableLiveData.postValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<StoriesResponse>> call, @NonNull Throwable t) {
                System.out.println("Failed");
                retrofitClient.storiesListResponseMutableLiveData.postValue(null);
                System.out.println("Error" + t.getMessage());
            }
        });
    }

    public LiveData<StoryResponse> getStoryResponseLiveData() { return retrofitClient.storyResponseMutableLiveData; }
    public LiveData<List<StoryResponse>> getStoryListResponseLiveData() { return retrofitClient.storyListResponseMutableLiveData; }
    public LiveData<StoriesResponse> getStoriesResponseLiveData() { return retrofitClient.storiesResponseMutableLiveData; }
    public LiveData<List<StoriesResponse>> getStoriesListResponseLiveData() { return retrofitClient.storiesListResponseMutableLiveData; }
}
