package com.example.smartrecipes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangeEmail extends AppCompatActivity {
    private FirebaseAuth mAuth = null;
    private FirebaseUser currentUser = null;

    private EditText emailField = null;
    private EditText passwordField = null;
    private Button changeEmailButton = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_email);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        emailField = (EditText)findViewById(R.id.change_email_field);
        passwordField = (EditText)findViewById(R.id.change_email_password);
        changeEmailButton = (Button)findViewById(R.id.change_email_button);
        changeEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (emailField.getText().toString().isEmpty())
                    emailField.setError("Podaj adres e-mail");
                else if (passwordField.getText().toString().isEmpty())
                    passwordField.setError("Podaj hasło");
                else
                    changeEmail();
            }
        });
    }

    private void changeEmail(){
        if(!emailField.getText().toString().isEmpty() && !passwordField.getText().toString().isEmpty()){
            AuthCredential credential = EmailAuthProvider.getCredential(currentUser.getEmail().toString(), passwordField.getText().toString());
            currentUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        currentUser.updateEmail(emailField.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Intent i = new Intent(getApplicationContext(), UserSettings.class);
                                    startActivity(i);
                                    Toast toast = Toast.makeText(getApplicationContext(), "Adres e-mail został poprawnie zmieniony", Toast.LENGTH_LONG);
                                    toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 200);
                                    toast.show();
                                }
                                else{
                                    if(task.getException().toString().contains("The email address is badly formatted"))
                                        showMessage("Niepoprawny format adresu e-mail", 1);
                                    else if(task.getException().toString().contains("The email address is already in use by another account"))
                                        showMessage("Podany adres e-mail jest już w użyciu", 1);
                                }
                            }
                        });
                    }
                    else{
                        if(task.getException().toString().contains("The password is invalid or the user does not have a password")) {
                            showMessage("Niepoprawne hasło", 1);
                        }
                        else if(task.getException().toString().contains("We have blocked all requests from this device due to unusual activity. Try again later.")){
                            showMessage("Zbyt wiele nieudanych prób logowania.\nSpróbuj ponownie później", 1);
                        }
                    }
                }
            });
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
