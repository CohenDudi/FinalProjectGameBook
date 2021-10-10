package com.example.finalprojectgamebook.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.finalprojectgamebook.model.FireBaseModel;
import com.example.finalprojectgamebook.model.Section;

import java.util.ArrayList;
import java.util.List;

public class AddViewModel extends ViewModel {
    private MutableLiveData<String> mText;
    private FireBaseModel fireBaseModel;
    List<Section> sections = new ArrayList<>();

    public AddViewModel() {
        mText = new MutableLiveData<>();
        fireBaseModel = FireBaseModel.getInstance();
        mText.setValue("This is add fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }

    public void addNewSection(Section section){
        fireBaseModel.addNewSection(section);
    }

    public int isSectionNotExist(Section s){
        sections = fireBaseModel.getSections();
        for (Section section:sections) {
            if (section.getName().toLowerCase().equals(s.getName().toLowerCase()))return 1;
            if(s.getName().equals(""))return 4;
            if(s.getType().equals(""))return 3;
            if(s.getDescription().equals(""))return 2;
            if(s.getDescription().length()>100)return 6;
            if(s.getImg() == null)return 7;

        }
        return 0;
    }
}
