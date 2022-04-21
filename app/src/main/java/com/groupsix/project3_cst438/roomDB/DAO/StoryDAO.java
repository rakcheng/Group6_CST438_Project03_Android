package com.groupsix.project3_cst438.roomDB.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.groupsix.project3_cst438.roomDB.AppDatabase;
import com.groupsix.project3_cst438.roomDB.entities.Story;

import java.util.List;

@Dao
public interface StoryDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Story... story);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(Story... story);

    @Delete
    void delete(Story story);

    @Query("SELECT * FROM " + AppDatabase.STORY_TABLE + " WHERE storyName = :storyName")
    Story getStoryByName(String storyName);

    @Query("SELECT * FROM " + AppDatabase.STORY_TABLE + " WHERE storyId = :storyId")
    Story getStoryById(int storyId);

    @Query("SELECT * FROM " + AppDatabase.STORY_TABLE + " WHERE userId = :userId")
    Story getStoryByUserId(int userId);

    @Query("SELECT * FROM " + AppDatabase.STORY_TABLE)
    LiveData<List<Story>> getAll();
}
