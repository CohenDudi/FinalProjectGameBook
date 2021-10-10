package com.example.finalprojectgamebook.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.finalprojectgamebook.model.FireBaseModel;

public class FeedViewModel extends ViewModel {
    private MutableLiveData<String> mText;
    private FireBaseModel fireBaseModel;

    public FeedViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is feed fragment");
        fireBaseModel = FireBaseModel.getInstance();
    }

    public LiveData<String> getText() {
        return mText;
    }

    public boolean isAnonymous(){
        return fireBaseModel.getUser().isAnonymous();
    }

}
