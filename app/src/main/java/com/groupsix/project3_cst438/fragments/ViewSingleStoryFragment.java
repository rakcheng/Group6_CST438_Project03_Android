package com.groupsix.project3_cst438.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import com.groupsix.project3_cst438.R;
import com.groupsix.project3_cst438.databinding.FragmentViewSingleStoryBinding;
import com.groupsix.project3_cst438.fragments.recyclerViews.ViewAllStoryAdapter;
import com.groupsix.project3_cst438.fragments.recyclerViews.ViewStoryAdapter;
import com.groupsix.project3_cst438.roomDB.entities.Stories;
import com.groupsix.project3_cst438.roomDB.entities.Story;
import com.groupsix.project3_cst438.viewmodels.StoriesViewModel;
import com.groupsix.project3_cst438.viewmodels.StoryViewModel;

import java.util.List;
import java.util.Objects;

/**
 *  View a single story and its list of stories from this fragment
 */
public class ViewSingleStoryFragment extends Fragment {
    private FragmentViewSingleStoryBinding binding;
    private StoryViewModel storyViewModel;
    private StoriesViewModel storiesViewModel;
    private Story mStory;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = null;
        storyViewModel = new ViewModelProvider(this).get(StoryViewModel.class);
        storiesViewModel = new ViewModelProvider(this).get(StoriesViewModel.class);

        // Get fragment safe args - in this case storyId
        if (getArguments() != null) {
            int storyId = ViewSingleStoryFragmentArgs.fromBundle(getArguments()).getStoryId();
            // Story was previously stored in local database so use that instead of API
            mStory = storyViewModel.getLocalById(storyId);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentViewSingleStoryBinding.inflate(inflater, container, false);
        View view =  binding.getRoot();

        binding.recyclerSingleStoryStories.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recyclerSingleStoryStories.setAdapter(new ViewStoryAdapter(getContext(), mStory.getStoryList()));

        binding.addStoriesBtn.setOnClickListener(view12 -> {
            String sentence = Objects.requireNonNull(binding.addToStoryEditText.getText()).toString();
            if (sentence.isEmpty()) {
                Toast.makeText(getActivity(), "Enter a sentence!", Toast.LENGTH_SHORT).show();
            } else {
                addToStory(sentence);
            }
        });

        // Setup text views to display story data
        binding.storyNameTextView.setText(mStory.getStoryName());
        setLikesAndDislikes();

        // Infinite like or dislikes, should have check to only limit to 1 like
        binding.likeBtn.setOnClickListener(view1 -> {
            mStory = storyViewModel.updateLikesAndDislikes(mStory, true, false);
            setLikesAndDislikes();
        });

        binding.dislikeBtn.setOnClickListener(view1 -> {
            mStory = storyViewModel.updateLikesAndDislikes(mStory, false, true);
            setLikesAndDislikes();
        });

        // If user clicks back button take them home
        binding.viewStoryBackBtn.setOnClickListener(view1 -> {
            NavController controller = NavHostFragment.findNavController(ViewSingleStoryFragment.this);
            controller.popBackStack();
        });

        // Hide button if story is closed or user is not creator of story
        // TODO: Shared preferences get user and compare with user that created story
        if (!mStory.getIsOpen()) {
            binding.finishStoryBtn.setVisibility(View.GONE);
        }

        // If user clicks finish button mark story as completed
        binding.finishStoryBtn.setOnClickListener(view1 -> {
            mStory.setIsOpen(false);
            storyViewModel.finishStoryExternal(mStory);
            storyViewModel.updateLocal(mStory);
            Toast.makeText(getActivity(), "Story closed successfully", Toast.LENGTH_SHORT).show();

            // Now pop backstack. Pops current fragment (view single story)
            NavController controller = NavHostFragment.findNavController(ViewSingleStoryFragment.this);
            controller.popBackStack();
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        // Update recyclerview whenever a story is added
        storiesViewModel.getAllLocalByStory(mStory).observe(getViewLifecycleOwner(), storiesList -> {
            binding.recyclerSingleStoryStories.setLayoutManager(new LinearLayoutManager(getActivity()));
            binding.recyclerSingleStoryStories.setAdapter(new ViewStoryAdapter(getContext(), storiesList));
        });

        // Refresh with swipe
        binding.swipeRefreshLayout.setOnRefreshListener(() -> {
            binding.recyclerSingleStoryStories.getAdapter().notifyDataSetChanged();
            binding.swipeRefreshLayout.setRefreshing(false);
        });
    }

    private void addToStory(String sentence) {
        List<Stories> storiesList = mStory.getStoryList();
        // TODO : Update userId once login is finished
        Stories stories = new Stories(1, sentence, mStory, mStory.getStoryId());
        // Insert new stories object into db
        storiesViewModel.insertExternal(stories);
        storiesViewModel.getStoriesResponseLiveData().observe(getViewLifecycleOwner(), storiesResponse -> {
            Stories stories1 = storiesResponse.getStory();
            stories1.setParentId(mStory.getStoryId());
            storiesList.add(stories1);
            storiesViewModel.insertLocal(stories1);
        });
        mStory.setStoryList(storiesList);
        storyViewModel.updateExternalStoriesList(mStory);

        storyViewModel.getStoryResponseUpdated().observe(getViewLifecycleOwner(), storyResponse -> {
            Toast.makeText(getActivity(), "Stories added successfully", Toast.LENGTH_SHORT).show();
            mStory = storyResponse.getStory();
            storyViewModel.updateLocal(mStory);
        });
    }

    private void setLikesAndDislikes() {
        binding.likeTextView.setText(String.format("%s", mStory.getLikes().toString()));
        binding.dislikeTextView.setText(String.format("%s", mStory.getDislikes().toString()));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}