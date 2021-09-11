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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.finalprojectgamebook.R;
import com.example.finalprojectgamebook.model.FireBaseModel;
import com.example.finalprojectgamebook.model.Section;
import com.example.finalprojectgamebook.viewmodel.addViewModel;
import com.example.finalprojectgamebook.viewmodel.feedViewModel;

import java.util.ArrayList;
import java.util.List;

public class addFragment extends Fragment {
    private addViewModel addViewModel;
    private FireBaseModel fireBaseModel;

    private EditText name;
    private EditText type;
    private EditText desc;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fireBaseModel = FireBaseModel.getInstance();
        addViewModel = new ViewModelProvider(this).get(addViewModel.class);
        View root = inflater.inflate(R.layout.fragment_add, container, false);
        name = root.findViewById(R.id.add_new_section_name);
        type = root.findViewById(R.id.add_new_section_type);
        desc = root.findViewById(R.id.add_new_section_desc);
        Button buttonNewSection = root.findViewById(R.id.button_new_section);

        buttonNewSection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Section section = new Section(name.getText().toString(), type.getText().toString(), desc.getText().toString());
                addViewModel.addNewSection(section);
            }
        });
        /**
        final TextView textView = root.findViewById(R.id.add_Text);

        addViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

                textView.setText(s);
            }
        });
         **/

        return root;
    }
}