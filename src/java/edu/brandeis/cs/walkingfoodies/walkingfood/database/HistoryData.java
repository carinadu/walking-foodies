package edu.brandeis.cs.walkingfoodies.walkingfood.database;

import android.provider.BaseColumns;

/**
 * Created by Han Du on 11/30/16.
 */

public class HistoryData{

    public static final String TABLE_NAME = "history";
    public static final String DATE = "date";
    public static final String STEPS = "steps";
    public static final String BURN = "burn";
    public static final String GAIN = "gain";

    public HistoryData(){}

    public static String[] getColArray() {
        String[] cols = new String[]{DATE, STEPS, BURN, GAIN};
        return cols;
    }

    public static String[] getTypeArray() {
        String[] types = new String[]{"TEXT", "INTEGER", "INTEGER", "INTEGER"};
        return types;
    }

    public static String getPrimaryKey() {
        return "";
    }

}
