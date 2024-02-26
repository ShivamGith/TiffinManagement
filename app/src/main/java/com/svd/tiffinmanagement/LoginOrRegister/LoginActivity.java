package com.svd.tiffinmanagement.LoginOrRegister;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.svd.tiffinmanagement.R;
import android.content.Context;
import com.svd.tiffinmanagement.MainActivity;

public class LoginActivity extends AppCompatActivity {

    
    private EditText editTextUserName, editTextPassword;
    private static final String PREF_NAME = "MyPrefs";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (isLoggedIn()) {
            // User is already logged in, navigate to the main activity
            SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
            login(sharedPreferences.getString(KEY_USERNAME, ""), sharedPreferences.getString(KEY_PASSWORD, ""));
        }
        editTextUserName = findViewById(R.id.editTextUserName);
        editTextPassword = findViewById(R.id.editTextPassword);
        Button buttonLogin = findViewById(R.id.buttonLogin);
        TextView textViewRegister = findViewById(R.id.textViewRegister);

        // Login button click listener
        buttonLogin.setOnClickListener(v -> login(editTextUserName.getText().toString().trim(), editTextPassword.getText().toString().trim()));

        // Register text click listener
        textViewRegister.setOnClickListener(v -> {
            // Navigate to the registration activity or screen
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    private void login(String userNameOrEmail, String password) {

        // Perform basic validation (you can add more validation as needed)
        if (userNameOrEmail.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Enter username or email!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        // Check if the input is an email or username
        if (userNameOrEmail.contains("@")) {
            // If the input contains '@', consider it as an email
            Query checkUserByEmail = databaseReference.orderByChild("email").equalTo(userNameOrEmail);
            checkUserByEmail.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    // Handle email login logic
                    handleLogin(snapshot, userNameOrEmail, password);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    showToast("An Unexpected Error Occurred: " + error.getMessage());
                }
            });
        } else {
            // Otherwise, consider it as a username
            Query checkUserByUsername = databaseReference.orderByChild("userName").equalTo(userNameOrEmail);
            checkUserByUsername.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    // Handle username login logic
                    handleLogin(snapshot, userNameOrEmail, password);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    showToast("An Unexpected Error Occurred: " + error.getMessage());
                }
            });
        }
    }

    private void handleLogin(@NonNull DataSnapshot snapshot, String userName, String password) {
        if (snapshot.exists()) {
            // User exists
            for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                String passwordFromDB = childSnapshot.child("password").getValue(String.class);
                if (passwordFromDB != null && passwordFromDB.equals(password)) {
                    setLoggedIn(userName, password);
                    // Password matches, proceed with login
                    String firstNameFromDB = childSnapshot.child("firstName").getValue(String.class);
                    String lastNameFromDB = childSnapshot.child("lastName").getValue(String.class);
                    String emailFromDB = childSnapshot.child("email").getValue(String.class);
                    String phoneFromDB = childSnapshot.child("phone").getValue(String.class);
                    navigateToMainActivity(firstNameFromDB, lastNameFromDB, userName, emailFromDB, phoneFromDB, passwordFromDB);
                    return;
                }
            }
            // Password does not match
            editTextPassword.setError("Wrong Password");
            editTextPassword.requestFocus();
        } else {
            // User does not exist
            editTextUserName.setError("User not found");
            editTextUserName.requestFocus();
        }
    }
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
    private void setLoggedIn(String userName, String password) {
        SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.putString(KEY_USERNAME, userName);
        editor.putString(KEY_PASSWORD, password);
        editor.apply();
    }
    private boolean isLoggedIn() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false);
    }
    private void navigateToMainActivity(String firstName, String lastName, String userName, String email, String phone, String password) {
        Toast.makeText(this, "User is already logged in", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.putExtra("firstName", firstName);
        intent.putExtra("lastName", lastName);
        intent.putExtra("userName", userName);
        intent.putExtra("email", email);
        intent.putExtra("phone", phone);
        intent.putExtra("password", password);

        startActivity(intent);
        finish();
    }

}
