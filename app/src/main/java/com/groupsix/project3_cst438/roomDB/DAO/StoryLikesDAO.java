package com.groupsix.project3_cst438.roomDB.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.groupsix.project3_cst438.roomDB.AppDatabase;
import com.groupsix.project3_cst438.roomDB.entities.Stories;
import com.groupsix.project3_cst438.roomDB.entities.StoryLikes;

import java.util.List;

@Dao
public interface StoryLikesDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(StoryLikes... storyLikes);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(StoryLikes... storyLikes);

    @Delete
    void delete(StoryLikes storyLikes);

    @Query("SELECT * FROM " + AppDatabase.LIKES_TABLE + " WHERE storyId = :storyId " + " AND userId = :userId")
    StoryLikes getByStoryIdAndUserId(int storyId, int userId);

    @Query("SELECT * FROM " + AppDatabase.LIKES_TABLE)
    LiveData<List<StoryLikes>> getAll();
}
