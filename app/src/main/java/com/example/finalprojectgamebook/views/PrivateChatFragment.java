package com.example.finalprojectgamebook.views;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.finalprojectgamebook.R;
import com.example.finalprojectgamebook.model.ChatSection;
import com.example.finalprojectgamebook.model.ChatSectionAdapter;
import com.example.finalprojectgamebook.viewmodel.PrivateChatViewModel;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PrivateChatFragment extends Fragment {
    private PrivateChatViewModel priavteChatViewModel;
    private String usersId;
    private String friendId;
    private List<ChatSection> chats = new ArrayList<>();
    private RecyclerView recyclerView;
    private ChatSectionAdapter adapter;
    private FirebaseUser user;
    private BroadcastReceiver receiver;
    final String API_TOKEN_KEY = "AAAAV1YpTgI:APA91bFsz1JYI6wx0TKdJK10hsrFthaZQlwLp6uLApQF-Z_3IHmneGgEgzkCZ1QTxvjgtRhxUnSAhTogwGm4iK7ObbbprcJCl1gy7T9f5YyMJhSX--IqWkzt1ZPZ1PFt1ypwXNOhQhGX";

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        priavteChatViewModel = new ViewModelProvider(this).get(PrivateChatViewModel.class);
        View root = inflater.inflate(R.layout.fragment_private_chat, container, false);
        usersId = (String)getArguments().getSerializable("chatId");
        friendId = (String)getArguments().getSerializable("friendId");
        priavteChatViewModel.changeMsgSeen(friendId,1);

        priavteChatViewModel.setUsersId(usersId);
        chats = priavteChatViewModel.getChats();
        user = priavteChatViewModel.getUser();

        recyclerView = root.findViewById(R.id.recyclerChatPrivate);
        EditText editText = root.findViewById(R.id.msg_input_chat_priavte);
        ImageButton imageButton = root.findViewById(R.id.msg_btn_chat_private);

        adapter = new ChatSectionAdapter(chats);
        adapter.setUserId(priavteChatViewModel.getUser().getUid());
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setHasFixedSize(false);
        recyclerView.setAdapter(adapter);
        adapter.setListener(new ChatSectionAdapter.chatListener() {
            @Override
            public void onChatClicked(int position, View view) {

            }

            @Override
            public void onChatLongClicked(int position, View view) {

            }
        });
        updateFeed();

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                priavteChatViewModel.addNewMsg(new ChatSection(user.getDisplayName(),user.getUid(),editText.getText().toString()));
                sendNotification(friendId, editText.getText().toString());
                editText.setText("");
                hideSoftKeyboard(getActivity());
                priavteChatViewModel.changeMsgSeen(friendId,0);
            }
        });

        return root;
    }

    public void updateFeed(){
        priavteChatViewModel.getFireBase().child("private chat").child(usersId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chats = priavteChatViewModel.getChats();
                adapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(chats.size()-1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        if(inputMethodManager.isAcceptingText()){
            inputMethodManager.hideSoftInputFromWindow(
                    activity.getCurrentFocus().getWindowToken(),
                    0
            );
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        priavteChatViewModel.changeMsgSeen(friendId,1);
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(receiver);
    }

    public void sendNotification(String friendId,String text){
        final JSONObject rootObject  = new JSONObject();
        try{
            rootObject.put("to", "/topics/" + friendId);
            rootObject.put("data",new JSONObject().put("message",text));
            String url = "https://fcm.googleapis.com/fcm/send";

            RequestQueue queue = Volley.newRequestQueue(getContext());
            StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) { }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }) {

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String,String> headers = new HashMap<>();
                    headers.put("Content-Type","application/json");
                    headers.put("Authorization","key="+API_TOKEN_KEY);
                    return headers;
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    return rootObject.toString().getBytes();
                }
            };
            queue.add(request);
            queue.start();

        }catch (JSONException e){
            e.printStackTrace();
        }

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) { }
        };
        IntentFilter filter = new IntentFilter("message_received");
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(receiver,filter);
    }
}