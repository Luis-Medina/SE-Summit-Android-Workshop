package com.summit.summitproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.summit.summitproject.prebuilt.login.LoginListener;
import com.summit.summitproject.prebuilt.login.LoginManager;
import com.summit.summitproject.prebuilt.model.Transaction;

import java.util.ArrayList;

/**
 * The first screen of our app. Takes in a username and password and interacts with the
 * {@link LoginManager} to retrieve user details. Also allows the user to check "Remember Me"
 * to locally store and recall credentials.
 */
public class LoginActivity extends AppCompatActivity {

    // UI Widgets

    private EditText username;

    private EditText password;

    private Button signIn;

    private ProgressBar progress;

    /**
     * Called the first time an Activity is created, but before any UI is shown to the user.
     * Prepares the layout and assigns UI widget variables.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        signIn = findViewById(R.id.sign_in);
        progress = findViewById(R.id.progress);

        setupWidgets();
    }

    /**
     * Set up listeners for various user interactions.
     */
    private void setupWidgets() {
        username.addTextChangedListener(textWatcher);
        password.addTextChangedListener(textWatcher);

        final String inputtedUsername = username.getText().toString();
        final String inputtedPassword = password.getText().toString();

        signIn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // Don't allow user input while logging in & show the progress bar
                setAllEnabled(false);
                progress.setVisibility(View.VISIBLE);

                // Instantiate the login manager, passing the username, password, and result listener
                LoginManager loginManager = new LoginManager(inputtedUsername, inputtedPassword, loginListener);

                // Kick off the login network call
                loginManager.execute();
            }
        });
    }

    /**
     * Used with {@link android.widget.TextView#addTextChangedListener(TextWatcher)} to get callbacks
     * when the user interacts with the text input widgets.
     */
    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}

        @Override
        public void afterTextChanged(Editable s) {
            // Enable the "Sign In" button if something has been inputted in both the username
            // and password.
            String usernameText = username.getText().toString().trim();
            String passwordText = password.getText().toString().trim();
            signIn.setEnabled(usernameText.length() > 0 && passwordText.length() > 0);
        }
    };

    private LoginListener loginListener = new LoginListener() {
        @Override
        public void onLoginSuccess(String name, String cardNum, ArrayList<Transaction> transactions) {
            // Allow user input (e.g. if the user returns to this screen) and
            // hide the progress bar again
            setAllEnabled(true);
            progress.setVisibility(View.INVISIBLE);

            Toast.makeText(LoginActivity.this, "Login success!", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onLoginError(Exception exception) {
            // Allow user input (eg. if the user returns to this screen) and
            // hide the progress bar again
            setAllEnabled(true);
            progress.setVisibility(View.INVISIBLE);

            Toast.makeText(LoginActivity.this, "Failed to login", Toast.LENGTH_LONG).show();
        }
    };

    /**
     * Enables or disables user interactions with the text input widgets & sign in button.
     */
    private void setAllEnabled(boolean enabled) {
        username.setEnabled(enabled);
        password.setEnabled(enabled);
        signIn.setEnabled(enabled);
    }
}

