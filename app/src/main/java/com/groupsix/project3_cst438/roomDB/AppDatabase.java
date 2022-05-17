package com.groupsix.project3_cst438.roomDB;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.groupsix.project3_cst438.roomDB.DAO.CommentDAO;
import com.groupsix.project3_cst438.roomDB.DAO.StoryLikesDAO;
import com.groupsix.project3_cst438.roomDB.DAO.StoriesDAO;
import com.groupsix.project3_cst438.roomDB.DAO.StoryDAO;
import com.groupsix.project3_cst438.roomDB.DAO.UserDAO;
import com.groupsix.project3_cst438.roomDB.entities.Comment;
import com.groupsix.project3_cst438.roomDB.entities.Stories;
import com.groupsix.project3_cst438.roomDB.entities.Story;
import com.groupsix.project3_cst438.roomDB.entities.StoryLikes;
import com.groupsix.project3_cst438.roomDB.entities.User;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {User.class, Story.class, Stories.class, Comment.class, StoryLikes.class}, version = 1, exportSchema = false)
@TypeConverters(CustomTypeConverters.class)
public abstract class AppDatabase extends RoomDatabase {
    public static final String DB_NAME = "LOCAL_ROOM_DATABASE";
    public static final String USER_TABLE = "USER_TABLE";
    public static final String STORY_TABLE = "STORY_TABLE";
    public static final String STORIES_TABLE = "STORIES_TABLE";
    public static final String COMMENTS_TABLE = "COMMENTS_TABLE";
    public static final String LIKES_TABLE = "LIKES_TABLE";

    private static volatile AppDatabase dbInstance;
    private static final Object LOCK = new Object();

    private static final int NUM_THREADS = 1;
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUM_THREADS);

    public abstract UserDAO getUserDAO();
    public abstract StoryDAO getStoryDAO();
    public abstract StoriesDAO getStoriesDAO();
    public abstract CommentDAO getCommentsDAO();
    public abstract StoryLikesDAO getStoryLikesDAO();

    // Create single instance of database.
    public static AppDatabase getInstance(final Context context) {
        if(dbInstance == null) {
            synchronized (LOCK) {
                if(dbInstance == null) {
                    dbInstance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, DB_NAME)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return dbInstance;
    }
}
