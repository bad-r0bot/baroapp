package com.baromet.j.baroapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overzichtscherm);

        new UpdateTask().execute();

        urljson = "http://www.fuujokan.nl/subject_list.json";

        ListView list = (ListView) findViewById(R.id.vakkenlijst);

        vakkenlijst(list);
        Log.d("oncreate", "vakkenlijst is creating");
    }

    public void clickHoofdscherm(View v)
    {
        //Go to hoofdscherm
        Button button13=(Button) v;
        startActivity(new Intent(Overzichtscherm.this, Hoofdscherm.class));
    }

    public void clickInvoerscherm(View v)
    {
        //Go to invoerscherm
        Button button14=(Button) v;
        startActivity(new Intent(Overzichtscherm.this, Invoerscherm.class));
    }

    public void clickOverzichtscherm(View v)
    {
        //Go to overzichtscherm
        Button button15=(Button) v;
        startActivity(new Intent(Overzichtscherm.this, Overzichtscherm.class));


    }

    public void clickVakdetailscherm(View v)
    {
        //Go to vakdetailscherm
        Button button16=(Button) v;
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

    private class UpdateTask extends AsyncTask<String, String,String> {
        protected String doInBackground(String urljson) {

            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(urljson);

            HttpResponse httpResponse = null;
            try {
                httpResponse = httpClient.execute(httpPost);
            } catch (IOException e) {
                e.printStackTrace();
            }
            HttpEntity httpEntity = httpResponse.getEntity();
            try {
                InputStream is = httpEntity.getContent();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected String doInBackground(String... params) {
            return null;
        }
    }

    private JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONObject json = new JSONObject(jsonText);
            return json;
        } finally {
            is.close();
        }
    }

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

}
