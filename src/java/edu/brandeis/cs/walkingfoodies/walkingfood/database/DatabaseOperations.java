package edu.brandeis.cs.walkingfoodies.walkingfood.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Han Du on 11/30/16.
 */

public class DatabaseOperations extends SQLiteOpenHelper {
    private static final int database_version = 7;
    private static final String DATABASE_NAME = "walking_food";
    private static SQLiteDatabase db;

    public DatabaseOperations(Context context) {
        super(context, DATABASE_NAME, null, database_version);
        String query = getCreateTableQuery(HistoryData.TABLE_NAME, HistoryData.getColArray(), HistoryData.getTypeArray(), HistoryData.getPrimaryKey());
        Log.d("Database operations", "database created : " + query);
    }

    @Override
    public void onCreate(SQLiteDatabase sdb) {
        //create table history
        sdb.execSQL(getCreateTableQuery(HistoryData.TABLE_NAME, HistoryData.getColArray(), HistoryData.getTypeArray(), HistoryData.getPrimaryKey()));
        //create table steps
        sdb.execSQL(getCreateTableQuery(StepData.TABLE_NAME, StepData.getColArray(), StepData.getTypeArray(), StepData.getPrimaryKey()));
        //create table meals
        sdb.execSQL(getCreateTableQuery(MealData.TABLE_NAME, MealData.getColArray(), MealData.getTypeArray(), MealData.getPrimaryKey()));
        //create table profile
        sdb.execSQL(getCreateTableQuery(ProfileData.TABLE_NAME, ProfileData.getColArray(), ProfileData.getTypeArray(), ProfileData.getPrimaryKey()));
        //Log.d("Database operations", "table created: ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sdb, int arg1, int arg2) {
        //Log.d("Database operations", "before update: ");
        sdb.execSQL("DROP TABLE IF EXISTS " + HistoryData.TABLE_NAME);
        sdb.execSQL("DROP TABLE IF EXISTS " + MealData.TABLE_NAME);
        sdb.execSQL("DROP TABLE IF EXISTS " + StepData.TABLE_NAME);
        sdb.execSQL("DROP TABLE IF EXISTS " + ProfileData.TABLE_NAME);
        //Log.d("Database operations", "updated: ");
        onCreate(sdb);
    }

    public void deleteAll() {
        db.execSQL("DROP TABLE IF EXISTS " + HistoryData.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MealData.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + StepData.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ProfileData.TABLE_NAME);
        //Log.d("Database operations", "updated: ");
        onCreate(db);
    }

    public void delete(String tableName) {
        db.execSQL("DROP TABLE IF EXISTS " + tableName);
        onCreate(db);
    }

    public void open() {
        db = getWritableDatabase();
    }

    public void close() {
        db.close();
    }

    /**
     *
     * @param tableName the table name
     * @param colNames column names of the table
     * @param types type of each column
     * @param primaryKey the name of the primary key
     * @return return the query string
     */
    private String getCreateTableQuery(String tableName, String[] colNames, String[] types, String primaryKey) {
        String query = "CREATE TABLE " + tableName + " (";
        int l = colNames.length;
        for(int i = 0; i < l - 1; i++) {
            query += colNames[i] + " " + types[i] + ", ";
        }
        if(primaryKey.equals("")) {
            query += colNames[l - 1] + " " + types[l - 1] + ");";
        }else {
            query += colNames[l - 1] + " " + types[l - 1] + ", ";
            query += "PRIMARY KEY (" + primaryKey + "));";
        }
        Log.d("Database operations ", tableName + " created");
        return query;
    }

    private void insert(String tableName, ContentValues cv) {
        db.insert(tableName, null, cv);
        Log.d("Database operations", "insert a row in " + tableName);
    }

    public void insertToHistory(String date, int steps, int burn, int gain) {
        ContentValues cv = new ContentValues();
        cv.put(HistoryData.DATE, date);
        cv.put(HistoryData.STEPS, steps);
        cv.put(HistoryData.BURN, burn);
        cv.put(HistoryData.GAIN, gain);
        insert(HistoryData.TABLE_NAME, cv);
    }

    public void insertToMeal(String time, int type, int calorie) {
        ContentValues cv = new ContentValues();
        cv.put(MealData.TYPE, type);
        cv.put(MealData.CALORIE, calorie);
        cv.put(MealData.TIME, time);
        insert(MealData.TABLE_NAME, cv);
    }

    public void insertToSteps(String time, int count, int calorie_burned) {
        ContentValues cv = new ContentValues();
        cv.put(StepData.TIME, time);
        cv.put(StepData.CALORIE_BURNED, calorie_burned);
        cv.put(StepData.COUNT, count);
        insert(StepData.TABLE_NAME, cv);
    }

    public void insertToProfile(double height, double weight, int age, boolean gender) {
        ContentValues cv = new ContentValues();
        cv.put(ProfileData.HEIGHT, height);
        cv.put(ProfileData.WEIGHT, weight);
        cv.put(ProfileData.AGE, age);
        cv.put(ProfileData.GENDER, gender);
        cv.put(ProfileData.INITIAL_WEIGHT, weight);
        insert(ProfileData.TABLE_NAME, cv);
    }

    public Cursor getAllData(String tableName, String[] cols) {
        return db.query(tableName, cols, null, null, null, null, null);
    }

    public Cursor getHistoryByRange(int days) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -days);
        String start = new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(cal.getTime());

        return db.query(HistoryData.TABLE_NAME, HistoryData.getColArray(), HistoryData.DATE +
                String.format(">= '%s'", start), null, null, null, null);

    }

    public void setHeight(double height) {

        Log.d("change height", height+"");
        String query = "UPDATE " + ProfileData.TABLE_NAME + " SET " + ProfileData.HEIGHT + "=" + height + " WHERE rowid=1;";
        db.execSQL(query);
    }

    public void setWeight(double weight) {
        Cursor cursor = getAllData(ProfileData.TABLE_NAME, ProfileData.getColArray());
        Log.d("cursor size ", cursor.getCount() + "");
        cursor.moveToFirst();
        String query = "UPDATE " + ProfileData.TABLE_NAME + " SET " + ProfileData.WEIGHT + "=" + weight + " WHERE rowid=1;";
        db.execSQL(query);
    }
}
