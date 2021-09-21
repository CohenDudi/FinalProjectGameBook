package com.example.finalprojectgamebook.views;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.finalprojectgamebook.R;
import com.example.finalprojectgamebook.model.FireBaseModel;
import com.example.finalprojectgamebook.viewmodel.LoginRegisterViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class MainActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    CoordinatorLayout coordinatorLayout;
    String fullName;

    FireBaseModel fireBase;
    FirebaseAuth.AuthStateListener authStateListener;
    LoginRegisterViewModel loginRegisterViewModel;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fireBase = FireBaseModel.getInstance();
        fireBase.setApp(this.getApplication());
        loginRegisterViewModel = new LoginRegisterViewModel(this.getApplication());
        //drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        coordinatorLayout = findViewById(R.id.coordinator);



        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                //drawerLayout.closeDrawers();

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                View dialogView  = getLayoutInflater().inflate(R.layout.sign_d,null);
                builder.setView(dialogView);

                final EditText usernameEt = dialogView.findViewById(R.id.username_input1);
                final EditText fullnameEt = dialogView.findViewById(R.id.fullname_input1);
                final EditText passwordEt = dialogView.findViewById(R.id.password_input1);
                AlertDialog alertDialog = builder.create();

                switch (item.getItemId()) {

                    case R.id.item_sign_up:
                        /**
                        String username  = usernameEt.getText().toString();
                        fullName = fullnameEt.getText().toString();
                        String password = passwordEt.getText().toString();
                        dialogView.findViewById(R.id.sign_in_up).setOnClickListener(v -> loginRegisterViewModel.register(username,password));
                         **/
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
                        alertDialog.show();
                        dialogView.findViewById(R.id.sign_in_up).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String username  = usernameEt.getText().toString();
                                fullName = fullnameEt.getText().toString();
                                String password = passwordEt.getText().toString();
                                loginRegisterViewModel.login(username,password);
                                alertDialog.dismiss();
                            }
                        });

                        dialogView.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alertDialog.dismiss();
                            }
                        });

                        break;
                    case R.id.item_sign_out:
                        loginRegisterViewModel.signOut();
                        break;
                }
                return false;
            }
        });
        //final CollapsingToolbarLayout collapsing  = findViewById(R.id.collapsing_layout);
        toolbar.setTitle("Please log in");
        //collapsing.setTitle("Please log in");
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                View headerView  = navigationView.getHeaderView(0);
                TextView userTv = headerView.findViewById(R.id.navigation_header_text_view);

                final FirebaseUser user = loginRegisterViewModel.getUser();

                if(user != null) {//sign up or sign in
                    if(fullName != null)
                    {
                        loginRegisterViewModel.updateName(fullName);
                        fullName = null;
                    }

                    userTv.setText(user.getDisplayName() + " logged in");
                    toolbar.setTitle(user.getDisplayName());

                    navigationView.getMenu().findItem(R.id.item_sign_in).setVisible(false);
                    navigationView.getMenu().findItem(R.id.item_sign_up).setVisible(false);
                    navigationView.getMenu().findItem(R.id.item_sign_out).setVisible(true);
                    loginRegisterViewModel.reloadContactAndSection();
                    //Read the user data base - missions

                }
                else {
                    userTv.setText("Please log in");
                    toolbar.setTitle("Please log in");

                    navigationView.getMenu().findItem(R.id.item_sign_in).setVisible(true);
                    navigationView.getMenu().findItem(R.id.item_sign_up).setVisible(true);
                    navigationView.getMenu().findItem(R.id.item_sign_out).setVisible(false);
                }
            }
        };
        createBottomNav();
    }
    @Override
    protected void onStart() {
        super.onStart();
        loginRegisterViewModel.setNewListener(authStateListener);
    }

    public void createBottomNav(){
        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_feed, R.id.navigation_favorite,R.id.navigation_games, R.id.navigation_add, R.id.navigation_Contacts)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);


        BottomNavigationView navViewSection = findViewById(R.id.nav_view_section);
        AppBarConfiguration appBarConfigurationSection = new AppBarConfiguration.Builder(
                R.id.navigation_discover, R.id.navigation_chat)
                .build();

        NavController navControllerSection = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navControllerSection, appBarConfigurationSection);
        NavigationUI.setupWithNavController(navViewSection, navControllerSection);



        navControllerSection.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                if(destination.getId() == R.id.navigation_chat || destination.getId() == R.id.navigation_discover) {
                    //navView.setVisibility(View.GONE);
                    navViewSection.setVisibility(View.VISIBLE);
                } else {
                    //toolbar.setVisibility(View.VISIBLE);
                    navViewSection.setVisibility(View.GONE);
                }

            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


}