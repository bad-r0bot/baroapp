package com.baromet.j.baroapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;

import database.DatabaseController;
import models.User;

public class Hoofdscherm extends AppCompatActivity implements OnClickListener, TextWatcher {

    TextView currentYear;
    TextView ectsContent;

    TextView studieAdvies;
    EditText nameField;

    Button loginButton;
    Button invoerButton;
    Button overzichtButton;

    DatabaseController dbc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.getBaseContext().deleteDatabase("barometer");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ectsContent = (TextView) findViewById(R.id.ectsContent);
        currentYear = (TextView) findViewById(R.id.currentYear);
        studieAdvies = (TextView) findViewById(R.id.studieAdvies);

        nameField = (EditText) findViewById(R.id.editText);
        nameField.addTextChangedListener(this);


        overzichtButton = (Button) findViewById(R.id.overzichtButton);
        overzichtButton.setEnabled(false);

        loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setEnabled(false);

        invoerButton = (Button) findViewById(R.id.invoerButton);
        invoerButton.setEnabled(false);

        dbc = new DatabaseController(getBaseContext());

        final Calendar c = Calendar.getInstance();
        int yy = c.get(Calendar.YEAR);

        // set current date into textview
        currentYear.setText(new StringBuilder()
                // Current year-1 to current year. eg 2015-2016
                .append(yy - 1).append(" ").append("-").append(yy));
        this.setTitle("BaroApp: Het is nu periode: " + dbc.getCurrentPeriod() + ", week: " + dbc.getCurrentWeek());

        if (isNetworkAvailable(this)) {
            // All good. Continue.
        } else {
            AlertDialog.Builder alertDiaglogBuilder = new AlertDialog.Builder(this);
            //set title
            alertDiaglogBuilder.setTitle("Geen internet");
            //set message
            alertDiaglogBuilder.setMessage("Er is geen internetbeschikbaar. De app zal niet werken.")
                    .setCancelable(true);
            alertDiaglogBuilder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // continue
                }
            })
            .show();
        }
    }

    public void clickHoofdscherm(View v) {
        //Go to hoofdscherm which can't happen because you are already here.
        //loginButton=(Button) v;
        startActivity(new Intent(Hoofdscherm.this, Hoofdscherm.class));
    }

    public void clickInvoerscherm(View v) {
        //Go to invoerscherm
        User user = dbc.getUserByName(nameField.getText().toString());
        Intent intent = new Intent(Hoofdscherm.this, Invoerscherm.class);

        //If user is unknown
        if (user == null) {
            dbc.storeUser(new User(nameField.getText().toString()));
            user = dbc.getUserByName(nameField.getText().toString());
        }

        intent.putExtra("userId", user.getId());
        startActivity(intent);
    }

    public void clickOverzichtscherm(View v) {
        //Go to invoerscherm
        User user = dbc.getUserByName(nameField.getText().toString());
        Intent intent = new Intent(Hoofdscherm.this, Overzichtscherm.class);

        //If user is unknown
        if (user == null) {
            dbc.storeUser(new User(nameField.getText().toString()));
            user = dbc.getUserByName(nameField.getText().toString());
        }

        intent.putExtra("userId", user.getId());
        startActivity(intent);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (nameField.getText().length() < 1) {
            invoerButton.setEnabled(false);
            overzichtButton.setEnabled(false);

        } else {
            invoerButton.setEnabled(true);
            overzichtButton.setEnabled(true);
        }

        User user = dbc.getUserByName(nameField.getText().toString());

        if (user != null) {
            ectsContent.setText("" + dbc.getTotalEcts(user));
            updateStudieAdvies(user);
        } else {
            ectsContent.setText(" Type een gebruikersnaam in.");
        }

    }

    private void updateStudieAdvies(User user) {
        int ects = dbc.getFailedEcts(user) + dbc.getMissedEcts(user);
        if (ects > 20) {
            studieAdvies.setText("Waarschuwing:Je krijgt een BSA!");
            studieAdvies.setTextColor(Color.RED);
        } else if (ects > 0 && ects <= 10) {
            studieAdvies.setText("Geen BSA. Je mag door naar het volgend jaar.");
            studieAdvies.setTextColor(Color.YELLOW);
        } else if (ects == 0) {
            studieAdvies.setText("Je hebt alle ECTS behaald!");
            studieAdvies.setTextColor(Color.GREEN);
        }
    }

    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
}
