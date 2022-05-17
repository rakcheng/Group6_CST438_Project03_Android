package com.groupsix.project3_cst438.retrofit;

import androidx.lifecycle.MutableLiveData;

import com.groupsix.project3_cst438.roomDB.entities.StoryLikes;

import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    public ApiInterface apiInterface;

    public MutableLiveData<UserResponse> userResponseMutableLiveData;
    public MutableLiveData<StoryResponse> storyResponseMutableLiveData;
    public MutableLiveData<List<StoryResponse>> storyListResponseMutableLiveData;
    public MutableLiveData<StoriesResponse> storiesResponseMutableLiveData;
    public MutableLiveData<List<StoriesResponse>> storiesListResponseMutableLiveData;
    public MutableLiveData<StoryLikesResponse> storyLikesResponseMutableLiveData;
    public MutableLiveData<List<StoryLikesResponse>> storyLikesListMutableLiveData;

    public RetrofitClient(String BASE_URL) {
        OkHttpClient client = new OkHttpClient();
        userResponseMutableLiveData = new MutableLiveData<>();
        storyResponseMutableLiveData = new MutableLiveData<>();
        storyListResponseMutableLiveData = new MutableLiveData<>();
        storiesResponseMutableLiveData = new MutableLiveData<>();
        storiesListResponseMutableLiveData = new MutableLiveData<>();
        storyLikesResponseMutableLiveData = new MutableLiveData<>();
        storyLikesListMutableLiveData = new MutableLiveData<>();

        apiInterface = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiInterface.class);

    }
}
