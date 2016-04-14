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
    private User user ;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoer_scherm);

        urljson = "http://www.fuujokan.nl/subject_lijst.json";

        itemArray = null;
        listname = null;
        list = (ListView) this.findViewById(R.id.vakkenlijst);


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

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client2.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Invoerscherm Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.baromet.j.baroapp/http/host/path")
        );
        AppIndex.AppIndexApi.start(client2, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Invoerscherm Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.baromet.j.baroapp/http/host/path")
        );
        AppIndex.AppIndexApi.end(client2, viewAction);
        client2.disconnect();
    }

    private class UpdateTask extends AsyncTask<String, Void, JSONArray> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(Invoerscherm.this, "", "");
            Log.d("Progress START", "Starting progress dialog.");
        }

        @Override
        protected JSONArray doInBackground(String... params) {

            String response;
            Log.d("Progress MID", "Starting JSON http call.");

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
                Log.d("Exception", "Exception\n" + e.getMessage());

            }
            Log.d("Progress MID", "Exiting JSON http call.");

            return null;
        }

        @Override
        protected void onPostExecute(JSONArray result){
            super.onPostExecute(result);
            itemArray = result;
            dbc.truncateCourses();
            Course first =null;
            for(int i = 0; i < result.length(); i++){
                try {

                    first = new Course(result.getJSONObject(0));
                    Course c = new Course(result.getJSONObject(i));
                    if(dbc.getCourseByName(c.getCourseName()) == null) {
                        dbc.storeCourse(c);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

        }
        fillList();
        progressDialog.dismiss();
        Log.d("Progress END", "Ending progress dialog.");

        }
    }

    /*
    private void saveClass{
        Somehow save this in a database?
    }
    */

    private void fillList() {
        Log.d("Fill List", "Filling list of classes.");
        listname = "name";
        list.setAdapter(new JsonListAdapter(itemArray, this, this, listname));
        Log.d("setAdapter", "Setting adapter through urljson");
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

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }


}
