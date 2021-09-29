package com.example.finalprojectgamebook.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalprojectgamebook.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class HomePostLookingForGameAdapter extends RecyclerView.Adapter<HomePostLookingForGameAdapter.HomePostLookingForGameViewHolder> {

    private List<HomePostLookingForGame> homePostLookingForGames;
    private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
    private Context context;
    private String selfId;

    public HomePostLookingForGameAdapter(List<HomePostLookingForGame> homePostLookingForGames, Context context, String selfId) {
        this.homePostLookingForGames = homePostLookingForGames;
        this.context = context;
        this.selfId = selfId;
    }

    public interface HomePostLookingForGameAdapterListener {
        void onHomePostLookingForGameAdapterClicked(int position, View view);
        void onHomePostLookingForGameAdapterLongClicked(int position, View view);
        void onHomePostLookingForGameAdapterRecyclerClicked(int position, View view);

    }

    private HomePostLookingForGameAdapter.HomePostLookingForGameAdapterListener listener;

    public void setListener(HomePostLookingForGameAdapter.HomePostLookingForGameAdapterListener listener) {
        this.listener = listener;
    }

    public class HomePostLookingForGameViewHolder extends RecyclerView.ViewHolder {

        TextView description;
        TextView roles;
        TextView name;
        RecyclerView recyclerView;

        public HomePostLookingForGameViewHolder(View itemView) {
            super(itemView);

            description = itemView.findViewById(R.id.home_post_description);
            //roles = itemView.findViewById(R.id.home_post_roles);
            name = itemView.findViewById(R.id.home_post_userName);
            recyclerView = itemView.findViewById(R.id.recyclerLookingForGame);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onHomePostLookingForGameAdapterClicked(getAdapterPosition(),view);
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    listener.onHomePostLookingForGameAdapterLongClicked(getAdapterPosition(),view);
                    return true;
                }
            });

            recyclerView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onHomePostLookingForGameAdapterRecyclerClicked(getAdapterPosition(),view);
                }
            });


        }
    }

    @NonNull
    @Override
    public HomePostLookingForGameAdapter.HomePostLookingForGameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_looking_for_game_card,parent,false);
        HomePostLookingForGameAdapter.HomePostLookingForGameViewHolder homePostLookingForGameViewHolder = new HomePostLookingForGameAdapter.HomePostLookingForGameViewHolder(view);
        return homePostLookingForGameViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull HomePostLookingForGameAdapter.HomePostLookingForGameViewHolder holder, int position) {

        HomePostLookingForGame homePostLookingForGame = homePostLookingForGames.get(position);
        holder.description.setText(homePostLookingForGame.getDescription());
        holder.name.setText(homePostLookingForGame.getUserName());


        RoleAdapter adapter = new RoleAdapter(homePostLookingForGame.getRoles());
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(context));
        holder.recyclerView.setHasFixedSize(true);
        holder.recyclerView.setAdapter(adapter);
        holder.recyclerView.setRecycledViewPool(viewPool);


        adapter.setListener(new RoleAdapter.RoleListener() {
            @Override
            public void onRoleClicked(int position, View view) {

            }

            @Override
            public void onRoleLongClicked(int position, View view) {

            }

            @Override
            public void onRemoveClicked(int position, View view) {

            }
        });


        /**
        StringBuilder roles = new StringBuilder();
        for (Role role:homePostLookingForGame.getRoles()) {
            roles.append(role.getName()).append(" ").append(role.getMin()).append("/").append(role.getMax()).append("\n");
        }
        holder.roles.setText(roles.toString().substring(0,roles.length()-1));
         **/

    }

    @Override
    public int getItemCount() {
        return homePostLookingForGames.size();
    }

}
