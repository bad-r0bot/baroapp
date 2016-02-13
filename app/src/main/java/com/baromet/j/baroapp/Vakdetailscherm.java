package com.baromet.j.baroapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import java.text.DecimalFormat;

public class Vakdetailscherm extends AppCompatActivity implements OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vakdetailscherm);
    }

    //method to fill data based on clicked item

    // method to check grade
    private boolean checkGrade(){
        DecimalFormat decimalGrade = new DecimalFormat("0.0");
        try{
            //get grade from text
            if (classGrade > 10 || classGrade < 1) {
                return false;
            }
            return true;
        }catch(Exception e){
            Log.d("EXCEPTION", "Incorrect number" + e.getMessage());
        }

    }
    /*
    DecimalFormat form = new DecimalFormat("0.00");
    textView2.setText(form.format(result) );
    */

    //method to save grade


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
        //Go to vakdetailscherm which can't happen because you are already here.
        Button button12=(Button) v;
        startActivity(new Intent(Vakdetailscherm.this, Vakdetailscherm.class));
    }
}
