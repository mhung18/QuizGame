package com.example.quizzone;

import static com.example.quizzone.DbQuery.ANSWERED;
import static com.example.quizzone.DbQuery.NOT_VISITED;
import static com.example.quizzone.DbQuery.REVIEW;
import static com.example.quizzone.DbQuery.UNANSWERED;
import static com.example.quizzone.DbQuery.g_catList;
import static com.example.quizzone.DbQuery.g_flashCardList;
import static com.example.quizzone.DbQuery.g_quesList;
import static com.example.quizzone.DbQuery.g_selected_cat_index;



import android.content.Intent;
import android.os.Bundle;

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

import com.example.quizzone.Adapter.FlashCardAdapter;
import com.example.quizzone.Adapter.FlashCardGridAdapter;



public class FlashCardActivity extends AppCompatActivity {
    private TextView tvFlashCardID,tv_CatName;
    private AppCompatButton btnFinish, btnFlipCard, btnMarkCard;
    private ImageView btnFlashCardListGrid, markImg;
    private RecyclerView rcvFlashCard;
    private ImageButton btnPrevCard, btnNextCard, drawerClose;
    private int flashCardID;
    FlashCardAdapter adapter;
    private DrawerLayout drawerLayout;
    private GridView cardListGV;
    private FlashCardGridAdapter flashCardGridAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.flash_card_list_layout);

        init();

        adapter = new FlashCardAdapter(g_flashCardList);
        rcvFlashCard.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rcvFlashCard.setLayoutManager(linearLayoutManager);

        flashCardGridAdapter = new FlashCardGridAdapter(this,g_flashCardList.size());
        cardListGV.setAdapter(flashCardGridAdapter);

        setSnapHelper();

        setClickListeners();

    }

    private void init(){
        tv_CatName = findViewById(R.id.tvCatName);
        tvFlashCardID = findViewById(R.id.tvFlashCardID);
        btnFinish = findViewById(R.id.btnFinish);
        btnFlashCardListGrid = findViewById(R.id.btnFlashCardListGrid);
        rcvFlashCard = findViewById(R.id.rcvFlashCard);
        btnPrevCard = findViewById(R.id.btnPrevCard);
        btnFlipCard = findViewById(R.id.btnFlipCard);
        btnMarkCard = findViewById(R.id.btnMarkCard);
        btnNextCard = findViewById(R.id.btnNextCard);
        drawerLayout = findViewById(R.id.drawer_layout);
        drawerClose = findViewById(R.id.drawer_close);
        cardListGV = findViewById(R.id.ques_list_gv);
        markImg = findViewById(R.id.mark_img);

        flashCardID = 0;
        tv_CatName.setText(g_catList.get(g_selected_cat_index).getName());
        tvFlashCardID.setText("1/" + String.valueOf(g_flashCardList.size()));

        g_flashCardList.get(0).setStatus(UNANSWERED);

    }

    private void setClickListeners() {
        btnPrevCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flashCardID > 0) {
                    rcvFlashCard.smoothScrollToPosition(flashCardID - 1);
                }
            }
        });
        btnNextCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flashCardID < g_flashCardList.size() - 1) {
                    rcvFlashCard.smoothScrollToPosition(flashCardID + 1);
                }
            }
        });

        btnFlipCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btnFlashCardListGrid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!drawerLayout.isDrawerOpen(GravityCompat.END)) {
                    flashCardGridAdapter.notifyDataSetChanged();
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

        btnMarkCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (markImg.getVisibility() != View.VISIBLE) {
                    markImg.setVisibility(View.VISIBLE);
                    g_flashCardList.get(flashCardID).setStatus(REVIEW);
                } else {
                    markImg.setVisibility(View.GONE);
                    g_flashCardList.get(flashCardID).setStatus(UNANSWERED);
                }
            }
        });

        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishStudying();
            }
        });
    }

    public void goToQuestion(int position){
        rcvFlashCard.smoothScrollToPosition(position);
        if(drawerLayout.isDrawerOpen(GravityCompat.END))
            drawerLayout.closeDrawer(GravityCompat.END);
    }

    private void finishStudying() {
        AlertDialog.Builder builder = new AlertDialog.Builder(FlashCardActivity.this);
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

                alertDialog.dismiss();
                FlashCardActivity.this.finish();
            }
        });

        alertDialog.show();
    }

    private void setSnapHelper() {
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(rcvFlashCard);

        rcvFlashCard.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                View view = snapHelper.findSnapView(recyclerView.getLayoutManager());
                flashCardID = recyclerView.getLayoutManager().getPosition(view);

                if (g_flashCardList.get(flashCardID).getStatus() == NOT_VISITED){
                    g_flashCardList.get(flashCardID).setStatus(UNANSWERED);
                }
                if (g_flashCardList.get(flashCardID).getStatus() == REVIEW){
                    markImg.setVisibility(View.VISIBLE);
                } else {
                    markImg.setVisibility(View.GONE);
                }

                tvFlashCardID.setText(String.valueOf(flashCardID + 1) + "/" + String.valueOf(g_flashCardList.size()));
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }
}