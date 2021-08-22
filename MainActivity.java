package com.example.fypapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView register;
    Button Signin;
    EditText Email1, pswrd1;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        register=(TextView)findViewById(R.id.register);
        register.setOnClickListener(this);

        Signin=(Button)findViewById(R.id.bt1);
        Signin.setOnClickListener(this);

        Email1=(EditText)findViewById(R.id.email);
        pswrd1=(EditText)findViewById(R.id.password);

        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.register:
                startActivity(new Intent(this,Registeruser.class));
                break;

            case R.id.bt1:
               userLogin();
                break;

        }

    }

    private void userLogin() {
        String email=Email1.getText().toString().trim();
        String password=pswrd1.getText().toString().trim();

        if (email.isEmpty()) {
            Email1.setError("Again enter your email!");
            Email1.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Email1.setError("Enter valid email!");
            Email1.requestFocus();
            return;
        }

        if (password.length()<8){
            pswrd1.setError("Again enter your password!");
            pswrd1.requestFocus();
            return;
        }
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    startActivity(new Intent(MainActivity.this,SelectCategoryActivity.class));

                }else{
                    Toast.makeText(MainActivity.this, "Error!!!"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
    }
        });


    }


}