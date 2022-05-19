package com.groupsix.project3_cst438.retrofit;

import com.google.gson.annotations.SerializedName;
import com.groupsix.project3_cst438.roomDB.entities.Stories;
import com.groupsix.project3_cst438.roomDB.entities.Story;

import java.util.List;

public class StoryResponse {
    @SerializedName("storyId")
    private Integer storyId;

    @SerializedName("userId")
    private Integer userId;

    @SerializedName("storyName")
    private String storyName;

    @SerializedName("likes")
    private Integer likes;

    @SerializedName("dislikes")
    private Integer dislikes;

    @SerializedName("open")
    private boolean open;

    @SerializedName("storyList")
    private List<Stories> storiesList;

    public Integer getStoryId() {
        return storyId;
    }

    public Integer getUserId() { return userId; }

    public Story getStory() {
        return new Story(storyId, userId, storyName, likes, dislikes, open, storiesList);
    }

}
