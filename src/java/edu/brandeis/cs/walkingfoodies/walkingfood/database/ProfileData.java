package edu.brandeis.cs.walkingfoodies.walkingfood.database;

/**
 * Created by Han Du on 11/30/16.
 */

public class ProfileData {

    public static final String TABLE_NAME = "profile";
    public static final String HEIGHT = "height";
    public static final String WEIGHT = "weight";
    public static final String GENDER = "gender";
    public static final String AGE = "age";
    public static final String INITIAL_WEIGHT = "initial_weight";

    public ProfileData() {}

    public static String[] getColArray() {
        String[] cols = new String[]{HEIGHT, WEIGHT, GENDER, AGE, INITIAL_WEIGHT};
        return cols;
    }

    public static String[] getTypeArray() {
        String[] types = new String[]{"DOUBLE", "DOUBLE", "BOOLEAN", "INTEGER", "DOUBLE"};
        return types;
    }

    public static String getPrimaryKey() {
        return "";
    }
}
