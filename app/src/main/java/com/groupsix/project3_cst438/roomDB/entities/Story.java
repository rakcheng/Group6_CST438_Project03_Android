package com.groupsix.project3_cst438.roomDB.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;
import com.groupsix.project3_cst438.roomDB.AppDatabase;

import java.util.List;
import java.util.Objects;

@Entity(tableName = AppDatabase.STORY_TABLE)
public class Story {
    @PrimaryKey(autoGenerate = true)
    private Integer storyId;
    private Integer userId;
    private String storyName;
    private Integer likes;
    private Integer dislikes;
    private Boolean isOpen;

    private List<Stories> storyList;

    @Ignore
    public Story(Integer storyId, Integer userId, String storyName, Integer likes, Integer dislikes, boolean isOpen, List<Stories> storyList) {
        this.storyId = storyId;
        this.userId = userId;
        this.storyName = storyName;
        this.likes = likes;
        this.dislikes = dislikes;
        this.isOpen = isOpen;
        this.storyList = storyList;
    }

    public Story(Integer userId, String storyName, List<Stories> storyList) {
        this.userId = userId;
        this.storyName = storyName;
        this.storyList = storyList;
        this.likes = 0;
        this.dislikes = 0;
        this.isOpen = true;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public Integer getDislikes() {
        return dislikes;
    }

    public void setDislikes(Integer dislikes) {
        this.dislikes = dislikes;
    }

    public Boolean getOpen() {
        return isOpen;
    }

    public void setOpen(Boolean open) {
        isOpen = open;
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

    public String getStoryName() {
        return storyName;
    }

    public void setStoryName(String storyName) {
        this.storyName = storyName;
    }

    public List<Stories> getStoryList() {
        return storyList;
    }

    public void setStoryList(List<Stories> storyList) {
        this.storyList = storyList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Story story = (Story) o;
        return Objects.equals(storyId, story.storyId) && Objects.equals(userId, story.userId) && Objects.equals(storyName, story.storyName) && Objects.equals(likes, story.likes) && Objects.equals(dislikes, story.dislikes) && Objects.equals(isOpen, story.isOpen) && Objects.equals(storyList, story.storyList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(storyId, userId, storyName, likes, dislikes, isOpen, storyList);
    }
}
