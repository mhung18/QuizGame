package com.example.quizzone.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chaos.view.PinView;
import com.example.quizzone.Model.FillInWordModel;
import com.example.quizzone.R;

import java.util.List;

public class FillInWordAdapter extends RecyclerView.Adapter<FillInWordAdapter.FillInWordViewHolder> {
    private List<FillInWordModel> fiwList;

    public FillInWordAdapter(List<FillInWordModel> fiwList) {
        this.fiwList = fiwList;
    }

    @NonNull
    @Override
    public FillInWordAdapter.FillInWordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fill_in_word_item_layout,parent,false);
        return new FillInWordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FillInWordAdapter.FillInWordViewHolder holder, int position) {
        holder.setData(position);
    }

    @Override
    public int getItemCount() {
        return fiwList.size();
    }

    public class FillInWordViewHolder extends RecyclerView.ViewHolder {
        private TextView hint;
        private PinView yourAnswer;

        public FillInWordViewHolder(@NonNull View itemView) {
            super(itemView);
            hint = itemView.findViewById(R.id.tvHint);
            yourAnswer = itemView.findViewById(R.id.yourAnswer);
        }
        private void setData(final int pos){
            hint.setText(fiwList.get(pos).getHint());
        }
    }
}
