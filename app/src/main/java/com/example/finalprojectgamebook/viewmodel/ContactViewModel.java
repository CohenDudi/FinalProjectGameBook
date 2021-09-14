package com.example.finalprojectgamebook.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.finalprojectgamebook.model.FireBaseModel;
import com.example.finalprojectgamebook.model.User;

import java.util.List;

public class ContactViewModel extends ViewModel {
    private FireBaseModel fireBaseModel;

    public ContactViewModel() {
        fireBaseModel = FireBaseModel.getInstance();
    }

    public List<User> getConacts(){
        return fireBaseModel.getContacts();
    }

}
