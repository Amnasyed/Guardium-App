package com.example.fypapplication.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fypapplication.Company.company;
import com.example.fypapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class AdminRegestration extends AppCompatActivity implements View.OnClickListener {
    private TextView top, register1;
    private EditText mail3, pwd1, emp1, no;
    private Button reg2;
    private ProgressBar progressBar2;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_regestration);
        mAuth = FirebaseAuth.getInstance();

        top = (TextView) (findViewById(R.id.namecompany2));
        top.setOnClickListener(this);

        register1 = (Button) (findViewById(R.id.RegisterMember2));
        register1.setOnClickListener(this);

        mail3 = (EditText) (findViewById(R.id.email3));
        pwd1 = (EditText) (findViewById(R.id.pass2));
        no = (EditText) (findViewById(R.id.number2));
        emp1 = findViewById(R.id.empid);


        progressBar2 = (ProgressBar) (findViewById(R.id.progress2));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.namecompany2:
                startActivity(new Intent(this, AdminLogin.class));
                break;
            case R.id.RegisterMember2:
                adminregister();
                break;
        }
    }

    private void adminregister() {
        final String mailg = mail3.getText().toString().trim();
        final String passp = pwd1.getText().toString().trim();
        final String emp = emp1.getText().toString().trim();
        final String numbera = no.getText().toString().trim();


        if (mailg.isEmpty()) {
            mail3.setError("Again enter your email!");
            mail3.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(mailg).matches()) {
            mail3.setError("Again enter your email!");
            mail3.requestFocus();
            return;
        }
        if (passp.isEmpty()) {
            pwd1.setError("Again enter your password!");
            pwd1.requestFocus();
            return;
        }
        if (passp.length() < 8) {
            pwd1.setError("Again enter your password!");
            pwd1.requestFocus();
            return;
        }
        if (emp.length() < 7) {
            emp1.setError("NTN no is incomplete!");
            emp1.requestFocus();
            return;
        }
        if (numbera.isEmpty()) {
            no.setError("Again enter your number!");
            no.requestFocus();
            return;
        }

        progressBar2.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(mailg, passp).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    company company = new company(mailg, passp, emp);
                    FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(company).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(AdminRegestration.this, "User has been registered successfully", Toast.LENGTH_LONG).show();
                                finish();
                            } else {
                                Toast.makeText(AdminRegestration.this, "Register Failed" + task.getException(), Toast.LENGTH_LONG).show();
                            }
                            progressBar2.setVisibility(View.GONE);
                        }
                    });

                } else {
                    Toast.makeText(AdminRegestration.this, "Register Failed" + task.getException(), Toast.LENGTH_LONG).show();
                    progressBar2.setVisibility(View.GONE);
                }
            }
        });
    }
}