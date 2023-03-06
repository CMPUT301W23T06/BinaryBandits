package com.example.binarybandits.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import com.example.binarybandits.R;

public class LogInActivity extends AppCompatActivity {

    EditText editUsernameField;
    Button loginBtn;
    Button signUpBtn;

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

        signUpBtn.setOnClickListener(new View.OnClickListener()   {
            public void onClick(View v)  {
                Intent myIntent = new Intent(LogInActivity.this, SignUpActivity.class);
                LogInActivity.this.startActivity(myIntent);
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener()   {
            public void onClick(View v)  {
                String username = editUsernameField.getText().toString();
                // - Intent myIntent = new Intent(LogInActivity.this, MainActivity.class);
                // - LogInActivity.this.startActivity(myIntent);
            }
        });
    }

}
