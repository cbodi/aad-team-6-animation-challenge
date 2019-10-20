package com.andela.dairyapp;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.andela.dairyapp.activities.HomeActivity;

public class MainActivity extends AppCompatActivity {

  private ImageView mAppLogo;
  private TextView mWelcomeText;
  private View mLayout;
  private ProgressBar loadingProgressAnim;
  public static final int ANIM_DURATION = 1500;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    mAppLogo = findViewById(R.id.app_logo);
    mWelcomeText = findViewById(R.id.intro_message);
    mLayout = findViewById(R.id.base_layout);
    loadingProgressAnim = findViewById(R.id.progressBar);


    mAppLogo.setVisibility(View.GONE);
    mWelcomeText.setVisibility(View.GONE);

    launchInitialAnimation();

    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        Intent toHomeActivity = new Intent(MainActivity.this, HomeActivity.class);
        startActivity(toHomeActivity);
      }
    }, 3500);


  }


  private void launchInitialAnimation() {

    ObjectAnimator logoFadeInAnimator = ObjectAnimator.ofFloat(mAppLogo, "alpha", 0.0f, 1.0f);
    logoFadeInAnimator.setDuration(ANIM_DURATION)
            .setInterpolator(new AccelerateDecelerateInterpolator());

    // This anim is used for moving the logo downwards
    ObjectAnimator logoDownwardMovementAnimator = ObjectAnimator.ofFloat(mAppLogo, "translationY", 0f, 200f);
    logoDownwardMovementAnimator.setDuration(ANIM_DURATION)
            .setInterpolator(new AccelerateDecelerateInterpolator());


    ObjectAnimator textFadeInAnimator = ObjectAnimator.ofFloat(mWelcomeText, "alpha", 0.0f, 1.0f);
    textFadeInAnimator.setDuration(ANIM_DURATION);

 ObjectAnimator textDownwardMovementAnimator = ObjectAnimator.ofFloat(mWelcomeText, "translationY", 0f, 200f);
    textDownwardMovementAnimator.setDuration(ANIM_DURATION);

    AnimatorSet set = new AnimatorSet();
    set.playTogether(logoFadeInAnimator, logoDownwardMovementAnimator, textFadeInAnimator, textDownwardMovementAnimator);
  /*  AnimatorSet sett = new AnimatorSet();
    sett.playTogether(textFadeInAnimator, textDownwardMovementAnimator);*/
    set.start();
//    sett.start();
    //textFadeInAnimator.start();

    //The code below represents an alternative way of animating the logo and welcome message. It should be used in case above fails.

    mAppLogo.setVisibility(View.VISIBLE);
    mWelcomeText.setVisibility(View.VISIBLE);


  }
}
