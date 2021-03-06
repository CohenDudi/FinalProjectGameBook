package com.example.finalprojectgamebook.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.finalprojectgamebook.model.FireBaseModel;
import com.example.finalprojectgamebook.model.Section;
import com.example.finalprojectgamebook.model.User;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

public class GameViewModel extends ViewModel {
    private MutableLiveData<String> mText;
    private FireBaseModel fireBaseModel;
    private List<Section> sections = new ArrayList<>();

    public GameViewModel() {
        mText = new MutableLiveData<>();
        fireBaseModel = FireBaseModel.getInstance();
    }

    public LiveData<String> getText() {
        return mText;
    }


    public List<Section> getSections(){
        return fireBaseModel.getSections();
    }

    public DatabaseReference getFireBase(){
        return fireBaseModel.getmDatabase();
    }

    public User getSelfUser(){
        return fireBaseModel.getSelfUser();
    }

    public void updateSelfUser(User user) {
        fireBaseModel.updateSelfUser(user);
    }

    }
