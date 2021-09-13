package com.example.finalprojectgamebook.views;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.finalprojectgamebook.R;
import com.example.finalprojectgamebook.model.FireBaseModel;
import com.example.finalprojectgamebook.model.Section;
import com.example.finalprojectgamebook.model.SectionAdapter;
import com.example.finalprojectgamebook.viewmodel.addViewModel;
import com.example.finalprojectgamebook.viewmodel.feedViewModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class feedFragment extends Fragment {
    private feedViewModel feedViewModel;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        feedViewModel = new ViewModelProvider(this).get(feedViewModel.class);
        View root = inflater.inflate(R.layout.fragment_feed, container, false);

        return root;
    }
}