package com.groupsix.project3_cst438.roomDB.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.groupsix.project3_cst438.roomDB.AppDatabase;

import java.util.Objects;

@Entity(tableName = AppDatabase.STORIES_TABLE)
public class Stories {
    @PrimaryKey(autoGenerate = true)
    private Integer storiesId;
    private Integer userId;
    private String story;

    public Stories(Integer userId, String story) {
        this.userId = userId;
        this.story = story;
    }

    public Integer getStoriesId() {
        return storiesId;
    }

    public void setStoriesId(Integer storiesId) {
        this.storiesId = storiesId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getStory() {
        return story;
    }

    public void setStory(String story) {
        this.story = story;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Stories stories = (Stories) o;
        return Objects.equals(storiesId, stories.storiesId) && Objects.equals(userId, stories.userId) && Objects.equals(story, stories.story);
    }

    @Override
    public int hashCode() {
        return Objects.hash(storiesId, userId, story);
    }
}
