package com.groupsix.project3_cst438.roomDB.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.groupsix.project3_cst438.roomDB.AppDatabase;

import java.util.List;
import java.util.Objects;

@Entity(tableName = AppDatabase.STORY_TABLE)
public class Story {
    @PrimaryKey(autoGenerate = true)
    private Integer storyId;

    // Make it foreign key
    private Integer userId;

    private String storyName;

    // Changed it to primitive or else type converter wasn't working.
    // It will store a list of integers (of storiesId per element)
    private List<Integer> storyList;


    public Story(Integer userId, String storyName, List<Integer> storyList) {
        this.userId = userId;
        this.storyName = storyName;
        this.storyList = storyList;
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

    public List<Integer> getStoryList() {
        return storyList;
    }

    public void setStoryList(List<Integer> storyList) {
        this.storyList = storyList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Story story = (Story) o;
        return Objects.equals(storyId, story.storyId) && Objects.equals(userId, story.userId) && Objects.equals(storyName, story.storyName) && Objects.equals(storyList, story.storyList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(storyId, userId, storyName, storyList);
    }
}
