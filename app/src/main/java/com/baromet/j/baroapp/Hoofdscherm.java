package com.baromet.j.baroapp;

import android.content.Intent;
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
        //Go to hoofdscherm
        Button button=(Button) v;
        startActivity(new Intent(Hoofdscherm.this, Hoofdscherm.class));
    }

    public void clickInvoerscherm(View v)
    {
        //Go to invoerscherm
        Button button2=(Button) v;
        startActivity(new Intent(Hoofdscherm.this, Invoerscherm.class));
    }

    public void clickOverzichtscherm(View v)
    {
        //Go to overzichtscherm
        Button button3=(Button) v;
        startActivity(new Intent(Hoofdscherm.this, Overzichtscherm.class));
    }

    public void clickVakdetailscherm(View v)
    {
        //Go to vakdetailscherm
        Button button4=(Button) v;
        startActivity(new Intent(Hoofdscherm.this, Vakdetailscherm.class));
    }

}
