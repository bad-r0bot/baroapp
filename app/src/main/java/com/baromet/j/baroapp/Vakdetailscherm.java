package com.baromet.j.baroapp;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

public class Vakdetailscherm extends AppCompatActivity implements OnClickListener {

    private JSONObject jsonItem;
    private int classGrade;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vakdetailscherm);

        Intent intent = getIntent();

        String itemString = intent.getStringExtra("json_object");

        try {
            jsonItem = new JSONObject(itemString);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void fillData() {
        TextView classView = (TextView) findViewById(R.id.className);
        TextView periodView = (TextView) findViewById(R.id.classPeriod);
        TextView gradeView = (TextView) findViewById(R.id.classGrade);
        TextView ectsView = (TextView) findViewById(R.id.classEcts);

        try {
            classView.setText(jsonItem.getString("name"));
            periodView.setText(jsonItem.getString("period"));
            gradeView.setText(jsonItem.getString("grade"));
            ectsView.setText(jsonItem.getString("ects"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private boolean checkGrade() {
        DecimalFormat decimalGrade = new DecimalFormat("0.0");
        try {
            //get grade from text
            if (classGrade < 1 || classGrade > 10) {
                Log.d("ClassGrade", "Not between 1 and 10" + classGrade);
                return false;
            }
            return true;
        } catch (Exception e) {
            Log.d("EXCEPTION", "Incorrect number" + e.getMessage());
            return false;
        }
    }

    /*
    DecimalFormat form = new DecimalFormat("0.00");
    textView2.setText(form.format(result) );
    */
    //method to save grade

    public void clickHoofdscherm(View v) {
        //Go to hoofdscherm
        Button button9 = (Button) v;
        startActivity(new Intent(Vakdetailscherm.this, Hoofdscherm.class));
    }

    public void clickInvoerscherm(View v) {
        //Go to invoerscherm
        Button button10 = (Button) v;
        startActivity(new Intent(Vakdetailscherm.this, Invoerscherm.class));
    }

    public void clickOverzichtscherm(View v) {
        //Go to overzichtscherm
        Button button11 = (Button) v;
        startActivity(new Intent(Vakdetailscherm.this, Overzichtscherm.class));
    }

    public void clickVakdetailscherm(View v) {
        //Go to vakdetailscherm which can't happen because you are already here.
        Button button12 = (Button) v;
        startActivity(new Intent(Vakdetailscherm.this, Vakdetailscherm.class));
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Vakdetailscherm Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.baromet.j.baroapp/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
        fillData();
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Vakdetailscherm Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.baromet.j.baroapp/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
