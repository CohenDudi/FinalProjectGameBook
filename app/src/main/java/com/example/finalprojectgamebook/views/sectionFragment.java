package com.example.finalprojectgamebook.views;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.finalprojectgamebook.R;
import com.example.finalprojectgamebook.model.Section;
import com.example.finalprojectgamebook.viewmodel.feedViewModel;
import com.example.finalprojectgamebook.viewmodel.gameViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class sectionFragment extends Fragment {
    gameViewModel gameViewModel;
    Section section;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        gameViewModel = new ViewModelProvider(this).get(gameViewModel.class);
        View root = inflater.inflate(R.layout.fragment_section, container, false);
        //section = (Section)getArguments().getSerializable("games");

        /**
        BottomNavigationView navView = root.findViewById(R.id.nav_view_section);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_discover, R.id.navigation_chat)
                .build();

        NavController navController = Navigation.findNavController((AppCompatActivity)getActivity(), R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController((AppCompatActivity) getActivity(), navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
        **/
        return root;
    }
}