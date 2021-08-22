package com.example.fypapplication.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fypapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class AdminLogin extends AppCompatActivity implements View.OnClickListener {
    Button first1;
    EditText email3, pswd2;
    private TextView register2;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);
        register2 = (TextView) findViewById(R.id.register2);
        register2.setOnClickListener(this);

        first1 = (Button) findViewById(R.id.bt3);
        first1.setOnClickListener(this);

        email3 = (EditText) findViewById(R.id.id1);
        pswd2 = (EditText) findViewById(R.id.password2);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.register2:
                startActivity(new Intent(this, AdminRegestration.class));
                break;

            case R.id.bt3:
                adminLogin();
                break;
        }

    }

    private void adminLogin() {
        String emailt = email3.getText().toString().trim();
        String passwordl = pswd2.getText().toString().trim();

        if (emailt.isEmpty()) {
            email3.setError("Again enter your email!");
            email3.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(emailt).matches()) {
            email3.setError("Enter valid email!");
            email3.requestFocus();
            return;
        }

        if (passwordl.length() < 8) {
            pswd2.setError("Again enter your password!");
            pswd2.requestFocus();
            return;
        }
        mAuth.signInWithEmailAndPassword(emailt, passwordl).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    startActivity(new Intent(AdminLogin.this, AdminDashboard.class));
                } else {
                    Toast.makeText(AdminLogin.this, "Error!!!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}