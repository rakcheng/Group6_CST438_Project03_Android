package com.groupsix.project3_cst438.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.groupsix.project3_cst438.retrofit.CommentResponse;
import com.groupsix.project3_cst438.retrofit.RetrofitClient;
import com.groupsix.project3_cst438.retrofit.UserResponse;
import com.groupsix.project3_cst438.roomDB.AppDatabase;
import com.groupsix.project3_cst438.roomDB.DAO.CommentDAO;
import com.groupsix.project3_cst438.roomDB.DAO.UserDAO;
import com.groupsix.project3_cst438.roomDB.entities.Comment;

import java.util.List;
import java.util.concurrent.Future;

public class CommentRepository {
    public static CommentRepository repoInstance;
    private AppDatabase mRoomDb;
    private CommentDAO mCommentDao;

    RetrofitClient mRetrofitClient;

    public MutableLiveData<CommentResponse> commentResponseMutableLiveData;
    public MutableLiveData<List<CommentResponse>> commentResponseListMutableLiveData;

    public CommentRepository(Context context) {
        mRetrofitClient = RetrofitClient.getInstance(context);
        mRoomDb = AppDatabase.getInstance(context);
        mCommentDao = mRoomDb.getCommentsDAO();

        commentResponseMutableLiveData = new MutableLiveData<>();
        commentResponseListMutableLiveData = new MutableLiveData<>();
    }

    public static CommentRepository getRepoInstance(Context context) {
        if (repoInstance == null) {
            repoInstance = new CommentRepository(context);
        }
        return repoInstance;
    }

    public LiveData<List<Comment>> getAllLocalCommentsLiveData() { return mCommentDao.getAllComments(); }
    public LiveData<CommentResponse> getCommentResponseLiveData() { return commentResponseMutableLiveData; }
    public LiveData<List<CommentResponse>> getListCommentResponseLiveData() { return commentResponseListMutableLiveData; }

    public void insertLocalComment(Comment comment) {
        AppDatabase.databaseWriteExecutor.execute(() ->{
            mCommentDao.insert(comment);
            mCommentDao = mRoomDb.getCommentsDAO();
        });
    }

    public void updateLocalComment(Comment comment) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            mCommentDao.update(comment);
            mCommentDao = mRoomDb.getCommentsDAO();
        });
    }

    public void deleteLocalComment(Comment comment) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            mCommentDao.delete(comment);
            mCommentDao = mRoomDb.getCommentsDAO();
        });
    }

    public Comment getLocalCommentById(int commentId) {
        Future<Comment> commentFuture = AppDatabase.databaseWriteExecutor.submit(() -> mCommentDao.getCommentById(commentId));
        Comment comment = null;

        try {
            comment = commentFuture.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return comment;
    }

    public Comment getLocalCommentByUserId(int userId) {
        Future<Comment> commentFuture = AppDatabase.databaseWriteExecutor.submit(() -> mCommentDao.getCommentByUserId(userId));
        Comment comment = null;


        try {
            comment = commentFuture.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return comment;
    }

    // ====================== REST OPERATIONS ======================================================

    // TODO: ADD COMMENT REST API ENDPOINTS TO BACKEND AND CONSUME HERE
}
