package com.example.finalprojectgamebook.model;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalprojectgamebook.R;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class RoleAdapter extends RecyclerView.Adapter<RoleAdapter.RoleViewHolder> {

    private List<Role> roles;
    private int flag;
    private Context context;
    private String originalPoster;
    int adapterPosition;


    public RoleAdapter(List<Role> roles,int flag,Context context,String originalPoster,int adapterPosition) {

        this.roles = roles;
        this.flag = flag;
        this.context = context;
        this.originalPoster = originalPoster;
        this.adapterPosition = adapterPosition;
    }

    public interface RoleListener {
        void onRoleClicked(int position, View view);
        void onRoleLongClicked(int position, View view);
        void onRemoveClicked(int position,View view);
        void onCreateCard(int position,View view);
    }

    private RoleAdapter.RoleListener listener;

    public void setListener(RoleAdapter.RoleListener listener) {
        this.listener = listener;
    }

    public class RoleViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView min;
        TextView max;
        ImageButton remove;
        RecyclerView recyclerView;



        public RoleViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.roll_name);
            min = itemView.findViewById(R.id.apply_number);
            max = itemView.findViewById(R.id.max_number);
            remove = itemView.findViewById(R.id.remove_role_btn);
            recyclerView = itemView.findViewById(R.id.recyclerAdd);


            remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onRemoveClicked(getAdapterPosition(), v);
                }
            });

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

            itemView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
                @Override
                public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
                    listener.onCreateCard(getAdapterPosition(),view);
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
        //holder.remove.setBackgroundResource(R.drawable.ic_baseline_add_circle_24);
        /**
        for (String s: role.getUsers()) {
            if(FireBaseModel.getInstance().getUser().getUid().equals(s)) {
                holder.remove.setBackgroundResource(R.drawable.ic_baseline_remove_24);
            }
        }
         **/
        FirebaseUser u = FireBaseModel.getInstance().getUser();
        User user = new User(u.getDisplayName(),u.getUid());
        if(role.isUserInList(user) || flag ==0){
            holder.remove.setBackgroundResource(R.drawable.ic_baseline_remove_24);
        }else{
            holder.remove.setBackgroundResource(R.drawable.ic_baseline_add_circle_24);
        }

        if(flag == 1){
            roleListAdapter adapter = new roleListAdapter(role.getUsers(),originalPoster);
            holder.recyclerView.setLayoutManager(new LinearLayoutManager(context));
            holder.recyclerView.setHasFixedSize(true);
            holder.recyclerView.setAdapter(adapter);
            adapter.setListener(new roleListAdapter.roleListListener() {
                int tempPos = position;
                @Override
                public void onRoleListClicked(int position, View view) {

                }

                @Override
                public void onRoleListLongClicked(int position, View view) {

                }

                @Override
                public void onRemoveClicked(int position, View view) {
                    role.getUsers().remove(position);
                    role.addMin(-1);
                    FireBaseSectionChat.getInstance().updatePost(adapterPosition,tempPos,role);


                }
            });

            if(FireBaseModel.getInstance().getUser().getUid().equals(originalPoster)){
                holder.remove.setVisibility(View.INVISIBLE);
            }else{
                holder.remove.setVisibility(View.VISIBLE);
            }

        }

        //holder.name.setText(role.getName());
    }

    @Override
    public int getItemCount() {
        return roles.size();
    }


    public void test(){}

}
