package com.groupsix.project3_cst438.fragments.recyclerViews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.groupsix.project3_cst438.R;
import com.groupsix.project3_cst438.roomDB.entities.Stories;
import com.groupsix.project3_cst438.roomDB.entities.Story;

import java.util.List;

/**
 *  An adapter for a recycler view that displays a story and its list of stories
 */

public class ViewStoryAdapter extends RecyclerView.Adapter<ViewStoryAdapter.ViewHolderClass> {
    Context context;
    List<Stories> mStories;

    public ViewStoryAdapter(Context context, Story story) {
        this.context = context;
        this.mStories = story.getStoryList();
    }

    @NonNull
    @Override
    public ViewHolderClass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_story, parent, false);
        return new ViewHolderClass(view);
    }

    @Override
    public void onBindViewHolder(ViewHolderClass holder, int position) {
        holder.story_textView.setText(mStories.get(position).getStory());
    }

    @Override
    public int getItemCount() {
        return mStories.size();
    }

    public static class ViewHolderClass extends RecyclerView.ViewHolder {
        private final TextView story_textView;

        public ViewHolderClass(@NonNull View storyView) {
            super(storyView);

            story_textView = (TextView) storyView.findViewById(R.id.storyTextViewRecycler);

        }
    }
}
