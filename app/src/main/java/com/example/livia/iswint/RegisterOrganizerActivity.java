package com.example.livia.iswint;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class RegisterOrganizerActivity extends AppCompatActivity {

    private String[] personalType = {"Female" , "Male"};
    private ArrayAdapter<String> adapterGenderType;
    private Spinner mGenderSpinner;

    private String[] DepartmentType = {"Design", "DLH", "Enter&Wsh" ,"FR", "PR&IA"};
    private ArrayAdapter<String> adapterDepartmentType;
    private Spinner mDepartmentSpinner;

    private Calendar myCalendar;
    private DatePickerDialog.OnDateSetListener date;
    private EditText mBirthDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_organizer);

        mBirthDate = (EditText) findViewById(R.id.datePicker);

        //Gender Spinner
        mGenderSpinner = (Spinner) findViewById(R.id.organizer_genderSpinner);

        adapterGenderType = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, personalType);
        adapterGenderType = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, personalType);

        adapterGenderType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mGenderSpinner.setAdapter(adapterGenderType);


        //Department Spinner
        mDepartmentSpinner = (Spinner) findViewById(R.id.organizer_departamentSpinner);

        adapterDepartmentType = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, personalType);
        adapterDepartmentType = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, personalType);

        adapterDepartmentType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mDepartmentSpinner.setAdapter(adapterDepartmentType);

        myCalendar = Calendar.getInstance();

        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                myCalendar.set(Calendar.YEAR, i);
                myCalendar.set(Calendar.MONTH, i1);
                myCalendar.set(Calendar.DAY_OF_MONTH, i2);
                updateLabel();
            }


        };

        mBirthDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(RegisterOrganizerActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void updateLabel() {

        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        mBirthDate.setText(sdf.format(myCalendar.getTime()));
    }

}
