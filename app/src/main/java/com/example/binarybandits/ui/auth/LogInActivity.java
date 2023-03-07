package com.example.binarybandits.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

import com.example.binarybandits.MainActivity;
import com.example.binarybandits.R;
import com.example.binarybandits.controllers.AuthController;

public class LogInActivity extends AppCompatActivity {

    private EditText editUsernameField;
    private Button loginBtn;
    private Button signUpBtn;
    private Animation vibrate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if(getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        editUsernameField = findViewById(R.id.editUsername);
        loginBtn = findViewById(R.id.loginBtn);
        signUpBtn = findViewById(R.id.createAccountBtn);
        vibrate = AnimationUtils.loadAnimation(this, R.anim.vibrate);

        signUpBtn.setOnClickListener(new View.OnClickListener()   {
            public void onClick(View v)  {
                Intent myIntent = new Intent(LogInActivity.this, SignUpActivity.class);
                LogInActivity.this.startActivity(myIntent);
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener()   {
            public void onClick(View v)  {
                String username = editUsernameField.getText().toString();
                if (username.isEmpty()) {
                    editUsernameField.startAnimation(vibrate);
                } else {
                    AuthController.login(LogInActivity.this, username);
                }
            }
        });

        // TODO: uncomment this line in production
        // this is added to always start login page when app opens
        AuthController.setUserLoggedInStatus(this,false);

        if (AuthController.getUserLoggedInStatus(this)) {
            Intent myIntent = new Intent(LogInActivity.this, MainActivity.class);
            LogInActivity.this.startActivity(myIntent);
        }
    }

}
