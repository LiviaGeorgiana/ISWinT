package com.example.livia.iswint;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class RegisterActivity extends AppCompatActivity {

    private EditText mNameField;
    private EditText mEmailField;
    private EditText mCountryField;
    private EditText mRoomField;
    private EditText mPhoneField;
    private EditText mPasswordField;
    private String mType = "Participant";

    //gender spinner
    private String[] personalType = {"Female" , "Male"};
    private ArrayAdapter<String> adapterGenderType;
    private Spinner mGenderSpinner;

    //wsh spinner
    private String[] WorkshopType = {"Improv Theater",
            "Youth's Startup",
            "Project Solutions",
            "Speaking Mastery",
            "Define a leader",
            "Intro to Marketing",
            "Debate the world",
            "Graphic Design",
            "Capture the moment",
            "Multicultural kitchen"};
    private ArrayAdapter<String> adapterWorkshopType;
    private Spinner mWorkshopSpinner;

    private Calendar myCalendar;
    private DatePickerDialog.OnDateSetListener date;
    private EditText mBirthDateField;

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
        mRoomField =(EditText) findViewById(R.id.roomField);
        mPhoneField = (EditText) findViewById(R.id.phoneField);
        mPasswordField = (EditText) findViewById(R.id.passField);

        mRegisterBtn = (Button) findViewById(R.id.registerBtn);

        mAuth = FirebaseAuth.getInstance();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        mProgress = new ProgressDialog(this);

        mBirthDateField = (EditText) findViewById(R.id.birthDateField);

        //Gender Spinner
        mGenderSpinner = (Spinner) findViewById(R.id.genderSpinner);

        adapterGenderType = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, personalType);
        adapterGenderType = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, personalType);

        adapterGenderType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mGenderSpinner.setAdapter(adapterGenderType);

        //Department Spinner
        mWorkshopSpinner = (Spinner) findViewById(R.id.workshopSpinner);

        adapterWorkshopType = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, WorkshopType);
        adapterWorkshopType = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, WorkshopType);
        adapterWorkshopType = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, WorkshopType);
        adapterWorkshopType = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, WorkshopType);
        adapterWorkshopType = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, WorkshopType);
        adapterWorkshopType = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, WorkshopType);
        adapterWorkshopType = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, WorkshopType);
        adapterWorkshopType = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, WorkshopType);
        adapterWorkshopType = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, WorkshopType);
        adapterWorkshopType = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, WorkshopType);


        adapterWorkshopType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mWorkshopSpinner.setAdapter(adapterWorkshopType);

        //birth date
        myCalendar = Calendar.getInstance();

        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker organizer_datePicker, int i, int i1, int i2) {
                myCalendar.set(Calendar.YEAR, i);
                myCalendar.set(Calendar.MONTH, i1);
                myCalendar.set(Calendar.DAY_OF_MONTH, i2);
                updateLabel();
            }


        };

        mBirthDateField.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(RegisterActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        mRegisterBtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View View){

                startRegister();
            }
        });
    }

    private  void startRegister(){

        final String name = mNameField.getText().toString().trim();
        final String email = mEmailField.getText().toString().trim();
        final String country = mCountryField.getText().toString().trim();
        final String phone = mPhoneField.getText().toString().trim();
        final String birthdate = mBirthDateField.getText().toString().trim();
        final String workshop = mWorkshopSpinner.getSelectedItem().toString().trim();
        final String room = mRoomField.getText().toString().trim();
        String password = mPasswordField.getText().toString().trim();
        final String gender = mGenderSpinner.getSelectedItem().toString().trim();
        final String type = mType.toString().trim();

        if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(country) && !TextUtils.isEmpty(phone)
                && !TextUtils.isEmpty(birthdate) && !TextUtils.isEmpty(workshop) && !TextUtils.isEmpty(room)
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
                        current_user_db.child("birthdate").setValue(birthdate);
                        current_user_db.child("workshop").setValue(workshop);
                        current_user_db.child("room").setValue(room);
                        current_user_db.child("gender").setValue(gender);
                        current_user_db.child("type").setValue(type);
                        current_user_db.child("email").setValue(email);
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

    private void updateLabel() {

        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        mBirthDateField.setText(sdf.format(myCalendar.getTime()));
    }

}
