package com.baromet.j.baroapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.app.ProgressDialog;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;

import adapters.JsonListAdapter;

public class Overzichtscherm extends AppCompatActivity implements OnClickListener {

    private ListView list;
    private String urljson;
    private GoogleApiClient client;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overzichtscherm);

        urljson = "http://www.fuujokan.nl/subject_lijst.json";

        //vakkenlijst(list);
        //Log.d("oncreate", "vakkenlijst is creating");

        new UpdateTask().execute(); //run updatetask

    }

    private class UpdateTask extends AsyncTask<String, Void ,JSONObject> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(Overzichtscherm.this,  "", "");
        }

        @Override
        protected JSONObject doInBackground(String... params) {

            String response;

            try {

                HttpClient httpclient = new DefaultHttpClient();

                HttpPost httppost = new HttpPost(urljson);
                //HttpPost httppost = new HttpPost(params[0]);
                // you can also pass it and get the Url here.

                HttpResponse responce = httpclient.execute(httppost);

                HttpEntity httpEntity = responce.getEntity();

                response = EntityUtils.toString(httpEntity);

                Log.d("response is", response);

                return new JSONObject(response);

            } catch (Exception e) {

///////////////////////////////////////
                //Hier loopt hij vast.
                e.printStackTrace();
                Log.d("AAAAGH", "Exceptopn\n" + e.getMessage());

            }

            return null;
        }

        @Override
        protected void onPostExecute(JSONObject result)
        {
            super.onPostExecute(result);

            progressDialog.dismiss();

        }
    }
/*
    private void vakkenlijst (View v){
        try {
            list.setAdapter(new JsonListAdapter(readJsonFromUrl(urljson)));
            Log.d("setAdapter", "Setting adapter through urljson");
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("AAAAGH", "IO Exceptopn while loading list\n" + e.getMessage());
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("AAAAGH", "JSONException while loading list\n" + e.getMessage());
        }

    }
*/

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







}
