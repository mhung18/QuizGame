package com.example.quizzone;


import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MyProfileActivity extends AppCompatActivity {
    private EditText name, email, phone;
    private LinearLayout btnEdit;
    private Button btnSave, btnCancel;
    private TextView profileImageText;
    private LinearLayout btnLayout;
    private String nameStr, phoneStr;
    private Dialog progressDialog;
    private TextView dialog_txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_my_profile);
        init();

        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("My Profile");

        progressDialog = new Dialog(MyProfileActivity.this);
        progressDialog.setContentView(R.layout.dialog_layout);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog_txt = progressDialog.findViewById(R.id.dialog_txt);
        dialog_txt.setText("Saving Data ...");


        disableEditing();
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enableEditing();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disableEditing();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validate()){
                    saveProfile();
                }
            }
        });
    }

    private boolean validate(){
        nameStr = name.getText().toString();
        phoneStr = phone.getText().toString();

        if(nameStr.isEmpty()){
            name.setError("Name cannot be empty");
            return false;
        }
        if(!phoneStr.isEmpty()){
            if(!(phoneStr.length() == 10 && TextUtils.isDigitsOnly(phoneStr))){
                phone.setError("Enter a valid phone number");
                return false;
            }
        }
        return true;
    }

    private void saveProfile() {
        progressDialog.show();
        if(phoneStr.isEmpty()){
            phoneStr = null;
        }

        DbQuery.saveProfileData(nameStr, phoneStr, new MyCompleteListener() {
            @Override
            public void OnSuccess() {
                Toast.makeText(MyProfileActivity.this,"Profile Updated Successfully",Toast.LENGTH_SHORT).show();
                disableEditing();
                progressDialog.dismiss();
            }

            @Override
            public void OnFailure() {
                Toast.makeText(MyProfileActivity.this, "Something went wrong ! Please try again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void enableEditing() {
        name.setEnabled(true);
        //email.setEnabled(true);
        phone.setEnabled(true);
        btnLayout.setVisibility(View.VISIBLE);
    }

    private void disableEditing() {
        name.setEnabled(false);
        email.setEnabled(false);
        phone.setEnabled(false);
        btnLayout.setVisibility(View.GONE);

        name.setText(DbQuery.myProfile.getName());
        email.setText(DbQuery.myProfile.getEmail());
        if(DbQuery.myProfile.getPhone() != null){
            phone.setText(DbQuery.myProfile.getPhone());
        }
        String profileName = DbQuery.myProfile.getName();
        profileImageText.setText(profileName.toUpperCase().substring(0,1));
    }

    private void init() {
        name = findViewById(R.id.mp_name);
        email = findViewById(R.id.mp_email);
        phone = findViewById(R.id.mp_phone);
        btnEdit = findViewById(R.id.btnEdit);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);
        profileImageText = findViewById(R.id.profile_image_text);
        btnLayout = findViewById(R.id.btn_layout);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            MyProfileActivity.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}