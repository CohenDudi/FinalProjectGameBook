package com.example.finalprojectgamebook.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.finalprojectgamebook.model.ChatSection;
import com.example.finalprojectgamebook.model.FireBaseModel;
import com.example.finalprojectgamebook.model.FireBaseSectionChat;
import com.example.finalprojectgamebook.model.Section;
import com.example.finalprojectgamebook.model.User;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

public class sectionViewModel extends ViewModel {
    //private MutableLiveData<Section> section = new MutableLiveData<Section>();;
    private FireBaseSectionChat fireBaseSectionChat;
    private FireBaseModel fireBaseModel;

    public sectionViewModel() {
        fireBaseSectionChat = FireBaseSectionChat.getInstance();
        fireBaseModel = FireBaseModel.getInstance();

    }

    public Section getSection() {
        return fireBaseSectionChat.getSection();
    }

    public void setSection(Section s){
        fireBaseSectionChat.setSection(s);
        fireBaseSectionChat.readSection();
    }

    public void addNewMsg(ChatSection chatSection){
        fireBaseSectionChat.addNewSection(chatSection);
    }

    public List<ChatSection> getChats(){
        return fireBaseSectionChat.getChats();
    }

    public FirebaseUser getUser(){
        return fireBaseSectionChat.getUser();
    }

    public DatabaseReference getFireBase(){
        return fireBaseSectionChat.getmDatabase();
    }

    public void addNewContact(User user){
        fireBaseModel.addNewContact(user);
    }

    public List<User> getContacts(){
        return fireBaseModel.getContacts();
    }


}
