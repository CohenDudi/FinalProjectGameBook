package com.example.finalprojectgamebook.model;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.finalprojectgamebook.R;
import com.example.finalprojectgamebook.views.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FireBaseModel {
    private static FireBaseModel single_instance = null;
    private Application application;
    private FirebaseAuth firebaseAuth;
    private MutableLiveData<FirebaseUser> userMutableLiveData;
    private DatabaseReference mDatabase;
    private User selfUser;
    private List<Section> sections = new ArrayList<>();
    private List<User> usersFriend = new ArrayList<>();
    private List<User> users = new ArrayList<>();
    private ArrayList<ArrayList<HomePostLookingForGame>> feedPosts = new ArrayList<ArrayList<HomePostLookingForGame>>();



    private FireBaseModel(){
        firebaseAuth = FirebaseAuth.getInstance();
        userMutableLiveData = new MutableLiveData<>();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        readSections();
        readAllPosts();
        readContacts();
    }


    public DatabaseReference getmDatabase(){
        return mDatabase;
    }

    public static FireBaseModel getInstance(){
        if (single_instance == null)
            single_instance = new FireBaseModel();
        return single_instance;
    }

    public void setApp(Application application){
        this.application = application;
    }


    public void register(String email, String password){
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    userMutableLiveData.postValue(firebaseAuth.getCurrentUser());
                }
                else
                    Toast.makeText(application, R.string.Register_Failed +task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void registerAnonymous(String email,String password){
        AuthCredential credential = EmailAuthProvider.getCredential(email, password);
        firebaseAuth.getCurrentUser().linkWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                        } else {
                            Toast.makeText(application, R.string.Authentication_Failed, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    public FirebaseAuth getFirebaseAuth(){
        return firebaseAuth;
    }

    public void signInAnonymously(){
        if(getUser()==null)
            firebaseAuth.signInAnonymously()
                    .addOnCompleteListener(application.getMainExecutor(),new OnCompleteListener<AuthResult>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task)
                        {
                            Log.d("FirebaseAuth", "signInAnonymously:onComplete:" + task.isSuccessful());

                            if (!task.isSuccessful())
                            {
                                Log.w("FirebaseAuth", "signInAnonymously", task.getException());
                                Toast.makeText(application.getApplicationContext(), R.string.Authentication_Failed,
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        else
            getUser().reload();
    }

    public void updateName(String fullName){
            if(fullName != null){
                getUser().updateProfile(new UserProfileChangeRequest.Builder().setDisplayName(fullName).build()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });
            }
        }
/*
    public void updateImg(Uri uri){
        getUser().updateProfile(new UserProfileChangeRequest.Builder().setPhotoUri(uri).build()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });
    }

    public void updateBitmap(Bitmap bitmap){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference mountainsRef = storageRef.child(getUser().getUid());

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = mountainsRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
            }
        });

    }

    public Bitmap getBitmap(String userId) throws IOException {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference mountainsRef = storageRef.child(getUser().getUid());
        File localFile = File.createTempFile("images", "jpg");

        mountainsRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                // Local temp file has been created
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
        Bitmap b = BitmapFactory.decodeFile(localFile.getAbsolutePath());
        return b;


    }


 */
    public void login(String email, String password){
        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                    Toast.makeText(application,R.string.Sign_In_Successful, Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(application,R.string.Sign_In_Failed, Toast.LENGTH_SHORT).show();
            }
        });
    }



    public MutableLiveData<FirebaseUser> getUserMutableLiveData() {
        return userMutableLiveData;

    }

    public FirebaseUser getUser(){
        final FirebaseUser user = firebaseAuth.getCurrentUser();
        return user;
    }


    public void setNewListener(FirebaseAuth.AuthStateListener authStateListener){
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    public void addNewSection(Section section){
        sections.add(section);
        mDatabase.child("section").setValue(sections);
    }

    public void updateSection(Section section,int position){
            mDatabase.child("section").child(String.valueOf(position)).setValue(section);

    }

    public void addNewContact(User user){
        users.add(user);
        mDatabase.child("contact").child(getUser().getUid()).setValue(users);
    }

    public void addNewFriendContact(User user,String friendUserId){
        usersFriend.add(user);
        mDatabase.child("contact").child(friendUserId).setValue(usersFriend);
    }

    public void updateSelfUser(User user){
        mDatabase.child("users").child(getUser().getUid()).setValue(user);
    }

    public void readSelfUser(){
        if(getUser()!=null)
        mDatabase.child("users").child(getUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                selfUser = null;
                if(dataSnapshot.exists()) {
                    selfUser = dataSnapshot.getValue(User.class);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public User getSelfUser(){
        return selfUser;
    }

    public void readSections(){

        mDatabase.child("section").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                sections.clear();
                if(dataSnapshot.exists()) {
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Section section = snapshot.getValue(Section.class);
                        sections.add(section);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void readAllPosts(){
        mDatabase.child("section feed").addValueEventListener(new ValueEventListener() {
            int i = 0;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                feedPosts.clear();
                Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();
                int length = (int) dataSnapshot.getChildrenCount();
                while(i < length) {
                    GenericTypeIndicator<ArrayList<HomePostLookingForGame>> t = new GenericTypeIndicator<ArrayList<HomePostLookingForGame>>() {};
                    feedPosts.add(iterator.next().getValue(t));
                    i++;
                }
                i = 0;
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    public List<HomePostLookingForGame> getAllPosts(){
        List<HomePostLookingForGame> temp = new ArrayList<>();
        for (List<HomePostLookingForGame> f:feedPosts) {
            for (HomePostLookingForGame h:f) {
                temp.add(h);
            }
        }
        return temp;
    }

    public void readContacts(){
        users.clear();
        if(getUser()!=null)
        if(!getUser().isAnonymous())
         {
            mDatabase.child("contact").child(getUser().getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    users.clear();
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            User user = snapshot.getValue(User.class);
                            users.add(user);
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) { }
            });
        }
    }

    public void readFriendContacts(String friendUserId){
        mDatabase.child("contact").child(friendUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usersFriend.clear();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        User user = snapshot.getValue(User.class);
                        usersFriend.add(user);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }





    public void signOut(){
        firebaseAuth.signOut();
    }

    public List<Section> getSections(){
        return sections;
    }

    public List<User> getContacts(){
        return users;
    }

    public List<User> geFriendContacts(){
        return usersFriend;
    }

    // 1 = self , 0 = friend
    public void changeMsgSeen(String friendUserId,int sender){
        if(sender == 1){
            for (User user:users) {
                if(user.getUserId().equals(friendUserId))
                    user.readMsg(false);
            }
            mDatabase.child("contact").child(getUser().getUid()).setValue(users);
        }
        else{
            for (User user:usersFriend) {
                if(user.getUserId().equals(getUser().getUid()))
                    user.readMsg(true);
            }
            mDatabase.child("contact").child(friendUserId).setValue(usersFriend);

        }
    }
}
