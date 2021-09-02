package com.example.finalprojectgamebook.views;

import android.os.Bundle;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.finalprojectgamebook.R;
import com.google.android.material.navigation.NavigationView;

public class LoginRegisterFragment extends Fragment {
    private EditText email;
    private EditText password;
    private EditText fullName;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login_register_fragments, container, false);

        return view;
    }
}