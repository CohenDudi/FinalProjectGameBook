package com.example.finalprojectgamebook.views;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.finalprojectgamebook.R;
import com.example.finalprojectgamebook.model.ChatSection;
import com.example.finalprojectgamebook.model.ChatSectionAdapter;
import com.example.finalprojectgamebook.model.Section;
import com.example.finalprojectgamebook.viewmodel.PriavteChatViewModel;
import com.example.finalprojectgamebook.viewmodel.feedViewModel;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PrivateChatFragment extends Fragment {
    private PriavteChatViewModel priavteChatViewModel;
    String usersId;
    String friendId;
    List<ChatSection> chats = new ArrayList<>();
    RecyclerView recyclerView;
    ChatSectionAdapter adapter;
    FirebaseUser user;



    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        priavteChatViewModel = new ViewModelProvider(this).get(PriavteChatViewModel.class);
        View root = inflater.inflate(R.layout.fragment_priavte_chat, container, false);
        usersId = (String)getArguments().getSerializable("chatId");
        friendId = (String)getArguments().getSerializable("friendId");
        priavteChatViewModel.changeMsgSeen(friendId,1);

        priavteChatViewModel.setUsersId(usersId);
        chats = priavteChatViewModel.getChats();
        user = priavteChatViewModel.getUser();

        recyclerView = root.findViewById(R.id.recyclerChatPrivate);
        EditText editText = root.findViewById(R.id.msg_input_chat_priavte);
        ImageButton imageButton = root.findViewById(R.id.msg_btn_chat_private);

        adapter = new ChatSectionAdapter(chats);
        adapter.setUserId(priavteChatViewModel.getUser().getUid());
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setHasFixedSize(false);
        recyclerView.setAdapter(adapter);
        updateFeed();

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                priavteChatViewModel.addNewMsg(new ChatSection(user.getDisplayName(),user.getUid(),editText.getText().toString()));
                editText.setText("");
                hideSoftKeyboard(getActivity());
                priavteChatViewModel.changeMsgSeen(friendId,0);
            }
        });

        return root;
    }

    public void updateFeed(){
        priavteChatViewModel.getFireBase().child("private chat").child(usersId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chats = priavteChatViewModel.getChats();
                adapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(chats.size()-1);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });


    }

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

    @Override
    public void onDestroy() {
        super.onDestroy();
        priavteChatViewModel.changeMsgSeen(friendId,1);
    }
}