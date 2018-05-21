package edu.brandeis.cs.walkingfoodies.walkingfood.frontend;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.view.View;
import android.view.View.OnClickListener;
import edu.brandeis.cs.walkingfoodies.walkingfood.R;

/**
 * Created by Mengdi Zhu on 12/3/16.
 */

public class BirthActivity extends AppCompatActivity {
    private EditText Age;
    private Button birth;
    public final static String EXTRA_GENDER = "GENDER";
    public final static String EXTRA_BIRTH = "BIRTH";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.birth);

        birth = (Button) findViewById(R.id.button2);
        Age = (EditText) findViewById(R.id.date);

        birth.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                String str1 = Age.getText().toString();
                String str2 = getIntent().getStringExtra(GenderActivity.EXTRA_GENDER);

                Intent intent = new Intent(getApplicationContext(), HeightActivity.class);
                intent.putExtra(EXTRA_BIRTH, str1);
                intent.putExtra(EXTRA_GENDER, str2);
                startActivity(intent);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    }

}
