package com.example.finalprojectgamebook.model;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalprojectgamebook.R;

import java.util.List;

public class RoleAdapter extends RecyclerView.Adapter<RoleAdapter.RoleViewHolder> {

    private List<Role> roles;

    public RoleAdapter(List<Role> roles) {
        this.roles = roles;
    }

    public interface RoleListener {
        void onRoleClicked(int position, View view);
        void onRoleLongClicked(int position, View view);
    }

    private RoleAdapter.RoleListener listener;

    public void setListener(RoleAdapter.RoleListener listener) {
        this.listener = listener;
    }

    public class RoleViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView min;
        TextView max;



        public RoleViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.roll_name);
            min = itemView.findViewById(R.id.apply_number);
            max = itemView.findViewById(R.id.max_number);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onRoleClicked(getAdapterPosition(),view);
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    listener.onRoleLongClicked(getAdapterPosition(),view);
                    return true;
                }
            });
        }
    }

    @NonNull
    @Override
    public RoleAdapter.RoleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_new_roll_card,parent,false);
        RoleAdapter.RoleViewHolder roleViewHolder = new RoleAdapter.RoleViewHolder(view);
        return roleViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RoleAdapter.RoleViewHolder holder, int position) {

        Role role = roles.get(position);
        holder.name.setText(role.getName());
        holder.min.setText(String.valueOf(role.getMin()));
        holder.max.setText(String.valueOf(role.getMax()));
        //holder.name.setText(role.getName());
    }

    @Override
    public int getItemCount() {
        return roles.size();
    }

}
