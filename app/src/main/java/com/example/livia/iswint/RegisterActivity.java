package com.example.livia.iswint;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private EditText mNameField;
    private EditText mEmailField;
    private EditText mCountryField;
    private EditText mAgeField;
    private EditText mWorkshopField;
    private EditText mRoomField;
    private EditText mPhoneField;
    private EditText mPasswordField;

    private Button mRegisterBtn;

    private FirebaseAuth mAuth;

    private DatabaseReference mDatabase;

    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mNameField =(EditText) findViewById(R.id.nameField);
        mEmailField = (EditText) findViewById(R.id.emailField);
        mCountryField = (EditText) findViewById(R.id.countryField);
        mAgeField = (EditText) findViewById(R.id.ageField);
        mWorkshopField = (EditText) findViewById(R.id.workshopField);
        mRoomField =(EditText) findViewById(R.id.roomField);
        mPhoneField = (EditText) findViewById(R.id.phoneField);
        mPasswordField = (EditText) findViewById(R.id.passField);

        mRegisterBtn = (Button) findViewById(R.id.registerBtn);

        mAuth = FirebaseAuth.getInstance();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Participants");

        mProgress = new ProgressDialog(this);

        mRegisterBtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View View){

                startRegister();
            }
        });
    }

    private  void startRegister(){

        final String name = mNameField.getText().toString().trim();
        String email = mEmailField.getText().toString().trim();
        final String country = mCountryField.getText().toString().trim();
        final String phone = mPhoneField.getText().toString().trim();
        final String age = mAgeField.getText().toString().trim();
        final String workshop = mWorkshopField.getText().toString().trim();
        final String room = mRoomField.getText().toString().trim();
        String password = mPasswordField.getText().toString().trim();

        if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(country) && !TextUtils.isEmpty(phone)
                && !TextUtils.isEmpty(age) && !TextUtils.isEmpty(workshop) && !TextUtils.isEmpty(room)
                && !TextUtils.isEmpty(password)) {

            mProgress.setMessage("Signin Up...");
            mProgress.show();

            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if(task.isSuccessful()){

                        String user_id = mAuth.getCurrentUser().getUid();

                        DatabaseReference current_user_db = mDatabase.child(user_id);

                        current_user_db.child("name").setValue(name);
                        current_user_db.child("country").setValue(country);
                        current_user_db.child("phone").setValue(phone);
                        current_user_db.child("age").setValue(age);
                        current_user_db.child("workshop").setValue(workshop);
                        current_user_db.child("room").setValue(room);
                        current_user_db.child("image").setValue("default");

                        mProgress.dismiss();

                        Intent mainIntent = new Intent(RegisterActivity.this, BlogActivity.class);
                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(mainIntent);

                    }

                }
            });


        }

    }

}
