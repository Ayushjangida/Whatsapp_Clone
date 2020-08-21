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

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText edtEmail, edtPassword;
    private Button btnLogin, btnSignUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtEmail = findViewById(R.id.edt_email_login);
        edtPassword = findViewById(R.id.edt_password_login);
        edtPassword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    onClick(btnLogin);
                }
                return false;
            }
        });
        btnLogin = findViewById(R.id.btn_login_login);
        btnSignUp = findViewById(R.id.btn_signup_login);

        btnLogin.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())  {
            case R.id.btn_login_login :
                if (edtEmail.getText().toString().equals("") || edtPassword.getText().toString().equals(""))    {
                    Toast.makeText(LoginActivity.this, "Please enter Email/Password", Toast.LENGTH_SHORT).show();
                }
                ParseUser.logInInBackground(edtEmail.getText().toString(), edtPassword.getText().toString(), new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        if (e == null)  {
                            if(user != null)    {
                                Toast.makeText(LoginActivity.this, ParseUser.getCurrentUser().getUsername() + " is logged in successfully", Toast.LENGTH_SHORT).show();
                                transitionToSocialMediaActivity();
                            }   else    {
                                Toast.makeText(LoginActivity.this, "User not found", Toast.LENGTH_SHORT).show();
                            }
                        }   else    {
                            Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
                break;

            case R.id.btn_signup_login :
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
                finish();
                break;

            default:
                break;
        }
    }

    public void loginLayoutTapped(View view)    {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

        }   catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void transitionToSocialMediaActivity()   {
        Intent intent = new Intent(LoginActivity.this, UserActivity.class);
        startActivity(intent);
        finish();
        }
    }