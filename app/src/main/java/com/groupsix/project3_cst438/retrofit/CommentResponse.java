package com.groupsix.project3_cst438.retrofit;

import com.google.gson.annotations.SerializedName;
import com.groupsix.project3_cst438.roomDB.entities.Comment;

public class CommentResponse {
    @SerializedName("commentId")
    private Integer commentId;

    @SerializedName("storyId")
    private Integer storyId;

    @SerializedName("userId")
    private Integer userId;

    @SerializedName("comment")
    private String comment;

    public Comment getCommentObject() {
        return new Comment(commentId, storyId, userId, comment);
    }
}
