package edu.brandeis.cs.walkingfoodies.walkingfood.database;

import java.util.Map;

/**
 * Created by Han Du on 11/30/16.
 */

public class MealData {

    public static final String TABLE_NAME = "meals";
    public static final String TIME = "time";
    public static final String TYPE = "type";
    public static final String CALORIE = "calorie";
    public static final String[] type = {"Breakfast", "Lunch", "Dinner", "Snack"};

    public MealData() {}

    public static String[] getColArray() {
        String[] cols = new String[]{TIME, TYPE, CALORIE};
        return cols;
    }

    public static String[] getTypeArray() {
        String[] types = new String[]{"TEXT", "Integer", "INTEGER"};
        return types;
    }

    public static String getPrimaryKey() {
        return "";
    }
}
