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
import com.example.finalprojectgamebook.viewmodel.GameViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class GameFragment extends Fragment {
    private GameViewModel gameViewModel;
    private List<Section> sections = new ArrayList<>();
    private SectionAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        gameViewModel = new ViewModelProvider(this).get(GameViewModel.class);
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
            public void onMissionLongClicked(int position, View view) { }

            @Override
            public void onFavoriteClicked(int position, View view) {
                if(!FirebaseAuth.getInstance().getCurrentUser().isAnonymous()) {
                    List<String> usersIds = sections.get(position).getUsersId();
                    String selfId = FireBaseModel.getInstance().getUser().getUid();
                    if (usersIds.contains(selfId))
                        usersIds.remove(selfId);
                    else
                        usersIds.add(selfId);
                    FireBaseModel.getInstance().updateSection(sections.get(position), position);
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
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }
}