package com.groupsix.project3_cst438.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.groupsix.project3_cst438.R;
import com.groupsix.project3_cst438.databinding.FragmentViewSingleStoryBinding;
import com.groupsix.project3_cst438.fragments.recyclerViews.ViewStoryAdapter;
import com.groupsix.project3_cst438.roomDB.entities.Story;
import com.groupsix.project3_cst438.viewmodels.StoryViewModel;

/**
 *  View a single story and its list of stories from this fragment
 */
public class ViewSingleStoryFragment extends Fragment {
    private FragmentViewSingleStoryBinding binding;
    private StoryViewModel storyViewModel;
    private Story mStory;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = null;
        storyViewModel = new ViewModelProvider(this).get(StoryViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentViewSingleStoryBinding.inflate(inflater, container, false);
        View view =  binding.getRoot();

        // Get fragment safe args - in this case storyId
        if (getArguments() != null) {
            int storyId = ViewSingleStoryFragmentArgs.fromBundle(getArguments()).getStoryId();
            // Story was previously stored in local database so use that instead of API
            mStory = storyViewModel.getLocalById(storyId);

            // Recycler view setup
            binding.recyclerSingleStoryStories.setLayoutManager(new LinearLayoutManager(getActivity()));
            binding.recyclerSingleStoryStories.setAdapter(new ViewStoryAdapter(getContext(), mStory));

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
                //controller.navigateUp();
            });
        }

        return view;
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