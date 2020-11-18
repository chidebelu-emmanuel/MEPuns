package com.example.mepuns;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {
    EditText Email, UserName, Password;
    Button But;
    ProgressBar progress;
    private static final String TAG = "EmailPassword";
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mAuth = FirebaseAuth.getInstance();
        But = findViewById(R.id.But);
        Email = findViewById(R.id.Email);
        UserName = findViewById(R.id.UserName);
        Password = findViewById(R.id.Password);
        progress = findViewById(R.id.progress);
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
       updateUI(currentUser);
    }

    private void createAccount(String Email, String Password) {
        Log.d(TAG, "createAccount:" + Email);
        if (!validateForm()) {
            return;
        }
        showProgressBar();

        mAuth.createUserWithEmailAndPassword(Email, Password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                            } else {

                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                        hideProgressBar();
                    }
                });

    }

    private void showProgressBar() {
        progress.setVisibility(View.INVISIBLE);

    }
    private void hideProgressBar() {
    progress.setVisibility(View.VISIBLE);
    }

    private boolean validateForm() {
        boolean valid = true;
        String email = Email.getText().toString();
        if (TextUtils.isEmpty(email)) {
            Email.setError("Required");
            valid = false;
        }
        String password = Password.getText().toString();
        if (TextUtils.isEmpty(password)) {
            Password.setError("Required.");
            valid = false;

        }
        return valid;
    }
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.but) {
            createAccount(Email.getText().toString(), Password.getText().toString());
        }
    }
    private void updateUI(FirebaseUser user) {
        hideProgressBar();
        if (user != null) {
            Email.setText(getString(R.string.emailpassword_status_fmt,
                    user.getEmail(), user.isEmailVerified());
            Password.setText(getString(R.string.firebase_status_fmt, user.getUid()));

            Email.setVisibility(View.GONE);
            Email.setVisibility(View.GONE);

        }
    }


}