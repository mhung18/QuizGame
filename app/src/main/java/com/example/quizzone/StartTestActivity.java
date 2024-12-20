package com.example.quizzone;

import static com.example.quizzone.DbQuery.g_catList;
import static com.example.quizzone.DbQuery.g_testList;
import static com.example.quizzone.DbQuery.loadQuestion;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class StartTestActivity extends AppCompatActivity {
    private TextView catName, testNo, totalQues, time, bestScore;
    private AppCompatButton btnStartTest;
    private ImageView btnBack;
    private Dialog progressDialog;
    private TextView dialog_txt;
    String type = DbQuery.g_catList.get(DbQuery.g_selected_cat_index).getName();
    public static int catIndex = DbQuery.g_testList.size();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_start_test);

        init();

        progressDialog = new Dialog(StartTestActivity.this);
        progressDialog.setContentView(R.layout.dialog_layout);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        dialog_txt = progressDialog.findViewById(R.id.dialog_txt);
        dialog_txt.setText("Loading ...");
        progressDialog.show();

        loadQuestion(new MyCompleteListener() {
            @Override
            public void OnSuccess() {
                setData();
                progressDialog.dismiss();
            }

            @Override
            public void OnFailure() {
                progressDialog.dismiss();
                Toast.makeText(StartTestActivity.this,"Something went wrong ! Please try again",Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void init(){
        catName = findViewById(R.id.st_cat_name);
        testNo = findViewById(R.id.st_test_no);
        totalQues = findViewById(R.id.st_total_ques);
        time = findViewById(R.id.st_time);
        bestScore = findViewById(R.id.st_best_score);
        btnStartTest = findViewById(R.id.btnStartTest);
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StartTestActivity.this.finish();
            }
        });

        btnStartTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(type.equals("Multiple Choice")){
                    Intent intent = new Intent(StartTestActivity.this, QuestionsActivity.class);
                    startActivity(intent);
                } else if(type.equals("Flash Card")){
                    Intent intent = new Intent(StartTestActivity.this, FlashCardActivity.class);
                    startActivity(intent);

                } else if(type.equals("Fill in word")){
                    Intent intent = new Intent(StartTestActivity.this, FillInWordActivity.class);
                    startActivity(intent);
                }
                finish();
            }
        });
    }

    private void setData(){
        catName.setText(g_catList.get(DbQuery.g_selected_cat_index).getName());
        testNo.setText("Test No. " + String.valueOf(DbQuery.g_selected_test_index + 1));
        if(type.equals("Multiple Choice")){
            totalQues.setText(String.valueOf(DbQuery.g_quesList.size()));
            bestScore.setText(String.valueOf(DbQuery.g_testList.get(DbQuery.g_selected_test_index).getTopScore()));
        }
        if(type.equals("Flash Card")){
            totalQues.setText(String.valueOf(DbQuery.g_flashCardList.size()));
            bestScore.setText(String.valueOf(0));
        }
        if(type.equals("Fill in word")){
            totalQues.setText(String.valueOf(DbQuery.g_fillInWordList.size()));
            bestScore.setText(String.valueOf(DbQuery.g_testList.get(DbQuery.g_selected_test_index).getTopScore()));
        }
        time.setText(String.valueOf(DbQuery.g_testList.get(DbQuery.g_selected_test_index).getTime()));
    }
}