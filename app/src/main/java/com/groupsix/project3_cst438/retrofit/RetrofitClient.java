package com.groupsix.project3_cst438.retrofit;

import androidx.lifecycle.MutableLiveData;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    public ApiInterface apiInterface;

    public MutableLiveData<UserResponse> userResponseMutableLiveData;
    public MutableLiveData<StoryResponse> storyResponseMutableLiveData;
    public MutableLiveData<StoriesResponse> storiesResponseMutableLiveData;

    public RetrofitClient(String BASE_URL) {
        OkHttpClient client = new OkHttpClient();
        userResponseMutableLiveData = new MutableLiveData<>();
        storyResponseMutableLiveData = new MutableLiveData<>();
        storiesResponseMutableLiveData = new MutableLiveData<>();

        apiInterface = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiInterface.class);

    }
}