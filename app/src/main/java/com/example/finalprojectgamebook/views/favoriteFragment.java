package com.example.finalprojectgamebook.views;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.finalprojectgamebook.R;
import com.example.finalprojectgamebook.viewmodel.favoriteViewModel;
import com.example.finalprojectgamebook.viewmodel.feedViewModel;

public class favoriteFragment extends Fragment {
    private favoriteViewModel favoriteViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        favoriteViewModel = new ViewModelProvider(this).get(favoriteViewModel.class);
        View root = inflater.inflate(R.layout.fragment_favorite, container, false);
        final TextView textView = root.findViewById(R.id.favorite_text);

        favoriteViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}