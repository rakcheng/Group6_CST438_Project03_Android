package com.groupsix.project3_cst438.roomDB.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.groupsix.project3_cst438.roomDB.AppDatabase;
import com.groupsix.project3_cst438.roomDB.entities.Comment;
import com.groupsix.project3_cst438.roomDB.entities.User;

import java.util.List;

@Dao
public interface CommentDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Comment... comment);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(Comment... comment);

    @Delete
    void delete(Comment comment);

    @Query("SELECT * FROM " + AppDatabase.COMMENTS_TABLE + " WHERE commentId = :commentId")
    Comment getCommentById(int commentId);

    @Query("SELECT * FROM " + AppDatabase.COMMENTS_TABLE + " WHERE userId = :userId")
    Comment getCommentByUserId(int userId);

    @Query("SELECT * FROM " + AppDatabase.COMMENTS_TABLE)
    LiveData<List<Comment>> getAllComments();
}
