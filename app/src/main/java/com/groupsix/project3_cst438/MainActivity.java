package com.groupsix.project3_cst438;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button goToCreateStory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupDisplay();

        goToCreateStory.setOnClickListener(view -> {
            Intent intent = CreateStory.intentFactory(getApplicationContext());
            startActivity(intent);
        });

    }

    private void setupDisplay() {
        goToCreateStory = findViewById(R.id.goCreateStoryBtn);

    }

    public static Intent intentFactory(Context context) {
        return new Intent(context, MainActivity.class);
    }
}