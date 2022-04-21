package com.groupsix.project3_cst438.retrofit;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiInterface {

    @POST("api/newuser")
    Call<UserResponse> insertUser (@Query("username") String username, @Query("password") String password);

    @POST("api/newstory")
    Call<StoryResponse> insertStory (@Query("userId") int userId, @Query("storyName") String storyName);

    @GET("api/story")
    Call<StoryResponse> getStoryById (@Query("storyId") int storyId);

    @GET("api/story")
    Call<StoryResponse> getStoryByUserId (@Query("userId") int userId);

    @GET("api/story")
    Call<StoryResponse> getStoryByName (@Query("storyName") String storyName);

}
