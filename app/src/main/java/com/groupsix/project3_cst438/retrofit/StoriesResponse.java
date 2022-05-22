package com.groupsix.project3_cst438.retrofit;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.groupsix.project3_cst438.roomDB.entities.Stories;
import com.groupsix.project3_cst438.roomDB.entities.Story;

public class StoriesResponse {
    @SerializedName("storiesId")
    private Integer storiesId;

    @SerializedName("userId")
    private Integer userId;

    @SerializedName("story")
    private String story;

    @SerializedName("storyParent")
    private String storyParentJson;

    public Stories getStory() {
        Gson gson = new Gson();
        Story storyParent = gson.fromJson(String.valueOf(storyParentJson), Story.class);
        Stories stories = new Stories(userId, story, storyParent, null);
        stories.setStoriesId(storiesId);
        return stories;
    }
}