package blueangels.com.layouts;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatSpinner;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ScrollView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import blueangels.com.layouts.Validation.Validation;

public class RegisterPasswordActivity extends AppCompatActivity {

    private String[] passOutYear;
    private ScrollView scrollView;
    private AppCompatEditText passwordEditText, confirmPasswordEditText, mobileNumberEditText;
    private AppCompatAutoCompleteTextView locationEditText;
    private TextInputLayout inputLayoutPassword, inputLayoutConfirmPassword, inputLayoutMobileNumber, inputLayoutLocation;
    private AppCompatSpinner industrySpinnerOne, industrySpinnerTwo, industrySpinnerThree, companySpinnerOne, companySpinnerTwo, companySpinnerThree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null)
            getSupportActionBar().hide();
        setContentView(R.layout.activity_register_password);

        addressingView();

        addingListener();

        passOutYear = getResources().getStringArray(R.array.year_arrays);

        settingPassOutYearSpinner(passOutYear);
    }

    private void settingPassOutYearSpinner(String[] passOutYear) {

        List<String> yearList = new ArrayList<String>();

        Collections.addAll(yearList, passOutYear);

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item_custom, yearList);
        industrySpinnerOne.setPrompt("  -- Select the Passout Year -- ");
        industrySpinnerOne.setAdapter(spinnerArrayAdapter);

        companySpinnerOne.setPrompt("  -- Select the Passout Year -- ");
        companySpinnerOne.setAdapter(spinnerArrayAdapter);

        industrySpinnerTwo.setPrompt("  -- Select the Passout Year -- ");
        industrySpinnerTwo.setAdapter(spinnerArrayAdapter);

        companySpinnerTwo.setPrompt("  -- Select the Passout Year -- ");

        industrySpinnerThree.setPrompt("  -- Select the Passout Year -- ");
        industrySpinnerThree.setAdapter(spinnerArrayAdapter);

        companySpinnerThree.setPrompt("  -- Select the Passout Year -- ");
        companySpinnerThree.setAdapter(spinnerArrayAdapter);


    }

    private void addressingView() {
        passwordEditText = (AppCompatEditText) findViewById(R.id.editViewPassword);
        confirmPasswordEditText = (AppCompatEditText) findViewById(R.id.editViewConfirmPassword);
        mobileNumberEditText = (AppCompatEditText) findViewById(R.id.editViewMobileNumber);
        locationEditText = (AppCompatAutoCompleteTextView) findViewById(R.id.editViewLocation);

        industrySpinnerOne = (AppCompatSpinner) findViewById(R.id.industry_spinner1);
        industrySpinnerTwo = (AppCompatSpinner) findViewById(R.id.industry_spinner2);
        industrySpinnerThree = (AppCompatSpinner) findViewById(R.id.industry_spinner3);

        companySpinnerOne = (AppCompatSpinner) findViewById(R.id.company_spinner1);
        companySpinnerTwo = (AppCompatSpinner) findViewById(R.id.company_spinner2);
        companySpinnerThree = (AppCompatSpinner) findViewById(R.id.company_spinner3);


        inputLayoutPassword = (TextInputLayout) findViewById(R.id.ViewPasswordTextInputLayout);
        inputLayoutConfirmPassword = (TextInputLayout) findViewById(R.id.ViewConfirmPasswordTextInputLayout);
        inputLayoutMobileNumber = (TextInputLayout) findViewById(R.id.ViewMobileNumberTextInputLayout);
        inputLayoutLocation = (TextInputLayout) findViewById(R.id.ViewLocationTextInputLayout);

        scrollView = (ScrollView) findViewById(R.id.scroll_view_activity_register_password);
        scrollView.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
        scrollView.setFocusable(true);
        scrollView.setFocusableInTouchMode(true);

    }

    private void addingListener() {
        passwordEditText.addTextChangedListener(new CustomWatcher(passwordEditText));
        confirmPasswordEditText.addTextChangedListener(new CustomWatcher(confirmPasswordEditText));
        mobileNumberEditText.addTextChangedListener(new CustomWatcher(mobileNumberEditText));
        locationEditText.addTextChangedListener(new CustomWatcher(locationEditText));

        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.requestFocusFromTouch();
                return false;
            }
        });
    }


    public void RegistrationSuccess(View view) {

        finalRegistrationSuccess();

    }

    private void finalRegistrationSuccess() {
        if (!Validation.validatePassword(passwordEditText, inputLayoutPassword, RegisterPasswordActivity.this)) {
            return;
        }
        if (!Validation.validatePassword(confirmPasswordEditText, inputLayoutConfirmPassword, RegisterPasswordActivity.this)) {
            return;
        }
        if (!Validation.validatePasswordConfirmPassword(passwordEditText, confirmPasswordEditText, inputLayoutConfirmPassword, RegisterPasswordActivity.this)) {
            return;
        }
        if (!Validation.validateMobileNumber(mobileNumberEditText, inputLayoutMobileNumber, RegisterPasswordActivity.this)) {
            return;
        }
        if (!Validation.validateLocation(locationEditText, inputLayoutLocation, RegisterPasswordActivity.this)) {
            return;
        }

        Toast.makeText(getApplicationContext(), "Registration Successfully", Toast.LENGTH_SHORT).show();

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
                case R.id.editTextPassword:
                    Validation.validatePassword(passwordEditText, inputLayoutPassword, RegisterPasswordActivity.this);
                    break;
                case R.id.editViewConfirmPassword:
                    Validation.validatePassword(confirmPasswordEditText, inputLayoutConfirmPassword, RegisterPasswordActivity.this);
                    break;
                case R.id.editViewMobileNumber:
                    Validation.validateMobileNumber(mobileNumberEditText, inputLayoutMobileNumber, RegisterPasswordActivity.this);
                    break;
                case R.id.editViewLocation:
                    Validation.validateLocation(locationEditText, inputLayoutLocation, RegisterPasswordActivity.this);
                    break;
            }
        }
    }
}
