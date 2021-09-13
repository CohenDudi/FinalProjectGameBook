package com.example.finalprojectgamebook.model;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
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

public class FireBaseModel {
    private static FireBaseModel single_instance = null;
    private Application application;
    private FirebaseAuth firebaseAuth;
    private MutableLiveData<FirebaseUser> userMutableLiveData;
    private DatabaseReference mDatabase;
    List<Section> sections = new ArrayList<>();


    private FireBaseModel(){
        firebaseAuth = FirebaseAuth.getInstance();
        userMutableLiveData = new MutableLiveData<>();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        readSections();
    }


    public DatabaseReference getmDatabase(){
        return mDatabase;
    }

    public static FireBaseModel getInstance(){
        if (single_instance == null)
            single_instance = new FireBaseModel();
        return single_instance;
    }

    public void setApp(Application application){
        this.application = application;
    }


    public void register(String email, String password){
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(application.getMainExecutor(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                    userMutableLiveData.postValue(firebaseAuth.getCurrentUser());
                else
                    Toast.makeText(application,"register failed"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    public void updateName(String fullName){
            if(fullName != null){
                getUser().updateProfile(new UserProfileChangeRequest.Builder().setDisplayName(fullName).build()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });
            }
        }


    public void login(String email, String password){
        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                    Toast.makeText(application,"Sign in successful", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(application,"Sign in failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public MutableLiveData<FirebaseUser> getUserMutableLiveData() {
        return userMutableLiveData;

    }

    public FirebaseUser getUser(){
        final FirebaseUser user = firebaseAuth.getCurrentUser();
        return user;
    }

    public void setNewListener(FirebaseAuth.AuthStateListener authStateListener){
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    public void addNewSection(Section section){
        sections.add(section);
        mDatabase.child("section").setValue(sections);
    }

    public void readSections(){
        mDatabase.child("section").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                sections.clear();
                if(dataSnapshot.exists()) {
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Section section = snapshot.getValue(Section.class);
                        sections.add(section);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void signOut(){
        firebaseAuth.signOut();
    }

    public List<Section> getSections(){
        return sections;
    }
}
