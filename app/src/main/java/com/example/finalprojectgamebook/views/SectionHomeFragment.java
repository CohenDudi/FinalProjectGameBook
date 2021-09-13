package com.example.finalprojectgamebook.views;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavArgument;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.finalprojectgamebook.R;
import com.example.finalprojectgamebook.model.Section;
import com.example.finalprojectgamebook.viewmodel.feedViewModel;
import com.example.finalprojectgamebook.viewmodel.sectionViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

public class SectionHomeFragment extends Fragment {
    sectionViewModel sectionViewModel;
    Section section;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        sectionViewModel = new ViewModelProvider(requireActivity()).get(sectionViewModel.class);
        View root = inflater.inflate(R.layout.fragment_section_home, container, false);
        if(getArguments() != null){
            section = (Section)getArguments().getSerializable("games");
            TextView text = root.findViewById(R.id.text_section_home);
            text.setText(section.getName());
            sectionViewModel.setSection(section);
        }
         else
             section = sectionViewModel.getSection();
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sectionViewModel.setSection(section);
    }
}