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

public class AnswersAdapter extends RecyclerView.Adapter<AnswersAdapter.ViewHolder> {

    private List<QuestionModel> quesList;

    public AnswersAdapter(List<QuestionModel> quesList) {
        this.quesList = quesList;
    }

    @NonNull
    @Override
    public AnswersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.answer_item_layout,parent,false);
        return new AnswersAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnswersAdapter.ViewHolder holder, int position) {
        String ques = quesList.get(position).getQuestion();
        String optionA = quesList.get(position).getOptionA();
        String optionB = quesList.get(position).getOptionB();
        String optionC = quesList.get(position).getOptionC();
        String optionD = quesList.get(position).getOptionD();
        int selectedAns = quesList.get(position).getSelectedAns();
        int correctAns = quesList.get(position).getCorrectAns();

        holder.setData(position, ques, optionA, optionB, optionC, optionD, selectedAns, correctAns);
    }

    @Override
    public int getItemCount() {
        return quesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView quesNo,question,optionA,optionB,optionC,optionD,result;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            quesNo = itemView.findViewById(R.id.quesNo);
            question = itemView.findViewById(R.id.question);
            optionA = itemView.findViewById(R.id.optionA);
            optionB = itemView.findViewById(R.id.optionB);
            optionC = itemView.findViewById(R.id.optionC);
            optionD = itemView.findViewById(R.id.optionD);
            result = itemView.findViewById(R.id.result);
        }

        private void setData(int pos, String ques, String a, String b, String c, String d, int selectedAns, int correctAns){
            quesNo.setText("Question No. " + String.valueOf(pos+1));
            question.setText(ques);
            optionA.setText("A. " + a);
            optionB.setText("B. " + b);
            optionC.setText("C. " + c);
            optionD.setText("D. " + d);
            if(selectedAns == -1){
                result.setText("Un-Answered");
                result.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.black));
                setOptionColor(selectedAns, R.color.black);
            }else if(selectedAns == correctAns){
                result.setText("CORRECT");
                result.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.green));
                setOptionColor(selectedAns, R.color.green);
            }else{
                result.setText("WRONG");
                result.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.red));
                setOptionColor(selectedAns, R.color.red);
            }
        }

        private void setOptionColor(int selectedAns, int color) {
            switch (selectedAns){
                case 1:
                    optionA.setTextColor(ContextCompat.getColor(itemView.getContext(), color));
                    break;
                case 2:
                    optionB.setTextColor(ContextCompat.getColor(itemView.getContext(), color));
                    break;
                case 3:
                    optionC.setTextColor(ContextCompat.getColor(itemView.getContext(), color));
                    break;
                case 4:
                    optionD.setTextColor(ContextCompat.getColor(itemView.getContext(), color));
                    break;
                default:
                    break;
            }
        }
    }
}
