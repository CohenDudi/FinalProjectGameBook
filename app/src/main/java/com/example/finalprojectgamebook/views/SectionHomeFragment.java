package com.example.finalprojectgamebook.views;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavArgument;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.finalprojectgamebook.R;
import com.example.finalprojectgamebook.model.Role;
import com.example.finalprojectgamebook.model.RoleAdapter;
import com.example.finalprojectgamebook.model.Section;
import com.example.finalprojectgamebook.viewmodel.feedViewModel;
import com.example.finalprojectgamebook.viewmodel.sectionViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class SectionHomeFragment extends Fragment {
    sectionViewModel sectionViewModel;
    Section section;
    FloatingActionButton fab;
    View root;
    RecyclerView recyclerView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        sectionViewModel = new ViewModelProvider(requireActivity()).get(sectionViewModel.class);
        root = inflater.inflate(R.layout.fragment_section_home, container, false);
        fab = root.findViewById(R.id.fab);

        if(getArguments() != null){
            section = (Section)getArguments().getSerializable("games");
            sectionViewModel.setSection(section);
        }
         else
             section = sectionViewModel.getSection();


         fab.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 openDialog();
             }
         });
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sectionViewModel.setSection(section);
    }

    public void openDialog(){
        List<Role> roles = new ArrayList();
        roles.add(new Role("dps",0,3));
        roles.add(new Role("healer",0,1));

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getContext());
        View view = layoutInflaterAndroid.inflate(R.layout.add_post_dialog, null);





        builder.setView(view);
        builder.setCancelable(false);
        AlertDialog alertDialog = builder.create();

        alertDialog.show();

        EditText nameEt = view.findViewById(R.id.role_name_dlg);
        EditText maxEt = view.findViewById(R.id.number_dlg);
        ImageButton addBtn = view.findViewById(R.id.add_btn);
        recyclerView = view.findViewById(R.id.recyclerSectionHomeDialog);
        RoleAdapter adapter = new RoleAdapter(roles);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Role role = new Role(nameEt.getText().toString(),0,Integer.parseInt(maxEt.getText().toString()));
                roles.add(role);
                adapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(roles.size()-1);
            }
        });


    }
}