package blueangels.com.layouts;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatSpinner;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import blueangels.com.layouts.Validation.Validation;

/**
 * Created by Admin on 5/2/2017.
 */

public class RegisterActivity extends AppCompatActivity {

    private String[] passOutYear;
    private ScrollView scrollView;
    private AppCompatEditText nameEditText, emailEditText;
    private AppCompatAutoCompleteTextView collegeEditText, departmentEditText;
    private AppCompatSpinner passOutYearSpinner;
    private TextInputLayout inputLayoutName, inputLayoutEmail, inputLayoutCollege, inputLayoutDepartment;
    private Button registerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getSupportActionBar() != null)
            getSupportActionBar().hide();
        setContentView(R.layout.activity_registration);

        addressingView();

        addingListener();

        passOutYear = getResources().getStringArray(R.array.year_arrays);

        settingPassOutYearSpinner(passOutYear);


    }

    private void settingPassOutYearSpinner(String[] passOutYear) {

        List<String> yearList = new ArrayList<String>();

        Collections.addAll(yearList, passOutYear);

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item_custom, yearList);
        passOutYearSpinner.setPrompt("  -- Select the Passout Year -- ");
        passOutYearSpinner.setAdapter(spinnerArrayAdapter);

    }

    private void addressingView() {

        nameEditText = (AppCompatEditText) findViewById(R.id.editViewName);
        emailEditText = (AppCompatEditText) findViewById(R.id.editViewEmail);
        collegeEditText = (AppCompatAutoCompleteTextView) findViewById(R.id.editViewCollegeName);
        departmentEditText = (AppCompatAutoCompleteTextView) findViewById(R.id.editViewDepartment);

        passOutYearSpinner = (AppCompatSpinner) findViewById(R.id.passed_out_year_spinner);

        inputLayoutName = (TextInputLayout) findViewById(R.id.name);
        inputLayoutEmail = (TextInputLayout) findViewById(R.id.emailAddress);
        inputLayoutCollege = (TextInputLayout) findViewById(R.id.viewCollege);
        inputLayoutDepartment = (TextInputLayout) findViewById(R.id.viewDepartment);

        scrollView = (ScrollView) findViewById(R.id.scroll_view_activity_register);
        scrollView.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
        scrollView.setFocusable(true);
        scrollView.setFocusableInTouchMode(true);

    }


    private void addingListener() {
        nameEditText.addTextChangedListener(new CustomWatcher(nameEditText));
        emailEditText.addTextChangedListener(new CustomWatcher(emailEditText));
        collegeEditText.addTextChangedListener(new CustomWatcher(collegeEditText));
        departmentEditText.addTextChangedListener(new CustomWatcher(departmentEditText));


        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.requestFocusFromTouch();
                return false;
            }
        });

        passOutYearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    //
                    setSpinnerError(passOutYearSpinner,"Field can't be empty");
                }else {
                    // Your code to process the selection
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void setSpinnerError(Spinner spinner, String error){
        View selectedView = spinner.getSelectedView();
        if (selectedView != null && selectedView instanceof TextView) {
            spinner.requestFocus();
            TextView selectedTextView = (TextView) selectedView;
            selectedTextView.setError("error"); // any name of the error will do
            selectedTextView.setTextColor(ContextCompat.getColor(RegisterActivity.this,R.color.colorAccent)); //text color in which you want your error message to be displayed
            selectedTextView.setText(error); // actual error message
            spinner.performClick(); // to open the spinner list if error is found.
        }
    }


    public void registerNew(View view) {

        submitRegistrationDetails();

    }

    private void submitRegistrationDetails() {
        if (!Validation.validateName(nameEditText, inputLayoutName, RegisterActivity.this)) {
            return;
        }

        if (!Validation.validateEmail(emailEditText, inputLayoutEmail, RegisterActivity.this)) {
            return;
        }

        if (!Validation.validateCollege(collegeEditText, inputLayoutCollege, RegisterActivity.this)) {
            return;
        }

        if (!Validation.validateDepartment(departmentEditText, inputLayoutDepartment, RegisterActivity.this)) {
            return;
        }

        Intent registrationCompleteIntent = new Intent(RegisterActivity.this, RegisterPasswordActivity.class);
        startActivity(registrationCompleteIntent);
    }


    private class CustomWatcher implements TextWatcher {

        private View view;

        CustomWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.editViewName:
                    Validation.validateName(nameEditText, inputLayoutName, RegisterActivity.this);
                    break;
                case R.id.editViewEmail:
                    Validation.validateEmail(emailEditText, inputLayoutEmail, RegisterActivity.this);
                    break;
                case R.id.editViewCollegeName:
                    Validation.validateCollege(collegeEditText, inputLayoutCollege, RegisterActivity.this);
                    break;
                case R.id.editViewDepartment:
                    Validation.validateDepartment(departmentEditText, inputLayoutDepartment, RegisterActivity.this);
                    break;
            }
        }
    }

}