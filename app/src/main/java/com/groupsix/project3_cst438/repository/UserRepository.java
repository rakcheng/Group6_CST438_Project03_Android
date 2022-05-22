package com.groupsix.project3_cst438.repository;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.groupsix.project3_cst438.retrofit.RetrofitClient;
import com.groupsix.project3_cst438.retrofit.StoryResponse;
import com.groupsix.project3_cst438.retrofit.UserResponse;
import com.groupsix.project3_cst438.roomDB.AppDatabase;
import com.groupsix.project3_cst438.roomDB.DAO.UserDAO;
import com.groupsix.project3_cst438.roomDB.entities.Story;
import com.groupsix.project3_cst438.roomDB.entities.User;

import java.util.List;
import java.util.concurrent.Future;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository {
    public static UserRepository repoInstance;
    private final AppDatabase mRoomDb;
    private UserDAO mUserDao;

    private final RetrofitClient mRetrofitClient;

    private UserRepository(Context context) {
        mRetrofitClient = RetrofitClient.getInstance(context);
        mRoomDb = AppDatabase.getInstance(context);
        mUserDao = mRoomDb.getUserDAO();
    }

    public static UserRepository getRepoInstance(Context context) {
        if (repoInstance == null) {
            repoInstance = new UserRepository(context);
        }
        return repoInstance;
    }

    // Livedata to check if user table was changed in room database
    public LiveData<List<User>> getAllLocalUsersLiveData() { return mUserDao.getAllUsers(); }
    public LiveData<UserResponse> getUserResponse() { return mRetrofitClient.userResponse; }

    public void insertLocalUser(User user) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
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
        Future<User> userFuture = AppDatabase.databaseWriteExecutor.submit(() -> mUserDao.getUserByUserId(userId));
        User user = null;

        try {
            user = userFuture.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    public User getLocalUserByUsername(String username) {
        Future<User> userFuture = AppDatabase.databaseWriteExecutor.submit(() -> mUserDao.getUserByUsername(username));
        User user = null;

        try {
            user = userFuture.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    // API
    public void insertUser(User user) {
        mRetrofitClient.apiInterface.insertUser(user.getUsername(), user.getPassword(), user.isAdmin()).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(@NonNull Call<UserResponse> call, @NonNull Response<UserResponse> response) {
                if (response.isSuccessful()) {
                    System.out.println("User created successfully");
                    mRetrofitClient.userResponse.postValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserResponse> call, @NonNull Throwable t) {
                mRetrofitClient.userResponse.postValue(null);
                System.out.println("Error" + t.getMessage());
            }
        });
    }

    public void getUserByName(String username) {
        mRetrofitClient.apiInterface.getUserByName(username).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(@NonNull Call<UserResponse> call, @NonNull Response<UserResponse> response) {
                if (response.isSuccessful()) {
                    System.out.println("User retrieved by name successfully");
                    mRetrofitClient.userResponse.postValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserResponse> call, @NonNull Throwable t) {
                mRetrofitClient.userResponse.postValue(null);
                System.out.println("Error" + t.getMessage());
            }
        });
    }

    public void getUserById(Integer userId) {
        mRetrofitClient.apiInterface.getUserById(userId).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(@NonNull Call<UserResponse> call, @NonNull Response<UserResponse> response) {
                if (response.isSuccessful()) {
                    System.out.println("User retrieved by id successfully");
                    mRetrofitClient.userResponse.postValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserResponse> call, @NonNull Throwable t) {
                mRetrofitClient.userResponse.postValue(null);
                System.out.println("Error" + t.getMessage());
            }
        });
    }




}
