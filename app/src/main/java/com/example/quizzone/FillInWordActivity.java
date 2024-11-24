package com.example.quizzone;

import static com.example.quizzone.DbQuery.ANSWERED;
import static com.example.quizzone.DbQuery.NOT_VISITED;
import static com.example.quizzone.DbQuery.REVIEW;
import static com.example.quizzone.DbQuery.UNANSWERED;
import static com.example.quizzone.DbQuery.g_catList;
import static com.example.quizzone.DbQuery.g_fillInWordList;
import static com.example.quizzone.DbQuery.g_selected_cat_index;
import static com.example.quizzone.DbQuery.g_selected_test_index;
import static com.example.quizzone.DbQuery.g_testList;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.view.GravityCompat;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.example.quizzone.Adapter.FillInWordAdapter;
import com.example.quizzone.Adapter.FillInWordGridAdapter;


import java.util.concurrent.TimeUnit;

public class FillInWordActivity extends AppCompatActivity {
    private TextView tv_QuesID, tv_Time,tv_CatName;
    private AppCompatButton btnSubmit, btnClearSelection, btnMarkQues;
    private ImageView btnBookmark, btnQuesListGrid, markImg;
    private RecyclerView rcvQuestion;
    private ImageButton btnPrevQues, btnNextQues, drawerClose;
    private int quesID;
    FillInWordAdapter adapter;
    private DrawerLayout drawerLayout;
    private GridView quesListGV;
    private FillInWordGridAdapter fillInWordGridAdapter;
    private CountDownTimer timer;
    private long timeLeft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.fill_in_word_list_layout);

        init();

        adapter = new FillInWordAdapter(g_fillInWordList);
        rcvQuestion.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rcvQuestion.setLayoutManager(linearLayoutManager);

        fillInWordGridAdapter = new FillInWordGridAdapter(this,g_fillInWordList.size());
        quesListGV.setAdapter(fillInWordGridAdapter);

        setSnapHelper();

        setClickListeners();

        startTimer();
    }
    private void startTimer() {
        long totalTime = g_testList.get(g_selected_test_index).getTime() * 60 * 1000;
        timer = new CountDownTimer(totalTime + 1000, 1000) {
            @Override
            public void onTick(long remainingTime) {

                timeLeft = remainingTime;

                String time = String.format("%02d : %02d min",
                        TimeUnit.MILLISECONDS.toMinutes(remainingTime),
                        TimeUnit.MILLISECONDS.toSeconds(remainingTime) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(remainingTime))
                );
                tv_Time.setText(time);
            }

            @Override
            public void onFinish() {
                Intent intent = new Intent(FillInWordActivity.this, ScoreActivity.class);
                startActivity(intent);
                FillInWordActivity.this.finish();
            }
        };
        timer.start();
    }

    private void setClickListeners() {
        btnPrevQues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (quesID > 0) {
                    rcvQuestion.smoothScrollToPosition(quesID - 1);
                }
            }
        });
        btnNextQues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (quesID < g_fillInWordList.size() - 1) {
                    rcvQuestion.smoothScrollToPosition(quesID + 1);
                }
            }
        });

        btnClearSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                g_fillInWordList.get(quesID).setYourAnswer("");
//                g_fillInWordList.get(quesID).setStatus(UNANSWERED);
//                markImg.setVisibility(View.GONE);
//                adapter.notifyDataSetChanged();
            }
        });

        btnQuesListGrid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!drawerLayout.isDrawerOpen(GravityCompat.END)) {
                    fillInWordGridAdapter.notifyDataSetChanged();
                    drawerLayout.openDrawer(GravityCompat.END);
                }
            }
        });

        drawerClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
                    drawerLayout.closeDrawer(GravityCompat.END);
                }
            }
        });

        btnMarkQues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (markImg.getVisibility() != View.VISIBLE) {
                    markImg.setVisibility(View.VISIBLE);
                    g_fillInWordList.get(quesID).setStatus(REVIEW);
                } else {
                    markImg.setVisibility(View.GONE);
                    g_fillInWordList.get(quesID).setStatus(UNANSWERED);
                }
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitTest();
            }
        });
    }

    private void submitTest() {
        AlertDialog.Builder builder = new AlertDialog.Builder(FillInWordActivity.this);
        builder.setCancelable(true);

        View view = getLayoutInflater().inflate(R.layout.alert_dialog_layout,null);
        Button btnCancel = view.findViewById(R.id.btnCancel);
        Button btnConfirm = view.findViewById(R.id.btnConfirm);

        builder.setView(view);
        AlertDialog alertDialog = builder.create();

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timer.cancel();
                alertDialog.dismiss();
                Intent intent = new Intent(FillInWordActivity.this, ScoreActivity.class);
                long totalTime = g_testList.get(g_selected_test_index).getTime() * 60 * 1000;
                intent.putExtra("TIME_TAKEN",totalTime - timeLeft);
                startActivity(intent);
                FillInWordActivity.this.finish();
            }
        });

        alertDialog.show();

    }

    public void goToQuestion(int position){
        rcvQuestion.smoothScrollToPosition(position);
        if(drawerLayout.isDrawerOpen(GravityCompat.END))
            drawerLayout.closeDrawer(GravityCompat.END);
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

                if (g_fillInWordList.get(quesID).getStatus() == NOT_VISITED){
                    g_fillInWordList.get(quesID).setStatus(UNANSWERED);
                }
                if (g_fillInWordList.get(quesID).getStatus() == REVIEW){
                    markImg.setVisibility(View.VISIBLE);
                } else {
                    markImg.setVisibility(View.GONE);
                }

                tv_QuesID.setText(String.valueOf(quesID + 1) + "/" + String.valueOf(g_fillInWordList.size()));
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
        btnQuesListGrid = findViewById(R.id.btnQuesListGrid);
        rcvQuestion = findViewById(R.id.rcvQuestion);
        btnPrevQues = findViewById(R.id.btnPrevQues);
        btnClearSelection = findViewById(R.id.btnClearSelection);
        btnMarkQues = findViewById(R.id.btnMarkQues);
        btnNextQues = findViewById(R.id.btnNextQues);
        drawerLayout = findViewById(R.id.drawer_layout);
        drawerClose = findViewById(R.id.drawer_close);
        quesListGV = findViewById(R.id.ques_list_gv);
        markImg = findViewById(R.id.mark_img);




        quesID = 0;
        tv_CatName.setText(g_catList.get(g_selected_cat_index).getName());
        tv_QuesID.setText("1/" + String.valueOf(g_fillInWordList.size()));

        g_fillInWordList.get(0).setStatus(UNANSWERED);


    }
}