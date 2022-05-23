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
import com.groupsix.project3_cst438.roomDB.entities.Story;

import java.util.List;

@Dao
public interface StoriesDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Stories... stories);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(Stories... stories);

    @Delete
    void delete(Stories stories);

    @Query("SELECT * FROM " + AppDatabase.STORIES_TABLE + " WHERE storiesId = :storiesId")
    Stories getStoriesById(int storiesId);

    @Query("SELECT * FROM " + AppDatabase.STORIES_TABLE + " WHERE userId = :userId")
    Stories getStoriesByUserId(int userId);

    @Query("SELECT * FROM " + AppDatabase.STORIES_TABLE)
    LiveData<List<Stories>> getAll();

    @Query("SELECT * FROM " + AppDatabase.STORIES_TABLE + " WHERE parentId = :parentId")
    LiveData<List<Stories>> getAllBelongingToStory(Integer parentId);
}
