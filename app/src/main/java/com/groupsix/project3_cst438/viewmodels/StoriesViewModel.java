package com.groupsix.project3_cst438.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.groupsix.project3_cst438.repository.AppRepository;
import com.groupsix.project3_cst438.retrofit.StoriesResponse;
import com.groupsix.project3_cst438.retrofit.StoryResponse;
import com.groupsix.project3_cst438.roomDB.entities.Stories;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class StoriesViewModel extends AndroidViewModel {
    private AppRepository mRepository;

    public StoriesViewModel(Application application) {
        super(application);
        mRepository = AppRepository.getRepoInstance(application.getApplicationContext());
    }

    public void insertLocal(Stories stories) { mRepository.insertLocalStories(stories);}
    public void insertExternal(Stories stories) {
        //TODO: insert in local if it doesn't exist yet.
        mRepository.insertStories(stories);
    }

    public void updateLocal(Stories stories) { mRepository.updateLocalStories(stories);}
    // TODO: Update Existing story from backend

    public void deleteLocal(Stories stories) { mRepository.deleteLocalStories(stories);}

    public Stories getLocalById(int storiesId) { return mRepository.getLocalStoriesById(storiesId); }
    public Stories getLocalByUserId(int userId) { return mRepository.getLocalStoriesByUserId(userId); }

    public void getExternalByUserIdAndStory(int userId, String story) {
        mRepository.getStoriesByUserIdAndStory(userId, story);
    }

    public LiveData<List<StoriesResponse>> getStoriesListResponseLiveData() {
        return mRepository.getStoriesListResponseLiveData();
    }

    public LiveData<StoriesResponse> getStoriesResponseLiveData() {
        return mRepository.getStoriesResponseLiveData();
    }
}
