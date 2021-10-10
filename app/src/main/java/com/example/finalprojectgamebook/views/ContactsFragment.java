package com.example.finalprojectgamebook.views;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.finalprojectgamebook.R;
import com.example.finalprojectgamebook.model.ContactAdapter;
import com.example.finalprojectgamebook.model.User;
import com.example.finalprojectgamebook.viewmodel.ContactViewModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ContactsFragment extends Fragment {
    private ContactViewModel contactViewModel;
    private ContactAdapter adapter;
    private List<User> users = new ArrayList<>();



    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contactViewModel = new ViewModelProvider(this).get(ContactViewModel.class);
        View root = inflater.inflate(R.layout.fragment_contacts, container, false);
        RecyclerView recyclerView = root.findViewById(R.id.recycler_contact);
        users = contactViewModel.getConacts();
        adapter = new ContactAdapter(users);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        adapter.setListener(new ContactAdapter.ContactListener() {
            @Override
            public void onContactClicked(int position, View view) {
                String userId1 = users.get(position).getUserId();
                String userId2 = contactViewModel.getUser().getUid();
                if(userId1.compareTo(userId2)>0){
                    String temp = userId1;
                    userId1 = userId2;
                    userId2 = temp;
                }

                Bundle bundle = new Bundle();
                bundle.putString("chatId",userId1+"_to_"+userId2);
                bundle.putString("friendId",users.get(position).getUserId());

                Navigation.findNavController(view).navigate(R.id.action_navigation_Contacts_to_privateChatFragment, bundle);
            }

            @Override
            public void onContactLongClicked(int position, View view) { }
        });

        updateContact();
        return root;
    }



    public void updateContact(){
        if (contactViewModel.getUser() != null){
            contactViewModel.getFireBase().child("contact").child(contactViewModel.getUser().getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    users = contactViewModel.getConacts();
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) { }
            });
        }
    }



}