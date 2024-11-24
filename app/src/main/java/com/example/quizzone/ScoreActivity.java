package com.example.quizzone;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.concurrent.TimeUnit;

public class ScoreActivity extends AppCompatActivity {
    private TextView score, time_taken, total_question, correct_question, wrong_question, no_answer_question;
    private Button btnCheckRanking, btnReAttempt, btnViewAnswer;
    private long timeTaken;
    private Dialog progressDialog;
    private TextView dialog_txt;
    private int finalScore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_score);

        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Result");

        progressDialog = new Dialog(ScoreActivity.this);
        progressDialog.setContentView(R.layout.dialog_layout);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        dialog_txt = progressDialog.findViewById(R.id.dialog_txt);
        dialog_txt.setText("Loading ...");
        progressDialog.show();


        init();

        loadData();

        btnViewAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ScoreActivity.this, AnswersActivity.class);
                startActivity(intent);
            }
        });

        btnReAttempt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ReAttempt();
            }
        });

        saveResult();

    }



    public void init(){
        score = findViewById(R.id.score);
        time_taken = findViewById(R.id.time_taken);
        total_question = findViewById(R.id.total_question);

        correct_question = findViewById(R.id.correct_question);
        wrong_question = findViewById(R.id.wrong_question);
        no_answer_question = findViewById(R.id.no_answer_question);

        btnCheckRanking = findViewById(R.id.btnCheckRanking);
        btnReAttempt = findViewById(R.id.btnReAttempt);
        btnViewAnswer = findViewById(R.id.btnViewAnswer);
    }

    private void loadData() {
        int correctQues = 0, wrongQues = 0, noAnswerQues = 0;

        for(int i = 0; i < DbQuery.g_quesList.size(); i++){
            if(DbQuery.g_quesList.get(i).getSelectedAns() == -1){
                noAnswerQues++;
            }else{
                if(DbQuery.g_quesList.get(i).getSelectedAns() == DbQuery.g_quesList.get(i).getCorrectAns()){
                    correctQues++;
                } else {
                    wrongQues++;
                }
            }
        }

        correct_question.setText(String.valueOf(correctQues));
        wrong_question.setText(String.valueOf(wrongQues));
        no_answer_question.setText(String.valueOf(noAnswerQues));

        total_question.setText(String.valueOf(DbQuery.g_quesList.size()));

        finalScore = (correctQues*100)/DbQuery.g_quesList.size();
        score.setText(String.valueOf(finalScore));


        timeTaken = getIntent().getLongExtra("TIME_TAKEN",0);
        String time = String.format("%02d : %02d min",
                TimeUnit.MILLISECONDS.toMinutes(timeTaken),
                TimeUnit.MILLISECONDS.toSeconds(timeTaken) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timeTaken))
        );

        time_taken.setText(time);
    }



    private void ReAttempt() {
        for(int i = 0;i <DbQuery.g_quesList.size();i++){
            DbQuery.g_quesList.get(i).setSelectedAns(-1);
            DbQuery.g_quesList.get(i).setStatus(DbQuery.NOT_VISITED);
        }

        Intent intent = new Intent(ScoreActivity.this,StartTestActivity.class);
        startActivity(intent);
        finish();
    }

    private void saveResult() {


        DbQuery.saveResult(finalScore, new MyCompleteListener() {
            @Override
            public void OnSuccess() {
                progressDialog.dismiss();
            }

            @Override
            public void OnFailure() {
                Toast.makeText(ScoreActivity.this,"Something went wrong ! Please try later again !",Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            ScoreActivity.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}