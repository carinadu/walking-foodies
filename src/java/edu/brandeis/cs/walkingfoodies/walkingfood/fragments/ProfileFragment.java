package edu.brandeis.cs.walkingfoodies.walkingfood.fragments;


import android.app.Dialog;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import edu.brandeis.cs.walkingfoodies.walkingfood.R;
import edu.brandeis.cs.walkingfoodies.walkingfood.database.DatabaseOperations;
import edu.brandeis.cs.walkingfoodies.walkingfood.database.ProfileData;

/**
 * Created by Han Du on 12/5/16.
 */

public class ProfileFragment extends Fragment {
    DatabaseOperations dbo;
    TextView age;
    TextView height;
    TextView gender;
    TextView originalW;
    TextView currentW;
    TextView improveW;
    Button updateW;
    ImageView editHeight;
    ImageView editAge;
    ImageView editGender;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_fragment, container, false);
        dbo = new DatabaseOperations(getActivity());
        age = (TextView)view.findViewById(R.id.ageNumber);
        height = (TextView)view.findViewById(R.id.heightNumber);
        gender = (TextView)view.findViewById(R.id.genderType);
        originalW = (TextView)view.findViewById(R.id.original_weight_number);
        currentW = (TextView)view.findViewById(R.id.current_weight_number);
        improveW = (TextView)view.findViewById(R.id.lose_weight_number);
        updateW = (Button)view.findViewById(R.id.weightBtn);
        editAge = (ImageView)view.findViewById(R.id.arrow1);
        editHeight = (ImageView)view.findViewById(R.id.arrow2);
        editGender = (ImageView)view.findViewById(R.id.arrow3);
        initialize();
        return view;
    }

    public void initialize() {
        Cursor cursor = dbo.getAllData(ProfileData.TABLE_NAME, ProfileData.getColArray());
        if(cursor.getCount() == 0) {

        }else {
            cursor.moveToFirst();
            double ow = cursor.getDouble(cursor.getColumnIndex(ProfileData.INITIAL_WEIGHT));
            originalW.setText(ow + "");
            age.setText(cursor.getInt(cursor.getColumnIndex(ProfileData.AGE)) + "");
            double cw = cursor.getDouble(cursor.getColumnIndex(ProfileData.WEIGHT));
            Log.d("get height", cursor.getString(cursor.getColumnIndex(ProfileData.HEIGHT)));
            String[] feet_inch = (cursor.getString(cursor.getColumnIndex(ProfileData.HEIGHT))).split("\\.");
            Log.d("get height", feet_inch.length + "");
            if(feet_inch.length == 2) {
                height.setText(feet_inch[0] + "'" + feet_inch[1] + "\"");
            }else if(feet_inch.length == 1){
                height.setText(feet_inch[0] + "'0\"");
            }
            currentW.setText(cw + "");
            improveW.setText(ow - cw + "");
            boolean genderType = cursor.getInt(cursor.getColumnIndex(ProfileData.GENDER)) > 0;
            if(genderType) gender.setText("F");
            else gender.setText("M");
        }

        //add listener to age edit
        editAge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        //add listener to height edit
        editHeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog d = new Dialog(getActivity());
                d.setContentView(R.layout.double_picker);
                NumberPicker np1 = (NumberPicker)d.findViewById(R.id.weightPicker1);
                NumberPicker np2 = (NumberPicker)d.findViewById(R.id.weightPicker2);
                ((TextView)d.findViewById(R.id.picker_title)).setText("Choose your height");
                ((TextView)d.findViewById(R.id.picker_sep1)).setText("'");
                ((TextView)d.findViewById(R.id.picker_sep2)).setText("\"");

                String heightStr = height.getText().toString();
                np1.setMinValue(0);
                np1.setMaxValue(10);
                np1.setWrapSelectorWheel(false);

                np2.setMinValue(0);
                np2.setMaxValue(11);
                np2.setWrapSelectorWheel(false);

                if(heightStr.isEmpty()) {
                    np1.setValue(5);
                    np2.setValue(5);
                }else {
                    String[] vals = heightStr.split("'");
                    np1.setValue(Integer.valueOf(vals[0]));
                    if(vals.length > 1) np2.setValue(Integer.valueOf(vals[1].substring(0, vals[1].length() - 1)));
                    else np2.setValue(0);
                }

                Button save_btn = (Button)d.findViewById(R.id.weight_save_btn);
                save_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        NumberPicker np1 = (NumberPicker)d.findViewById(R.id.weightPicker1);
                        NumberPicker np2 = (NumberPicker)d.findViewById(R.id.weightPicker2);
                        String newV = np1.getValue() + "." + np2.getValue();
                        dbo.setHeight(Double.parseDouble(newV));
                        height.setText(np1.getValue() + "'" + np2.getValue() + "\"");
                        d.dismiss();
                    }
                });
                d.show();
                Window window = d.getWindow();

                window.setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
            }
        });
        //add listener to gender edit
        editGender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        //add listener to weight update button
        updateW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog d = new Dialog(getContext());
                d.setContentView(R.layout.double_picker);
                NumberPicker np1 = (NumberPicker)d.findViewById(R.id.weightPicker1);
                NumberPicker np2 = (NumberPicker)d.findViewById(R.id.weightPicker2);
                String weightStr = currentW.getText().toString();
                np1.setMinValue(0);
                np1.setMaxValue(500);

                np2.setMinValue(0);
                np2.setMaxValue(9);
                np2.setWrapSelectorWheel(false);

                if(weightStr.isEmpty()) {
                    np1.setValue(170);
                    np2.setValue(0);
                }else {
                    String[] vals = weightStr.split("\\.");
                    np1.setValue(Integer.valueOf(vals[0]));
                    if(vals.length > 1) np2.setValue(Integer.valueOf(vals[1]));
                    else np2.setValue(0);
                }

                Button save_btn = (Button)d.findViewById(R.id.weight_save_btn);
                save_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        NumberPicker np1 = (NumberPicker)d.findViewById(R.id.weightPicker1);
                        NumberPicker np2 = (NumberPicker)d.findViewById(R.id.weightPicker2);
                        String newV = np1.getValue() + "." + np2.getValue();
                        dbo.setWeight(Double.parseDouble(newV));
                        currentW.setText(newV);
                        NumberFormat formatter = new DecimalFormat("#0.00");
                        improveW.setText((formatter.format(Double.parseDouble(originalW.getText().toString()) - Double.parseDouble(newV))) + "");
                        d.dismiss();
                    }
                });
                d.show();
                Window window = d.getWindow();

                window.setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
            }
        });
    }
}
