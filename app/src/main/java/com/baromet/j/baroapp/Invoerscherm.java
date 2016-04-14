 package com.baromet.j.baroapp;

 import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Reader;

import adapters.JsonListAdapter;
import database.DatabaseController;
import models.Course;
import models.User;

 public class Invoerscherm extends AppCompatActivity implements OnClickListener {

    private ListView list;
    private String urljson;
    private GoogleApiClient client;
    private ProgressDialog progressDialog;
    private JSONArray itemArray;
    private String listname;
    private Context context;
    private DatabaseController dbc;
    private User user;
    private Long starttime;

    private GoogleApiClient client2;

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoer_scherm);

        urljson = "http://www.fuujokan.nl/subject_lijst.json";

        itemArray = null;
        listname = null;
        list = (ListView) this.findViewById(R.id.vakkenlijst);

        starttime = System.currentTimeMillis();
        Log.d("Progress :::: ", "CURRENT TIME: " + starttime);

        dbc = new DatabaseController(getBaseContext());
        user = dbc.getUser(getIntent().getExtras().getInt("userId"));

        if(!dbc.attendenceIsInitialisedForUser(user)) {
            dbc.initAttendance(user);
        }

        setTitle(user.getName()+" "+ user.getId());

        context = this;

        new UpdateTask().execute(); //run updatetask

        client2 = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Vakdetailscherm vakdetailscherm = new Vakdetailscherm();

                Intent intent = new Intent(context, Vakdetailscherm.class);

                JSONObject message = (JSONObject) list.getItemAtPosition(position);

                intent.putExtra("json_object", message.toString());
                intent.putExtra("userId", user.getId());
                startActivity(intent);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();


        client2.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW,
                "Invoerscherm Page",
                Uri.parse("http://host/path"),

                Uri.parse("android-app://com.baromet.j.baroapp/http/host/path")
        );
        AppIndex.AppIndexApi.start(client2, viewAction);
    }




    private void fillList() {

        listname = "name";
        list.setAdapter(new JsonListAdapter(itemArray, this, this, listname));

    }


    public void clickHoofdscherm(View v) {
        //Go to hoofdscherm
        Button button13 = (Button) v;
        startActivity(new Intent(Invoerscherm.this, Hoofdscherm.class));
    }

    public void clickInvoerscherm(View v) {
        //Go to invoerscherm
        Button button14 = (Button) v;
        startActivity(new Intent(Invoerscherm.this, Invoerscherm.class));
    }

    public void clickOverzichtscherm(View v) {
        Intent intent = new Intent(Invoerscherm.this, Overzichtscherm.class);
        intent.putExtra("userId", user.getId());
        startActivity(intent);
    }

    public void clickVakdetailscherm(View v) {
        //Go to vakdetailscherm
        Button button16 = (Button) v;
        startActivity(new Intent(Invoerscherm.this, Vakdetailscherm.class));
    }

    private class UpdateTask extends AsyncTask<String, Void, JSONArray> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("Progress :::: ", "onPreExecute start time: " + ((System.currentTimeMillis() - starttime)) + " milliseconds");

            progressDialog = ProgressDialog.show(Invoerscherm.this, "", "");
            Log.d("Progress :::: ", "onPreExecute elapsed time: " + ((System.currentTimeMillis() - starttime)) + " milliseconds");
        }

        @Override
        protected JSONArray doInBackground(String... params) {

            String response;

            Log.d("Progress :::: ", "doInBackground start time: " + ((System.currentTimeMillis() - starttime)) + " milliseconds");

            try {

                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(urljson);
                HttpResponse responce = httpclient.execute(httppost);
                HttpEntity httpEntity = responce.getEntity();

                response = EntityUtils.toString(httpEntity);

                Log.d("Progress :::: ", "TRY jon elapsed time: " + ((System.currentTimeMillis() - starttime)) + " milliseconds");

                return new JSONArray(response);

            } catch (Exception e) {
                e.printStackTrace();
                Log.d("Exception", "Exception\n" + e.getMessage());
                Log.d("Progress :::: ", "TRY jon CATCH elapsed time: " + ((System.currentTimeMillis() - starttime)) + " milliseconds");


            }

            return null;
        }

        @Override
        protected void onPostExecute(JSONArray result){
            super.onPostExecute(result);

            itemArray = result;
            dbc.truncateCourses();
            Log.d("Progress :::: ", "onPostExecute start time: " + ((System.currentTimeMillis() - starttime)) + " milliseconds");
            Course first =null;
            for(int i = 0; i < result.length(); i++){
                Log.d("Progress :::: ", "LOOP onPostExecute: " + (((System.currentTimeMillis() - starttime)))  + " milliseconds");
                try {

                    first = new Course(result.getJSONObject(0));
                    Course c = new Course(result.getJSONObject(i));
                    if(dbc.getCourseByName(c.getCourseName()) == null) {
                        Log.d("Progress :::: ", "LOOP IF onPostExecute: " + (((System.currentTimeMillis() - starttime)))  + " milliseconds");
                        dbc.storeCourse(c);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

        }
        fillList();
        progressDialog.dismiss();

        Log.d("Progress :::: ", "onPostExecute elapsed time: " + ((System.currentTimeMillis() - starttime)) + " milliseconds");


        }
    }

     @Override
     public void onStop() {
         super.onStop();
         Log.d("Progress :::: ", "onStop start time: " + (((System.currentTimeMillis() - starttime)))  + " milliseconds");
         Action viewAction = Action.newAction(
                 Action.TYPE_VIEW,
                 "Invoerscherm Page",
                 Uri.parse("http://host/path"),

                 Uri.parse("android-app://com.baromet.j.baroapp/http/host/path")
         );
         AppIndex.AppIndexApi.end(client2, viewAction);
         client2.disconnect();
         Log.d("Progress :::: ", "onStop elapsed time: " + ((System.currentTimeMillis() - starttime)) + " milliseconds");
     }

}
