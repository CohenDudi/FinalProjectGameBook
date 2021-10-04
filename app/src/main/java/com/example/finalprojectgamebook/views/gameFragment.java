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
import com.example.finalprojectgamebook.model.FireBaseModel;
import com.example.finalprojectgamebook.model.Section;
import com.example.finalprojectgamebook.model.SectionAdapter;
import com.example.finalprojectgamebook.model.User;
import com.example.finalprojectgamebook.viewmodel.feedViewModel;
import com.example.finalprojectgamebook.viewmodel.gameViewModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class gameFragment extends Fragment {
    private gameViewModel gameViewModel;
    List<Section> sections = new ArrayList<>();

    SectionAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        gameViewModel = new ViewModelProvider(this).get(gameViewModel.class);
        View root = inflater.inflate(R.layout.fragment_game, container, false);

        RecyclerView recyclerView = root.findViewById(R.id.recycler);

        sections = gameViewModel.getSections();
        adapter = new SectionAdapter(sections);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        adapter.setListener(new SectionAdapter.SectionListener() {
            @Override
            public void onMissionClicked(int position, View view) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("games",sections.get(position));
                Navigation.findNavController(view).navigate(R.id.action_navigation_games_to_navigation_discover, bundle);

            }

            @Override
            public void onMissionLongClicked(int position, View view) {

            }

            @Override
            public void onFavoriteClicked(int position, View view) {
                User user = gameViewModel.getSelfUser();
                if(!user.isFavorite(sections.get(position).getName())){
                    user.addFavorite(sections.get(position).getName());
                    gameViewModel.updateSelfUser(user);
                }

            }
        });
        updateFeed();
        return root;
    }

    public void updateFeed(){
        gameViewModel.getFireBase().child("section").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                sections = gameViewModel.getSections();
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
    }

}