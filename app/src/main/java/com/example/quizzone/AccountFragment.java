package com.example.quizzone;

import static com.example.quizzone.DbQuery.g_userCount;
import static com.example.quizzone.DbQuery.g_userList;
import static com.example.quizzone.DbQuery.myPerformance;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Locale;


public class AccountFragment extends Fragment {
    private LinearLayout btnLogout, btnProfile, btnLeaderboard, btnChangePass;
    private TextView name, rank, totalScore, profileImageText;
    private BottomNavigationView bottomNavigationView;
    private Dialog progressDialog;
    private TextView dialog_txt;

    public AccountFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        init(view);

        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        ((MainActivity)getActivity()).getSupportActionBar().setTitle("My Account");

        progressDialog = new Dialog(getContext());
        progressDialog.setContentView(R.layout.dialog_layout);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        dialog_txt = progressDialog.findViewById(R.id.dialog_txt);
        dialog_txt.setText("Loading ...");


        String username = DbQuery.myProfile.getName();
        Log.d("username",username);
        profileImageText.setText(username.toUpperCase().substring(0,1));

        name.setText(username);

        totalScore.setText(String.valueOf(DbQuery.myPerformance.getScore()));

        if(DbQuery.g_userList.size() == 0){
            progressDialog.show();
            DbQuery.getTopUsers(new MyCompleteListener() {
                @Override
                public void OnSuccess() {
                    if(myPerformance.getScore() != 0){
                        if(!DbQuery.isMeOnTopList){
                            calculateRank();
                        }
                        totalScore.setText(String.valueOf(myPerformance.getScore()));
                        rank.setText(String.valueOf(myPerformance.getRank()));
                    }
                    progressDialog.dismiss();
                }

                @Override
                public void OnFailure() {
                    Toast.makeText(getContext(),"Something went wrong ! Please try again", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            });
        } else {
            totalScore.setText(String.valueOf(myPerformance.getScore()));
            if(myPerformance.getScore() != 0){
                rank.setText(String.valueOf(myPerformance.getRank()));
            }
        }


        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                SignOut();
            }
        });

        btnChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ChangePasswordActivity.class);
                startActivity(intent);
            }
        });

        btnLeaderboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomNavigationView.setSelectedItemId(R.id.nav_leaderboard);
            }
        });

        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), MyProfileActivity.class);
                startActivity(intent);

            }
        });

        return view;
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

    private void init(View view) {
        btnLogout = view.findViewById(R.id.btnLogout);
        btnProfile = view.findViewById(R.id.btnProfile);
        btnLeaderboard = view.findViewById(R.id.btnLeaderboard);
        btnChangePass = view.findViewById(R.id.btnChangePass);
        name = view.findViewById(R.id.name);
        rank = view.findViewById(R.id.rank);
        totalScore = view.findViewById(R.id.toal_score);
        profileImageText = view.findViewById(R.id.profile_image_text);
        bottomNavigationView = getActivity().findViewById(R.id.bottom_navbar);
    }

    private void SignOut() {
        Intent intent = new Intent(getContext(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}