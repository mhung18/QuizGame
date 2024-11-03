package com.example.quizzone;

import static com.example.quizzone.DbQuery.g_catList;
import static com.example.quizzone.DbQuery.g_quesList;
import static com.example.quizzone.DbQuery.g_selected_cat_index;
import static com.example.quizzone.DbQuery.g_selected_test_index;
import static com.example.quizzone.DbQuery.g_testList;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import java.util.concurrent.TimeUnit;

public class QuestionsActivity extends AppCompatActivity {
    private TextView tv_QuesID, tv_Time,tv_CatName;
    private AppCompatButton btnSubmit, btnClearSelection, btnMarkQues;
    private ImageView btnBookmark, btnQuesListGrid;
    private RecyclerView rcvQuestion;
    private ImageButton btnPrevQues, btnNextQues;
    private int quesID;
    QuestionsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_questions);

        init();

        adapter = new QuestionsAdapter(g_quesList);
        rcvQuestion.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rcvQuestion.setLayoutManager(linearLayoutManager);

        setSnapHelper();
        
        setClickListeners();

        startTimer();

    }

    private void startTimer() {
        long totalTime = g_testList.get(g_selected_test_index).getTime() * 60 * 1000;
        CountDownTimer timer = new CountDownTimer(totalTime + 1000, 1000) {
            @Override
            public void onTick(long remainingTime) {
                String time = String.format("%02d : %02d min",
                        TimeUnit.MILLISECONDS.toMinutes(remainingTime),
                        TimeUnit.MILLISECONDS.toSeconds(remainingTime) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(remainingTime))
                        );
                tv_Time.setText(time);
            }

            @Override
            public void onFinish() {

            }
        };
        timer.start();
    }

    private void setClickListeners() {
        btnPrevQues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(quesID > 0){
                    rcvQuestion.smoothScrollToPosition(quesID - 1);
                }
            }
        });
        btnNextQues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(quesID < g_quesList.size() - 1){
                    rcvQuestion.smoothScrollToPosition(quesID + 1);
                }
            }
        });

        btnClearSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                g_quesList.get(quesID).setSelectedAns(-1);
                adapter.notifyDataSetChanged();
            }
        });

    }

    private void setSnapHelper() {
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(rcvQuestion);

        rcvQuestion.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                View view = snapHelper.findSnapView(recyclerView.getLayoutManager());
                quesID = recyclerView.getLayoutManager().getPosition(view);

                tv_QuesID.setText(String.valueOf(quesID + 1) + "/" + String.valueOf(g_quesList.size()));
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    private void init(){
        tv_QuesID = findViewById(R.id.tvQuesID);
        tv_Time = findViewById(R.id.tvTime);
        btnSubmit = findViewById(R.id.btnSubmit);
        tv_CatName = findViewById(R.id.tvCatName);
        btnBookmark = findViewById(R.id.btnBookmark);
        btnQuesListGrid = findViewById(R.id.btnQuesListGrid);
        rcvQuestion = findViewById(R.id.rcvQuestion);
        btnPrevQues = findViewById(R.id.btnPrevQues);
        btnClearSelection = findViewById(R.id.btnClearSelection);
        btnMarkQues = findViewById(R.id.btnMarkQues);
        btnNextQues = findViewById(R.id.btnNextQues);

        quesID = 0;
        tv_CatName.setText(g_catList.get(g_selected_cat_index).getName());
        tv_QuesID.setText("1/" + String.valueOf(g_quesList.size()));


    }
}