package com.example.finalprojectgamebook.model;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FireBaseModel {
    private Application application;
    private FirebaseAuth firebaseAuth;
    private MutableLiveData<FirebaseUser> userMutableLiveData;



    public FireBaseModel(Application application){
        this.application = application;
        firebaseAuth = FirebaseAuth.getInstance();
        userMutableLiveData = new MutableLiveData<>();
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

    public MutableLiveData<FirebaseUser> getUserMutableLiveData() {
        return userMutableLiveData;

    }


}
