package com.example.finalprojectgamebook.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.finalprojectgamebook.model.ChatSection;
import com.example.finalprojectgamebook.model.FireBaseModel;
import com.example.finalprojectgamebook.model.FireBaseSectionChat;
import com.example.finalprojectgamebook.model.Section;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

public class PriavteChatViewModel extends ViewModel {
    private FireBaseSectionChat fireBaseSectionChat;

    public PriavteChatViewModel() {
        fireBaseSectionChat = FireBaseSectionChat.getInstance();
    }

    public List<ChatSection> getChats(){
        return fireBaseSectionChat.getChats();
    }


    public void addNewMsg(ChatSection chatSection){
        fireBaseSectionChat.addNewPrivateMsg(chatSection);
    }

    public FirebaseUser getUser(){
        return fireBaseSectionChat.getUser();
    }

    public DatabaseReference getFireBase(){
        return fireBaseSectionChat.getmDatabase();
    }

    public void setUsersId(String usersId){
        fireBaseSectionChat.setUsersId(usersId);
        fireBaseSectionChat.readPrivateChat();
    }





}
