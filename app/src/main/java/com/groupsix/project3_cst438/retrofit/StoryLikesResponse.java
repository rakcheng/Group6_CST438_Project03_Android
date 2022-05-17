package com.groupsix.project3_cst438.retrofit;

import com.google.gson.annotations.SerializedName;
import com.groupsix.project3_cst438.roomDB.entities.StoryLikes;

public class StoryLikesResponse {
    @SerializedName("likesId")
    private Integer likesId;

    @SerializedName("storyId")
    private Integer storyId;

    @SerializedName("userId")
    private Integer userId;

    @SerializedName("isLiked")
    private boolean isLiked;

    @SerializedName("isDisliked")
    private boolean isDisliked;

    public StoryLikes getStoryLikesObject() {
        return new StoryLikes(likesId, storyId, userId, isLiked, isDisliked);
    }
}
