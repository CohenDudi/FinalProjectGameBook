package com.example.finalprojectgamebook.views;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.TextView;

import com.example.finalprojectgamebook.R;
import com.example.finalprojectgamebook.model.FireBaseModel;
import com.example.finalprojectgamebook.model.FireBaseSectionChat;
import com.example.finalprojectgamebook.model.HomePostLookingForGame;
import com.example.finalprojectgamebook.model.HomePostLookingForGameAdapter;
import com.example.finalprojectgamebook.model.Section;
import com.example.finalprojectgamebook.model.SectionAdapter;
import com.example.finalprojectgamebook.model.User;
import com.example.finalprojectgamebook.viewmodel.addViewModel;
import com.example.finalprojectgamebook.viewmodel.feedViewModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class feedFragment extends Fragment {
    private feedViewModel feedViewModel;
    List<HomePostLookingForGame> feedPosts = new ArrayList<>();
    HomePostLookingForGameAdapter adapter;
    User user;
    ValueEventListener eventUpdatePosts;
    View root;
    RecyclerView recyclerView;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(FireBaseModel.getInstance().getUser()!=null)
            if(!FireBaseModel.getInstance().getUser().isAnonymous())
                user = new User(FireBaseModel.getInstance().getUser().getDisplayName(),FireBaseModel.getInstance().getUser().getUid());
            else{
                user = new User("Anonymous",FireBaseModel.getInstance().getUser().getUid());
            }
        feedPosts = FireBaseModel.getInstance().getAllPosts();

        //feedPosts = FireBaseModel.getInstance().getAllPosts();

        adapter = new HomePostLookingForGameAdapter(feedPosts,getContext(),user,1);

        adapter.setListener(new HomePostLookingForGameAdapter.HomePostLookingForGameAdapterListener() {
            @Override
            public void onHomePostLookingForGameAdapterClicked(int position, View view) {
                Bundle bundle = new Bundle();
                Section section = null;
                for (Section s:FireBaseModel.getInstance().getSections()) {
                    if(s.getName().equals(feedPosts.get(position).getGameName()))
                        section = s;
                }
                bundle.putSerializable("games",section);
                Navigation.findNavController(view).navigate(R.id.action_navigation_feed_to_navigation_discover, bundle);

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

            @Override
            public void onLeaderClicked(int position, View view) {

            }
        });

        updatePosts();
    }



    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        feedViewModel = new ViewModelProvider(this).get(feedViewModel.class);
        root = inflater.inflate(R.layout.fragment_feed, container, false);
        recyclerView = root.findViewById(R.id.recyclerFeed);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        return root;
    }

    public void updatePosts(){
        eventUpdatePosts = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                feedPosts.clear();
                feedPosts.addAll(FireBaseModel.getInstance().getAllPosts());
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
