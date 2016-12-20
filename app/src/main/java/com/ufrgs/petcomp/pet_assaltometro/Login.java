package com.ufrgs.petcomp.pet_assaltometro;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;


import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import android.app.Dialog;
import android.app.ProgressDialog;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;


/**
 * A login screen that offers login via email/password.
 */
public class Login extends AppCompatActivity {

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */

    private UserLoginTask mAuthTask = null;

    // UI references.
    private EditText mEmailView;
    private EditText mPasswordView;

    public static final String USER_NAME = "USERNAME";

    private View mProgressView;
    private View mLoginFormView;
    private View mLoginButtonsFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.assal_login);
        // Set up the login form.
        mEmailView = (EditText) findViewById(R.id.etUsername);

        mPasswordView = (EditText) findViewById(R.id.etPassword);

        Button mEmailSignInButton = (Button) findViewById(R.id.btnLogin);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mLoginButtonsFormView = findViewById(R.id.buttonsLogin);
        mProgressView = findViewById(R.id.login_progress);
    }

    private void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(email, password);
            Log.d(UserLoginTask.class.getSimpleName(),"executing auth task");
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        //return email.contains("@");
        return true;
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
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

            mLoginButtonsFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginButtonsFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginButtonsFormView.setVisibility(show ? View.GONE : View.VISIBLE);
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
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        private Dialog loadingDialog;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingDialog = ProgressDialog.show(Login.this, "Please wait", "Loading...");
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            String loginURL = "http://10.0.2.2/newProject/login.php";
            String registerURL = "http://10.0.2.2/newProject/register.php";

            Boolean result = false;

            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            try {
                URL url = new URL(loginURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

                String post_data = URLEncoder.encode("nameString", "UTF-8") + "=" + URLEncoder.encode(mEmail, "UTF-8") + "&" +
                        URLEncoder.encode("passwordString", "UTF-8") + "=" + URLEncoder.encode(mPassword, "UTF-8");

                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));

                String s = "";
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    s += line;

                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                String preResult = s.trim();
                Log.d(UserLoginTask.class.getSimpleName(),"PreResult is: " + preResult );
                result = preResult.equalsIgnoreCase("success");
                Log.d(UserLoginTask.class.getSimpleName(),"result is: " + result.toString() );

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return result;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);
            loadingDialog.dismiss();

            if (success) {
                Log.d(UserLoginTask.class.getSimpleName(),"creating new intent");
                Intent intent = new Intent(Login.this, UserPanel.class);
                intent.putExtra(USER_NAME, mEmail);
                finish();
                startActivity(intent);
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }

    /**
     * Handles everything encryption related
     */
    public class PasswordEncrypt{

        public String getEncryptedKey(String password) throws NoSuchAlgorithmException{
            String encryptedKey;

            //Define hash algorithm
            MessageDigest md = MessageDigest.getInstance("SHA-1");

            //Salt generation
            SecureRandom random = new SecureRandom();

            //byte[] salt = new byte[20]; //enable to salt have the same size of SHA hash, that is, 20 bytes, 2 chars per byte
            byte[] salt = new byte[12]; //salt will have 12 bytes, 2 chars per byte so that we get 64 bytes in the end

            //create salt randomnly
            random.nextBytes(salt);


            StringBuilder sb2 = new StringBuilder();
            for (int i = 0; i < salt.length; i++) {
                System.out.print(salt[i] + ","); //only enable if you want to see signed integers for each byte

                //make a 40bit hash appending each byte
                sb2.append(Integer.toString((salt[i] & 0xff) + 0x100, 16).substring(1));
            }

            System.out.println("Salt hash is "+ sb2.toString());
            md.update(salt); //we append this salt to the end of the hash before storing

            //get the entry bytes to put in the hash function
            byte[] b = password.getBytes(Charset.forName("UTF-8"));

            //put the password for digest
            md.update(b);

            //do the hash magic
            byte[] mdbytes = md.digest();


            //get hex from bytes of the digest
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < mdbytes.length; i++) {
                sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
            }

            // or we could use other way to get digest to hex
           /*
           //second method to get the digest in hex format
           StringBuffer hexString = new StringBuffer();
            for (int i=0;i<mdbytes.length;i++) {
                String hex=Integer.toHexString(0xff & mdbytes[i]);
                if(hex.length()==1) hexString.append('0');
                hexString.append(hex);
            }
            System.out.println("Digest(in hex format):: " + hexString.toString());
           */


            encryptedKey = sb.toString() + sb2.toString(); //concatenate both strings so that we get a 64 char string

            return encryptedKey;
        }


        public String getKeyFromPassAndSalt(String password, String salt) throws NoSuchAlgorithmException {

            MessageDigest md = MessageDigest.getInstance("SHA-1"); //Hash function
            byte[] saltByteArray = hexStringToByteArray(salt);


            //for more info on this part, just refer to the getEncryptedKey function above
            for (int i = 0; i < saltByteArray.length; i++) {
                System.out.print(saltByteArray[i] + ",");
            }

            md.update(saltByteArray);
            md.update(password.getBytes(Charset.forName("UTF-8")));

            byte[] mdbytes = md.digest();

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < mdbytes.length; i++) {
                sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1)); //get hex from bytes of the digest
            }

            String convertedString = sb.toString();

            return convertedString;
        }


        public byte[] hexStringToByteArray(String s) {
            int len = s.length();
            byte[] data = new byte[len / 2]; //roughly 2 chars per byte
            for (int i = 0; i < len; i += 2) {
                data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) //first char in the 4 bigger bits
                        + Character.digit(s.charAt(i+1), 16)); //second in the smaller ones
            }
            return data;
        }

    }

}

