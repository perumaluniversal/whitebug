package com.dennismwebia.angar.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;

import com.dennismwebia.angar.AINVService;
import com.dennismwebia.angar.R;
import com.dennismwebia.angar.TransmitService;
import com.dennismwebia.angar.utils.PreferenceHelper;
import com.dennismwebia.angar.utils.Utils;
import com.dennismwebia.angar.views.Btn;
import com.dennismwebia.angar.views.Edt;
import com.dennismwebia.angar.views.TxtSemiBold;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private PreferenceHelper preferenceHelper;
    private Utils utils;
    private Edt edtLoginUsername, edtLoginPassword;
    private TxtSemiBold txtShowPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        preferenceHelper = new PreferenceHelper(this);
        utils = new Utils(this, this);
        utils.hideKeyPad();

        initUI();
        checkRecordPermission();
        if (preferenceHelper.getIsLoggedIn()){
            Intent intentDashboard = new Intent(LoginActivity.this, DashboardActivity.class);
            if(preferenceHelper.getLoggedInUsername().equals("test")) {
                //transmitter
                startService(new Intent(getApplicationContext(), AINVService.class));
                startActivity(intentDashboard);
            }

            if(preferenceHelper.getLoggedInUsername().equals("raja"))
            {
                //receiver
            }

            finish();
        }
    }

    private void checkRecordPermission() {

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this , new String[]{android.Manifest.permission.RECORD_AUDIO},
                    123);
        }
    }

    private void initUI() {
        Btn btnSignIn = (Btn) findViewById(R.id.btnSignIn);
        edtLoginUsername = (Edt) findViewById(R.id.edtLoginUsername);
        edtLoginPassword = (Edt) findViewById(R.id.edtLoginPassword);
        txtShowPassword = (TxtSemiBold) findViewById(R.id.txtShowPassword);

        btnSignIn.setOnClickListener(this);
        txtShowPassword.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSignIn:
                login(edtLoginUsername.getText().toString(), edtLoginPassword.getText().toString());
                break;
            case R.id.txtShowPassword:
                if (edtLoginPassword.getTransformationMethod() == PasswordTransformationMethod.getInstance()) {
                    //password is visible
                    edtLoginPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    txtShowPassword.setText(getResources().getString(R.string.string_hide_password));
                } else if (edtLoginPassword.getTransformationMethod() == HideReturnsTransformationMethod.getInstance()) {
                    //password is hidden
                    edtLoginPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    txtShowPassword.setText(getResources().getString(R.string.string_show_password));
                }
                break;
            default:
                break;
        }
    }

    private void login(String username, String password) {
        if (username.equals("raja") && password.equals("Universa*123@456")) {
            startActivity(username, "Admin Account");
        }
        else if (username.equals("test") && password.equals("Zse45rdX123@456")) {
            startActivity(username, "Test Account");
        } else if (TextUtils.isEmpty(username)) {
            utils.showErrorToast("Please enter your username.");
        } else if (TextUtils.isEmpty(password)) {
            utils.showErrorToast("Please enter your password.");
        } else if (TextUtils.isEmpty(username) && TextUtils.isEmpty(password)) {
            utils.showErrorToast("Please enter your login details.");
        }
    }

    private void startActivity(String username, String payload) {
        Intent intentDashboard = new Intent(LoginActivity.this, DashboardActivity.class);
        intentDashboard.putExtra("username", payload);
        preferenceHelper.putIsLoggedIn(true);
        preferenceHelper.putLoggedInName(payload);
        preferenceHelper.putLoggedInUsername(username);
        startActivity(intentDashboard);
        finish();
    }
}
