package com.example.finalprojectgamebook.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.finalprojectgamebook.model.FireBaseModel;
import com.example.finalprojectgamebook.model.FireBaseSectionChat;
import com.example.finalprojectgamebook.model.HomePostLookingForGame;
import com.example.finalprojectgamebook.model.Section;

import java.util.ArrayList;
import java.util.List;

public class FavoriteViewModel extends ViewModel {
    private MutableLiveData<String> mText;
    private FireBaseSectionChat fireBaseSectionChat;
    private FireBaseModel fireBaseModel;

    public FavoriteViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is favorite fragment");
        fireBaseModel = FireBaseModel.getInstance();
    }

    public LiveData<String> getText() {
        return mText;
    }

    public List<HomePostLookingForGame> getFavoritePosts(List<HomePostLookingForGame> feedPosts){
        List<HomePostLookingForGame> temp = new ArrayList<>();
        List<Section> sections = fireBaseModel.getSections();
        List<String> favoriteGames = new ArrayList<>();
        String selfId = fireBaseModel.getUser().getUid();
        for (Section s:sections) {
            if(s.getUsersId().contains(selfId))
                favoriteGames.add(s.getName());
        }

        for (HomePostLookingForGame home:feedPosts) {
            if(favoriteGames.contains(home.getGameName()))
                temp.add(home);
            }
        return temp;

    }


}
