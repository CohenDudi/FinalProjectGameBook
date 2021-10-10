package com.example.finalprojectgamebook.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FireBaseSectionChat {
    private static FireBaseSectionChat single_instance = null;
    private Application application;
    private FirebaseAuth firebaseAuth;
    private MutableLiveData<FirebaseUser> userMutableLiveData;
    private DatabaseReference mDatabase;
    private Section section;
    private String usersId;
    private List<ChatSection> chats = new ArrayList<>();
    private List<HomePostLookingForGame> homePostLookingForGames = new ArrayList<>();


    public static FireBaseSectionChat getInstance(){
        if (single_instance == null)
            single_instance = new FireBaseSectionChat();
        return single_instance;
    }

    private FireBaseSectionChat(){
        firebaseAuth = FirebaseAuth.getInstance();
        userMutableLiveData = new MutableLiveData<>();
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public void setApp(Application application){
        this.application = application;
    }

    public void setSection(Section section){
        this.section = section;
    }

    public Section getSection(){
        return section;
    }

    public void readSection(){
        readChat();
        readHomeGames();
    }

    public void addNewSection(ChatSection chatSection ){
        chats.add(chatSection);
        mDatabase.child("chat").child(section.getName()).setValue(chats);
    }

    public void addNewPost(HomePostLookingForGame homePostLookingForGame ){
        homePostLookingForGames.add(homePostLookingForGame);
        mDatabase.child("section feed").child(section.getName()).setValue(homePostLookingForGames);
    }

    public void updatePost(int positionSection , int positionRole , Role role,String gameName){
        //Role r = homePostLookingForGames.get(positionSection).getRoles().get(positionRole);
        mDatabase.child("section feed").child(section.getName()).child(String.valueOf(positionSection)).child("roles").child(String.valueOf(positionRole)).setValue(role);
    }

    public void updatePosts(int pos ,HomePostLookingForGame homePostLookingForGame){
        mDatabase.child("section feed").child(section.getName()).child(String.valueOf(pos)).setValue(homePostLookingForGame);
    }


    public void updateAllPosts(List<HomePostLookingForGame> homePostLookingForGames){
        mDatabase.child("section feed").child(section.getName()).setValue(homePostLookingForGames);
    }


    public void addNewPrivateMsg(ChatSection chatSection){
        chats.add(chatSection);
        mDatabase.child("private chat").child(usersId).setValue(chats);
    }

    public FirebaseUser getUser(){
        final FirebaseUser user = firebaseAuth.getCurrentUser();
        return user;
    }

    public DatabaseReference getmDatabase(){
        return mDatabase;
    }

    public void setUsersId(String usersId){
        this.usersId = usersId;
    }

    public void readPrivateChat(){
        mDatabase.child("private chat").child(usersId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chats.clear();
                if(dataSnapshot.exists()) {
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        ChatSection chatSection = snapshot.getValue(ChatSection.class);
                        chats.add(chatSection);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    void readChat(){
        mDatabase.child("chat").child(section.getName()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chats.clear();
                if(dataSnapshot.exists()) {
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        ChatSection chatSection = snapshot.getValue(ChatSection.class);
                        chats.add(chatSection);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    void readHomeGames(){
        mDatabase.child("section feed").child(section.getName()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                homePostLookingForGames.clear();
                if(dataSnapshot.exists()) {
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        HomePostLookingForGame homePostLookingForGame = snapshot.getValue(HomePostLookingForGame.class);
                        homePostLookingForGames.add(homePostLookingForGame);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    public List<ChatSection> getChats(){
        return chats;
    }
    public List<HomePostLookingForGame> getPosts(){
        return homePostLookingForGames;
    }


}
