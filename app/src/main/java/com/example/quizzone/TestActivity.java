package com.example.quizzone;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizzone.Adapter.TestAdapter;

public class TestActivity extends AppCompatActivity {
    private RecyclerView rcvTest;
    private Toolbar toolbar;
    private TestAdapter adapter;
    private Dialog progressDialog;
    private TextView dialog_txt;
    public static int g_selected_test_index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_test);

        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(DbQuery.g_catList.get(DbQuery.g_selected_cat_index).getName());

        rcvTest = findViewById(R.id.rcvTest);

        Intent intent = getIntent();


        progressDialog = new Dialog(TestActivity.this);
        progressDialog.setContentView(R.layout.dialog_layout);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        dialog_txt = progressDialog.findViewById(R.id.dialog_txt);
        dialog_txt.setText("Loading ...");
        progressDialog.show();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        rcvTest.setLayoutManager(layoutManager);

        String type = DbQuery.g_catList.get(DbQuery.g_selected_cat_index).getName();
        if(type.equals("Multiple Choice")){
            DbQuery.g_selected_cat_index = 0;
        } else if(type.equals("Flash Card")){
            DbQuery.g_selected_cat_index = 1;
        } else if(type.equals("Fill in word")){
            DbQuery.g_selected_cat_index = 2;
        }
        Toast.makeText(TestActivity.this,type,Toast.LENGTH_SHORT).show();


        DbQuery.loadTestData(new MyCompleteListener() {
            @Override
            public void OnSuccess() {
                DbQuery.loadMyScores(new MyCompleteListener() {
                    @Override
                    public void OnSuccess() {
                        adapter = new TestAdapter(DbQuery.g_testList);
                        rcvTest.setAdapter(adapter);
                        progressDialog.dismiss();
                    }

                    @Override
                    public void OnFailure() {
                        progressDialog.dismiss();
                        Toast.makeText(TestActivity.this,"Something went wrong ! Please try again",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void OnFailure() {
                progressDialog.dismiss();
                Toast.makeText(TestActivity.this,"Something went wrong ! Please try again",Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            TestActivity.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}