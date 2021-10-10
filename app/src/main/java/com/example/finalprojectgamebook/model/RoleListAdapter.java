package com.example.finalprojectgamebook.model;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalprojectgamebook.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class RoleListAdapter extends RecyclerView.Adapter<RoleListAdapter.roleListViewHolder> {

    private List<User> users;
    private String originalPoster;

    public RoleListAdapter(List<User> users, String originalPoster) {
        this.originalPoster = originalPoster;
        this.users = users;
    }

    public interface roleListListener {
        void onRoleListClicked(int position, View view);
        void onRoleListLongClicked(int position, View view);
        void onRemoveClicked(int position,View view);
    }

    private RoleListAdapter.roleListListener listener;

    public void setListener(RoleListAdapter.roleListListener listener) {
        this.listener = listener;
    }

    public class roleListViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        ImageView removeBtn;


        public roleListViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.user_name_section);
            removeBtn = itemView.findViewById(R.id.remove_user_btn);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onRoleListClicked(getAdapterPosition(),view);
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    listener.onRoleListLongClicked(getAdapterPosition(),view);
                    return true;
                }
            });

            removeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onRemoveClicked(getAdapterPosition(),view);
                }
            });
        }
    }

    @NonNull
    @Override
    public RoleListAdapter.roleListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.name_card,parent,false);
        RoleListAdapter.roleListViewHolder roleListViewHolder = new RoleListAdapter.roleListViewHolder(view);
        return roleListViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RoleListAdapter.roleListViewHolder holder, int position) {

        User user = users.get(position);
        holder.name.setText(user.getName());
        if(FirebaseAuth.getInstance().getCurrentUser() != null) {
            if (FireBaseModel.getInstance().getUser().getUid().equals(originalPoster)) {
                holder.removeBtn.setVisibility(View.VISIBLE);
            } else {
                holder.removeBtn.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

}
