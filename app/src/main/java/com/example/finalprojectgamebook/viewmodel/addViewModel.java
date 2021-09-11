package com.example.finalprojectgamebook.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.finalprojectgamebook.model.FireBaseModel;
import com.example.finalprojectgamebook.model.Section;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class addViewModel extends ViewModel {
    private MutableLiveData<String> mText;
    private FireBaseModel fireBaseModel;
    List<Section> sections = new ArrayList<>();

    public addViewModel() {
        mText = new MutableLiveData<>();
        fireBaseModel = FireBaseModel.getInstance();
        mText.setValue("This is add fragment");
        //sections = fireBaseModel.getSections();
    }

    public LiveData<String> getText() {
        return mText;
    }

    public void addNewSection(Section section){
        fireBaseModel.addNewSection(section);
    }



}
