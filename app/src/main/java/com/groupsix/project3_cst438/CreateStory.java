package com.groupsix.project3_cst438;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.groupsix.project3_cst438.retrofit.StoryResponse;
import com.groupsix.project3_cst438.roomDB.entities.Story;
import com.groupsix.project3_cst438.viewmodels.StoryViewModel;

import java.util.ArrayList;
import java.util.List;

// TODO: Use fragments instead

public class CreateStory extends AppCompatActivity implements LifecycleObserver {
    private EditText mStoryNameField;
    private String mStoryName;
    private Button mCreateStoryBtn;
    private StoryViewModel storyViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_story);

        storyViewModel = new ViewModelProvider(this).get(StoryViewModel.class);
        getLifecycle().addObserver(this);

        setupDisplay();

        mCreateStoryBtn.setOnClickListener(view -> {
            if (getInputFields()) {
                // TODO: get userId from shared preferences
                // TODO: Add option to fill initial story
                List<Integer> stories = new ArrayList<>();
                Story story = new Story(1, mStoryName, stories);
                storyViewModel.insertExternal(story);
                storyViewModel.insertLocal(story);
                Toast.makeText(getApplicationContext(), "Story created successfully", Toast.LENGTH_SHORT).show();
            }
        });

        // TODO: add initial text option and create a "stories" object. Add it to new list and link
        //  that with the current story and user
    }

    private boolean storyExists() {
        Story story = storyViewModel.getLocalByName(mStoryName);
        // Story doesn't exist in room
        // TODO: Check backend too
        return story != null;

    }

    private boolean getInputFields() {
        mStoryName = mStoryNameField.getText().toString();

        if (mStoryName.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Fill all fields", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void setupDisplay() {
        mStoryNameField = findViewById(R.id.editTextStoryName);
        mCreateStoryBtn = findViewById(R.id.createStoryBtn);
    }

    public static Intent intentFactory(Context context) {
        Intent intent = new Intent(context, CreateStory.class);
        return intent;
    }
}