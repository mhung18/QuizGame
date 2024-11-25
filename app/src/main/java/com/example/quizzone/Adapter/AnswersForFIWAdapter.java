package com.example.quizzone.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizzone.DbQuery;
import com.example.quizzone.Model.FillInWordModel;
import com.example.quizzone.Model.QuestionModel;
import com.example.quizzone.R;

import java.util.List;

public class AnswersForFIWAdapter extends RecyclerView.Adapter<AnswersForFIWAdapter.ViewHolder> {

    private List<FillInWordModel> fillInWordList;;

    public AnswersForFIWAdapter(List<FillInWordModel> fillInWordList) {
        this.fillInWordList = fillInWordList;
    }

    @NonNull
    @Override
    public AnswersForFIWAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.answer_for_fiw_item_layout,parent,false);
        return new AnswersForFIWAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnswersForFIWAdapter.ViewHolder holder, int position) {
        String ques = fillInWordList.get(position).getHint();
        String yourAnswer = DbQuery.g_fillInWordList.get(position).getYourAnswer();
        String answer = DbQuery.g_fillInWordList.get(position).getAnswer();

        holder.setData(position, ques, yourAnswer, answer);
    }

    @Override
    public int getItemCount() {
        return fillInWordList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView quesNo,question,yAnswer,result;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            quesNo = itemView.findViewById(R.id.quesNo);
            question = itemView.findViewById(R.id.question);
            yAnswer = itemView.findViewById(R.id.yourAnswer);
            result = itemView.findViewById(R.id.result);
        }

        private void setData(int pos, String ques, String yourAnswer, String answer){
            quesNo.setText("Question No. " + String.valueOf(pos+1));
            question.setText(ques);
            yAnswer.setText("Your Answer : " + yourAnswer);
            if(yAnswer.equals("")){
                result.setText("Un-Answered");
                result.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.black));
//                setOptionColor(yourAnswer, R.color.black);
            }else if(yAnswer.equals(answer)){
                result.setText("CORRECT");
                result.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.green));
//                setOptionColor(yourAnswer, R.color.green);
            }else{
                result.setText("WRONG");
                result.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.red));
//                setOptionColor(yourAnswer, R.color.red);
            }
        }

//        private void setOptionColor(String yourAnswer, int color) {
//            yAnswer.setTextColor(ContextCompat.getColor(itemView.getContext(), color));
//        }
    }
}
