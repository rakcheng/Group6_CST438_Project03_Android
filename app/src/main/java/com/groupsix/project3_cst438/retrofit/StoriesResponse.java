package com.groupsix.project3_cst438.retrofit;

import com.google.gson.annotations.SerializedName;
import com.groupsix.project3_cst438.roomDB.entities.Stories;

public class StoriesResponse {
    @SerializedName("storiesId")
    private Integer storiesId;

    @SerializedName("userId")
    private Integer userId;

    @SerializedName("story")
    private String story;

    public Stories getStory() {
        Stories stories = new Stories(userId, story);
        stories.setStoriesId(storiesId);
        return stories;
    }
}
