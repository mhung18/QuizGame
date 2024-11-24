package com.example.quizzone.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizzone.DbQuery;
import com.example.quizzone.Model.TestModel;
import com.example.quizzone.R;
import com.example.quizzone.StartTestActivity;

import java.util.List;

public class TestAdapter extends RecyclerView.Adapter<TestAdapter.ViewHolder> {
    private List<TestModel> testList;

    public TestAdapter(List<TestModel> testList) {
        this.testList = testList;
    }

    @NonNull
    @Override
    public TestAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.test_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TestAdapter.ViewHolder holder, int position) {
        int progress = testList.get(position).getTopScore();
        holder.setData(position,progress);
    }

    @Override
    public int getItemCount() {
        return testList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView testNo, topScore;
        private ProgressBar progressBar;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            testNo = itemView.findViewById(R.id.testNo);
            topScore = itemView.findViewById(R.id.score);
            progressBar = itemView.findViewById(R.id.testProgressbar);

        }

        private void setData(int pos, int progress){
            String type = DbQuery.getTypeOfQuiz(DbQuery.g_selected_cat_index);
            if(type.equals("mc")){
                testNo.setText("Test No " + String.valueOf(pos+1) + ": " + DbQuery.getTopicOfQuiz(pos));
                topScore.setText(String.valueOf(progress) + "%");
                progressBar.setProgress(progress);

            } else if(type.equals("fc")){
                testNo.setText("Study Flash Card No " + String.valueOf(pos+1) + ": " + DbQuery.getTopicOfQuiz(pos));
                testNo.setPadding(0,50,0,50);
                topScore.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
            } else if (type.equals("fiw")) {
                testNo.setText("Test No " + String.valueOf(pos+1) + ": " + DbQuery.getTopicOfQuiz(pos));
                topScore.setText(String.valueOf(progress) + "%");
                progressBar.setProgress(progress);

            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DbQuery.g_selected_test_index = pos;
                    Intent intent = new Intent(itemView.getContext(), StartTestActivity.class);
                    itemView.getContext().startActivity(intent);
                }
            });
        }
    }
}
