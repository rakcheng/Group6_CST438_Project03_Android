package com.groupsix.project3_cst438.roomDB.entities;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.groupsix.project3_cst438.roomDB.AppDatabase;

import java.util.Objects;

@Entity(tableName = AppDatabase.COMMENTS_TABLE)
public class Comment {
    @PrimaryKey
    private Integer commentId;
    private Integer storyId;
    private Integer userId;

    private String comment;

    @Ignore
    public Comment(Integer commentId, Integer storyId, Integer userId, String comment) {
        this.commentId = commentId;
        this.storyId = storyId;
        this.userId = userId;
        this.comment = comment;
    }

    public Comment(Integer storyId, Integer userId, String comment) {
        this.storyId = storyId;
        this.userId = userId;
        this.comment = comment;
    }

    public Integer getCommentId() {
        return commentId;
    }

    public void setCommentId(Integer commentId) {
        this.commentId = commentId;
    }

    public Integer getStoryId() {
        return storyId;
    }

    public void setStoryId(Integer storyId) {
        this.storyId = storyId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comments = (Comment) o;
        return Objects.equals(commentId, comments.commentId) && Objects.equals(storyId, comments.storyId) && Objects.equals(userId, comments.userId) && Objects.equals(comment, comments.comment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(commentId, storyId, userId, comment);
    }
}
