package com.groupsix.project3_cst438;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.groupsix.project3_cst438.roomDB.entities.User;
import com.groupsix.project3_cst438.viewmodels.UserViewModel;

public class MainActivity extends AppCompatActivity {

    // Move to another activity (testing purposes only)
    private UserViewModel mUserViewModel;
    private EditText mUsernameField;
    private EditText mPasswordField;
    private Button mCreateBtn;

    private User mUser;
    private String mUsername;
    private String mPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // MOVE THIS LATER TO ITS OWN ACTIVITY
        // Initialize user view model
        mUserViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        setupDisplay();

        mCreateBtn.setOnClickListener(view -> {
            if (getInputFields()) {
                User user = new User(mUsername, mPassword, false);
                mUserViewModel.insertExternal(user);
                mUserViewModel.insertLocal(user);

                mUser = mUserViewModel.getLocalByUsername(mUsername);
                Toast.makeText(getApplicationContext(), "Account created successfully", Toast.LENGTH_SHORT).show();
                // TODO: Login user and redirect to landing page
            }
        });

    }

    private boolean userExists() {
        mUser = mUserViewModel.getLocalByUsername(mUsername);

        if(mUser != null) {
            return true;
        } else {
            // TODO: Check backend for existing user.
            return false;
        }
    }

    private void setupDisplay() {
        mUsernameField = findViewById(R.id.username);
        mPasswordField = findViewById(R.id.password);
        mCreateBtn = findViewById(R.id.createAccountBtn);
    }

    private boolean getInputFields() {
        mUsername = mUsernameField.getText().toString();
        mPassword = mPasswordField.getText().toString();

        if(mUsername.isEmpty() || mPassword.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Fill Empty fields", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public static Intent intentFactory(Context context) {
        return new Intent(context, MainActivity.class);
    }
}