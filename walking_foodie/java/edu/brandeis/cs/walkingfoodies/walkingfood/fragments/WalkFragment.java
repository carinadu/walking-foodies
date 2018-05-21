package edu.brandeis.cs.walkingfoodies.walkingfood.fragments;

import android.app.AlertDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;


import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.BubbleChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.BubbleData;
import com.github.mikephil.charting.data.BubbleDataSet;
import com.github.mikephil.charting.data.BubbleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import edu.brandeis.cs.walkingfoodies.walkingfood.R;
import edu.brandeis.cs.walkingfoodies.walkingfood.database.DatabaseOperations;
import edu.brandeis.cs.walkingfoodies.walkingfood.database.MealData;
import edu.brandeis.cs.walkingfoodies.walkingfood.database.StepData;

/**
 * Created by Han Du on 11/29/2016.
 */
public class WalkFragment extends Fragment {
    DatabaseOperations dbo;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.walk_fragment, container, false);
        dbo = new DatabaseOperations(getActivity());
        BarChart dailySteps = (BarChart) view.findViewById(R.id.step_chart);
        //BubbleChart dailyMeals = (BubbleChart) view.findViewById(R.id.meal_bubble_chart);
        PieChart dailyMealAgg = (PieChart) view.findViewById(R.id.meal_pie_chart);
        drawSteps(dailySteps);
        drawMeals(dailyMealAgg);
        return view;
    }

    private void drawSteps(final BarChart dailyStep) {
        dailyStep.setTouchEnabled(true);
        dailyStep.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {

            }

            @Override
            public void onNothingSelected() {

            }
        });

        //the whole chart
        Description des = new Description();
        des.setText("");
        dailyStep.setDescription(des);
        dailyStep.getLegend().setEnabled(false);

        //X axis setting
        dailyStep.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        dailyStep.getXAxis().setDrawGridLines(false);
        dailyStep.getXAxis().setAxisMaximum(24);
        dailyStep.getXAxis().setAxisMinimum(0);
        dailyStep.getXAxis().setGranularity(12f);

        //set left and right axises
        dailyStep.getAxisLeft().setEnabled(false);
        dailyStep.getAxisRight().setEnabled(false);
        List<StepChartData> dataObjects = getStepData();
        Log.d("data set size ", dataObjects.size() + "");
        int[] counts = new int[24];
        Random rand = new Random();
        if(dataObjects.size() > 0) {
                List<BarEntry> steps = new ArrayList<BarEntry>();
                for (StepChartData data : dataObjects) {
                    counts[data.getHour()] += data.getValueY();
                }
                for(int i = 0; i < counts.length; i++) {
                    steps.add(new BarEntry(i, counts[i]));
                }
                BarDataSet dataSet = new BarDataSet(steps, "daily steps");
            dataSet.setColors(getResources().getColor(R.color.graphBlue1),
                    getResources().getColor(R.color.graphBlue2),
                    getResources().getColor(R.color.graphBlue3),
                    getResources().getColor(R.color.graphBlue4));
                BarData lineData = new BarData(dataSet);
                //lineData.setDrawValues(true);
                dailyStep.setData(lineData);
                dailyStep.invalidate();
        }
    }

    private void drawMeals(PieChart dailyMealAgg) {
        int[] mealAgg = new int[4];
        //Draw the bubble chart
        //Set x axis
//        dailyMeals.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
//        dailyMeals.getXAxis().setDrawGridLines(false);
//        dailyMeals.getAxisLeft().setAxisMinimum(0);
//        dailyMeals.getAxisLeft().setAxisMaximum(4);
//
//        //set left axis
//        dailyMeals.getAxisLeft().setDrawGridLines(false);
//        dailyMeals.getAxisLeft().setAxisMinimum(0);
//        dailyMeals.getAxisLeft().setAxisMaximum(24);
//
//        //set right axis
//        dailyMeals.getAxisRight().setDrawAxisLine(false);
//        dailyMeals.getAxisRight().setDrawLabels(false);
        List<MealChartData> dataObjects = getMealData();
        if(dataObjects.size() > 0) {
            List<BubbleEntry> meals = new ArrayList<>();
            for (MealChartData data : dataObjects) {
                Log.d("bubble entry: ", data.getType() + " " + data.getTime() + " " + (float) data.getCalories());
                //meals.add(new BubbleEntry(data.getType(), (float)data.getTime(), (float) data.getCalories()));
                mealAgg[data.getType()] += data.getCalories();
            }
            Log.d("bubble entry size: ", meals.size() + "");
            /*meals.add(new BubbleEntry(0, 10f, 800f));
            meals.add(new BubbleEntry(1, 13f, 600f));
            meals.add(new BubbleEntry(2, 21f, 700f));
            meals.add(new BubbleEntry(3, 11f, 910f));
            meals.add(new BubbleEntry(0, 14f, 980f));
            meals.add(new BubbleEntry(1, 11f, 420f));
            meals.add(new BubbleEntry(2, 3f, 810f));*/
//            BubbleDataSet bubbleDataSet = new BubbleDataSet(meals, "meals");
//            bubbleDataSet.setColors(getResources().getColor(R.color.graphBlue2),
//                    getResources().getColor(R.color.graphOrange1),
//                    getResources().getColor(R.color.graphGreen4),
//                    getResources().getColor(R.color.graphOrange2),
//                    getResources().getColor(R.color.graphGreen1),
//                    getResources().getColor(R.color.graphBlue1));
//            BubbleData bubbleData = new BubbleData(bubbleDataSet);
//            dailyMeals.setData(bubbleData);
//            dailyMeals.invalidate();

            //draw the pie chart

            //the whole chart
            Description des = new Description();
            des.setText("");
            dailyMealAgg.setDescription(des);

            //the hole
            dailyMealAgg.setDrawSliceText(false);
            dailyMealAgg.setDrawHoleEnabled(true);
            dailyMealAgg.setHoleColor(getResources().getColor(R.color.secondaryBackground));
            dailyMealAgg.setHoleRadius(45f);
            dailyMealAgg.setTransparentCircleRadius(55f);

            List<PieEntry> entries = new ArrayList<>();
            for (int i = 0; i < mealAgg.length; i++) {
                if(mealAgg[i] != 0)
                    entries.add(new PieEntry(mealAgg[i], MealData.type[i]));
            }
            PieDataSet pieDataSet = new PieDataSet(entries, "");
            pieDataSet.setColors(getResources().getColor(R.color.graphBlue1),
                    getResources().getColor(R.color.graphBlue2),
                    getResources().getColor(R.color.graphBlue3),
                    getResources().getColor(R.color.graphBlue4));
            PieData data = new PieData(pieDataSet);
            dailyMealAgg.setData(data);
            dailyMealAgg.invalidate(); // refresh
        }
    }


    private List<StepChartData> getStepData() {
        List<StepChartData> dataObjects = new ArrayList<>();
        Cursor cursor = dbo.getAllData(StepData.TABLE_NAME, StepData.getColArray());
        if(cursor.moveToFirst()) {
            do{
                String[] time = cursor.getString(cursor.getColumnIndex(StepData.TIME)).split(":");
                int hour = Integer.parseInt(time[0]), minutes = Integer.parseInt(time[1]);
                dataObjects.add(new StepChartData(hour, minutes, cursor.getInt(cursor.getColumnIndex(StepData.COUNT))));
            }while(cursor.moveToNext());
        }
        cursor.close();
        return dataObjects;
    }

    private List<MealChartData> getMealData() {
        List<MealChartData> dataObjects = new ArrayList<>();
        Cursor cursor = dbo.getAllData(MealData.TABLE_NAME, MealData.getColArray());

//        if(cursor.getCount() == 0) {
//            Random rand = new Random();
//            for(int i = 0; i < 10; i++) {
//                dbo.insertToMeal(i + "", i % 4, rand.nextInt(200));
//            }
//        }

        if(cursor.moveToFirst()) {
            do{
                dataObjects.add(new MealChartData(Integer.parseInt(cursor.getString(cursor.getColumnIndex(MealData.TIME))),
                        cursor.getInt(cursor.getColumnIndex(MealData.TYPE)),
                        cursor.getInt(cursor.getColumnIndex(MealData.CALORIE))));
            }while(cursor.moveToNext());
        }
        cursor.close();
        return dataObjects;
    }


    private static class StepChartData {
        private int hour;
        private int minute;
        private int y;
        public StepChartData(int h, int m, int yValue) {
            hour = h;
            minute = m;
            y = yValue;
        }
        public int getHour() {
            return hour;
        }
        public int getMinute() {
            return minute;
        }

        public int getValueY() {
            return y;
        }
    }

    private static class MealChartData {
        private int time;
        private int type;
        private int calories;
        public MealChartData(int t, int ty, int c) {
            time = t;
            type = ty;
            calories = c;
        }
        public int getTime() {return time;}
        public int getType() {return type;}
        public int getCalories() {return calories;}
    }
}