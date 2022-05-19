package com.groupsix.project3_cst438.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.groupsix.project3_cst438.repository.StoryLikesRepository;
import com.groupsix.project3_cst438.repository.StoryRepository;
import com.groupsix.project3_cst438.retrofit.StoryLikesResponse;
import com.groupsix.project3_cst438.retrofit.StoryResponse;
import com.groupsix.project3_cst438.roomDB.entities.Story;
import com.groupsix.project3_cst438.roomDB.entities.StoryLikes;

import java.util.List;

public class StoryViewModel extends AndroidViewModel {
    private final StoryRepository mStoryRepo;
    private final StoryLikesRepository mStoryLikesRepo;

    public StoryViewModel(@NonNull Application application) {
        super(application);
        mStoryRepo = StoryRepository.getRepoInstance(application.getApplicationContext());
        mStoryLikesRepo = StoryLikesRepository.getRepoInstance(application.getApplicationContext());
    }

    // ============ GET LIVE DATA ===============
    public LiveData<List<StoryResponse>> getStoryListResponseLiveData() { return mStoryRepo.getStoryListResponseLiveData(); }
    public LiveData<StoryResponse> getStoryResponseLiveData() { return mStoryRepo.getStoryResponseLiveData(); }
    public LiveData<StoryLikesResponse> getStoryLikesResponseLiveData() { return mStoryLikesRepo.getStoryLikesResponseLiveData(); }
    public LiveData<List<Story>> getAllLocal() {
        return mStoryRepo.getAllLocalStoryLiveData();
    }
    // ==========================================

    public void insertLocal(Story story) { mStoryRepo.insertLocalStory(story);}
    public void insertExternal(Story story) { mStoryRepo.insertStory(story);}

    public void updateLocal(Story story) { mStoryRepo.updateLocalStory(story);}
    public void updateLikesCount(Story story) { mStoryRepo.updateStoryLikesCount(story); }
    public void updateDislikesCount(Story story) { mStoryRepo.updateStoryDislikesCount(story); }
    public void finishStoryExternal(Story story) { mStoryRepo.finishStory(story); }
    public void deleteLocal(Story story) { mStoryRepo.deleteLocalStory(story);}

    public Story getLocalById(int storyId) { return mStoryRepo.getLocalStoryById(storyId); }
    public Story getLocalByUserId(int userId) { return mStoryRepo.getLocalStoryByUserId(userId); }
    public Story getLocalByName(String storyName) { return mStoryRepo.getLocalStoryByName(storyName); }

    public StoryLikes getLocalStoryLikesByStoryIdAndUserId(int storyId, int userId) { return mStoryLikesRepo.getLocalLikesByStoryIdAndUserId(storyId, userId); }

    public void getAllStory() {
        mStoryRepo.getAllStory();
    }
    public void getAllOpenStory() {
        mStoryRepo.getAllOpenStory();
    }
    public void getAllClosedStory() {
        mStoryRepo.getAllClosedStory();
    }

    public void insertLikesEntryExternal(StoryLikes storyLikes) { mStoryLikesRepo.insertLikesEntry(storyLikes);}
    public void insertLocalLikesEntry(StoryLikes storyLikes) { mStoryLikesRepo.insertLocalLikesEntry(storyLikes);}
    public void updateStoryIsLiked(StoryLikes storyLikes) { mStoryLikesRepo.updateStoryLikesIsLiked(storyLikes); }
    public void updateStoryIsDisliked(StoryLikes storyLikes) { mStoryLikesRepo.updateStoryLikesIsDisliked(storyLikes); }
    public void updateLocalStoryLikesEntry(StoryLikes storyLikes) { mStoryLikesRepo.updateLocalLikesEntry(storyLikes);}

    public Story updateLikesAndDislikes(Story story, boolean isLike, boolean isDislike) {
        // Get likes entry belonging to this story. Use it to check if story has been liked/disliked by user
        StoryLikes storyLikes = getLocalStoryLikesByStoryIdAndUserId(story.getStoryId(), story.getUserId());

        // Current Like and dislike count belonging to this story
        int likes = story.getLikes();
        int dislikes = story.getDislikes();

        // If not null then user has probably liked this story
        if (storyLikes != null) {
            if (storyLikes.isLiked() && isLike) {
                return story;
            } else if (storyLikes.isDisliked() && isDislike) {
                return story;
            }

            // If isLike true story is being liked. If story hasn't already been liked by user increment it
            if (isLike && !storyLikes.isLiked()) {
                likes++;
                dislikes--;
                // Story was liked so update database to reflect that user has liked this story
                storyLikes.setLiked(true);
                storyLikes.setDisliked(false);
                updateStoryIsLiked(storyLikes);
            } else if (isDislike && !storyLikes.isDisliked()) {
                // If isDislike true then story is being disliked. If story hasn't already been disliked increment it
                likes--;
                dislikes++;
                // Story was disliked. Update database to reflect user disliked story
                storyLikes.setDisliked(true);
                storyLikes.setLiked(false);
                updateStoryIsDisliked(storyLikes);
            }
            // Update story likes entry for current story and user.
            updateLocalStoryLikesEntry(storyLikes);

        } else {
            // User has not liked this story
            if (isLike) {
                likes++;
                dislikes--;
            } else if (isDislike) {
                likes--;
                dislikes++;
            }
        }

        // Make sure likes and dislikes doesn't go below 0
        if (likes < 0) {
            likes = 0;
        } else if (dislikes < 0) {
            dislikes = 0;
        }

        // Set the current story's likes and disliked to updated values
        story.setLikes(likes);
        story.setDislikes(dislikes);

        // Update count of likes or disliked. Makes API call.
        updateLikesCount(story);
        updateDislikesCount(story);

        return story;
    }
}
