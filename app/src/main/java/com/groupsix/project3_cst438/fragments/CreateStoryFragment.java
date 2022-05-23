package com.groupsix.project3_cst438.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.groupsix.project3_cst438.R;
import com.groupsix.project3_cst438.databinding.FragmentCreateStoryBinding;
import com.groupsix.project3_cst438.retrofit.StoryResponse;
import com.groupsix.project3_cst438.roomDB.entities.Stories;
import com.groupsix.project3_cst438.roomDB.entities.Story;
import com.groupsix.project3_cst438.roomDB.entities.StoryLikes;
import com.groupsix.project3_cst438.viewmodels.StoriesViewModel;
import com.groupsix.project3_cst438.viewmodels.StoryViewModel;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *  Fragment for creating a new story
 *  Useful Resources:
 *
 *  How binding works (alternative to find view by id)
 *  https://developer.android.com/topic/libraries/view-binding#java
 */

public class CreateStoryFragment extends Fragment {
    // Note: Fragment owned object in onCreateView is reset every time fragment is restored.
    // Binding for create story fragment, each fragment has its own binding
    private FragmentCreateStoryBinding binding;

    private StoryViewModel storyViewModel;
    private StoriesViewModel storiesViewModel;

    String mStoryName;
    String mInitialStory;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = null;
        storyViewModel = new ViewModelProvider(this).get(StoryViewModel.class);
        storiesViewModel = new ViewModelProvider(this).get(StoriesViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCreateStoryBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.createStoryBtn.setOnClickListener(view1 -> {
            view1.setOnClickListener(null); // Prevent multiple clicks
            System.out.println("Clicked create story Button");

            // 1. Create the story first.
            // 2. Insert into backend database
            // 3. Observe live data for changes
            // 4. Get updated storyId from response
            // 5. Create stories object and use story as parent of it.
            // 6. Insert stories into backend database
            // 7. Update story in backend database

            List<Stories> storyList = new ArrayList<>();
            // TODO: Replace with shared preferences user Id
            Story story =  new Story(2, "empty",0 ,0 ,true, storyList);
            // Create story first, null object
            if (getInputFields()) {
                story.setStoryName(mStoryName);
                storyViewModel.insertExternal(story); // Insert story to get its ID
            }

            storyViewModel.getStoryResponseLiveData().observe(getViewLifecycleOwner(), storyResponse -> {
                System.out.println("New story was inserted and livedata observed");
                Stories stories =  new Stories(2, mInitialStory, storyResponse.getStory(), storyResponse.getStoryId());
                story.setStoryId(storyResponse.getStoryId());
                storiesViewModel.insertExternal(stories);
            });

            storiesViewModel.getStoriesResponseLiveData().observe(getViewLifecycleOwner(), storiesResponse -> {
                System.out.println("New stories was inserted and observed");
                storyList.add(storiesResponse.getStory());
                story.setStoryList(storyList);
                storyViewModel.updateExternalStoriesList(story);
            });

            storyViewModel.getStoryResponseUpdated().observe(getViewLifecycleOwner(), storyResponse1 -> {
                System.out.println("Previously created story was updated and observed");
                Toast.makeText(getActivity(), "Story created successfully", Toast.LENGTH_SHORT).show();
                binding.initialStoryEditText.setText("");
                binding.storyNameEditText.setText("");

                assert storyResponse1 != null;
                storyViewModel.insertLocal(storyResponse1.getStory());

                // TODO: Check if it exists already. One entry per user per story
                StoryLikes storyLikes = new StoryLikes(storyResponse1.getStoryId(), storyResponse1.getUserId(), false, false);
                storyViewModel.insertLikesEntryExternal(storyLikes);
            });

            storyViewModel.getStoryLikesResponseLiveData().observe(getViewLifecycleOwner(), storyLikesResponse -> {
                System.out.println("New likes entry was inserted and observed");
                // TODO: Check if it already exists in room database if so update it if not insert it
                storyViewModel.insertLocalLikesEntry(storyLikesResponse.getStoryLikesObject());

                // Now navigate to fragment where you can view it (Stopped working)
                //System.out.println("Navigating to Viewing single story fragment Current ID is: " + story.getStoryId());
                //NavDirections action = CreateStoryFragmentDirections.actionCreateStoryFragmentToViewSingleStoryFragment().setStoryId(story.getStoryId());
                //NavHostFragment.findNavController(CreateStoryFragment.this).navigate(action);
                NavHostFragment.findNavController(CreateStoryFragment.this).popBackStack(R.id.createStoryFragment, false);
            });
        });
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private boolean getInputFields() {
        mStoryName = Objects.requireNonNull(binding.storyNameEditText.getText()).toString();
        mInitialStory = Objects.requireNonNull(binding.initialStoryEditText.getText()).toString();

        if (mStoryName.isEmpty() || mInitialStory.isEmpty()) {
            Toast.makeText(getActivity(), "Fill all fields", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}