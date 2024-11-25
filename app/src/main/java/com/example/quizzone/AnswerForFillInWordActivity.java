package com.example.quizzone;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizzone.Adapter.AnswersAdapter;
import com.example.quizzone.Adapter.AnswersForFIWAdapter;

public class AnswerForFillInWordActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private RecyclerView rcvAnswers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_answer_for_fill_in_word);
        toolbar = findViewById(R.id.aa_toolbar);
        rcvAnswers = findViewById(R.id.aa_rcvAnswers);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Answers");

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        rcvAnswers.setLayoutManager(layoutManager);
        AnswersForFIWAdapter adapter = new AnswersForFIWAdapter(DbQuery.g_fillInWordList);
        rcvAnswers.setAdapter(adapter);

    }
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            AnswerForFillInWordActivity.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}