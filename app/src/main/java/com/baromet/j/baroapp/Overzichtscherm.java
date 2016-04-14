package com.baromet.j.baroapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;

import java.util.ArrayList;
import java.util.List;

import database.DatabaseController;
import models.User;

public class Overzichtscherm extends AppCompatActivity implements OnClickListener {

    private PieChart mChart;
    public static final int MAX_ECTS = 60;
    public static int currentEcts = 0;
    private DatabaseController dbc;
    private User user;

    Button hoofdscherm;
    Button invoerButton;
    Button overzichtButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overzichtscherm);

        dbc = new DatabaseController(getBaseContext());
        user = dbc.getUser(getIntent().getExtras().getInt("userId"));


        mChart = (PieChart) findViewById(R.id.chart);
        mChart.setDescription("");
        mChart.setTouchEnabled(false);
        mChart.setDrawSliceText(true);
        mChart.getLegend().setEnabled(false);
        mChart.setTransparentCircleColor(Color.rgb(130, 130, 130));
        mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);

        setData(dbc.getTotalEcts(user), dbc.getFailedEcts(user));
        this.setTitle("Overzicht voor" + user.getName());

        overzichtButton = (Button) findViewById(R.id.overzichtButton);
        overzichtButton.setEnabled(false);

        hoofdscherm = (Button) findViewById(R.id.hoofdButton);
        hoofdscherm.setEnabled(true);

        invoerButton = (Button) findViewById(R.id.invoerButton);
        invoerButton.setEnabled(true);

    }
    private void setData(int passed, int failed) {
        ArrayList<Entry> yValues = new ArrayList<>();
        ArrayList<String> xValues = new ArrayList<>();

        int missing = (60 - passed) - failed;

        List<Integer> colors = new ArrayList<Integer>();

        yValues.add(new Entry(passed, 0));
        xValues.add("Passed ECTS");
        colors.add(Color.GREEN);

        yValues.add(new Entry(failed, 1));
        xValues.add("Failed ECTS");
        colors.add(Color.RED);

        if(missing > 0) {
            yValues.add(new Entry(missing, 2));
            xValues.add("Missing ECTS");
            colors.add(Color.YELLOW);
        }

        PieDataSet dataSet = new PieDataSet(yValues, "ECTS");
        dataSet.setColors(colors);

        PieData data = new PieData(xValues, dataSet);
        mChart.setData(data); // bind dataset aan chart.
        mChart.invalidate();  // Aanroepen van een redraw
        Log.d("aantal =", "" + currentEcts);
    }
    public void clickHoofdscherm(View v)
    {
        //Go to hoofdscherm
        Button button5=(Button) v;
        startActivity(new Intent(Overzichtscherm.this, Hoofdscherm.class));
    }

    public void clickInvoerscherm(View v)
    {
        //Go to invoerscherm which can't happen because you are already here.
        Button button6=(Button) v;
        startActivity(new Intent(Overzichtscherm.this, Invoerscherm.class));
    }

    public void clickOverzichtscherm(View v)
    {
        //Go to overzichtscherm
        Button button7=(Button) v;
        startActivity(new Intent(Overzichtscherm.this, Overzichtscherm.class));
    }

    public void clickVakdetailscherm(View v)
    {
        //Go to vakdetailscherm
        Button button8=(Button) v;
        startActivity(new Intent(Overzichtscherm.this, Vakdetailscherm.class));
    }
}
