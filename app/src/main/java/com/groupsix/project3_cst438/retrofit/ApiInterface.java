package com.groupsix.project3_cst438.retrofit;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 *  Interface for backend REST API
 */

public interface ApiInterface {

    @POST("api/newstory")
    @FormUrlEncoded
    Call<StoryResponse> insertStory (@Field("userId") int userId, @Field("storyName") String storyName, @Field("storyList") List<Integer> storyList);

    @GET("api/story")
    Call<StoryResponse> getStoryById (@Query("storyId") int storyId);

    @GET("api/story")
    Call<List<StoryResponse>> getAllStoryByUserId (@Query("userId") int userId);

    @GET("api/story")
    Call<StoryResponse> getStoryByName (@Query("storyName") String storyName);

    @GET("api/stories")
    Call<List<StoriesResponse>> getAllStoriesByUserId (@Query("userId") int userId);

    @GET("api/stories")
    Call<List<StoriesResponse>> getStoriesByUserIdAndStory (@Query("userId") int userId, @Query("story") String story);

    @POST("api/newstories")
    @FormUrlEncoded
    Call<StoriesResponse> insertStories (@Field("userId") int userId, @Field("story") String story);
}
