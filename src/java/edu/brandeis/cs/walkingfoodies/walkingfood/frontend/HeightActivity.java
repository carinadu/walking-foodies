package edu.brandeis.cs.walkingfoodies.walkingfood.frontend;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;

import edu.brandeis.cs.walkingfoodies.walkingfood.R;

/**
 * Created by Mengdi Zhu on 12/3/16.
 */

public class HeightActivity extends AppCompatActivity {
    private EditText Height;
    private Button heightButton;
    public final static String EXTRA_GENDER = "GENDER";
    public final static String EXTRA_HEIGHT = "HEIGHT";
    public final static String EXTRA_BIRTH = "BIRTH";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.height);

        Height = (EditText) findViewById (R.id.height);
        heightButton = (Button) findViewById(R.id.button3);

        Height.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show();
            }
        });

        heightButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String str1 = Height.getText().toString();
                str1 = str1.replace("'", ".").replace("\"", "");
                String str2 = getIntent().getStringExtra(BirthActivity.EXTRA_BIRTH);
                String str3 = getIntent().getStringExtra(BirthActivity.EXTRA_GENDER);

                Intent intent = new Intent(getApplicationContext(), WeightActivity.class);
                intent.putExtra(EXTRA_BIRTH, str2);
                intent.putExtra(EXTRA_HEIGHT, str1);
                intent.putExtra(EXTRA_GENDER, str3);
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
        ((TextView)d.findViewById(R.id.picker_title)).setText("Choose your height");
        ((TextView)d.findViewById(R.id.picker_sep1)).setText("'");
        ((TextView)d.findViewById(R.id.picker_sep2)).setText("\"");

        String heightStr = Height.getText().toString();
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
            Log.d("123", vals[1]);
            np2.setValue(Integer.valueOf(vals[1].substring(0, vals[1].length() - 1)));
        }

        Button save_btn = (Button)d.findViewById(R.id.weight_save_btn);
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NumberPicker np1 = (NumberPicker)d.findViewById(R.id.weightPicker1);
                NumberPicker np2 = (NumberPicker)d.findViewById(R.id.weightPicker2);
                Height.setText(np1.getValue() + "'" + np2.getValue() + "\"");
                d.dismiss();
            }
        });
        d.show();
        Window window = d.getWindow();

        window.setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
    }

}
