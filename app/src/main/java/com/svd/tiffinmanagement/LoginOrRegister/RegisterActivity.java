package com.svd.tiffinmanagement.LoginOrRegister;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.svd.tiffinmanagement.R;

import Models.UsersHelperClass;

public class RegisterActivity extends AppCompatActivity {

    private EditText editTextFirstName,editTextLastName,editTextPhone, editTextEmail, editTextPassword;
    FirebaseDatabase rootNode;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        rootNode = FirebaseDatabase.getInstance();
        databaseReference = rootNode.getReference("Users");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

//        ImageView profileIcon = findViewById(R.id.profileIcon);
        editTextFirstName = findViewById(R.id.editTextFirstName);
        editTextLastName = findViewById(R.id.editTextLastName);
        editTextPhone = findViewById(R.id.editTextPhone);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        Button buttonRegister = findViewById(R.id.buttonRegister);
        TextView textViewLogin = findViewById(R.id.textViewLogin);

        // Register button click listener
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

        // Login text click listener
        textViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the login activity
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void register() {
        // Get values from EditText fields
        String firstName = editTextFirstName.getText().toString().trim();
        String lastName = editTextLastName.getText().toString().trim();
        String phone = editTextPhone.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        // Perform validation for each field
        if (firstName.isEmpty()) {
            showToast("Please enter your first name");
        } else if (lastName.isEmpty()) {
            showToast("Please enter your last name");
        } else if (phone.isEmpty()) {
            showToast("Please enter your phone number");
        } else if (!isValidPhone(phone)) {
            showToast("Please enter a valid phone number");
        } else if (email.isEmpty()) {
            showToast("Please enter your email address");
        } else if (!isValidEmail(email)) {
            showToast("Please enter a valid email address");
        } else if (password.isEmpty()) {
            showToast("Please enter your password");
        } else if (!isValidPassword(password)) {
            showToast("Please enter a password with at least 6 characters");
        } else {
            // All fields are valid, check if the user already exists
            checkExistingUser(email, new OnUserCheckListener() {
                @Override
                public void onUserCheck(boolean exists) {
                    if (!exists) {
                        // User does not exist, proceed with user registration
                        UsersHelperClass user = new UsersHelperClass(firstName, lastName, email, phone, password);
                        addUserToDatabase(user);
                    } else {
                        // User already exists, show error message
                        showToast("User already exists. Please Login to continue.");
                    }
                }
            });
        }
    }

    private void checkExistingUser(String email, OnUserCheckListener listener) {
        databaseReference.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Check if any user with the provided email exists
                boolean exists = false;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    UsersHelperClass user = dataSnapshot.getValue(UsersHelperClass.class);
                    if (user != null && user.getEmail().equals(email)) {
                        exists = true;
                        break;
                    }
                }
                listener.onUserCheck(exists);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled event
                showToast("Database operation cancelled: " + error.getMessage());
            }
        });
    }
    private void addUserToDatabase(UsersHelperClass user) {
        // Get a unique key for the new user
        String userId = databaseReference.push().getKey();
        if (userId != null) {
            // Add the user to the database
            databaseReference.child(userId).setValue(user, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@NonNull DatabaseError error, @NonNull DatabaseReference ref) {
                    if (error == null) {
                        showToast("Registration successful. Please Login with same credentials");
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent);
                    } else {
                        showToast("Failed to register user: " + error.getMessage());
                    }
                }
            });
        } else {
            showToast("Failed to generate user ID");
        }
    }

    private interface OnUserCheckListener {
        void onUserCheck(boolean exists);
    }


    private boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isValidPhone(String phone) {
        // For demonstration purposes, let's assume a valid phone number is 10 digits long
        return phone.length() == 10 && phone.matches("[0-9]+");
    }

    private boolean isValidPassword(String password) {
        return password.length() >= 6;
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}

