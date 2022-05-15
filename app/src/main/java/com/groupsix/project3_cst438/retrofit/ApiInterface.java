package com.groupsix.project3_cst438.retrofit;

import com.groupsix.project3_cst438.roomDB.entities.Stories;

import org.json.JSONObject;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 *  Interface for backend REST API
 */

public interface ApiInterface {

    //@Headers("Content-Type: application/json")
    @POST("api/newstory")
    Call<StoryResponse> insertStory (@Query("userId") int userId, @Query("storyName") String storyName, @Body RequestBody storyList);

    @PATCH("api/story/update")
    Call<StoryResponse> updateStoryIsOpen(@Query("storyId") int storyId, @Query("isOpen") boolean isOpen);

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
