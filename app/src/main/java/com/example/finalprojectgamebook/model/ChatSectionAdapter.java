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

    public ChatSectionAdapter(List<ChatSection> chats) {
        this.chats = chats;
    }

        public interface chatListener {
            void onMissionClicked(int position, View view);
            void onMissionLongClicked(int position, View view);
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
                        listener.onMissionClicked(getAdapterPosition(),view);
                    }
                });

                itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        listener.onMissionLongClicked(getAdapterPosition(),view);
                        return true;
                    }
                });
            }
        }

        @NonNull
        @Override
        public ChatSectionAdapter.ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_card_layout,parent,false);
            ChatSectionAdapter.ChatViewHolder chatViewHolder = new ChatSectionAdapter.ChatViewHolder(view);
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
