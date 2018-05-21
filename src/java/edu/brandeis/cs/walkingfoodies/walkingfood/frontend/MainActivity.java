package edu.brandeis.cs.walkingfoodies.walkingfood.frontend;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;

import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;


import com.shinelw.library.ColorArcProgressBar;

import java.util.ArrayList;

import devlight.io.library.ntb.NavigationTabBar;
import edu.brandeis.cs.walkingfoodies.walkingfood.adapters.MyPagerAdapter;
import edu.brandeis.cs.walkingfoodies.walkingfood.database.DatabaseOperations;
import edu.brandeis.cs.walkingfoodies.walkingfood.utils.MyColorArcProgressBar;
import edu.brandeis.cs.walkingfoodies.walkingfood.utils.MyRippleBackground;
import edu.brandeis.cs.walkingfoodies.walkingfood.R;
import edu.brandeis.cs.walkingfoodies.walkingfood.backend.CaloriesBackend;
import edu.brandeis.cs.walkingfoodies.walkingfood.backend.MainBackend;

/**
 * Created by 聂聂聂 on 11/22/2016.
 */

public class MainActivity extends AppCompatActivity{
    //backend instance
    public static MainBackend mainBackend;
    public static CaloriesBackend caloriesBackend;

    //for updating interface view
    int currentStep = 0;
    int totalStep = 1;
    double rate = 0;

    //interface elements
    private MyColorArcProgressBar progressBar;
    private TextView tv_steps;
    private MyRippleBackground rippleBackground;
    public static AppBarLayout appBar;
    public DatabaseOperations dbo;
    public static MyPagerAdapter myPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainBackend = new MainBackend(this);
        caloriesBackend = new CaloriesBackend();
        setup();

    }

    private void setup() {
        mainBackend.setup();
        caloriesBackend.setup();

        progressBar = (MyColorArcProgressBar) findViewById(R.id.bar1);
        rippleBackground=(MyRippleBackground) findViewById(R.id.content);
        appBar=(AppBarLayout) findViewById(R.id.app_bar);

        tv_steps = (TextView) findViewById(R.id.tv_steps);

        //Use a seekBar to control fake step rates for test/demo purpose
        SeekBar seekBar = (SeekBar) findViewById(R.id.sb);
        seekBar.setProgress(100);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                MainBackend.setFakeRate(i < 10? 10 : 500 * 100 / i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //start the ripple
        rippleBackground.startRippleAnimation();

        //define a handler to run two background processes.
        final Handler handler = new Handler();

        //every 0.1 second check with the backend to see whether there are new steps updated
        //if so, update the colorful circle progress bar.
        Runnable test = new Runnable() {
            @Override
            public void run() {
                updateProgressCircle();
                handler.postDelayed(this, 100);
            }
        };
        handler.postDelayed(test, 100);

        //Every "rippleDuration"(the time the ripple view takes to finish one ripple animation) check
        //with the backend to see whether the walking rate has changed. If so, update the rippleDuration
        //according to the walking rate so that the faster the user is walking, the faster the ripple
        //animation animates.
        Runnable test2 = new Runnable() {
            @Override
            public void run() {
                int rippleDuration = updateRippleRate();
                handler.postDelayed(this, rippleDuration);
            }
        };
        handler.postDelayed(test2, 100);

        //set up the viewPager and fragments:
        final ViewPager viewPager = (ViewPager) findViewById(R.id.vp_horizontal_ntb);
        myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(myPagerAdapter);

        //set up the navigation tab bar at the bottom
        NTBSetup(viewPager);

        //set up the draggable toolbar
        final CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar);
        collapsingToolbarLayout.setExpandedTitleColor(Color.parseColor("#009F90AF"));
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.parseColor("#9f90af"));
    }

    private void updateProgressCircle() {
        //Update the progress colorful circle
        if (totalStep != mainBackend.getTotalSteps()) {
            totalStep = mainBackend.getTotalSteps();
            //setMaxValues() will set the the total value of the progressBar
            progressBar.setMaxValues(totalStep==0? 1 : totalStep);
            progressBar.setCurrentValues(currentStep);
        }
        if (currentStep != mainBackend.getStepsWalked()){
            currentStep = mainBackend.getStepsWalked();
            //setCurrentValues() will set the point where the colorful arc reaches.
            progressBar.setCurrentValues(currentStep);
        }
        //change the text shown at the right bottom corner
        tv_steps.setText(currentStep + " / " + totalStep + " steps");

        //change the ripple color if necessary
        if (totalStep == currentStep) {
            rippleBackground.changeColor(getResources().getColor(R.color.my_Green));
        }
        else {
            rippleBackground.changeColor(getResources().getColor(R.color.my_Red));
        }
    }

    private int updateRippleRate() {
        //set the default ripple duration to be 1000
        int rippleDuration = 1500;
        if (rate != mainBackend.getRate()) {
            double rate = mainBackend.getRate();
            rippleDuration = (int) (3 / rate);
            if (rippleDuration < 1000) rippleDuration = 1000;
            if (rippleDuration > 5000) rippleDuration = 5000;
            rippleBackground.changeRate(rippleDuration);
        }
        return rippleDuration;
    }

    private void NTBSetup(ViewPager viewPager) {

        final String[] colors = getResources().getStringArray(R.array.default_preview);

        final NavigationTabBar navigationTabBar = (NavigationTabBar) findViewById(R.id.ntb_horizontal);
        final ArrayList<NavigationTabBar.Model> models = new ArrayList<>();
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.bowl),
                        getResources().getColor(R.color.navigationTabBarActiveBackground))
                        .title("Eat")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.exercise),
                        getResources().getColor(R.color.navigationTabBarActiveBackground))
                        .title("Walk")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.chart),
                        getResources().getColor(R.color.navigationTabBarActiveBackground))
                        .title("Chart")
                        .build()
        );

        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.usericon),
                        getResources().getColor(R.color.navigationTabBarActiveBackground))
                        .title("Me")
                        .build()
        );


        navigationTabBar.setModels(models);
        navigationTabBar.setViewPager(viewPager, 1);
        navigationTabBar.setOnTabBarSelectedIndexListener(new NavigationTabBar.OnTabBarSelectedIndexListener() {
            @Override
            public void onStartTabSelected(NavigationTabBar.Model model, int index) {
                appBar.setExpanded(false);
            }

            @Override
            public void onEndTabSelected(NavigationTabBar.Model model, int index) {
                appBar.setExpanded(false);
            }
        });

        //IMPORTANT: ENABLE SCROLL BEHAVIOUR IN COORDINATOR LAYOUT
        navigationTabBar.setBehaviorEnabled(true);
    }
}
