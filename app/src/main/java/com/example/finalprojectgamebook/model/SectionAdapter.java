package com.example.finalprojectgamebook.model;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalprojectgamebook.R;

import java.util.List;

public class SectionAdapter extends RecyclerView.Adapter<SectionAdapter.SectionViewHolder> {

    private List<Section> sections;

    public SectionAdapter(List<Section> sections) {
        this.sections = sections;
    }

    public interface SectionListener {
        void onMissionClicked(int position, View view);
        void onMissionLongClicked(int position, View view);
        void onFavoriteClicked(int position,View view);
    }

    private SectionListener listener;

    public void setListener(SectionListener listener) {
        this.listener = listener;
    }

    public class SectionViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        ImageButton favoriteBtn;

        public SectionViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.section_name);
            favoriteBtn = itemView.findViewById(R.id.favorite_star_btn);

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

            favoriteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onFavoriteClicked(getAdapterPosition(),v);
                }
            });
        }
    }

    @NonNull
    @Override
    public SectionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.section_layout,parent,false);
        SectionViewHolder sectionViewHolder = new SectionViewHolder(view);
        return sectionViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SectionViewHolder holder, int position) {

        Section section = sections.get(position);
        holder.name.setText(section.getName());
        //if(FireBaseModel.getInstance().getSelfUser().isFavorite(section.getName()))
          //  holder.favoriteBtn.setImageResource(R.drawable.favorite_star_icon);
        //else
        List<String> usersIds = section.getUsersId();
        String selfId = FireBaseModel.getInstance().getUser().getUid();
        if(usersIds.contains(selfId)){
            holder.favoriteBtn.setImageResource(R.drawable.favorite_star_icon);
        }else{
            holder.favoriteBtn.setImageResource(R.drawable.favorite_star_icon_gray);
        }


    }

    @Override
    public int getItemCount() {
        return sections.size();
    }

}

