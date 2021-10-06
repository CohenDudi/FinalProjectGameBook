package com.example.finalprojectgamebook.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.finalprojectgamebook.model.FireBaseModel;
import com.example.finalprojectgamebook.model.Section;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

public class feedViewModel extends ViewModel {
    private MutableLiveData<String> mText;
    private FireBaseModel fireBaseModel;

    public feedViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is feed fragment");
        fireBaseModel = FireBaseModel.getInstance();
        //sections = fireBaseModel.getSections();

    }

    public LiveData<String> getText() {
        return mText;
    }

    public boolean isAnonymous(){
        return fireBaseModel.getUser().isAnonymous();
    }

}
