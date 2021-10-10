package com.example.finalprojectgamebook.views;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.finalprojectgamebook.R;
import com.example.finalprojectgamebook.model.FireBaseModel;
import com.example.finalprojectgamebook.model.Section;
import com.example.finalprojectgamebook.viewmodel.AddViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class AddFragment extends Fragment {
    private AddViewModel addViewModel;
    private FireBaseModel fireBaseModel;

    private EditText name;
    private EditText type;
    private EditText desc;
    private Button addImgBtn;
    private Bitmap imageBitmap;
    private ImageView sectionImg;
    private String encoded;
    private final String[] okFileExtensions = new String[] {
            "jpg",
            "png",
            "gif",
            "jpeg"
    };


    public AddFragment(){}


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fireBaseModel = FireBaseModel.getInstance();
        addViewModel = new ViewModelProvider(this).get(AddViewModel.class);
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
                        Snackbar.make(root ,section.getName() + getText(R.string.Created) , Snackbar.LENGTH_SHORT).show();
                        Navigation.findNavController(v).navigate(R.id.action_navigation_add_to_navigation_games);
                        break;
                    case 1:
                        Snackbar.make(root , R.string.Name_Already_Exist, Snackbar.LENGTH_SHORT).show();
                        break;
                    case 4:
                        Snackbar.make(root , R.string.Game_Name_Is_Empty, Snackbar.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Snackbar.make(root , R.string.Description_Is_Empty, Snackbar.LENGTH_SHORT).show();
                        break;
                    case 3:
                        Snackbar.make(root , R.string.Game_Type_Is_Empty, Snackbar.LENGTH_SHORT).show();
                        break;
                    case 6:
                        Snackbar.make(root , R.string.Description_is_too_long, Snackbar.LENGTH_SHORT).show();
                        break;
                    case 7:
                        Snackbar.make(root , R.string.Please_add_a_picture, Snackbar.LENGTH_SHORT).show();
                        break;
                    default:
                        Snackbar.make(root , R.string.Empty, Snackbar.LENGTH_SHORT).show();
                        break;
                }
                    }
                else Snackbar.make(root , R.string.Please_Login, Snackbar.LENGTH_SHORT).show();

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
                            if (accept(imageUri)) {
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

                                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) sectionImg.getLayoutParams();
                                params.width = 450;
                                params.height = 450;
                                // existing height is ok as is, no need to edit it
                                sectionImg.setLayoutParams(params);

                            }
                        }
                        else
                            Snackbar.make(root , R.string.Invalid_Image_Type, Snackbar.LENGTH_SHORT).show();
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
                else Snackbar.make(root , R.string.Please_Login, Snackbar.LENGTH_SHORT).show();

            }
        });
        return root;
    }

    public boolean accept(Uri uri) {
        String extension;
        ContentResolver contentResolver = getContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        extension= mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
        for(int i = 0; i < okFileExtensions.length; i++){
            if(okFileExtensions[i].equals(extension)) return true;
        }
        return false;
    }
}