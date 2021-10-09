package com.example.finalprojectgamebook.views;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.finalprojectgamebook.R;
import com.example.finalprojectgamebook.model.ChatSection;
import com.example.finalprojectgamebook.model.ChatSectionAdapter;
import com.example.finalprojectgamebook.model.FireBaseSectionChat;
import com.example.finalprojectgamebook.model.Section;
import com.example.finalprojectgamebook.model.SectionAdapter;
import com.example.finalprojectgamebook.model.User;
import com.example.finalprojectgamebook.viewmodel.feedViewModel;
import com.example.finalprojectgamebook.viewmodel.sectionViewModel;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SectionChatFragment extends Fragment {
    sectionViewModel sectionViewModel;
    List<ChatSection> chats = new ArrayList<>();
    ChatSectionAdapter adapter;
    FireBaseSectionChat fireBaseSectionChat;
    Section section;
    FirebaseUser user;
    RecyclerView recyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        sectionViewModel = new ViewModelProvider(requireActivity()).get(sectionViewModel.class);
        View root = inflater.inflate(R.layout.fragment_section_chat, container, false);
        section = sectionViewModel.getSection();
        chats = sectionViewModel.getChats();

        recyclerView = root.findViewById(R.id.recyclerChat);
        EditText editText = root.findViewById(R.id.msg_input_chat);
        ImageButton imageButton = root.findViewById(R.id.msg_btn_chat);

        if(sectionViewModel.isAnonymous()){
            editText.setEnabled(false);
            editText.setText("Please Login");
            editText.setTextColor(Color.RED);
            imageButton.setEnabled(false);
        }

        adapter = new ChatSectionAdapter(chats);
        adapter.setUserId(sectionViewModel.getUser().getUid());
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setHasFixedSize(false);
        recyclerView.setAdapter(adapter);

        user = sectionViewModel.getUser();
        updateFeed();


        adapter.setListener(new ChatSectionAdapter.chatListener() {
            @Override
            public void onChatClicked(int position, View view) {
                openFriendProfile(position);
                //sectionViewModel.addNewContact(new User(chats.get(position).getName(),chats.get(position).getUserId()));
            }

            @Override
            public void onChatLongClicked(int position, View view) {

            }
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sectionViewModel.addNewMsg(new ChatSection(user.getDisplayName(),user.getUid(),editText.getText().toString()));
                editText.setText("");
                hideSoftKeyboard(getActivity());
            }
        });

        return root;
    }

    public void updateFeed(){
        sectionViewModel.getFireBase().child("chat").child(section.getName()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chats = sectionViewModel.getChats();
                adapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(chats.size()-1);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });


    }
    //hide keyboard after sending a msg
    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        if(inputMethodManager.isAcceptingText()){
            inputMethodManager.hideSoftInputFromWindow(
                    activity.getCurrentFocus().getWindowToken(),
                    0
            );
        }
    }


    public void openFriendProfile(int position){
        List<User> users = sectionViewModel.getContacts();
        final Boolean[] ifFriends = {checkIfFriends(chats.get(position).getUserId(),users)};

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getContext());
        View view = layoutInflaterAndroid.inflate(R.layout.friend_dialog, null);
        builder.setView(view);
        builder.setCancelable(false);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        Button close_btn = view.findViewById(R.id.close_btn);
        Button add_friend_btn = view.findViewById(R.id.add_to_friend_btn);
        TextView friendName = view.findViewById(R.id.friend_name_txt);



        friendName.setText(chats.get(position).getName());

        if(ifFriends[0] || chats.get(position).getUserId().equals(sectionViewModel.getUser().getUid())){
            add_friend_btn.setEnabled(false);
            add_friend_btn.setTextColor(Color.GRAY);
        }


        close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        add_friend_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!ifFriends[0] ){
                    User me = new User(sectionViewModel.getUser().getDisplayName(),sectionViewModel.getUser().getUid());
                    sectionViewModel.addNewContact(new User(chats.get(position).getName(),chats.get(position).getUserId()));
                    sectionViewModel.addNewFriendContact(me,chats.get(position).getUserId());
                    ifFriends[0] = true;
                    add_friend_btn.setEnabled(false);
                    add_friend_btn.setTextColor(Color.GRAY);
                }
            }
        });

        if(sectionViewModel.isAnonymous())
        {
            add_friend_btn.setEnabled(false);
            add_friend_btn.setText("Please Login");

        }



    }

    public boolean checkIfFriends(String userID,List<User> users){
        for (User user:users) {
            if(user.getUserId().equals(userID))return true;
        }
        return false;
    }





}