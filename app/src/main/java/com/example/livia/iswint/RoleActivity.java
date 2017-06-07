package com.example.livia.iswint;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class RoleActivity extends AppCompatActivity {

    private ImageButton mPatricipantBtn;
    private ImageButton mOrganizerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_role);

        mPatricipantBtn = (ImageButton) findViewById(R.id.participantBtn);
        mOrganizerBtn = (ImageButton) findViewById(R.id.organizerBtn);

        mPatricipantBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(RoleActivity.this, RegisterActivity.class));
            }
        });



        mOrganizerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(RoleActivity.this, RegisterOrganizerActivity.class));
            }
        });

    }
}
