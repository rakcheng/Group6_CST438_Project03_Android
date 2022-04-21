package com.groupsix.project3_cst438.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.groupsix.project3_cst438.repository.AppRepository;
import com.groupsix.project3_cst438.roomDB.entities.User;

import java.util.List;

public class UserViewModel extends AndroidViewModel {
    private AppRepository mRepository;
    private LiveData<List<User>> mUsers;

    public UserViewModel(@NonNull Application application) {
        super(application);
        mRepository = AppRepository.getRepoInstance(application.getApplicationContext());
        mUsers = mRepository.getAllUsers();
    }

    public void insertLocal(User user) { mRepository.insertLocalUser(user);}
    public void insertExternal(User user) { mRepository.insertUser(user);}

    public void updateLocal(User user) { mRepository.updateLocalUser(user);}
    // TODO: update external user (for backend db)

    public void deleteLocal(User user) { mRepository.deleteLocalUser(user);}

    public User getLocalById(int userId) { return mRepository.getLocalUserById(userId);}
    public User getLocalByUsername(String username) { return mRepository.getLocalUserByUsername(username);}
}
