package com.example.se_proj;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegistrationActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private EditText emailEditText;
    private Button registerButton;
    private DatabaseHelper databaseHelper;

    TextView tvLogIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);


        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        emailEditText = findViewById(R.id.emailEditText);
        registerButton = findViewById(R.id.registerButton);
        tvLogIn=findViewById(R.id.tvLogIn);

        databaseHelper = new DatabaseHelper(this);

        tvLogIn.setOnClickListener(v -> {
            Intent intent = new Intent(RegistrationActivity.this,LoginActivity.class);
            startActivity(intent);
            finish();
        });


        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String email = emailEditText.getText().toString();



                if (username.isEmpty() || password.isEmpty() ||email.isEmpty()){
                    Toast.makeText(RegistrationActivity.this, "Fill All Fields", Toast.LENGTH_SHORT).show();
                    return;

                }

                // Insert user details into the database
                boolean isInserted = databaseHelper.insertUser( username,  password,  email);

                if (isInserted) {
                    SharedPreferences prefs = getApplicationContext().getSharedPreferences("com.example.se_proj", Context.MODE_PRIVATE);

                    prefs.edit().putString("name",username).apply();
                    Intent intent = new Intent(RegistrationActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {

                    Toast.makeText(RegistrationActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}