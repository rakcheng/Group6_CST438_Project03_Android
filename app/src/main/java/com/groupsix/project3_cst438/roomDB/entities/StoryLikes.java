package com.groupsix.project3_cst438.roomDB.entities;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.groupsix.project3_cst438.roomDB.AppDatabase;

import java.util.Objects;

@Entity(tableName = AppDatabase.LIKES_TABLE)
public class StoryLikes {
    @PrimaryKey(autoGenerate = true)
    Integer likesId;
    Integer storyId;
    Integer userId;

    boolean isLiked;
    boolean isDisliked;

    @Ignore
    public StoryLikes(Integer likesId, Integer storyId, Integer userId, boolean isLiked, boolean isDisliked) {
        this.likesId = likesId;
        this.storyId = storyId;
        this.userId = userId;
        this.isLiked = isLiked;
        this.isDisliked = isDisliked;
    }

    public StoryLikes(Integer storyId, Integer userId, boolean isLiked, boolean isDisliked) {
        this.storyId = storyId;
        this.userId = userId;
        this.isLiked = isLiked;
        this.isDisliked = isDisliked;
    }

    public Integer getLikesId() {
        return likesId;
    }

    public void setLikesId(Integer likesId) {
        this.likesId = likesId;
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

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }

    public boolean isDisliked() {
        return isDisliked;
    }

    public void setDisliked(boolean disliked) {
        isDisliked = disliked;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StoryLikes that = (StoryLikes) o;
        return isLiked == that.isLiked && isDisliked == that.isDisliked && Objects.equals(likesId, that.likesId) && Objects.equals(storyId, that.storyId) && Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(likesId, storyId, userId, isLiked, isDisliked);
    }
}
