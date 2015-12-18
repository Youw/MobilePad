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

        final AlertDialog dialog = new AlertDialog.Builder(this).create();
        View dialogView = View.inflate(dialog.getContext(), R.layout.dialog_signup, null);

        final EditText mName = (EditText) dialogView.findViewById(R.id.dialog_signup_name);
        final EditText mEmail = (EditText) dialogView.findViewById(R.id.dialog_signup_email);
        final EditText mPassword = (EditText) dialogView.findViewById(R.id.dialog_signup_password);

        dialog.setTitle(getString(R.string.signup_dialog_title));
        dialog.setView(dialogView);
        dialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.signup_dialog_OK), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // reimplemented later
            }
        });
        dialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.signup_dialog_Cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // default behaviour will cancel dialog
            }
        });

        dialog.create();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                validateInput(mName, mPassword, mEmail, new OnProcessRegisterInput() {
                    @Override
                    public void process(String name, String password, String email, ProcessRegisterResult result) {
                        switch (result) {
                            case INPUT_OK:
                                showProgress(true);
                                mSignUpTask = new UserSignUpTask(name, email, password);
                                mSignUpTask.execute((Void) null);
                                dialog.dismiss();
                                break;
                            case USERNAME_EMPTY:
                                mName.setError(getText(R.string.enter_username));
                                mName.requestFocus();
                                break;
                            case PASSWORD_EMPTY:
                                mPassword.setError(getText(R.string.enter_password));
                                mPassword.requestFocus();
                                break;
                            case PASSWORD_TOO_SHORT:
                                mPassword.setError(getString(R.string.signup_error_password_too_short));
                                mPassword.requestFocus();
                                break;
                            case EMAIL_EMPTY:
                                mEmail.setError(getString(R.string.signup_error_enter_email));
                                mEmail.requestFocus();
                                break;
                            case EMAIL_NOT_VALID:
                                mEmail.setError(getString(R.string.signup_error_wrong_email));
                                mEmail.requestFocus();
                                break;
                        }
                    }
                });

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
            public void process(String name, String password, ProcessResult result) {
                // Reset errors.
                mNameView.setError(null);
                mPasswordView.setError(null);

                String password_error = "";
                switch (result) {
                    case INPUT_OK:
                        // Show a progress spinner, and kick off a background task to
                        // perform the user login attempt.
                        showProgress(true);
                        mAuthTask = new UserLoginTask(name, password);
                        mAuthTask.execute((Void) null);
                        break;
                    case PASSWORD_EMPTY:
                        password_error = getString(R.string.enter_password);
                    case PASSWORD_TOO_SHORT:
                        if (TextUtils.isEmpty(password_error)) {
                            password_error = getString(R.string.password_is_incorrect);
                        }
                        mPasswordView.setError(password_error);
                        mPasswordView.requestFocus();
                        break;
                    case USERNAME_EMPTY:
                        mNameView.setError(getString(R.string.enter_username_or_email));
                        mNameView.requestFocus();
                        break;
                }
            }
        });
    }

    private void validateInput(TextView mNameView, EditText mPasswordView, OnProcessInput process) {
        // Store values at the time of the login attempt.
        String name = mNameView.getText().toString().trim();
        String password = mPasswordView.getText().toString().trim();

        OnProcessInput.ProcessResult result = OnProcessInput.ProcessResult.INPUT_OK;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(name)) {
            result = OnProcessInput.ProcessResult.USERNAME_EMPTY;
        }

        // Check for a valid password, if the user entered one.
        if (result == OnProcessInput.ProcessResult.INPUT_OK && TextUtils.isEmpty(password)) {
            result = OnProcessInput.ProcessResult.PASSWORD_EMPTY;
        }

        if (result == OnProcessInput.ProcessResult.INPUT_OK && !isPasswordLongEnough(password))
        {
            result = OnProcessInput.ProcessResult.PASSWORD_TOO_SHORT;
        }
        process.process(name, password, result);
    }

    private void validateInput(TextView mNameView, EditText mPasswordView, EditText mEmailView, final OnProcessRegisterInput process) {
        final String email = mEmailView.getText().toString().trim();
        validateInput(mNameView, mPasswordView, new OnProcessInput() {
            @Override
            public void process(String name, String password, ProcessResult result) {
                OnProcessRegisterInput.ProcessRegisterResult validation_result = OnProcessRegisterInput.ProcessRegisterResult.INPUT_OK;
                switch (result) {
                    case INPUT_OK:
                    case PASSWORD_EMPTY:
                        if (TextUtils.isEmpty(email)) {
                            validation_result = OnProcessRegisterInput.ProcessRegisterResult.EMAIL_EMPTY;
                            break;
                        }
                        if (!isEmailValid(email)) {
                            validation_result = OnProcessRegisterInput.ProcessRegisterResult.EMAIL_NOT_VALID;
                            break;
                        }
                        if (result == ProcessResult.PASSWORD_EMPTY) {
                            validation_result = OnProcessRegisterInput.ProcessRegisterResult.PASSWORD_EMPTY;
                            break;
                        }
//                        validation_result = OnProcessRegisterInput.ProcessRegisterResult.INPUT_OK;
                        break;
                    case USERNAME_EMPTY:
                        validation_result = OnProcessRegisterInput.ProcessRegisterResult.USERNAME_EMPTY;
                        break;
                    case PASSWORD_TOO_SHORT:
                        validation_result = OnProcessRegisterInput.ProcessRegisterResult.PASSWORD_TOO_SHORT;
                        break;
                }
                process.process(name, password, email, validation_result);
            }
        });
    }

    private boolean isEmailValid(String email) {
        return email.matches("([A-Za-z][\\w_\\.]*\\w)@(\\w+\\.\\w+(?:.\\w+)*)");
    }

    private boolean isPasswordLongEnough(String password) {
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

        private final String mLogin;
        private final String mPassword;
        private final Boolean mIsLoginByEmail;
        private Profile mProfile = null;

        UserLoginTask(String login_input, String password) {
            mLogin = login_input;
            mPassword = password;
            mIsLoginByEmail = isEmailValid(mLogin);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            if (mIsLoginByEmail) {
                mProfile = dbConnection.getProfileByEmail(mLogin);
            } else {
                mProfile = dbConnection.getProfileByUsername(mLogin);
            }
            return mProfile != null && Cryptography.verify(mPassword, mProfile.getPasswordHash());
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
                    if (mIsLoginByEmail) {
                        mNameView.setError(getString(R.string.user_email_not_exists));
                    } else {
                        mNameView.setError(getString(R.string.username_not_exists));
                    }
                    mNameView.requestFocus();
                } else {
                    mPasswordView.setError(getString(R.string.password_is_incorrect));
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
        private String mEmail;
        private String mPassword;

        private String mStatus;

        public UserSignUpTask(String name, String email, String password){
            mName = name;
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            if (dbConnection.getProfileByUsername(mName) != null){
                mStatus = getString(R.string.signup_error_existing_username);
                return false;
            }
            if (dbConnection.getProfileByEmail(mEmail) != null){
                mStatus = getString(R.string.signup_error_existing_email);
                return false;
            }
            byte[] passwordHash = Cryptography.encrypt(mPassword);
            if (!dbConnection.update(new Profile(-1, mName, mEmail, passwordHash))){
                mStatus = getString(R.string.signup_error_unknown_error);
                return false;
            }
            mStatus = getString(R.string.signup_registration_success);
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success){
            mSignUpTask = null;
            showProgress(false);

            if (success){
                mNameView.setText(mName);
            } else {
                mNameView.setText("");
            }
            mPasswordView.setText("");
            Toast.makeText(getApplicationContext(), mStatus, Toast.LENGTH_LONG).show();
        }

        @Override
        protected void onCancelled() {
            mSignUpTask = null;
            showProgress(false);
        }
    }

    private interface OnProcessInput {
        enum ProcessResult {
            INPUT_OK,
            USERNAME_EMPTY,
            PASSWORD_EMPTY,
            PASSWORD_TOO_SHORT
        }

        void process(String name, String password, ProcessResult result);
    }

    private interface OnProcessRegisterInput  {
        enum ProcessRegisterResult {
            INPUT_OK,
            USERNAME_EMPTY,
            PASSWORD_EMPTY,
            PASSWORD_TOO_SHORT,
            EMAIL_EMPTY,
            EMAIL_NOT_VALID
        }

        void process(String name, String password, String email, ProcessRegisterResult result);
    }

}

