package com.example.binarybandits.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import com.example.binarybandits.R;
import com.example.binarybandits.controllers.AuthController;

/**
 * An activity got the Sign-Up Page
 */
public class SignUpActivity extends AppCompatActivity {

    private EditText editUsernameField;
    private EditText editFullNameField;
    private EditText editPhoneField;
    private Button signUpBtn;
    private Animation vibrate;

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
        vibrate = AnimationUtils.loadAnimation(this, R.anim.vibrate);

        signUpBtn.setOnClickListener(new View.OnClickListener()   {
            public void onClick(View v)  {
                String name = editFullNameField.getText().toString();
                String username = editUsernameField.getText().toString();
                String phone = editPhoneField.getText().toString();

                if (username.isEmpty()) {
                    editUsernameField.startAnimation(vibrate);
                } else if (name.isEmpty()) {
                    editFullNameField.startAnimation(vibrate);
                } else {
                    if(phone.isEmpty()) {
                        phone = null;
                    }
                    AuthController.register(SignUpActivity.this, username, name, phone);
                    editUsernameField.setText("");
                    editFullNameField.setText("");
                    editPhoneField.setText("");
                }
            }
        });

    }

    /**
     * Function to redirect to LogInActivity on click
     * @param v A refernce to the view that was just clicked
     */
    public void onClickLogInBtn(View v) {
        Intent myIntent = new Intent(SignUpActivity.this, LogInActivity.class);
        SignUpActivity.this.startActivity(myIntent);
    }
}
