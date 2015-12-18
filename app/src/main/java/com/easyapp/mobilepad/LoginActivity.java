package com.easyapp.mobilepad;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.easyapp.mobilepad.datacontract.Profile;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends Activity {

    private UserLoginTask mAuthTask = null;
    private UserSignUpTask mSignUpTask = null;
    private DBConnection dbConnection = null;

    // UI references.
    private TextView mNameView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle(R.string.title_activity_login);
        if (dbConnection == null) {
            dbConnection = SQLiteDBConnection.getInstance(getApplicationContext());
        }

        // Set up the login form.
        mNameView = (TextView) findViewById(R.id.name);

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login_text || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mLoginButton = (Button) findViewById(R.id.login_button);
        mLoginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        Button mSignUpButton = (Button) findViewById(R.id.signup_button);
        mSignUpButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptSignUp();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    /**
     * Attempts to sign up a new user.
     */
    private void attemptSignUp() {
        if (mSignUpTask != null) {
            return;
        }

        AlertDialog dialog = new AlertDialog.Builder(this).create();
        View dialogView = View.inflate(dialog.getContext(), R.layout.dialog_signup, null);

        final EditText mName = (EditText) dialogView.findViewById(R.id.dialog_signup_name);
        final EditText mPassword = (EditText) dialogView.findViewById(R.id.dialog_signup_password);

        dialog.setTitle(getString(R.string.signup_dialog_title));
        dialog.setView(dialogView);
        dialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.signup_dialog_OK), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                validateInput(mName, mPassword, new OnProcessInput() {
                    @Override
                    public void process(String name, String password) {
                        showProgress(true);
                        mSignUpTask = new UserSignUpTask(name, password);
                        mSignUpTask.execute((Void) null);
                    }
                });
            }
        });
        dialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.signup_dialog_Cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        dialog.show();
    }

    /**
     * Attempts to sign in the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        validateInput(mNameView, mPasswordView, new OnProcessInput() {
            @Override
            public void process(String name, String password) {
                // Show a progress spinner, and kick off a background task to
                // perform the user login attempt.
                showProgress(true);
                mAuthTask = new UserLoginTask(name, password);
                mAuthTask.execute((Void) null);
            }
        });
    }

    private void validateInput(TextView mNameView, EditText mPasswordView, OnProcessInput onSuccess) {
        // Reset errors.
        mNameView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String name = mNameView.getText().toString().trim();
        String password = mPasswordView.getText().toString().trim();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid name address.
        if (TextUtils.isEmpty(name)) {
            mNameView.setError(getString(R.string.error_field_required));
            focusView = mNameView;
            cancel = true;
        } else if (!isEmailValid(name)) {
            mNameView.setError(getString(R.string.error_invalid_email));
            focusView = mNameView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            onSuccess.process(name, password);
        }
    }

    private boolean isEmailValid(String email) {
        return email.matches("([A-Za-z][\\w_\\.]*\\w)@(\\w+\\.\\w+(?:.\\w+)*)");
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    /**
     * Represents an asynchronous login task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mName;
        private final String mPassword;
        private Profile mProfile = null;

        UserLoginTask(String name, String password) {
            mName = name;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            mProfile = dbConnection.getProfile(mName);
            return mProfile != null && Cryptography.verify(mPassword, mProfile.getPassword());
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                Bundle options = new Bundle();
                options.putInt("profile", mProfile.getId());
                Intent connections_intent = new Intent(getBaseContext(), Connections.class);
                connections_intent.putExtras(options);
                startActivity(connections_intent);
                finish();
            } else {
                if (mProfile == null){
                    mNameView.setError(getText(R.string.error_incorrect_profile));
                    mNameView.requestFocus();
                } else {
                    mPasswordView.setError(getString(R.string.error_incorrect_password));
                    mPasswordView.requestFocus();
                }
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }

    /**
     * Represents an asynchronous registration task used to sign up
     * the user.
     */
    public class UserSignUpTask extends AsyncTask<Void, Void, Boolean> {

        private String mName;
        private String mPassword;
        private String mStatus;

        public UserSignUpTask(String name, String password){
            mName = name;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            if (dbConnection.getProfile(mName) != null){
                mStatus = getString(R.string.error_existing_profile);
                return false;
            }
            byte[] hash = Cryptography.encrypt(mPassword);
            if (!dbConnection.update(new Profile(-1, mName, hash))){
                mStatus = getString(R.string.error_unknown_error);
                return false;
            }
            mStatus = getString(R.string.registration_success);
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success){
            mSignUpTask = null;
            showProgress(false);

            if (success){
                mNameView.setText(mName);
                mPasswordView.setText(mPassword);
            }
            Toast.makeText(getApplicationContext(), mStatus, Toast.LENGTH_LONG).show();
        }

        @Override
        protected void onCancelled() {
            mSignUpTask = null;
            showProgress(false);
        }
    }

    private interface OnProcessInput {
        void process(String name, String password);
    }

}

