package com.example.quizzone;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePasswordActivity extends AppCompatActivity {
    Button btnGoBack, btnChangePassword;
    EditText yourEmail, currentPass, newPass;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_change_password);

        btnGoBack = findViewById(R.id.btnGoBack);
        btnChangePassword = findViewById(R.id.btnChangePass);
        currentPass = findViewById(R.id.currentPass);
        newPass = findViewById(R.id.newPass);

        mAuth = FirebaseAuth.getInstance();
        FirebaseAuth.getInstance().setLanguageCode("en");


        btnGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangePasswordActivity.this.finish();
            }
        });

        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String currentPassword = currentPass.getText().toString().trim();
                String newPassword = newPass.getText().toString().trim();
                reauthenticateAndChangePassword(currentPassword, newPassword);
            }
        });



    }
    private void reauthenticateAndChangePassword(String currentPassword, String newPassword) {
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null && user.getEmail() != null) {
            // Tạo AuthCredential từ email và mật khẩu hiện tại
            AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), currentPassword);

            // Xác thực lại
            user.reauthenticate(credential)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                // Xác thực lại thành công, cập nhật mật khẩu
                                user.updatePassword(newPassword)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(ChangePasswordActivity.this, "Mật khẩu đã được cập nhật thành công!", Toast.LENGTH_SHORT).show();
                                                    ChangePasswordActivity.this.finish();
                                                } else {
                                                    Toast.makeText(ChangePasswordActivity.this, "Cập nhật mật khẩu thất bại.", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            } else {
                                Toast.makeText(ChangePasswordActivity.this, "Mật khẩu bạn đã nhập không đúng.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            Toast.makeText(this, "Người dùng chưa đăng nhập hoặc email không khả dụng.", Toast.LENGTH_SHORT).show();
        }
    }

}