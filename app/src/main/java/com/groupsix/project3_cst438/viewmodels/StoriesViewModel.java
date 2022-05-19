package com.groupsix.project3_cst438.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.groupsix.project3_cst438.repository.StoriesRepository;
import com.groupsix.project3_cst438.retrofit.StoriesResponse;
import com.groupsix.project3_cst438.roomDB.entities.Stories;

import java.util.List;

public class StoriesViewModel extends AndroidViewModel {
    private StoriesRepository mStoriesRepo;

    public StoriesViewModel(Application application) {
        super(application);
        mStoriesRepo = StoriesRepository.getRepoInstance(application.getApplicationContext());
    }

    public void insertLocal(Stories stories) { mStoriesRepo.insertLocalStories(stories);}
    public void insertExternal(Stories stories) {
        //TODO: insert in local if it doesn't exist yet.
        mStoriesRepo.insertStories(stories);
    }

    public void updateLocal(Stories stories) { mStoriesRepo.updateLocalStories(stories);}
    // TODO: Update Existing story from backend

    public void deleteLocal(Stories stories) { mStoriesRepo.deleteLocalStories(stories);}

    public Stories getLocalById(int storiesId) { return mStoriesRepo.getLocalStoriesById(storiesId); }
    public Stories getLocalByUserId(int userId) { return mStoriesRepo.getLocalStoriesByUserId(userId); }

    public void getExternalByUserIdAndStory(int userId, String story) {
        mStoriesRepo.getStoriesByUserIdAndStory(userId, story);
    }

    public LiveData<List<StoriesResponse>> getStoriesListResponseLiveData() {
        return mStoriesRepo.getStoriesListResponseLiveData();
    }

    public LiveData<StoriesResponse> getStoriesResponseLiveData() {
        return mStoriesRepo.getStoriesResponseLiveData();
    }
}
