package com.example.finalprojectgamebook.viewmodel;

import androidx.lifecycle.ViewModel;

import com.example.finalprojectgamebook.model.FireBaseModel;
import com.example.finalprojectgamebook.model.User;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

public class ContactViewModel extends ViewModel {
    private FireBaseModel fireBaseModel;

    public ContactViewModel() {
        fireBaseModel = FireBaseModel.getInstance();
    }

    public List<User> getConacts(){
        return fireBaseModel.getContacts();
    }

    public DatabaseReference getFireBase(){
        return fireBaseModel.getmDatabase();
    }

    public FirebaseUser getUser(){
        return fireBaseModel.getUser();
    }

}
