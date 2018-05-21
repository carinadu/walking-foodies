package edu.brandeis.cs.walkingfoodies.walkingfood.frontend;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.RadioButton;

import android.view.View;
import android.view.View.OnClickListener;
import edu.brandeis.cs.walkingfoodies.walkingfood.R;

/**
 * Created by Mengdi Zhu on 12/3/16.
 */

public class GenderActivity extends AppCompatActivity {
    private RadioGroup sexGroup;
    private RadioButton sexButton;
    private Button genderButton;
    public final static String EXTRA_GENDER = "GENDER";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gender);

        addListenerOnButton();

    }

    public void addListenerOnButton() {

        sexGroup = (RadioGroup) findViewById(R.id.sex);
        genderButton = (Button) findViewById(R.id.button1);

        genderButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                int selectedId = sexGroup.getCheckedRadioButtonId();
                sexButton = (RadioButton) findViewById(selectedId);
                String str = sexButton.getText().toString();

                Intent intent = new Intent(getApplicationContext(),BirthActivity.class);
                intent.putExtra(EXTRA_GENDER, str);
                startActivity(intent);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);

            }

        });

    }
}
