package com.baromet.j.baroapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.gms.common.api.GoogleApiClient;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.Reader;

import adapters.JsonListAdapter;

public class Overzichtscherm extends AppCompatActivity implements OnClickListener {

    public ListView list;
    private String urljson;
    private GoogleApiClient client;
    private ProgressDialog progressDialog;
    private JSONArray itemArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overzichtscherm);

        urljson = "http://www.fuujokan.nl/subject_lijst.json";

        itemArray = null;
        list = (ListView) this.findViewById(R.id.vakkenlijst);


        new UpdateTask().execute(); //run updatetask

    }

    private class UpdateTask extends AsyncTask<String, Void, JSONArray> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(Overzichtscherm.this,  "", "");
        }

        @Override
        protected JSONArray doInBackground(String... params) {

            String response;

            try {

                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(urljson);
                HttpResponse responce = httpclient.execute(httppost);
                HttpEntity httpEntity = responce.getEntity();

                response = EntityUtils.toString(httpEntity);

                Log.d("response is", response);

                return new JSONArray(response);

            } catch (Exception e) {
                e.printStackTrace();
                Log.d("AAAAGH", "Exception\n" + e.getMessage());

            }

            return null;
        }

        @Override
        protected void onPostExecute(JSONArray result)
        {
            super.onPostExecute(result);
            itemArray = result;
            fillList();
            progressDialog.dismiss();

        }
    }
    private void fillList (){
            list.setAdapter(new JsonListAdapter(itemArray));
            Log.d("setAdapter", "Setting adapter through urljson");
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







}
