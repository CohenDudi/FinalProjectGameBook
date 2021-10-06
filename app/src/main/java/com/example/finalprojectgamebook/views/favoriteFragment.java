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
import android.widget.Toolbar;

import com.example.finalprojectgamebook.R;
import com.example.finalprojectgamebook.model.FireBaseModel;
import com.example.finalprojectgamebook.model.HomePostLookingForGame;
import com.example.finalprojectgamebook.model.HomePostLookingForGameAdapter;
import com.example.finalprojectgamebook.model.User;
import com.example.finalprojectgamebook.viewmodel.favoriteViewModel;
import com.example.finalprojectgamebook.viewmodel.feedViewModel;
import com.example.finalprojectgamebook.viewmodel.gameViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class favoriteFragment extends Fragment {
    private favoriteViewModel favoriteViewModel;
    List<HomePostLookingForGame> feedPosts = new ArrayList<>();
    List<HomePostLookingForGame> favPosts = new ArrayList<>();

    HomePostLookingForGameAdapter adapter;
    User user;
    ValueEventListener eventUpdatePosts;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        favoriteViewModel = new ViewModelProvider(this).get(favoriteViewModel.class);
        View root = inflater.inflate(R.layout.fragment_favorite, container, false);
        feedPosts = FireBaseModel.getInstance().getAllPosts();
        favPosts.addAll(favoriteViewModel.getFavoritePosts(feedPosts));
        RecyclerView recyclerView = root.findViewById(R.id.recyclerFavorite);
        //feedPosts = FireBaseModel.getInstance().getAllPosts();

        adapter = new HomePostLookingForGameAdapter(favPosts,getContext(),user,1);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        adapter.setListener(new HomePostLookingForGameAdapter.HomePostLookingForGameAdapterListener() {
            @Override
            public void onHomePostLookingForGameAdapterClicked(int position, View view) {

            }

            @Override
            public void onHomePostLookingForGameAdapterLongClicked(int position, View view) {

            }

            @Override
            public void onHomePostLookingForGameAdapterRecyclerClicked(int position, View view) {

            }

            @Override
            public void onClosedClicked(int position, View view) {

            }
        });

        updatePosts();
        return root;
    }

    public void updatePosts(){
        eventUpdatePosts = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                feedPosts.clear();
                favPosts.clear();
                feedPosts.addAll(FireBaseModel.getInstance().getAllPosts());
                favPosts.addAll(favoriteViewModel.getFavoritePosts(feedPosts));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        FireBaseModel.getInstance().getmDatabase().child("section feed").addValueEventListener(eventUpdatePosts);
    }

    @Override
    public void onPause() {
        super.onPause();
        FireBaseModel.getInstance().getmDatabase().child("section feed").removeEventListener(eventUpdatePosts);
    }

    @Override
    public void onStop() {
        super.onStop();
        FireBaseModel.getInstance().getmDatabase().child("section feed").removeEventListener(eventUpdatePosts);

    }

}