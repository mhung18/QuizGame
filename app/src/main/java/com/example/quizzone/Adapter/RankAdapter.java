package com.example.quizzone.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizzone.Model.RankModel;
import com.example.quizzone.R;

import java.util.List;

public class RankAdapter extends RecyclerView.Adapter<RankAdapter.ViewHolder> {
    private List<RankModel> userList;

    public RankAdapter(List<RankModel> userList) {
        this.userList = userList;
    }

    @NonNull
    @Override
    public RankAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rank_item_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RankAdapter.ViewHolder holder, int position) {
        String name = userList.get(position).getName();
        int score = userList.get(position).getScore();
        int rank = userList.get(position).getRank();

        holder.setData(name, score, rank);
    }

    @Override
    public int getItemCount() {
        if (userList.size() > 10){
            return 10;
        }
        else {
            return userList.size();
        }
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName, tvRank, tvScore, tvImg;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.name);
            tvRank = itemView.findViewById(R.id.rank);
            tvScore = itemView.findViewById(R.id.score);
            tvImg = itemView.findViewById(R.id.img_text);
        }

        private void setData(String name, int score, int rank){
            tvName.setText(name);
            tvScore.setText("Score : " + score);
            tvRank.setText("Rank - " + rank);
            tvImg.setText(name.toUpperCase().substring(0,1));
        }
    }
}
