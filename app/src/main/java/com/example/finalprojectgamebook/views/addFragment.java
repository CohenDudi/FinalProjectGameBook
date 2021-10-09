package com.example.finalprojectgamebook.views;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalprojectgamebook.R;
import com.example.finalprojectgamebook.model.FireBaseModel;
import com.example.finalprojectgamebook.model.Section;
import com.example.finalprojectgamebook.viewmodel.addViewModel;
import com.example.finalprojectgamebook.viewmodel.feedViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class addFragment extends Fragment {
    private addViewModel addViewModel;
    private FireBaseModel fireBaseModel;

    private EditText name;
    private EditText type;
    private EditText desc;
    private Button addImgBtn;
    private Bitmap imageBitmap;
    private ImageView sectionImg;
    String encoded;


    public addFragment(){}


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fireBaseModel = FireBaseModel.getInstance();
        addViewModel = new ViewModelProvider(this).get(addViewModel.class);
        View root = inflater.inflate(R.layout.fragment_add, container, false);
        name = root.findViewById(R.id.add_new_section_name);
        type = root.findViewById(R.id.add_new_section_type);
        desc = root.findViewById(R.id.add_new_section_desc);
        addImgBtn = root.findViewById(R.id.add_picture_btn);
        sectionImg = root.findViewById(R.id.img_section);
        Button buttonNewSection = root.findViewById(R.id.button_new_section);


        buttonNewSection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fireBaseModel.getUser()!=null)
                    if(!fireBaseModel.getUser().isAnonymous()){


                Section section = new Section(name.getText().toString(), type.getText().toString(), desc.getText().toString(),encoded);
                int validateSection = addViewModel.isSectionNotExist(section);
                switch (validateSection){
                    case 0:
                        addViewModel.addNewSection(section);
                        Snackbar.make(root ,section.getName() + " created!" , Snackbar.LENGTH_SHORT).show();
                        break;
                    case 1:
                        Snackbar.make(root , "Name already exist", Snackbar.LENGTH_SHORT).show();
                        break;
                    case 4:
                        Snackbar.make(root , "Game name is empty", Snackbar.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Snackbar.make(root , "Description is empty", Snackbar.LENGTH_SHORT).show();
                        break;
                    case 3:
                        Snackbar.make(root , "Game type is empty", Snackbar.LENGTH_SHORT).show();
                        break;
                    case 6:
                        Snackbar.make(root , "Description is too long", Snackbar.LENGTH_SHORT).show();
                        break;
                    case 7:
                        Snackbar.make(root , "Please add a picture", Snackbar.LENGTH_SHORT).show();
                        break;
                    default:
                        Snackbar.make(root , "Empty", Snackbar.LENGTH_SHORT).show();
                        break;
                }
                    }
                else Snackbar.make(root , "Please Login", Snackbar.LENGTH_SHORT).show();

            }
        });

        ActivityResultLauncher<Intent> takePictureActivity = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            Uri imageUri = data.getData();
                            try {
                                imageBitmap = MediaStore.Images.Media.getBitmap(getActivity().getApplicationContext().getContentResolver(), imageUri);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 10, baos); //bm is the bitmap object
                            byte[] b = baos.toByteArray();
                            encoded = Base64.encodeToString(b, Base64.DEFAULT);
                            sectionImg.setImageBitmap(imageBitmap);

                        }
                    }
                });


        addImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fireBaseModel.getUser() != null){
                    if(!fireBaseModel.getUser().isAnonymous())
                    {
                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                        takePictureActivity.launch(intent);
                    }
                }
                else Snackbar.make(root , "Please Login", Snackbar.LENGTH_SHORT).show();

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