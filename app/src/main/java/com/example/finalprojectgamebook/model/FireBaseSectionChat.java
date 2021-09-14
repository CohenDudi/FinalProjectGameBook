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
    List<ChatSection> chats = new ArrayList<>();

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
    }

    public void addNewSection(ChatSection chatSection ){
        chats.add(chatSection);
        mDatabase.child("chat").child(section.getName()).setValue(chats);
    }

    public FirebaseUser getUser(){
        final FirebaseUser user = firebaseAuth.getCurrentUser();
        return user;
    }

    public DatabaseReference getmDatabase(){
        return mDatabase;
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

    public List<ChatSection> getChats(){
        return chats;
    }

}
