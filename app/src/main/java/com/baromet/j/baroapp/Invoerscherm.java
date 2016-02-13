package com.baromet.j.baroapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Invoerscherm extends AppCompatActivity implements OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoerscherm);
    }

    public void clickHoofdscherm(View v)
    {
        //Go to hoofdscherm
        Button button5=(Button) v;
        startActivity(new Intent(Invoerscherm.this, Hoofdscherm.class));
    }

    public void clickInvoerscherm(View v)
    {
        //Go to invoerscherm which can't happen because you are already here.
        Button button6=(Button) v;
        startActivity(new Intent(Invoerscherm.this, Invoerscherm.class));
    }

    public void clickOverzichtscherm(View v)
    {
        //Go to overzichtscherm
        Button button7=(Button) v;
        startActivity(new Intent(Invoerscherm.this, Overzichtscherm.class));
    }

    public void clickVakdetailscherm(View v)
    {
        //Go to vakdetailscherm
        Button button8=(Button) v;
        startActivity(new Intent(Invoerscherm.this, Vakdetailscherm.class));
    }
}
