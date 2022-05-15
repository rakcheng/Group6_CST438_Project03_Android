package com.groupsix.project3_cst438.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.groupsix.project3_cst438.R;
import com.groupsix.project3_cst438.databinding.FragmentViewSingleStoryBinding;
import com.groupsix.project3_cst438.fragments.adapters.ViewStoryAdapter;
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
        View view = binding.getRoot();

        // Recycler view setup
        binding.recyclerSingleStoryStories.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recyclerSingleStoryStories.setAdapter(new ViewStoryAdapter(getContext(),mStory.getStoryList()));

        binding.storyNameTextView.setText(mStory.getStoryName());

        binding.viewStoryBackBtn.setOnClickListener(view1 -> {
            NavController controller = NavHostFragment.findNavController(ViewSingleStoryFragment.this);
            controller.navigate(R.id.actionSingleStoryToHome);
        });

        binding.finishStoryBtn.setOnClickListener(view1 -> {
            mStory.setOpen(false);
            storyViewModel.finishStoryExternal(mStory);
            storyViewModel.updateLocal(mStory);
            Toast.makeText(getActivity(), "Story closed successfully", Toast.LENGTH_SHORT).show();

            // Now redirect to home and reset fragment
            NavController controller = NavHostFragment.findNavController(ViewSingleStoryFragment.this);
            controller.popBackStack();
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}