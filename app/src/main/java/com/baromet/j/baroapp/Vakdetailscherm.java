package com.baromet.j.baroapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Vakdetailscherm extends AppCompatActivity implements OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vakdetailscherm);
    }

    public void clickHoofdscherm(View v)
    {
        //Go to hoofdscherm
        Button button9=(Button) v;
        startActivity(new Intent(Vakdetailscherm.this, Hoofdscherm.class));
    }

    public void clickInvoerscherm(View v)
    {
        //Go to invoerscherm
        Button button10=(Button) v;
        startActivity(new Intent(Vakdetailscherm.this, Invoerscherm.class));
    }

    public void clickOverzichtscherm(View v)
    {
        //Go to overzichtscherm
            Button button11=(Button) v;
        startActivity(new Intent(Vakdetailscherm.this, Overzichtscherm.class));
    }

    public void clickVakdetailscherm(View v)
    {
        //Go to vakdetailscherm
        Button button12=(Button) v;
        startActivity(new Intent(Vakdetailscherm.this, Vakdetailscherm.class));
    }
}
