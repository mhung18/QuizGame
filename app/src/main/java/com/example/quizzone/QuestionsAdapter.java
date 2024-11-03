package com.example.quizzone;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class QuestionsAdapter extends RecyclerView.Adapter<QuestionsAdapter.QuestionViewHolder> {

    private List<QuestionModel> quesList;
    public QuestionsAdapter(List<QuestionModel> quesList) {
        this.quesList = quesList;
    }

    @NonNull
    @Override
    public QuestionsAdapter.QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.question_item_layout,parent,false);
        return new QuestionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionsAdapter.QuestionViewHolder holder, int position) {
        holder.setData(position);
    }

    @Override
    public int getItemCount() {
        return quesList.size();
    }

    public class QuestionViewHolder extends RecyclerView.ViewHolder {
        private TextView ques;
        private AppCompatButton optionA, optionB, optionC, optionD, prevSelectedOption;
        public QuestionViewHolder(@NonNull View itemView) {
            super(itemView);
            ques = itemView.findViewById(R.id.tvQuestion);
            optionA = itemView.findViewById(R.id.optionA);
            optionB = itemView.findViewById(R.id.optionB);
            optionC = itemView.findViewById(R.id.optionC);
            optionD = itemView.findViewById(R.id.optionD);

            prevSelectedOption = null;

        }

        private void setData(final int pos){
            ques.setText((quesList.get(pos).getQuestion()));
            optionA.setText(quesList.get(pos).getOptionA());
            optionB.setText(quesList.get(pos).getOptionB());
            optionC.setText(quesList.get(pos).getOptionC());
            optionD.setText(quesList.get(pos).getOptionD());

            setOption(optionA,1,pos);
            setOption(optionB,2,pos);
            setOption(optionC,3,pos);
            setOption(optionD,4,pos);



            optionA.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectedOption(optionA,1,pos);
                }
            });

            optionB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectedOption(optionB,2,pos);
                }
            });

            optionC.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectedOption(optionC,3,pos);
                }
            });

            optionD.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectedOption(optionD,4,pos);
                }
            });
        }
        private void selectedOption(AppCompatButton btn, int option_no, int quesID){
            if(prevSelectedOption == null){
                btn.setBackgroundResource(R.drawable.selected_btn);
                DbQuery.g_quesList.get(quesID).setSelectedAns(option_no);

                prevSelectedOption = btn;

            } else {
                if(prevSelectedOption.getId() == btn.getId()){
                    btn.setBackgroundResource(R.drawable.unselected_btn);
                    DbQuery.g_quesList.get(quesID).setSelectedAns(-1);

                    prevSelectedOption = null;
                } else {
                    prevSelectedOption.setBackgroundResource(R.drawable.unselected_btn);
                    btn.setBackgroundResource(R.drawable.selected_btn);

                    DbQuery.g_quesList.get(quesID).setSelectedAns(option_no);

                    prevSelectedOption = btn;
                }
            }
        }

        private void setOption(AppCompatButton btn, int option_no, int quesID){
            if(DbQuery.g_quesList.get(quesID).getSelectedAns() == option_no){
                btn.setBackgroundResource(R.drawable.selected_btn);
            }
            else{
                btn.setBackgroundResource(R.drawable.unselected_btn);
            }

        }
    }
}
