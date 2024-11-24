package com.example.quizzone.Adapter;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizzone.Model.FlashCardModel;
import com.example.quizzone.R;

import java.util.List;

public class FlashCardAdapter extends RecyclerView.Adapter<FlashCardAdapter.FlashCardViewHolder> {
    private List<FlashCardModel> flashCardList;
    public FlashCardAdapter(List<FlashCardModel> flashCardList) {
        this.flashCardList = flashCardList;
    }

    @NonNull
    @Override
    public FlashCardAdapter.FlashCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.flash_card_item_layout,parent,false);
        return new FlashCardAdapter.FlashCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FlashCardAdapter.FlashCardViewHolder holder, int position) {
        holder.setData(position);
    }

    @Override
    public int getItemCount() {
        return flashCardList.size();
    }

    public class FlashCardViewHolder extends RecyclerView.ViewHolder {
        private AnimatorSet frontAnim;
        private AnimatorSet backAnim;
        private boolean isFront = true;
        CardView card;
        TextView question, answer;
        public FlashCardViewHolder(@NonNull View itemView) {
            super(itemView);
            card = itemView.findViewById(R.id.card);
            question = itemView.findViewById(R.id.card_front);
            answer = itemView.findViewById(R.id.card_back);

            float scale = itemView.getContext().getResources().getDisplayMetrics().density;
            question.setCameraDistance(8000 * scale);
            answer.setCameraDistance(8000 * scale);

            frontAnim = (AnimatorSet) AnimatorInflater.loadAnimator(itemView.getContext(), R.animator.flip_in);
            backAnim = (AnimatorSet) AnimatorInflater.loadAnimator(itemView.getContext(), R.animator.flip_out);
        }

        public void setData(int position) {
            question.setText(flashCardList.get(position).getQuestion());
            answer.setText(flashCardList.get(position).getTranslate());

            card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isFront) {
                        frontAnim.setTarget(question);
                        backAnim.setTarget(answer);
                        frontAnim.start();
                        backAnim.start();
                        isFront = false;
                    } else {
                        frontAnim.setTarget(answer);
                        backAnim.setTarget(question);
                        backAnim.start();
                        frontAnim.start();
                        isFront = true;
                    }
                }
            });
        }
    }
}
