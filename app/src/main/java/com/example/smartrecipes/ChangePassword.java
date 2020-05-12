package com.example.smartrecipes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePassword extends AppCompatActivity {
    private FirebaseAuth mAuth = null;
    private FirebaseUser currentUser = null;

    private EditText passwordField = null;
    private EditText oldPasswordField = null;
    private Button changePasswordButton = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        passwordField = (EditText)findViewById(R.id.change_password_field);
        oldPasswordField = (EditText)findViewById(R.id.change_password_old);
        changePasswordButton = (Button)findViewById(R.id.change_password_button);
        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (passwordField.getText().toString().isEmpty())
                    passwordField.setError("Podaj nowe hasło");
                else if (oldPasswordField.getText().toString().isEmpty())
                    oldPasswordField.setError("Podaj stare hasło");
                else
                    changePassword();
            }
        });
    }

    private void changePassword(){
        if(!passwordField.getText().toString().isEmpty() && !oldPasswordField.getText().toString().isEmpty()){
            if(!passwordField.getText().toString().equals(oldPasswordField.getText().toString())){
                AuthCredential credential = EmailAuthProvider.getCredential(currentUser.getEmail().toString(), oldPasswordField.getText().toString());
                currentUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            currentUser.updatePassword(passwordField.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Intent i = new Intent(getApplicationContext(), UserSettings.class);
                                        startActivity(i);
                                        Toast toast = Toast.makeText(getApplicationContext(), "Hasło zostało poprawnie zmienione", Toast.LENGTH_LONG);
                                        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 200);
                                        toast.show();
                                    }
                                    else {
                                        if(task.getException().toString().contains("Password should be at least 6 characters"))
                                            showMessage("Nowe hasło powinno składać się\nco najmniej z 6 znaków", 1);
                                    }
                                }
                            });
                        }
                        else{
                            if(task.getException().toString().contains("The password is invalid or the user does not have a password")) {
                                showMessage("Niepoprawne stare hasło", 1);
                            }
                            else if(task.getException().toString().contains("We have blocked all requests from this device due to unusual activity. Try again later.")){
                                showMessage("Zbyt wiele nieudanych prób logowania.\nSpróbuj ponownie później.", 1);
                            }
                        }
                    }
                });
            }
            else{
                showMessage("Nowe hasło musi różnić się od poprzedniego", 1);
            }
        }
    }

    private void showMessage(final String message, final int length) {
        View root = findViewById(android.R.id.content);
        Toast toast = Toast.makeText(this, message, length);
        int yOffset = Math.max(0, root.getHeight() - toast.getYOffset());
        toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, yOffset);
        toast.show();
    }
}