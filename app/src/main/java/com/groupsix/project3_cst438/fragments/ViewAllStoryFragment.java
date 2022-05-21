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

import com.groupsix.project3_cst438.R;
import com.groupsix.project3_cst438.databinding.FragmentViewAllStoryBinding;
import com.groupsix.project3_cst438.fragments.recyclerViews.RecyclerViewInterface;
import com.groupsix.project3_cst438.fragments.recyclerViews.ViewAllStoryAdapter;
import com.groupsix.project3_cst438.roomDB.entities.Story;
import com.groupsix.project3_cst438.viewmodels.StoryViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 *  A class to view a list of stories using a recycler view. Each item can be clicked.
 */

public class ViewAllStoryFragment extends Fragment implements RecyclerViewInterface {
    private FragmentViewAllStoryBinding binding;
    private StoryViewModel storyViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = null;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentViewAllStoryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        storyViewModel = new ViewModelProvider(this).get(StoryViewModel.class);
        // Observe changes to story list (stored in room database)
        storyViewModel.getAllLocal().observe(getViewLifecycleOwner(), stories -> {
            binding.recyclerAllStoryNames.setLayoutManager(new LinearLayoutManager(getActivity()));
            binding.recyclerAllStoryNames.setAdapter(new ViewAllStoryAdapter(getContext(), stories, this));
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    /**
     * When clicking on a story in the list of stories in the recycler view. Navigate to that single story view.
     * @param position for item
     * @param storyId is the story id of the selected story
     */
    @Override
    public void onItemClick(int position, int storyId) {
        NavDirections action = ViewAllStoryFragmentDirections.actionViewAllStoryToViewSingle().setStoryId(storyId);
        NavHostFragment.findNavController(ViewAllStoryFragment.this).navigate(action);
    }
}