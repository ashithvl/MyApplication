package blueangels.com.layouts.Validation;

import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatSpinner;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import blueangels.com.layouts.R;
import blueangels.com.layouts.RegisterActivity;

/**
 * Created by Ashith VL on 5/6/2017.
 */

public class Validation {

    public static boolean validateName(AppCompatEditText nameEditText, TextInputLayout inputLayoutName, RegisterActivity registerActivity) {
        if (nameEditText.getText().toString().trim().isEmpty() || nameEditText.getText().length() < 3) {
            inputLayoutName.setError(registerActivity.getString(R.string.err_msg_name));
            requestFocus(nameEditText, registerActivity);
            return false;
        } else {
            inputLayoutName.setErrorEnabled(false);
        }

        return true;
    }

    public static boolean validateEmail(AppCompatEditText emailEditText, TextInputLayout inputLayoutEmail, RegisterActivity registerActivity) {
        String email = emailEditText.getText().toString().trim();

        if (email.isEmpty() || !isValidEmail(email)) {
            inputLayoutEmail.setError(registerActivity.getString(R.string.err_msg_email));
            requestFocus(emailEditText, registerActivity);
            return false;
        } else {
            inputLayoutEmail.setErrorEnabled(false);
        }

        return true;
    }

    public static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean validateCollege(AppCompatAutoCompleteTextView collegeEditText, TextInputLayout inputLayoutCollege, RegisterActivity registerActivity) {
        if (collegeEditText.getText().toString().trim().isEmpty() || collegeEditText.getText().length() < 3) {
            inputLayoutCollege.setError(registerActivity.getString(R.string.err_msg_college));
            requestFocus(collegeEditText, registerActivity);
            return false;
        } else {
            inputLayoutCollege.setErrorEnabled(false);
        }

        return true;
    }

    public static boolean validateDepartment(AppCompatAutoCompleteTextView departmentEditText, TextInputLayout inputLayoutDepartment, RegisterActivity registerActivity) {
        if (departmentEditText.getText().toString().trim().isEmpty() || departmentEditText.getText().length() < 3) {
            inputLayoutDepartment.setError(registerActivity.getString(R.string.err_msg_departmrnt));
            requestFocus(departmentEditText, registerActivity);
            return false;
        } else {
            inputLayoutDepartment.setErrorEnabled(false);
        }

        return true;
    }

    public static void requestFocus(View view, RegisterActivity registerActivity) {
        if (view.requestFocus()) {
            registerActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

}
