package com.groupsix.project3_cst438.roomDB.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.groupsix.project3_cst438.roomDB.AppDatabase;
import com.groupsix.project3_cst438.roomDB.entities.User;

import java.util.List;

@Dao
public interface UserDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(User... user);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(User... user);

    @Delete
    void delete(User user);

    @Query("SELECT * FROM " + AppDatabase.USER_TABLE + " WHERE username = :username")
    User getUserByUsername(String username);

    @Query("SELECT * FROM " + AppDatabase.USER_TABLE + " WHERE userId = :userId")
    User getUserByUserId(int userId);

    @Query("SELECT * FROM " + AppDatabase.USER_TABLE)
    LiveData<List<User>> getAllUsers();
}
