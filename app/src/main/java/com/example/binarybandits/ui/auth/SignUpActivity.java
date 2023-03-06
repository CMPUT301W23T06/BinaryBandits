package com.example.binarybandits.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import com.example.binarybandits.R;

public class SignUpActivity extends AppCompatActivity {

    EditText editUsernameField;
    EditText editFullNameField;
    EditText editPhoneField;
    Button signUpBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        if(getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        editUsernameField = findViewById(R.id.editUsernameSignUp);
        editFullNameField = findViewById(R.id.editFullNameSignUp);
        editPhoneField = findViewById(R.id.editPhoneSignUp);
        signUpBtn = findViewById(R.id.createAccountBtnSignUp);

        signUpBtn.setOnClickListener(new View.OnClickListener()   {
            public void onClick(View v)  {

            }
        });

    }

    public void onClickLogInBtn(View v) {
        Intent myIntent = new Intent(SignUpActivity.this, LogInActivity.class);
        SignUpActivity.this.startActivity(myIntent);
    }
}
