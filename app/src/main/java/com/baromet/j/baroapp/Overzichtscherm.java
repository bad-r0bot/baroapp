package com.baromet.j.baroapp;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;

import adapters.JsonListAdapter;

public class Overzichtscherm extends AppCompatActivity implements OnClickListener {

    private ListView list;
    private String urljson;
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overzichtscherm);


        urljson = "http://www.fuujokan.nl/subject_lijst.json";


        vakkenlijst(list);
        Log.d("oncreate", "vakkenlijst is creating");
    }

    public void clickHoofdscherm(View v) {
        //Go to hoofdscherm
        Button button13 = (Button) v;
        startActivity(new Intent(Overzichtscherm.this, Hoofdscherm.class));
    }

    public void clickInvoerscherm(View v) {
        //Go to invoerscherm
        Button button14 = (Button) v;
        startActivity(new Intent(Overzichtscherm.this, Invoerscherm.class));
    }

    public void clickOverzichtscherm(View v) {
        //Go to overzichtscherm
        Button button15 = (Button) v;
        startActivity(new Intent(Overzichtscherm.this, Overzichtscherm.class));


    }

    public void clickVakdetailscherm(View v) {
        //Go to vakdetailscherm
        Button button16 = (Button) v;
        startActivity(new Intent(Overzichtscherm.this, Vakdetailscherm.class));
    }

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    URL requestUrl = "http://www.fuujokan.nl/subject_lijst.json";

    private void jsonTest(View )
    new AsyncTask<URL, Void, JSONObject>() {

        @Override
        protected Boolean doInBackground(URL requestUrl) {
            loadJSON(url);
        }

        @Override
        protected void onPostExecute(JSONObject jsonData) {
            try {
                // Getting Array of albums

                albums = json.getJSONArray(TAG_ALBUMS);
                sngs=json.getJSONArray(TAG_SONGS);
                // looping through All albums

                etc.
            }
        }.execute(requestUrl);


    public void loadJSON(URL url) {
        // Creating JSON Parser instance
        JSONParser jParser = new JSONParser();

        // getting JSON string from URL
        JSONObject json = jParser.getJSONFromUrl(url);

        return json;
    }





}
