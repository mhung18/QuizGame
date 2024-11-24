package com.example.quizzone.Adapter;

import static com.example.quizzone.DbQuery.ANSWERED;
import static com.example.quizzone.DbQuery.NOT_VISITED;
import static com.example.quizzone.DbQuery.REVIEW;
import static com.example.quizzone.DbQuery.UNANSWERED;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.quizzone.DbQuery;
import com.example.quizzone.FlashCardActivity;
import com.example.quizzone.QuestionsActivity;
import com.example.quizzone.R;

public class FlashCardGridAdapter extends BaseAdapter {
    private int numOfCard;
    private Context context;

    public FlashCardGridAdapter(Context context,int numOfCard) {
        this.context = context;
        this.numOfCard = numOfCard;
    }
    @Override
    public int getCount() {
        return numOfCard;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View mView;
        if(view == null){
            mView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.question_grid_item, viewGroup, false);
        } else {
            mView = view;
        }

        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(context instanceof FlashCardActivity){
                    ((FlashCardActivity)context).goToQuestion(i);
                    notifyDataSetChanged();

                }
            }
        });

        TextView quesNum = mView.findViewById(R.id.ques_num);
        quesNum.setText(String.valueOf(i + 1));

        switch (DbQuery.g_flashCardList.get(i).getStatus()){
            case NOT_VISITED:
                quesNum.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(mView.getContext(), R.color.grey)));
                break;
            case ANSWERED:
                quesNum.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(mView.getContext(), R.color.green)));
                break;
            case UNANSWERED:
                quesNum.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(mView.getContext(), R.color.red)));
                break;
            case REVIEW:
                quesNum.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(mView.getContext(), R.color.pink)));
                break;

            default:
                break;
        }

        return mView;
    }
}
