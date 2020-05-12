package com.example.smartrecipes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth mAuth = null;
    private DatabaseReference usernamesInUse = null;
    private EditText login = null;
    private EditText email = null;
    private EditText password = null;
    private Button registerButton = null;

    private List<String> usernamesList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();
        usernamesInUse = FirebaseDatabase.getInstance().getReference().child("usernamesInUse");
        login = (EditText)findViewById(R.id.login);
        email = (EditText)findViewById(R.id.email);
        password = (EditText)findViewById(R.id.password);
        registerButton = (Button)findViewById(R.id.loginButton);

        usernamesList = new ArrayList<>();

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (login.getText().toString().isEmpty())
                    login.setError("Podaj nazwę użytkownika");

                else if (email.getText().toString().isEmpty())
                    email.setError("Podaj adres e-mail");

                else if(password.getText().toString().isEmpty())
                    password.setError("Podaj hasło");
                else
                    createAccount(email.getText().toString(), password.getText().toString());
            }
        });

        usernamesInUse.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usernamesList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String s = ds.getValue(String.class);
                    usernamesList.add(s);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void createAccount(String email, String password){
        if(!email.isEmpty() && !password.isEmpty() && !login.getText().toString().isEmpty()) {
            if(validateUsername(login.getText().toString())) {
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    setDisplayName(user);
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);
                                } else {
                                    if (task.getException().toString().contains("The email address is already in use by another account."))
                                        Toast.makeText(getApplicationContext(), "Użytkownik o podanym adresie email już istnieje.", Toast.LENGTH_LONG).show();
                                    else if (task.getException().toString().contains("The email address is badly formatted."))
                                        Toast.makeText(getApplicationContext(), "Niepoprawny format adresu email.", Toast.LENGTH_LONG).show();
                                    else if (task.getException().toString().contains("Password should be at least 6 characters"))
                                        Toast.makeText(getApplicationContext(), "Hasło powinno składać się co najmniej z 6 znaków.", Toast.LENGTH_LONG).show();
                                    Log.e("TAG", "createUserWithEmail:failure", task.getException());
                                    // If sign in fails, display a message to the user.
                                }
                            }
                        });
            }
            else {
                Toast.makeText(getApplicationContext(), "Użytkownik o podanej nazwie użytkownika już istnieje.", Toast.LENGTH_LONG).show();
            }
        }
    }

    private boolean validateUsername(String username){
        if(!usernamesList.contains(username))
            return true;
        else
            return false;
    }

    private void setDisplayName(FirebaseUser user){
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(login.getText().toString())
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            usernamesInUse.push().setValue(login.getText().toString());
                        }
                    }
                });
    }
}