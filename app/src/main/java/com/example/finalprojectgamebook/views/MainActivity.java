package com.example.finalprojectgamebook.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.finalprojectgamebook.R;
import com.example.finalprojectgamebook.model.FireBaseModel;
import com.example.finalprojectgamebook.viewmodel.LoginRegisterViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;

public class MainActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    CoordinatorLayout coordinatorLayout;
    String fullName;

    FireBaseModel fireBase;
    LoginRegisterViewModel loginRegisterViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fireBase = new FireBaseModel(this.getApplication());
        loginRegisterViewModel = new LoginRegisterViewModel(this.getApplication());
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        coordinatorLayout = findViewById(R.id.coordinator);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                drawerLayout.closeDrawers();

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                View dialogView  = getLayoutInflater().inflate(R.layout.sign_d,null);
                builder.setView(dialogView);

                final EditText usernameEt = dialogView.findViewById(R.id.username_input1);
                final EditText fullnameEt = dialogView.findViewById(R.id.fullname_input1);
                final EditText passwordEt = dialogView.findViewById(R.id.password_input1);


                switch (item.getItemId()) {

                    case R.id.item_sign_up:
                        /**
                        String username  = usernameEt.getText().toString();
                        fullName = fullnameEt.getText().toString();
                        String password = passwordEt.getText().toString();
                        dialogView.findViewById(R.id.sign_in_up).setOnClickListener(v -> loginRegisterViewModel.register(username,password));
                         **/
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                        dialogView.findViewById(R.id.sign_in_up).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String username  = usernameEt.getText().toString();
                                fullName = fullnameEt.getText().toString();
                                String password = passwordEt.getText().toString();
                                loginRegisterViewModel.register(username,password);
                                alertDialog.dismiss();
                            }
                        });

                        dialogView.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alertDialog.dismiss();
                            }
                        });
                        /**
                        builder.setView(dialogView).setPositiveButton("Register", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                //sign up the user
                                loginRegisterViewModel.register(username,password);
                            }
                        }).show();
                         **/

                        break;
                    case R.id.item_sign_in:

                        break;
                    case R.id.item_sign_out:
                        break;
                }
                return false;
            }
        });


    }
}