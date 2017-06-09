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

import static android.R.attr.country;

public class RegisterOrganizerActivity extends AppCompatActivity {

    private EditText mOrgNameField;
    private EditText mOrgEmailField;
    private EditText mOrgPhoneField;
    private EditText mOrgRoomField;
    private EditText mOrgPasswordField;
    private String mOrgType = "Organizer";
    private String mOrgCountry = "Romania";
    private Button mOrgRegisterBtn;

    private FirebaseAuth mAuth;

    private DatabaseReference mDatabase;

    private ProgressDialog mProgress;

    private String[] personalType = {"Female" , "Male"};
    private ArrayAdapter<String> adapterGenderType;
    private Spinner mOrgGenderSpinner;

    private String[] DepartmentType = {"Design", "DLH", "Enter&Wsh" ,"FR", "PR&IA"};
    private ArrayAdapter<String> adapterDepartmentType;
    private Spinner mOrgDepartmentSpinner;

    private Calendar myCalendar;
    private DatePickerDialog.OnDateSetListener date;
    private EditText mOrgBirthDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_organizer);



        mOrgNameField = (EditText) findViewById(R.id.organizer_nameField);
        mOrgEmailField = (EditText) findViewById(R.id.organizer_emailField);
        mOrgPhoneField = (EditText) findViewById(R.id.organizer_phoneField);
        mOrgRoomField = (EditText) findViewById(R.id.organizer_roomField);
        mOrgPasswordField = (EditText) findViewById(R.id.organizer_passwordField);

        mOrgRegisterBtn = (Button) findViewById(R.id.organizer_registerBtn);

        mAuth = FirebaseAuth.getInstance();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        mProgress = new ProgressDialog(this);

        mOrgBirthDate = (EditText) findViewById(R.id.organizer_datePicker);

        //Gender Spinner
        mOrgGenderSpinner = (Spinner) findViewById(R.id.organizer_genderSpinner);

        adapterGenderType = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, personalType);
        adapterGenderType = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, personalType);

        adapterGenderType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mOrgGenderSpinner.setAdapter(adapterGenderType);


        //Department Spinner
        mOrgDepartmentSpinner = (Spinner) findViewById(R.id.organizer_departmentSpinner);

        adapterDepartmentType = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, DepartmentType);
        adapterDepartmentType = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, DepartmentType);
        adapterDepartmentType = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, DepartmentType);
        adapterDepartmentType = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, DepartmentType);
        adapterDepartmentType = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, DepartmentType);

        adapterDepartmentType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mOrgDepartmentSpinner.setAdapter(adapterDepartmentType);

        //organizer.datePicker
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

        mOrgBirthDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(RegisterOrganizerActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        mOrgRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startOrgRegister();
            }
        });
    }

    private void startOrgRegister(){

        final String name = mOrgNameField.getText().toString().trim();
        final String email = mOrgEmailField.getText().toString().trim();
        final String phone = mOrgPhoneField.getText().toString().trim();
        final String birth = mOrgBirthDate.getText().toString().trim();
        final String gender = mOrgGenderSpinner.getSelectedItem().toString().trim();
        final String workshop = mOrgDepartmentSpinner.getSelectedItem().toString().trim();
        final String room = mOrgRoomField.getText().toString().trim();
        String password = mOrgPasswordField.getText().toString().trim();
        final String type = mOrgType.toString().trim();
        final String country = mOrgCountry.toString().trim();

        if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(phone) && !TextUtils.isEmpty(birth)
                && !TextUtils.isEmpty(gender) && !TextUtils.isEmpty(workshop) && !TextUtils.isEmpty(room)
                && !TextUtils.isEmpty(password)){

            mProgress.setMessage("Signin Up...");
            mProgress.show();


            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if(task.isSuccessful()) {

                        String user_id = mAuth.getCurrentUser().getUid();

                        DatabaseReference current_user_db = mDatabase.child(user_id);

                        current_user_db.child("name").setValue(name);
                        current_user_db.child("phone").setValue(phone);
                        current_user_db.child("birthdate").setValue(birth);
                        current_user_db.child("workshop").setValue(workshop);
                        current_user_db.child("room").setValue(room);
                        current_user_db.child("gender").setValue(gender);
                        current_user_db.child("type").setValue(type);
                        current_user_db.child("country").setValue(country);
                        current_user_db.child("email").setValue(email);
                        current_user_db.child("image").setValue("default");

                        mProgress.dismiss();

                        Intent mainIntent = new Intent(RegisterOrganizerActivity.this, BlogActivity.class);
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

        mOrgBirthDate.setText(sdf.format(myCalendar.getTime()));
    }


}
