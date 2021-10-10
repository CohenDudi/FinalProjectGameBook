package com.example.finalprojectgamebook.views;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.finalprojectgamebook.R;
import com.example.finalprojectgamebook.model.AlertReceiver;
import com.example.finalprojectgamebook.model.FireBaseModel;
import com.example.finalprojectgamebook.model.HomePostLookingForGame;
import com.example.finalprojectgamebook.model.HomePostLookingForGameAdapter;
import com.example.finalprojectgamebook.model.Role;
import com.example.finalprojectgamebook.model.RoleAdapter;
import com.example.finalprojectgamebook.model.Section;
import com.example.finalprojectgamebook.model.TimePickerFragment;
import com.example.finalprojectgamebook.model.User;
import com.example.finalprojectgamebook.viewmodel.SectionViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SectionHomeFragment extends Fragment {
    private SectionViewModel sectionViewModel;
    private Section section;
    private FloatingActionButton fab;
    private View root;
    private RecyclerView recyclerView;
    private TextView validInput;
    private List<HomePostLookingForGame> homePostLookingForGames = new ArrayList();
    private HomePostLookingForGameAdapter adapterHome;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        sectionViewModel = new ViewModelProvider(requireActivity()).get(SectionViewModel.class);
        root = inflater.inflate(R.layout.fragment_section_home, container, false);
        fab = root.findViewById(R.id.fab);

        if(getArguments() != null){
            section = (Section)getArguments().getSerializable("games");
            sectionViewModel.setSection(section);
        }
         else
             section = sectionViewModel.getSection();
         ImageView mainImg = root.findViewById(R.id.section_img);
         TextView titleTxt = root.findViewById(R.id.section_title_txt);
         TextView descTxt = root.findViewById(R.id.section_desc_txt);
         Bitmap temp = StringToBitMap(section.getImg());
         mainImg.setImageBitmap(temp);
         titleTxt.setText(section.getName());
         descTxt.setText(section.getDescription());
         if(sectionViewModel.getUser()==null){
            fab.setEnabled(false);
         }else{
            if(sectionViewModel.getUser().isAnonymous())
                fab.setEnabled(false);
         }
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

    public Bitmap StringToBitMap(String encodedString){
        try {
            byte [] encodeByte= Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sectionViewModel.setSection(section);
    }

    public void recyclerHome(){
        FirebaseUser u = FireBaseModel.getInstance().getUser();
        User user = new User(u.getDisplayName(),u.getUid());
        RecyclerView recyclerViewHome = root.findViewById(R.id.recyclerSectionHome);

        adapterHome = new HomePostLookingForGameAdapter(homePostLookingForGames,getContext(),user,0);
        recyclerViewHome.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewHome.setHasFixedSize(true);
        recyclerViewHome.setAdapter(adapterHome);

        adapterHome.setListener(new HomePostLookingForGameAdapter.HomePostLookingForGameAdapterListener() {
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
                homePostLookingForGames.remove(position);
                sectionViewModel.updateAllPosts(homePostLookingForGames);
            }

            @Override
            public void onLeaderClicked(int position, View view) {
                openFriendProfile(position);
            }

            @Override
            public void onTimeClicked(int position, View view) {

                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getFragmentManager(), "time picker");

            }
        });
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
        validInput = view.findViewById(R.id.input_text_invalid);
        recyclerView = view.findViewById(R.id.recyclerSectionHomeDialog);

        RoleAdapter adapter = new RoleAdapter(roles,0,getContext(),"",0,"",0);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        adapter.setListener(new RoleAdapter.RoleListener() {
            @Override
            public void onRoleClicked(int position, View view) { }

            @Override
            public void onRoleLongClicked(int position, View view) { }

            @Override
            public void onRemoveClicked(int position, View view) {
                roles.remove(position);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCreateCard(int position, View view) {

            }
        });


        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int number;
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
                HomePostLookingForGame h = new HomePostLookingForGame(roles,sectionViewModel.getUser().getDisplayName(),sectionViewModel.getUser().getUid(),description.getText().toString(),section.getName());
                sectionViewModel.addNewPost(h);
                alertDialog.dismiss();
            }
        });
    }

    public void openFriendProfile(int position){
        List<User> users = sectionViewModel.getContacts();
        final Boolean[] ifFriends = {checkIfFriends(homePostLookingForGames.get(position).getUserId(),users)};

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getContext());
        View view = layoutInflaterAndroid.inflate(R.layout.friend_dialog, null);
        builder.setView(view);
        builder.setCancelable(false);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        Button close_btn = view.findViewById(R.id.close_btn);
        Button add_friend_btn = view.findViewById(R.id.add_to_friend_btn);
        TextView friendName = view.findViewById(R.id.friend_name_txt);

        friendName.setText(homePostLookingForGames.get(position).getUserName());

        if(ifFriends[0] || homePostLookingForGames.get(position).getUserId().equals(sectionViewModel.getUser().getUid())){
            add_friend_btn.setEnabled(false);
            add_friend_btn.setTextColor(Color.GRAY);
        }

        close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        add_friend_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!ifFriends[0] ){
                    User me = new User(sectionViewModel.getUser().getDisplayName(),sectionViewModel.getUser().getUid());
                    sectionViewModel.addNewContact(new User(homePostLookingForGames.get(position).getUserName(),homePostLookingForGames.get(position).getUserId()));
                    sectionViewModel.addNewFriendContact(me,homePostLookingForGames.get(position).getUserId());
                    ifFriends[0] = true;
                    add_friend_btn.setEnabled(false);
                    add_friend_btn.setTextColor(Color.GRAY);
                }
            }
        });

        if(sectionViewModel.isAnonymous())
        {
            add_friend_btn.setEnabled(false);
            add_friend_btn.setText("Please Login");
        }
    }

    public boolean checkIfFriends(String userID,List<User> users){
        for (User user:users) {
            if(user.getUserId().equals(userID))return true;
        }
        return false;
    }


}