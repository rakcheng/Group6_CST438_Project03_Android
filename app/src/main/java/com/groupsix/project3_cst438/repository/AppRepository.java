package com.groupsix.project3_cst438.repository;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.groupsix.project3_cst438.retrofit.RetrofitClient;
import com.groupsix.project3_cst438.retrofit.StoryResponse;
import com.groupsix.project3_cst438.roomDB.AppDatabase;
import com.groupsix.project3_cst438.roomDB.DAO.StoriesDAO;
import com.groupsix.project3_cst438.roomDB.DAO.StoryDAO;
import com.groupsix.project3_cst438.roomDB.DAO.UserDAO;
import com.groupsix.project3_cst438.roomDB.entities.Stories;
import com.groupsix.project3_cst438.roomDB.entities.Story;
import com.groupsix.project3_cst438.roomDB.entities.User;

import java.util.List;
import java.util.concurrent.Future;

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

    public LiveData<List<User>> mUsersLiveData;
    public LiveData<List<Story>> mStoryLiveData;
    public LiveData<List<Stories>> mStoriesLiveData;

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

    public LiveData<List<User>> getAllUsers() { return mUserDao.getAllUsers(); }
    public LiveData<List<Story>> getAllStory() { return mStoryDao.getAll(); }
    public LiveData<List<Stories>> getAllStories() { return mStoriesDao.getAll(); }

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
        retrofitClient.apiInterface.insertStory(story.getUserId(), story.getStoryName(), story.getStoryList()).enqueue(new Callback<StoryResponse>() {
            @Override
            public void onResponse(@NonNull Call<StoryResponse> call, @NonNull Response<StoryResponse> response) {
                System.out.println("Story created successfully");
                if(response.isSuccessful()) {
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

}
