package com.groupsix.project3_cst438.retrofit;

import com.google.gson.annotations.SerializedName;

public class StoryResponse {
    @SerializedName("storyId")
    private Integer storyId;

    @SerializedName("userId")
    private Integer userId;

    @SerializedName("storyName")
    private String storyName;
}
