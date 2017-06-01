package com.example.livia.iswint;

import android.content.Intent;
import android.support.v4.util.LogWriter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class account_activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
    }

    @Override
    public void onBackPressed(){
        Intent mLogIn = new Intent(account_activity.this, LogIn.class);
        startActivity(mLogIn);
    }

}
