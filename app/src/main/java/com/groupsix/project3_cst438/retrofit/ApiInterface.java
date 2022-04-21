package com.groupsix.project3_cst438.retrofit;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiInterface {

    @POST("api/newstory")
    Call<StoryResponse> insertStory (@Query("userId") int userId, @Query("storyName") String storyName, @Query("storyList") List<Integer> storyList);

    @GET("api/story")
    Call<StoryResponse> getStoryById (@Query("storyId") int storyId);

    @GET("api/story")
    Call<StoryResponse> getStoryByUserId (@Query("userId") int userId);

    @GET("api/story")
    Call<StoryResponse> getStoryByName (@Query("storyName") String storyName);

}
