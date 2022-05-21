package com.groupsix.project3_cst438.retrofit;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    //public static final String BASE_URL = "https://calm-ravine-21524.herokuapp.com/"; // Use heroku backend app
    public static final String BASE_URL = "http://10.0.2.2:8080/"; // Using ip of local host for android emulator

    public ApiInterface apiInterface;
    private static RetrofitClient retrofitInstance;

    public MutableLiveData<UserResponse> userResponse;
    public MutableLiveData<StoryResponse> storyResponse;
    public MutableLiveData<List<StoryResponse>> storyResponseList;
    public MutableLiveData<StoriesResponse> storiesResponse;
    public MutableLiveData<List<StoriesResponse>> storiesResponseList;
    public MutableLiveData<StoryLikesResponse> storyLikesResponse;
    public MutableLiveData<List<StoryLikesResponse>> storyLikesResponseList;
    public MutableLiveData<CommentResponse> commentResponse;
    public MutableLiveData<List<CommentResponse>> commentResponseList;

    public RetrofitClient(String BASE_URL) {
        OkHttpClient client = new OkHttpClient();
        userResponse = new MutableLiveData<>();
        storyResponse = new MutableLiveData<>();
        storyResponseList = new MutableLiveData<>();
        storiesResponse = new MutableLiveData<>();
        storiesResponseList = new MutableLiveData<>();
        storyLikesResponse = new MutableLiveData<>();
        storyLikesResponseList = new MutableLiveData<>();

        apiInterface = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiInterface.class);
    }

    public static RetrofitClient getInstance(final Context context) {
        if (retrofitInstance == null) {
           retrofitInstance = new RetrofitClient(BASE_URL);
        }
        return retrofitInstance;
    }
}
