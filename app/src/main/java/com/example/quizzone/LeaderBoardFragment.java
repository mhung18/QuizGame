package com.example.quizzone;

import static com.example.quizzone.DbQuery.g_userCount;
import static com.example.quizzone.DbQuery.g_userList;
import static com.example.quizzone.DbQuery.myPerformance;

import android.app.Dialog;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quizzone.Adapter.RankAdapter;


public class LeaderBoardFragment extends Fragment {
    private TextView totalUsers, myImgText, myScore, myRank;
    private RecyclerView rcvUser;
    private RankAdapter adapter;
    private Dialog progressDialog;
    private TextView dialog_txt;

    public LeaderBoardFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View view =  inflater.inflate(R.layout.fragment_leader_board, container, false);

        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        ((MainActivity)getActivity()).getSupportActionBar().setTitle("Leader Board");
        init(view);

        progressDialog = new Dialog(getContext());
        progressDialog.setContentView(R.layout.dialog_layout);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        dialog_txt = progressDialog.findViewById(R.id.dialog_txt);
        dialog_txt.setText("Loading ...");
        progressDialog.show();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rcvUser.setLayoutManager(layoutManager);

        adapter = new RankAdapter(DbQuery.g_userList);
        rcvUser.setAdapter(adapter);
        DbQuery.getTopUsers(new MyCompleteListener() {
            @Override
            public void OnSuccess() {
                adapter.notifyDataSetChanged();
                if(myPerformance.getScore() != 0){
                    if(!DbQuery.isMeOnTopList){
                        calculateRank();
                    }
                    myScore.setText("Score : " + myPerformance.getScore());
                    myRank.setText("Rank - " + myPerformance.getRank());
                }
                progressDialog.dismiss();
            }

            @Override
            public void OnFailure() {
                Toast.makeText(getContext(),"Something went wrong ! Please try again", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });

        totalUsers.setText("Total Users : " + DbQuery.g_userCount);
        myImgText.setText(myPerformance.getName().toUpperCase().substring(0,1));

        return view;
    }

    private void init(View view){
        totalUsers = view.findViewById(R.id.total_users);
        myImgText = view.findViewById(R.id.img_text);
        myScore = view.findViewById(R.id.total_score);
        myRank = view.findViewById(R.id.rank);

        rcvUser = view.findViewById(R.id.rcvUser);

    }

    private void calculateRank(){
        int lowTopScore = g_userList.get(g_userList.size() - 1).getScore();

        int ramaining_slots = g_userCount - 20;

        int mySlot = (myPerformance.getScore() * ramaining_slots) / lowTopScore;

        int rank;

        if(lowTopScore != myPerformance.getScore()){
            rank = g_userCount - mySlot;
        } else {
            rank = 21;
        }

        myPerformance.setRank(rank);

    }
}