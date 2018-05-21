package edu.brandeis.cs.walkingfoodies.walkingfood.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

import edu.brandeis.cs.walkingfoodies.walkingfood.R;
import edu.brandeis.cs.walkingfoodies.walkingfood.database.DatabaseOperations;
import edu.brandeis.cs.walkingfoodies.walkingfood.database.HistoryData;
import edu.brandeis.cs.walkingfoodies.walkingfood.utils.MyMarkerView;

/**
 * Created by 聂聂聂 on 11/29/2016.
 */
public class HistoryFragment extends Fragment implements RadioGroup.OnCheckedChangeListener{

    View view;
    RadioGroup typeGroup, periodGroup;
    LineChart chart;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.history_fragment, container, false);
        chart = (LineChart) view.findViewById(R.id.hist_chart);
        initChart(chart);

        typeGroup = (RadioGroup)view.findViewById(R.id.chart_type_switch);
        typeGroup.setOnCheckedChangeListener(this);

        periodGroup = (RadioGroup)view.findViewById(R.id.chart_period_switch);
        periodGroup.setOnCheckedChangeListener(this);

        typeGroup.check(R.id.step_btn);
        periodGroup.check(R.id.week_btn);


        return view;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        plotHistory(typeGroup.getCheckedRadioButtonId(),
                periodGroup.getCheckedRadioButtonId());
    }

    public void initChart(LineChart chart) {
        Legend legend = chart.getLegend();
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setTextSize(16.0f);
        XAxis axis = chart.getXAxis();
        axis.setDrawGridLines(false);
        axis.setDrawLabels(false);
        axis.setAxisLineWidth(2);
        axis.setPosition(XAxis.XAxisPosition.BOTTOM);

        YAxis left = chart.getAxisLeft();
        left.setDrawGridLines(false);
        left.setAxisLineWidth(2);
        chart.getAxisRight().setEnabled(false);
        Description desc = new Description();
        desc.setText("");
        chart.setDescription(desc);
        chart.setTouchEnabled(true);

    }


    public void plotHistory(int typeId, int checkedId) {
        DatabaseOperations op = new DatabaseOperations(view.getContext());
        int period = getPeriod(checkedId);
        Cursor cursor = op.getHistoryByRange(period);
        int index = 0;
        if(typeId == R.id.step_btn) {
            List<Entry> entries = new ArrayList<>();
            int totalSteps = 0;
            while (cursor.moveToNext()) {
                int steps = cursor.getInt(cursor.getColumnIndex(HistoryData.STEPS));
                totalSteps += steps;
                entries.add(new Entry(++index, steps));
            }
            ((TextView)view.findViewById(R.id.step_title)).setText("Your daily average steps is ");
            TextView stepView = (TextView)view.findViewById(R.id.step_value);
            if(index == 0) stepView.setText("0 steps");
            else stepView.setText("" + totalSteps/index + " steps");
            if(entries.isEmpty()) return;
            if(entries.size() > 20) entries = aggregatePoints(entries, 20);
            LineDataSet dataSet = new LineDataSet(entries, "steps");
            LineData lineData = new LineData(dataSet);
            lineData.setDrawValues(false);

            defaultLineSetup(dataSet, R.color.graphOrange1);

            chart.setData(lineData);
        }else if(typeId == R.id.cal_btn) {
            List<Entry> totals = new ArrayList<>(), burns = new ArrayList<>();
            int totalCals = 0;
            while(cursor.moveToNext()) {
                int total = cursor.getInt(cursor.getColumnIndex(HistoryData.GAIN));
                int burn = cursor.getInt(cursor.getColumnIndex(HistoryData.BURN));
                totalCals += total;
                totals.add(new Entry(++index, total));
                burns.add(new Entry(index, burn));
            }
            ((TextView)view.findViewById(R.id.step_title)).
                    setText("Your daily average cals intake is ");
            TextView stepView = (TextView)view.findViewById(R.id.step_value);
            if(index == 0) stepView.setText("0 cals");
            else stepView.setText("" + totalCals/index + " cals");
            if(totals.isEmpty()) return;
            if(totals.size() > 20) {
                totals = aggregatePoints(totals, 20);
                burns = aggregatePoints(burns, 20);
            }
            LineDataSet tDataSet = new LineDataSet(totals, "total calories");
            LineDataSet bDataSet = new LineDataSet(burns, "burned calories");
            LineData lineData = new LineData();
            lineData.addDataSet(tDataSet);
            lineData.addDataSet(bDataSet);
            lineData.setDrawValues(false);

            defaultLineSetup(tDataSet, R.color.graphBlue1);
            defaultLineSetup(bDataSet, R.color.graphOrange1);

            chart.setData(lineData);
        }
        chart.animateX(900);
        chart.setMarker(new MyMarkerView(getContext(), R.layout.marker_layout));
        chart.highlightValue(null);
        chart.invalidate();
        cursor.close();
    }

    public void defaultLineSetup(LineDataSet dataSet, int color) {
        dataSet.setLineWidth(3.5f);
        dataSet.setCircleRadius(5.0f);
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        dataSet.setCircleColor(ContextCompat.getColor(getContext(), color));
        dataSet.setColor(ContextCompat.getColor(getContext(), color));
    }

    public List<Entry> aggregatePoints(List<Entry> entries, int max) {
        List<Entry> ret = new ArrayList<>();
        int factor = (entries.size() - 1) / max + 1;
        for(int i = 0; i < entries.size(); i += factor) {
            int value = 0, count = 0;
            for( ; count < factor && i + count < entries.size(); ++count) {
                Entry entry = entries.get(i);
                value += entry.getY();
            }
            ret.add(new Entry(i, value/count));
        }
        return ret;
    }

    public int getPeriod(int checkedId) {
        if(checkedId == R.id.week_btn)
            return 7;
        else if(checkedId == R.id.month_btn)
            return 30;
        else if(checkedId == R.id.year_btn)
            return 365;
        return 7;
    }
}
