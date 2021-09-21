package com.example.finalprojectgamebook.model;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalprojectgamebook.R;

import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {

    private List<User> users;

    public ContactAdapter(List<User> users) {
        this.users = users;
    }

    public interface ContactListener {
        void onContactClicked(int position, View view);
        void onContactLongClicked(int position, View view);
    }

    private ContactAdapter.ContactListener listener;

    public void setListener(ContactAdapter.ContactListener listener) {
        this.listener = listener;
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        ImageView newMsgIcon;

        public ContactViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.contact_name);
            newMsgIcon = itemView.findViewById(R.id.msg_icon_iv);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onContactClicked(getAdapterPosition(),view);
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    listener.onContactLongClicked(getAdapterPosition(),view);
                    return true;
                }
            });
        }
    }

    @NonNull
    @Override
    public ContactAdapter.ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_card,parent,false);
        ContactAdapter.ContactViewHolder contactViewHolder = new ContactAdapter.ContactViewHolder(view);
        return contactViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ContactAdapter.ContactViewHolder holder, int position) {

        User user = users.get(position);
        holder.name.setText(user.getName());
        if(users.get(position).getNewMsg()) holder.newMsgIcon.setVisibility(View.VISIBLE);
        else holder.newMsgIcon.setVisibility(View.INVISIBLE);

    }

    @Override
    public int getItemCount() {
        return users.size();
    }

}
