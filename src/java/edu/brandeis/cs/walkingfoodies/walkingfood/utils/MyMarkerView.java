package edu.brandeis.cs.walkingfoodies.walkingfood.utils;

import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;

import edu.brandeis.cs.walkingfoodies.walkingfood.R;

/**
 * Created by wangzheng on 12/4/16.
 */

public class MyMarkerView extends MarkerView {

    private TextView tvContent;
    public MyMarkerView (Context context, int layoutResource) {
        super(context, layoutResource);
        tvContent = (TextView) this.findViewById(R.id.tvContent);
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        tvContent.setText("" + e.getY()); // set the entry-value as the display text
    }


}
