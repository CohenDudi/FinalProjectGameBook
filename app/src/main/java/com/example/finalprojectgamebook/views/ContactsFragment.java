package com.example.finalprojectgamebook.views;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.finalprojectgamebook.R;
import com.example.finalprojectgamebook.model.ContactAdapter;
import com.example.finalprojectgamebook.model.SectionAdapter;
import com.example.finalprojectgamebook.model.User;
import com.example.finalprojectgamebook.viewmodel.ContactViewModel;
import com.example.finalprojectgamebook.viewmodel.feedViewModel;

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

        return root;
    }
}