package edu.brandeis.cs.walkingfoodies.walkingfood.database;

/**
 * Created by Han Du on 11/30/16.
 */

public class StepData {

    public static final String TABLE_NAME = "steps";
    public static final String TIME = "time";
    public static final String COUNT = "count";
    public static final String CALORIE_BURNED = "calorie_burned";

    public StepData() {}

    public static String[] getColArray() {
        String[] cols = new String[]{TIME, COUNT, CALORIE_BURNED};
        return cols;
    }

    public static String[] getTypeArray() {
        String[] types = new String[]{"TEXT", "INTEGER", "INTEGER"};
        return types;
    }

    public static String getPrimaryKey() {
        return TIME;
    }
}
