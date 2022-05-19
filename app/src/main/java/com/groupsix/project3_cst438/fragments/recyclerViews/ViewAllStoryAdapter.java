package com.groupsix.project3_cst438.fragments.recyclerViews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.groupsix.project3_cst438.R;
import com.groupsix.project3_cst438.roomDB.entities.Story;

import java.util.List;

/**
 *  Recycler view adapter to view all story
 *  Useful resources:
 *
 *  Add onclick listener to recycler view
 *  https://www.youtube.com/watch?v=7GPUpvcU1FE
 */

public class ViewAllStoryAdapter extends RecyclerView.Adapter<ViewAllStoryAdapter.ViewHolderClass> {
    private final RecyclerViewInterface recylerViewInterface;

    private Context context;
    private List<Story> mStoryList;

    public ViewAllStoryAdapter(Context context, List<Story> mStoryList, RecyclerViewInterface recylerViewInterface) {
        this.context = context;
        this.mStoryList = mStoryList;
        this.recylerViewInterface = recylerViewInterface;
    }

    @NonNull
    @Override
    public ViewHolderClass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_story_name_only, parent, false);
        return new ViewHolderClass(view, recylerViewInterface);
    }

    @Override
    public void onBindViewHolder(ViewHolderClass holder, int position) {
        String storyStatus;

        // Set text for text views here
        holder.storyName_textView.setText(mStoryList.get(position).getStoryName());
        holder.itemView.setId(mStoryList.get(position).getStoryId()); // Set item id to storyId
        storyStatus = mStoryList.get(position).getOpen() ? "Open" : "Closed";
        holder.storyStatus_textView.setText(String.format("Status: %s", storyStatus));
    }

    @Override
    public int getItemCount() {
        return mStoryList.size();
    }

    public static class ViewHolderClass extends RecyclerView.ViewHolder {
        private final TextView storyName_textView;
        private final TextView storyStatus_textView;

        public ViewHolderClass(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);

            storyName_textView = (TextView) itemView.findViewById(R.id.storyNameTextViewRecycler);
            storyStatus_textView = (TextView) itemView.findViewById(R.id.storyStatusTextView);

            // Make each item have an onclick listener
            itemView.setOnClickListener(view -> {
                if (recyclerViewInterface != null) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                       recyclerViewInterface.onItemClick(pos, view.getId());
                    }
                }
            });

        }
    }
}
