package com.groupsix.project3_cst438.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.groupsix.project3_cst438.repository.AppRepository;
import com.groupsix.project3_cst438.roomDB.entities.Story;

import java.util.List;

public class StoryViewModel extends AndroidViewModel {
    private AppRepository mRepository;
    private LiveData<List<Story>> mStory;

    public StoryViewModel(@NonNull Application application) {
        super(application);
        mRepository = AppRepository.getRepoInstance(application.getApplicationContext());
        mStory = mRepository.getAllStory();
    }

    public void insertLocal(Story story) { mRepository.insertLocalStory(story);}
    //public void insertExternal(Story story) { mRepository.insertStory(story);}

    public void updateLocal(Story story) { mRepository.updateLocalStory(story);}
    // TODO: Update Existing story from backend

    public void deleteLocal(Story story) { mRepository.deleteLocalStory(story);}

    public Story getLocalById(int storyId) { return mRepository.getLocalStoryById(storyId); }
    public Story getLocalByUserId(int userId) { return mRepository.getLocalStoryByUserId(userId); }
    public Story getLocalByName(String storyName) { return mRepository.getLocalStoryByName(storyName); }
}
