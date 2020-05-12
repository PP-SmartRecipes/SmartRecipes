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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChangeUsername extends AppCompatActivity {
private EditText usernameText = null;
private EditText passwordText = null;
private Button changeUsernameButton = null;

private DatabaseReference dbref = null;
private FirebaseAuth mAuth = null;
private FirebaseUser currentUser = null;

private List<String> usernamesInUse = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_username);

        dbref = FirebaseDatabase.getInstance().getReference().child("usernamesInUse");
        usernamesInUse = new ArrayList<>();

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        usernameText = (EditText) findViewById(R.id.change_username_field);
        passwordText = (EditText) findViewById(R.id.change_username_password);
        changeUsernameButton = (Button)findViewById(R.id.change_username_button);

        changeUsernameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (usernameText.getText().toString().isEmpty())
                    usernameText.setError("Podaj nową nazwę użytkownika");
                else if (passwordText.getText().toString().isEmpty())
                    passwordText.setError("Podaj hasło");
                else
                    changeUsername();
            }
        });

        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usernamesInUse.clear();
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    String s = ds.getValue(String.class);
                    usernamesInUse.add(s);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void changeUsername(){
        if(!usernameText.getText().toString().isEmpty() && !passwordText.getText().toString().isEmpty()){
            AuthCredential credential = EmailAuthProvider.getCredential(currentUser.getEmail().toString(), passwordText.getText().toString());
            currentUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        if(!usernamesInUse.contains(usernameText.getText().toString())){
                            final String oldUsername = currentUser.getDisplayName().toString();
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(usernameText.getText().toString())
                                    .build();

                            currentUser.updateProfile(profileUpdates)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Query query = dbref.orderByValue().equalTo(oldUsername);
                                                query.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        for(DataSnapshot ds : dataSnapshot.getChildren()){
                                                            ds.getRef().setValue(usernameText.getText().toString());
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                    }
                                                });
                                                Intent i = new Intent(getApplicationContext(), UserSettings.class);
                                                startActivity(i);
                                                Toast toast = Toast.makeText(getApplicationContext(), "Nazwa użytkownika została\npoprawnie zmieniona", Toast.LENGTH_LONG);
                                                toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 200);
                                                toast.show();
                                            }
                                        }
                                    });
                        }
                        else{
                            showMessage("Podana nazwa użytkownika jest już w użyciu", 1);
                        }
                    }
                    else{
                        if(task.getException().toString().contains("The password is invalid or the user does not have a password")) {
                            showMessage("Niepoprawne hasło", 1);
                        }
                        else if(task.getException().toString().contains("We have blocked all requests from this device due to unusual activity. Try again later.")){
                            showMessage("Zbyt wiele nieudanych prób logowania.\nSpróbuj ponownie później.", 1);
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