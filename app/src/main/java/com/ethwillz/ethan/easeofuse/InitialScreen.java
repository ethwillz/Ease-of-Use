package com.ethwillz.ethan.easeofuse;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class InitialScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //If user is already registered it will bypass initial screen and go immediately to the main screen
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            onGoMain();
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.initial_screen);

        //Sets up variables for all of the text views and the button
        final TextView logo1 = (TextView)findViewById(R.id.logo1);
        final TextView logo2 = (TextView)findViewById(R.id.logo2);
        final TextView logo3 = (TextView)findViewById(R.id.logo3);
        final TextView logo4 = (TextView)findViewById(R.id.logo4);
        final TextView logo5 = (TextView)findViewById(R.id.logo5);
        final TextView logo6 = (TextView)findViewById(R.id.logo6);
        final Button enter = (Button)findViewById(R.id.enter);
        enter.setVisibility(View.INVISIBLE);
        final Typeface main = Typeface.createFromAsset(getAssets(), "fonts/Barrio Santo.ttf");
        final Typeface button = Typeface.createFromAsset(getAssets(), "fonts/Barrio Santo.ttf");

        //Sets typeface to the logo typeface
        logo1.setTypeface(main);
        logo2.setTypeface(main);
        logo3.setTypeface(main);
        logo4.setTypeface(main);
        logo5.setTypeface(main);
        logo6.setTypeface(main);
        enter.setTypeface(button);

        //All of the animations for the main page
        final Animation out1 = new AlphaAnimation(1, 0);
        final Animation out2 = new AlphaAnimation(1, 0);
        final Animation out3 = new AlphaAnimation(1, 0);
        final Animation out4 = new AlphaAnimation(1, 0);
        final Animation out5 = new AlphaAnimation(1, 0);
        final Animation out6 = new AlphaAnimation(1, 0);
        final Animation out7 = new AlphaAnimation(1, 0);

        out1.setDuration(1400);
        out2.setDuration(1200);
        out2.setStartOffset(200);
        out3.setDuration(1000);
        out3.setStartOffset(400);
        out4.setDuration(800);
        out4.setStartOffset(600);
        out5.setDuration(600);
        out5.setStartOffset(800);
        out6.setDuration(400);
        out6.setStartOffset(1000);
        Animation in = new AlphaAnimation(0, 1);

        logo1.startAnimation(out1);
        logo2.startAnimation(out2);
        logo3.startAnimation(out3);
        logo4.startAnimation(out4);
        logo5.startAnimation(out5);
        logo6.startAnimation(out6);

        //Starts and manages the animation of the logo
        out6.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                logo1.setVisibility(View.INVISIBLE);
                logo2.setVisibility(View.INVISIBLE);
                logo3.setVisibility(View.INVISIBLE);
                logo4.setVisibility(View.INVISIBLE);
                logo5.setVisibility(View.INVISIBLE);
                logo6.setVisibility(View.INVISIBLE);
                enter.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    //Directs the user to the main activity
    public void onGoMain() {
        Intent i = new Intent(this, Main.class);
        startActivity(i);
    }

    //Directs the user to the sign in page if they do not have an account
    public void onGoSignIn(View view) {
        Intent i = new Intent(this, SignIn.class);
        startActivity(i);
    }
}
