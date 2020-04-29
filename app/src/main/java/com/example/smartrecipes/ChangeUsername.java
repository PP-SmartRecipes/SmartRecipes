package com.example.smartrecipes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
private TextView usernameText = null;
private TextView passwordText = null;
private Button changeUsernameButton = null;

private DatabaseReference dbref = null;
private FirebaseAuth mAuth = null;
private FirebaseUser currentUser = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_username);

        dbref = FirebaseDatabase.getInstance().getReference().child("usernamesInUse");

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        usernameText = (TextView)findViewById(R.id.change_username_field);
        passwordText = (TextView)findViewById(R.id.change_username_password);
        changeUsernameButton = (Button)findViewById(R.id.change_username_button);

        changeUsernameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!usernameText.getText().toString().isEmpty() && !passwordText.getText().toString().isEmpty()){
                    AuthCredential credential = EmailAuthProvider.getCredential(currentUser.getEmail().toString(), passwordText.getText().toString());
                    currentUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
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
                                                            ds.getRef().removeValue();
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                    }
                                                });
                                                Intent i = new Intent(getApplicationContext(), UserSettings.class);
                                                startActivity(i);
                                            }
                                            dbref.push().setValue(usernameText.getText().toString());
                                        }
                                    });
                        }
                    });
                }
            }
        });

    }
}
