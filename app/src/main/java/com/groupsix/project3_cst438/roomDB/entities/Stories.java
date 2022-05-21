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

    private Story storyParent;

    public Stories(Integer userId, String story, Story storyParent) {
        this.userId = userId;
        this.story = story;
        this.storyParent = storyParent;
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

    public Story getStoryParent() {
        return storyParent;
    }

    public void setStoryParent(Story storyParent) {
        this.storyParent = storyParent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Stories stories = (Stories) o;
        return Objects.equals(storiesId, stories.storiesId) && Objects.equals(userId, stories.userId) && Objects.equals(story, stories.story) && Objects.equals(storyParent, stories.storyParent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(storiesId, userId, story, storyParent);
    }
}
