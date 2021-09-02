package com.example.finalprojectgamebook.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.finalprojectgamebook.model.FireBaseModel;
import com.google.firebase.auth.FirebaseUser;

public class LoginRegisterViewModel extends AndroidViewModel {
    private FireBaseModel fireBase;

    private MutableLiveData<FirebaseUser> userMutableLiveData;


    public LoginRegisterViewModel(@NonNull Application application) {
        super(application);

        fireBase = new FireBaseModel(application);
        userMutableLiveData = fireBase.getUserMutableLiveData();

    }

    public void register(String email, String password){
        fireBase.register(email, password);
    }

    public MutableLiveData<FirebaseUser> getUserMutableLiveData() {
        return userMutableLiveData;
    }
}
