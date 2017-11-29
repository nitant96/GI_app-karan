package gov.cipam.gi.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import gov.cipam.gi.R;
import gov.cipam.gi.common.SharedPref;
import gov.cipam.gi.firebasemanager.FirebaseAuthentication;
import gov.cipam.gi.model.Users;
import gov.cipam.gi.utils.Constants;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {
    TextView mCreateAccount, mForgotPass;
    private EditText mEmailField, mPassField;
    Button mSignInButton;
    ProgressDialog mProgressDialog;
    private FirebaseAuth mAuth;
    private String email, password;
    private static String TAG = "SignInActivity";
    SharedPreferences mPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
//        launchActivity();
        mAuth = FirebaseAuth.getInstance();
        mPrefs = getPreferences(MODE_PRIVATE);

        mCreateAccount =  findViewById(R.id.createAccText);
        mForgotPass =  findViewById(R.id.forgotPass);

        mEmailField =  findViewById(R.id.emailField);
        mPassField =  findViewById(R.id.passField);
        mSignInButton =  findViewById(R.id.signinButton);

        mProgressDialog = new ProgressDialog(this);

        mSignInButton.setOnClickListener(this);
        mForgotPass.setOnClickListener(this);
        mCreateAccount.setOnClickListener(this);
    }

    private void forgotPassword() {
        email = mEmailField.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            mEmailField.setError(getString(R.string.empty_email_error));
            Toast.makeText(SignInActivity.this, getString(R.string.toast_empty_email),
                    Toast.LENGTH_SHORT).show();
        } else {
            mEmailField.setError(getString(R.string.empty_email_error));
            mAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(SignInActivity.this, getString(R.string.toast_password_reset_email),
                                        Toast.LENGTH_SHORT).show();
                                Log.d(TAG, "Email sent.");
                            }
                        }
                    });
        }
    }

//    public void launchActivity() {
//        SharedPreferences preferences =
//                getSharedPreferences(Constants.MY_PREFERENCES, MODE_PRIVATE);
//
//        if (!preferences.getBoolean(Constants.ONBOARDING_COMPLETE, false)) {
//
//            startActivity(new Intent(this, IntroActivity.class));
//
//            finish();
//        }
//    }


    @Override
    protected void onStart() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            startActivity(new Intent(SignInActivity.this, HomePageActivity.class));
        }
        super.onStart();
    }

    private void startSignIn(){

        mProgressDialog.setTitle(getString(R.string.register_progress_dialog_title));
        mProgressDialog.setMessage(getString(R.string.register_progress_dialog_message));
        mProgressDialog.setCanceledOnTouchOutside(false);

        email = mEmailField.getText().toString().trim();
        password = mPassField.getText().toString().trim();
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            if (TextUtils.isEmpty(email)) {
                mEmailField.setError(String.valueOf(R.string.email_error));
            }
            if (TextUtils.isEmpty(password)) {
                mPassField.setError(String.valueOf(R.string.password_error));
            }

        } else {
            FirebaseAuthentication firebaseAuthentication=new FirebaseAuthentication(this);
            firebaseAuthentication.startSignIn(email,password,mProgressDialog);
        }
    }


    @Override
    public void onClick(View view) {
        int id=view.getId();

        switch (id){
            case R.id.signinButton:
               startSignIn();
                break;

            case R.id.forgotPass:
                forgotPassword();
                break;

            case R.id.createAccText:
                startActivity(new Intent(SignInActivity.this, CreateAccountActivity.class));
                break;
        }
    }
}
