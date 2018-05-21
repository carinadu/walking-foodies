package edu.brandeis.cs.walkingfoodies.walkingfood.frontend;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;

import edu.brandeis.cs.walkingfoodies.walkingfood.R;

/**
 * Created by mengdi Zhu on 12/3/16.
 */

public class WeightActivity extends AppCompatActivity {
    public final static String EXTRA_GENDER = "GENDER";
    public final static String EXTRA_HEIGHT = "HEIGHT";
    public final static String EXTRA_BIRTH = "BIRTH";
    public final static String EXTRA_WEIGHT = "WEIGHT";

    private EditText Weight;
    private Button weightButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weight);

        weightButton = (Button) findViewById(R.id.button4);
        Weight = (EditText) findViewById(R.id.weight);
        Weight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show();
            }
        });

        weightButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String str1 = Weight.getText().toString();
                String str2 = getIntent().getStringExtra(HeightActivity.EXTRA_BIRTH);
                String str3 = getIntent().getStringExtra(HeightActivity.EXTRA_HEIGHT);
                String str4 = getIntent().getStringExtra(HeightActivity.EXTRA_GENDER);

                Intent intent = new Intent(getApplicationContext(), LoadingPage.class);
                intent.putExtra(EXTRA_WEIGHT, str1);
                intent.putExtra(EXTRA_BIRTH, str2);
                intent.putExtra(EXTRA_HEIGHT, str3);
                intent.putExtra(EXTRA_GENDER, str4);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    }

    private void show() {
        final Dialog d = new Dialog(this);
        d.setContentView(R.layout.double_picker);
        NumberPicker np1 = (NumberPicker)d.findViewById(R.id.weightPicker1);
        NumberPicker np2 = (NumberPicker)d.findViewById(R.id.weightPicker2);
        String weightStr = Weight.getText().toString();
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
            np2.setValue(Integer.valueOf(vals[1]));
        }

        Button save_btn = (Button)d.findViewById(R.id.weight_save_btn);
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NumberPicker np1 = (NumberPicker)d.findViewById(R.id.weightPicker1);
                NumberPicker np2 = (NumberPicker)d.findViewById(R.id.weightPicker2);
                Weight.setText(np1.getValue() + "." + np2.getValue());
                d.dismiss();
            }
        });
        d.show();
        Window window = d.getWindow();

        window.setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
    }

}
