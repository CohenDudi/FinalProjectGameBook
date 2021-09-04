package com.example.finalprojectgamebook.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class favoriteViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    public favoriteViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is favorite fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
