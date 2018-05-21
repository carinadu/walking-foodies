package edu.brandeis.cs.walkingfoodies.walkingfood.frontend;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import edu.brandeis.cs.walkingfoodies.walkingfood.database.DatabaseOperations;
import edu.brandeis.cs.walkingfoodies.walkingfood.database.ProfileData;

/**
 * Created by wangzheng on 12/5/16.
 */

public class RedirectActivity extends AppCompatActivity{

    private final static String IS_NEW_USER = "isNewUser";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DatabaseOperations op = new DatabaseOperations(this);
        op.open();
        //op.deleteAll();
        //op.open();
        Cursor cursor = op.getAllData(ProfileData.TABLE_NAME, ProfileData.getColArray());
        if(cursor.getCount() == 0) startActivity(new Intent(this, GenderActivity.class));
        else {
            Intent intent = new Intent(this, LoadingPage.class);
            intent.putExtra(IS_NEW_USER, "");
            startActivity(intent);
        }
        finish();
    }
}