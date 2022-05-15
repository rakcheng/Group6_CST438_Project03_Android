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

    @SerializedName("storyList")
    private List<Stories> storiesList;

    public Integer getStoryId() {
        return storyId;
    }

    public Story getStory() {
        return new Story(storyId, userId, storyName, storiesList);
    }

}
