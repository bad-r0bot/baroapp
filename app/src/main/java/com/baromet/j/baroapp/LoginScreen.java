package com.baromet.j.baroapp;

import android.content.Intent;
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

public class LoginScreen extends AppCompatActivity implements OnClickListener, TextWatcher{

    TextView currentYear;
    EditText nameField;

    Button loginButton;
    Button invoerButton;
    Button overzichtButton;

    DatabaseController dbc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currentYear = (TextView) findViewById(R.id.currentYear);
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
    }

    public void clickHoofdscherm(View v)    {
        //Go to hoofdscherm which can't happen because you are already here.
        //loginButton=(Button) v;
        startActivity(new Intent(LoginScreen.this, LoginScreen.class));
    }

    public void clickInvoerscherm(View v)
    {
        //Go to invoerscherm
        User user = dbc.getUserByName(nameField.getText().toString());
        Intent intent = new Intent(LoginScreen.this, Invoerscherm.class);

        //If user is unknown
        if(user==null) {
            dbc.storeUser(new User(nameField.getText().toString()));
            user = dbc.getUserByName(nameField.getText().toString());
        }


        intent.putExtra("userName", user.getName());
        intent.putExtra("userId", user.getId());
        startActivity(intent);
    }

    public void clickOverzichtscherm(View v)
    {
        //Go to overzichtscherm
       // overzichtButton=(Button) v;
        startActivity(new Intent(LoginScreen.this, Overzichtscherm.class));
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if(nameField.getText().length()<1){
            invoerButton.setEnabled(false);
            overzichtButton.setEnabled(false);
        }else{
            invoerButton.setEnabled(true);
            overzichtButton.setEnabled(true);
        }
    }
}
