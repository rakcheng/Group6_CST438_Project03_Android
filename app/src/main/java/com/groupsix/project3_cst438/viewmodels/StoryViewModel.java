package com.groupsix.project3_cst438.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.groupsix.project3_cst438.repository.AppRepository;
import com.groupsix.project3_cst438.retrofit.StoryResponse;
import com.groupsix.project3_cst438.roomDB.entities.Story;

import java.util.List;

public class StoryViewModel extends AndroidViewModel {
    private AppRepository mRepository;

    public StoryViewModel(@NonNull Application application) {
        super(application);
        mRepository = AppRepository.getRepoInstance(application.getApplicationContext());
    }

    public void insertLocal(Story story) { mRepository.insertLocalStory(story);}
    public void insertExternal(Story story) { mRepository.insertStory(story);}

    public void updateLocal(Story story) { mRepository.updateLocalStory(story);}
    // TODO: Update Existing story from backend
    public void finishStoryExternal(Story story) { mRepository.finishStory(story); }

    public void deleteLocal(Story story) { mRepository.deleteLocalStory(story);}

    public Story getLocalById(int storyId) { return mRepository.getLocalStoryById(storyId); }
    public Story getLocalByUserId(int userId) { return mRepository.getLocalStoryByUserId(userId); }
    public Story getLocalByName(String storyName) { return mRepository.getLocalStoryByName(storyName); }

    public LiveData<List<Story>> getAllLocal() {
        return mRepository.getAllLocalStoryLiveData();
    }


    // This will trigger livedata to change. Get livedata list
    public void getAllStory() {
        mRepository.getAllStory();
    }

    public void getAllOpenStory() {
        mRepository.getAllOpenStory();
    }

    public void getAllClosedStory() {
        mRepository.getAllClosedStory();
    }

    public LiveData<List<StoryResponse>> getStoryListResponseLiveData() {
        return mRepository.getStoryListResponseLiveData();
    }

    public LiveData<StoryResponse> getStoryResponseLiveData() {
        return mRepository.getStoryResponseLiveData();
    }
    // To get story from rest API observe livedata in activity and call methods from mRepository to get by story id ,user id, or story name
    // Then get storyResponseLivedata
}
