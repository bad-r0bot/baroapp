package com.baromet.j.baroapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Hoofdscherm extends AppCompatActivity implements OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //
    }

    public void clickHoofdscherm(View v)
    {
        Button button=(Button) v;
    }

    public void clickInvoerscherm(View v)
    {
        Button button2=(Button) v;
    }

    public void clickOverzichtscherm(View v)
    {
        Button button3=(Button) v;
    }

    public void clickVakdetailscherm(View v)
    {
        Button button4=(Button) v;
    }

}
