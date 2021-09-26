package com.example.finalprojectgamebook.views;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
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
import com.example.finalprojectgamebook.model.HomePostLookingForGame;
import com.example.finalprojectgamebook.model.HomePostLookingForGameAdapter;
import com.example.finalprojectgamebook.model.Role;
import com.example.finalprojectgamebook.model.RoleAdapter;
import com.example.finalprojectgamebook.model.Section;
import com.example.finalprojectgamebook.model.SectionAdapter;
import com.example.finalprojectgamebook.viewmodel.feedViewModel;
import com.example.finalprojectgamebook.viewmodel.sectionViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class SectionHomeFragment extends Fragment {
    sectionViewModel sectionViewModel;
    Section section;
    FloatingActionButton fab;
    View root;
    RecyclerView recyclerView;
    TextView validInput;
    List<HomePostLookingForGame> homePostLookingForGames = new ArrayList();
    HomePostLookingForGameAdapter adapterHome;


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

        homePostLookingForGames = sectionViewModel.getPosts();

         fab.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 openDialog();
             }
         });
        recyclerHome();
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sectionViewModel.setSection(section);
    }

    public void recyclerHome(){
        RecyclerView recyclerViewHome = root.findViewById(R.id.recyclerSectionHome);
        adapterHome = new HomePostLookingForGameAdapter(homePostLookingForGames);
        recyclerViewHome.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewHome.setHasFixedSize(true);
        recyclerViewHome.setAdapter(adapterHome);
        updatePosts();

    }

    public void updatePosts() {
        sectionViewModel.getFireBase().child("section feed").child(section.getName()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                homePostLookingForGames = sectionViewModel.getPosts();
                adapterHome.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
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
        EditText description = view.findViewById(R.id.add_post_description);
        //AppCompatButton closeBtn = view.findViewById(R.id.close_btn);
        validInput = view.findViewById(R.id.input_text_invalid);
        recyclerView = view.findViewById(R.id.recyclerSectionHomeDialog);

        RoleAdapter adapter = new RoleAdapter(roles);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        adapter.setListener(new RoleAdapter.RoleListener() {
            @Override
            public void onRoleClicked(int position, View view) {

            }

            @Override
            public void onRoleLongClicked(int position, View view) {

            }

            @Override
            public void onRemoveClicked(int position, View view) {
                roles.remove(position);
                adapter.notifyDataSetChanged();
            }
        });


        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int number;
                //                        Snackbar.make(root ,section.getName() + " created!" , Snackbar.LENGTH_SHORT).show();

                try {
                    number = Integer.parseInt(maxEt.getText().toString());
                    Role role = new Role(nameEt.getText().toString(),0,Integer.parseInt(maxEt.getText().toString()));
                    roles.add(role);
                    adapter.notifyDataSetChanged();
                    validInput.setVisibility(View.INVISIBLE);
                    recyclerView.scrollToPosition(roles.size()-1);

                } catch(NumberFormatException e) {
                    validInput.setVisibility(View.VISIBLE);

                } catch(NullPointerException e) {
                    validInput.setVisibility(View.VISIBLE);
                }

            }
        });
        view.findViewById(R.id.close_add_btn).setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            alertDialog.dismiss();
        }
    });
        view.findViewById(R.id.submit_add_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sectionViewModel.addNewPost(new HomePostLookingForGame(roles,sectionViewModel.getUser().getDisplayName(),sectionViewModel.getUser().getUid(),description.getText().toString()));
                alertDialog.dismiss();
            }
        });


    }
}