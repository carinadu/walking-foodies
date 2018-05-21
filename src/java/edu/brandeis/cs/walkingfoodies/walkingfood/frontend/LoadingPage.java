package edu.brandeis.cs.walkingfoodies.walkingfood.frontend;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import java.util.Random;

import edu.brandeis.cs.walkingfoodies.walkingfood.R;
import edu.brandeis.cs.walkingfoodies.walkingfood.database.DatabaseOperations;
import edu.brandeis.cs.walkingfoodies.walkingfood.database.HistoryData;
import edu.brandeis.cs.walkingfoodies.walkingfood.database.StepData;

public class LoadingPage extends AppCompatActivity {

    private final static String IS_NEW_USER = "isNewUser";
    private final int SPLASH_DISPLAY_LENGTH = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(LoadingPage.this, MainActivity.class);

                DatabaseOperations dbo = new DatabaseOperations(LoadingPage.this);

                if(getIntent().getStringExtra(IS_NEW_USER) == null){

                    String str1 = getIntent().getStringExtra(WeightActivity.EXTRA_BIRTH);
                    String str2 = getIntent().getStringExtra(WeightActivity.EXTRA_HEIGHT);
                    String str3 = getIntent().getStringExtra(WeightActivity.EXTRA_WEIGHT);
                    String str4 = getIntent().getStringExtra(WeightActivity.EXTRA_GENDER);

                    double height = str2.isEmpty() ? 5.5 : Double.parseDouble(str2);
                    double weight = str3.isEmpty() ? 160 : Double.parseDouble(str3);
                    int age = str1.isEmpty() ? 20 : Integer.parseInt(str1);
                    boolean gender = false;
                    if(str4.equals("Female")) gender = true;

                    dbo.insertToProfile(height, weight, age, gender);
                }
                Cursor cursor = dbo.getAllData(StepData.TABLE_NAME, StepData.getColArray());
                if(cursor.getCount() == 0) {
                    Random rand = new Random();
                    for (int i = 0; i < 7; i++) {
                        dbo.insertToSteps(i + ":00", 0, 0);
                    }
                    dbo.insertToSteps("7:00", 79, 100);
                    dbo.insertToSteps("8:00", 138, 230);
                    dbo.insertToSteps("9:00", 110, 130);
                    for (int i = 10; i < 19; i++) {
                        dbo.insertToSteps(i + ":00", rand.nextInt(300), 130);
                    }
                }

                cursor = dbo.getAllData(HistoryData.TABLE_NAME, HistoryData.getColArray());
                if(cursor.getCount() == 0) {
                    dbo.insertToHistory("2016-11-12", 1300, 2400, 3200);
                    dbo.insertToHistory("2016-11-13", 1100, 2100, 3300);
                    dbo.insertToHistory("2016-11-14", 1900, 1000, 2000);
                    dbo.insertToHistory("2016-11-15", 1200, 2000, 3000);

                    dbo.insertToHistory("2016-11-16", 1300, 2400, 3200);
                    dbo.insertToHistory("2016-11-17", 1100, 2100, 3300);
                    dbo.insertToHistory("2016-11-18", 1900, 1000, 2000);
                    dbo.insertToHistory("2016-11-19", 1200, 2000, 3000);
                    dbo.insertToHistory("2016-11-20", 1300, 1100, 1300);
                    dbo.insertToHistory("2016-11-21", 2900, 1000, 2000);
                    dbo.insertToHistory("2016-11-22", 1100, 2100, 3300);
                    dbo.insertToHistory("2016-11-23", 1900, 2000, 3000);
                    dbo.insertToHistory("2016-11-24", 1300, 1000, 2000);
                    dbo.insertToHistory("2016-11-25", 800, 2600, 3100);

                    dbo.insertToHistory("2016-11-26", 1300, 1100, 1300);
                    dbo.insertToHistory("2016-11-27", 2900, 1000, 2000);
                    dbo.insertToHistory("2016-11-28", 1100, 2100, 3300);
                    dbo.insertToHistory("2016-11-29", 1900, 2000, 3000);
                    dbo.insertToHistory("2016-11-30", 1300, 1000, 2000);
                    dbo.insertToHistory("2016-12-01", 800, 2600, 3100);
                    dbo.insertToHistory("2016-12-02", 800, 2600, 3100);
                    dbo.insertToHistory("2016-12-03", 800, 2600, 3100);
                    dbo.insertToHistory("2016-12-04", 3000, 2000, 4000);
                    dbo.insertToHistory("2016-12-05", 2600, 1800, 3200);
                }
                cursor.close();
                LoadingPage.this.startActivity(intent);
                LoadingPage.this.finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        }, SPLASH_DISPLAY_LENGTH);

    }

}
