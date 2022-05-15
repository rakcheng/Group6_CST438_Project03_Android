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
import com.groupsix.project3_cst438.viewmodels.StoriesViewModel;
import com.groupsix.project3_cst438.viewmodels.StoryViewModel;

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
            if (getInputFields()) {
                // TODO: Replace with shared preferences user Id
                Stories stories = new Stories(2, mInitialStory);
                storiesViewModel.insertExternal(stories);
            }

            storiesViewModel.getStoriesResponseLiveData().observe(getViewLifecycleOwner(), storiesResponse -> {
                System.out.println("Stories livedata has been observed!");

                // Stories now has updated storiesId from external database
                List<Stories> storyList = new ArrayList<>();
                storyList.add(storiesResponse.getStory());

                Story story = new Story(2, mStoryName, storyList);

                // Once other endpoints are done check if it already exists in local/backend database
                storyViewModel.insertExternal(story);
                Toast.makeText(getActivity(), "Story created successfully", Toast.LENGTH_SHORT).show();

                binding.initialStoryEditText.setText("");
                binding.storyNameEditText.setText("");

                // Pass storyId argument to fragment once story live data changes
                storyViewModel.getStoryResponseLiveData().observe(getViewLifecycleOwner(), storyResponse -> {
                    assert storyResponse != null;
                    storyViewModel.insertLocal(storyResponse.getStory());

                    // Pass safe args (storyId)
                    NavDirections action = CreateStoryFragmentDirections.actionCreateStoryFragmentToViewSingleStoryFragment().setStoryId(storyResponse.getStoryId());
                    NavHostFragment.findNavController(CreateStoryFragment.this).navigate(action);
                });
            });
        });
        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
        storiesViewModel.getStoriesListResponseLiveData().removeObservers(getViewLifecycleOwner());
        storyViewModel.getStoryResponseLiveData().removeObservers(getViewLifecycleOwner());
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