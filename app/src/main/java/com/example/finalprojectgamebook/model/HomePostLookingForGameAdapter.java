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

public class HomePostLookingForGameAdapter extends RecyclerView.Adapter<HomePostLookingForGameAdapter.HomePostLookingForGameViewHolder> {

    private List<HomePostLookingForGame> homePostLookingForGames;

    public HomePostLookingForGameAdapter(List<HomePostLookingForGame> homePostLookingForGames) {
        this.homePostLookingForGames = homePostLookingForGames;
    }

    public interface HomePostLookingForGameAdapterListener {
        void onHomePostLookingForGameAdapterClicked(int position, View view);
        void onHomePostLookingForGameAdapterLongClicked(int position, View view);
    }

    private HomePostLookingForGameAdapter.HomePostLookingForGameAdapterListener listener;

    public void setListener(HomePostLookingForGameAdapter.HomePostLookingForGameAdapterListener listener) {
        this.listener = listener;
    }

    public class HomePostLookingForGameViewHolder extends RecyclerView.ViewHolder {

        TextView description;
        TextView roles;
        TextView name;

        public HomePostLookingForGameViewHolder(View itemView) {
            super(itemView);

            description = itemView.findViewById(R.id.home_post_description);
            roles = itemView.findViewById(R.id.home_post_roles);
            name = itemView.findViewById(R.id.home_post_userName);

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
        StringBuilder roles = new StringBuilder();
        for (Role role:homePostLookingForGame.getRoles()) {
            roles.append(role.getName()).append(" ").append(role.getMin()).append("/").append(role.getMax()).append("\n");
        }
        holder.roles.setText(roles.toString().substring(0,roles.length()-1));
        holder.name.setText(homePostLookingForGame.getUserName());

    }

    @Override
    public int getItemCount() {
        return homePostLookingForGames.size();
    }

}
