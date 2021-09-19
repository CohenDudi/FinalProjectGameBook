package com.example.finalprojectgamebook.model;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalprojectgamebook.R;

import java.util.List;

public class ChatSectionAdapter extends RecyclerView.Adapter<ChatSectionAdapter.ChatViewHolder> {

        private List<ChatSection> chats;
        private String userID;

    public void setUserId(String userID){this.userID=userID;}
    public ChatSectionAdapter(List<ChatSection> chats) {
        this.chats = chats;
    }

        public interface chatListener {
            void onChatClicked(int position, View view);
            void onChatLongClicked(int position, View view);
        }

        private ChatSectionAdapter.chatListener listener;

        public void setListener(ChatSectionAdapter.chatListener listener) {
        this.listener = listener;
    }

        public class ChatViewHolder extends RecyclerView.ViewHolder {

            TextView name;
            TextView msg;

            public ChatViewHolder(View itemView) {
                super(itemView);

                name = itemView.findViewById(R.id.chat_name);
                msg = itemView.findViewById(R.id.chat_msg);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        listener.onChatClicked(getAdapterPosition(),view);
                    }
                });

                itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        listener.onChatLongClicked(getAdapterPosition(),view);
                        return true;
                    }
                });
            }
        }

    @Override
    public int getItemViewType(int position) {
            if (chats.get(position).getUserId().equals(userID))
                return 0;
        return 1;
    }

    @NonNull
        @Override
        public ChatSectionAdapter.ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_card_layout,parent,false);
        View view2 = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_card_layout_self,parent,false);
        ChatSectionAdapter.ChatViewHolder chatViewHolder = new ChatSectionAdapter.ChatViewHolder(view);
        ChatSectionAdapter.ChatViewHolder chatViewHolder2 = new ChatSectionAdapter.ChatViewHolder(view2);

        switch (viewType) {
            case 0: return chatViewHolder2;
            case 1: return chatViewHolder;
        }
        return chatViewHolder;

    }

        @Override
        public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {

            ChatSection chat = chats.get(position);
            holder.name.setText(chat.getName());
            holder.msg.setText(chat.getMsg());
    }

        @Override
        public int getItemCount() {
        return chats.size();
    }



}
