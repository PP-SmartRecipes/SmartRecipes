package com.example.smartrecipes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserSettings extends AppCompatActivity implements View.OnClickListener {
    private TextView user = null;
    private Button out = null;
    private Button changeUsernameButton = null;
    private Button changePasswordButton = null;
    private Button changeEmailButtom = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings);

        FirebaseUser user_name = FirebaseAuth.getInstance().getCurrentUser();
        String name = user_name.getDisplayName().toString();

        user = (TextView)findViewById(R.id.userName);
        user.setText(name);

        out = (Button)findViewById(R.id.button_sign_out);
        out.setOnClickListener(this);

        changeUsernameButton = (Button)findViewById(R.id.button_change_username);
        changeUsernameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), ChangeUsername.class);
                startActivity(i);
            }
        });

        changePasswordButton = (Button)findViewById(R.id.button_change_password);
        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), ChangePassword.class);
                startActivity(i);
            }
        });

        changeEmailButtom = (Button)findViewById(R.id.button_change_email);
        changeEmailButtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), ChangeEmail.class);
                startActivity(i);
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.home);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.category:
                        startActivity(new Intent(getApplicationContext(),Category.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.history:
                        startActivity(new Intent(getApplicationContext(),History.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.shopping:
                        startActivity(new Intent(getApplicationContext(),ShoppingList.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.favorite:
                        if(FirebaseAuth.getInstance().getCurrentUser() != null) {
                            startActivity(new Intent(getApplicationContext(), FavouritesActivity.class));
                            overridePendingTransition(0, 0);
                            return true;
                        }
                        else{
                            startActivity(new Intent(getApplicationContext(), SignInActivity.class));
                            overridePendingTransition(0, 0);
                            return true;
                        }
                }
                return false;
            }
        });
    }
    public void onClick(View v) {
            if (v.getId() == R.id.button_sign_out) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(this,SignInActivity.class);
                startActivity(intent);
                finish();
            }
        }
}