package com.example.fypapplication;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;

public class Splashscreen extends AppCompatActivity {
       LottieAnimationView lottieAnimationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        lottieAnimationView=findViewById(R.id.lotte);

        lottieAnimationView.animate().translationY(-1600).setDuration(1000).setStartDelay(4000);
      lottieAnimationView.addAnimatorListener(new Animator.AnimatorListener() {
          @Override
          public void onAnimationStart(Animator animation) {

          }

          @Override
          public void onAnimationEnd(Animator animation) {
              Intent mainIntent = new Intent(Splashscreen.this,StartActivity.class);
              Splashscreen.this.startActivity(mainIntent);
              Splashscreen.this.finish();
          }

          @Override
          public void onAnimationCancel(Animator animation) {

          }

          @Override
          public void onAnimationRepeat(Animator animation) {

          }
      });
    }
    }