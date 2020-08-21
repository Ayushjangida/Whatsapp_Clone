package com.example.whatsappclone;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.whatsappclone.R;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText edtEmail, edtUsername, edtPassword;
    private Button btnSignUp, btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_main);

        edtEmail = findViewById(R.id.edt_email_signup);
        edtUsername = findViewById(R.id.edt_username_signup);
        edtPassword = findViewById(R.id.edt_password_signup);
        edtPassword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    onClick(btnSignUp);
                }
                return false;
            }
        });
        btnSignUp = findViewById(R.id.btn_signup_signup);
        btnLogin = findViewById(R.id.btn_login_signup);

        btnSignUp.setOnClickListener(this);
        btnLogin.setOnClickListener(this);

//        if (ParseUser.getCurrentUser() != null)  transitionToSocialMediaActivity();
    }




    @Override
    public void onClick(View v) {
        switch (v.getId())  {
            case R.id.btn_signup_signup :
                if( edtEmail.getText().toString().equals("")
                        || edtUsername.getText().toString().equals("")
                        || edtPassword.getText().toString().equals("")) {

                    Toast.makeText(SignUpActivity.this, "Empty field is not allowed", Toast.LENGTH_SHORT).show();
                    break;
                }
                final ParseUser user = new ParseUser();
                user.setEmail(edtEmail.getText().toString());
                user.setUsername(edtUsername.getText().toString());
                user.setPassword(edtPassword.getText().toString());

                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null)  {
                            Toast.makeText(SignUpActivity.this, edtUsername.getText().toString() + " signed up successfully!", Toast.LENGTH_SHORT).show();
//                            transitionToSocialMediaActivity();
                        }   else    {
                            Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
                break;

            case R.id.btn_login_signup :
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;

            default:
                break;
        }
    }

    public void rootLayoutTapped(View view) {

        try {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }   catch (Exception e) {
            e.printStackTrace();
        }
    }

//    public void transitionToSocialMediaActivity()   {
//        Intent intent = new Intent(SignUpActivity.this, SocialMediaActivity.class);
//        startActivity(intent);
//        finish();
//    }
}
