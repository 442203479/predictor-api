package com.example.se_proj;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    EditText usernameLogin;
    EditText edtPassword;

    Button btnLogin;
    TextView tvSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameLogin=findViewById(R.id.loginUsername);
        edtPassword=findViewById(R.id.passwordEditText);
        btnLogin=findViewById(R.id.loginButton);
        tvSignUp=findViewById(R.id.tvSignUp);

        tvSignUp.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
            startActivity(intent);
            finish();
        });

        btnLogin.setOnClickListener(v -> {
            String username=usernameLogin.getText().toString();




            String password=edtPassword.getText().toString();

            if (username.isEmpty() || password.isEmpty()){
                Toast.makeText(LoginActivity.this,"Fill All Fields",Toast.LENGTH_SHORT).show();
                return;

            }
            loginUser(username,password);

        });

    }
    public void loginUser(String username, String password) {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        if (dbHelper.loginUser(username, password)) {
            // Successful login
            SharedPreferences prefs = this.getSharedPreferences(
                    "com.example.se_proj", Context.MODE_PRIVATE);
            prefs.edit().putString("name",username).apply();
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            // Login failed
            Toast.makeText(LoginActivity.this,"Login failed. Please check your username and password",Toast.LENGTH_SHORT).show();
        }
    }
}
