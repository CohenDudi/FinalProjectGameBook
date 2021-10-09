package com.example.finalprojectgamebook.views;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.finalprojectgamebook.R;
import com.example.finalprojectgamebook.model.FireBaseModel;
import com.example.finalprojectgamebook.model.User;
import com.example.finalprojectgamebook.viewmodel.LoginRegisterViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
//import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {
    NavigationView navigationView;
    CoordinatorLayout coordinatorLayout;
    String fullName;
    FireBaseModel fireBase;
    FirebaseAuth.AuthStateListener authStateListener;
    LoginRegisterViewModel loginRegisterViewModel;
    ImageButton profileBtn;
    TextView userTv;
    Toolbar toolbar;

    FirebaseMessaging messaging = FirebaseMessaging.getInstance();
    BroadcastReceiver receiver;

    ActivityResultLauncher<Intent> takePictureActivity;
    Bitmap bitmap;
    ImageView photo;

    //FirebaseMessaging messaging = FirebaseMessaging.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fireBase = FireBaseModel.getInstance();
        fireBase.setApp(this.getApplication());

        setContentView(R.layout.activity_main);

        loginRegisterViewModel = new LoginRegisterViewModel(this.getApplication());
        //drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        coordinatorLayout = findViewById(R.id.coordinator);
        toolbar = findViewById(R.id.toolbar);
        profileBtn = findViewById(R.id.userProfileBtn);
        setSupportActionBar(toolbar);

        View headerView  = navigationView.getHeaderView(0);
        userTv = headerView.findViewById(R.id.navigation_header_text_view);
        //loginRegisterViewModel.signInAnonymously();


        /**
        if(loginRegisterViewModel.getUser()==null) {
            loginRegisterViewModel.signInAnonymously();
        }


        if(loginRegisterViewModel.getUser().isAnonymous())
            userTv.setText("Welcome Guest");
         **/

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24);

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
                        alertDialog.show();
                        dialogView.findViewById(R.id.sign_in_up).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String username  = usernameEt.getText().toString();
                                fullName = fullnameEt.getText().toString();
                                String password = passwordEt.getText().toString();
                                //loginRegisterViewModel.register(username,password);
                                if(FireBaseModel.getInstance().getUser() != null) {
                                    loginRegisterViewModel.anonymousRegister(username, password);
                                }else{
                                    loginRegisterViewModel.register(username,password);
                                }
                                loginRegisterViewModel.updateName(fullName);

                                userTv.setText("Welcome " + fullName);
                                toolbar.setTitle("Welcome " + fullName);
                                fullName = null;


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
                    case R.id.item_sign_in:
                        fullnameEt.setVisibility(View.GONE);
                        alertDialog.show();
                        dialogView.findViewById(R.id.sign_in_up).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String username  = usernameEt.getText().toString();
                                String password = passwordEt.getText().toString();
                                loginRegisterViewModel.login(username,password);

                                userTv.setText("Welcome "+ loginRegisterViewModel.getUser().getDisplayName());
                                toolbar.setTitle("Welcome "+ loginRegisterViewModel.getUser().getDisplayName());

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
                        loginRegisterViewModel.signInAnonymously();
                        break;
                }
                return false;
            }
        });

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                View headerView  = navigationView.getHeaderView(0);
                TextView userTv = headerView.findViewById(R.id.navigation_header_text_view);
                FirebaseUser user = loginRegisterViewModel.getUser();
                    if(user != null) {
                        messaging.subscribeToTopic(user.getUid());
                        if (user.isAnonymous()) {
                            userTv.setText("Please Login");
                            toolbar.setTitle("Hello Guest");
                            navigationView.getMenu().findItem(R.id.item_sign_in).setVisible(true);
                            navigationView.getMenu().findItem(R.id.item_sign_up).setVisible(true);
                            navigationView.getMenu().findItem(R.id.item_sign_out).setVisible(false);
                        } else {
                            userTv.setText("Welcome " + user.getDisplayName());
                            toolbar.setTitle("Welcome " + user.getDisplayName());
                            navigationView.getMenu().findItem(R.id.item_sign_in).setVisible(false);
                            navigationView.getMenu().findItem(R.id.item_sign_up).setVisible(false);
                            navigationView.getMenu().findItem(R.id.item_sign_out).setVisible(true);
                            fireBase.readContacts();
                        }
                    }
            }
        };

        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    openProfile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        createBottomNav();

    }
    @Override
    protected void onStart() {
        super.onStart();
        loginRegisterViewModel.signInAnonymously();
        loginRegisterViewModel.setNewListener(authStateListener);


    }


    @Override
    protected void onStop() {
        super.onStop();
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

    public void openProfile() throws IOException {
        //List<User> users = sectionViewModel.getContacts();
        //final Boolean[] ifFriends = {checkIfFriends(chats.get(position).getUserId(),users)};

        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(this);
        View view = layoutInflaterAndroid.inflate(R.layout.profile_dialog, null);
        builder.setView(view);
        builder.setCancelable(false);
        androidx.appcompat.app.AlertDialog alertDialog = builder.create();
        alertDialog.show();

        Button close_btn = view.findViewById(R.id.profile_close_btn);
        TextView emailTv = view.findViewById(R.id.email_profile_tv);
        EditText userNameEt = view.findViewById(R.id.profile_name_et);
        EditText passwordEt = view.findViewById(R.id.profile_password_et);
        ImageButton usernameBtn = view.findViewById(R.id.profile_name_btn);
        ImageButton passwordBtn = view.findViewById(R.id.profile_password_btn);

        FirebaseUser user = fireBase.getUser();
        emailTv.setText(user.getEmail());
        userNameEt.setText(user.getDisplayName());


        close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        usernameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!userNameEt.isEnabled()){
                    userNameEt.setEnabled(true);
                    userNameEt.setText("");
                    userNameEt.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(userNameEt, InputMethodManager.SHOW_IMPLICIT);

                }else{
                    userNameEt.setEnabled(false);
                    loginRegisterViewModel.updateName(userNameEt.getText().toString());
                    //user.reload();
                    userTv.setText("Welcome "+ userNameEt.getText().toString());
                    toolbar.setTitle("Welcome "+ userNameEt.getText().toString());

                    Snackbar.make(findViewById(android.R.id.content), "Username Changed", Snackbar.LENGTH_LONG).show();

                }
            }
        });

        passwordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!passwordEt.isEnabled()){
                    passwordEt.setEnabled(true);
                    passwordEt.setText("");
                    passwordEt.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(passwordEt, InputMethodManager.SHOW_IMPLICIT);

                }else{
                    passwordEt.setEnabled(false);
                    user.updatePassword(passwordEt.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Snackbar.make(findViewById(android.R.id.content), "Password Changed", Snackbar.LENGTH_LONG).show();
                            }else{
                                Snackbar.make(findViewById(android.R.id.content), "Password Not Changed "+ task.getException().getMessage().toString(), Snackbar.LENGTH_LONG).show();

                            }
                        }
                    });
                }
            }
        });



    }


}