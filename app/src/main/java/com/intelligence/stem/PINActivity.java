package com.intelligence.stem;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PINActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnPin;
    EditText edtPin;
    TextView tvError;
    ProgressDialog progressDialog;
    private String email = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin);

        edtPin = findViewById(R.id.edt_pin);
        btnPin = findViewById(R.id.btn_pin_enter);
        btnPin.setOnClickListener(this);
        tvError = findViewById(R.id.error_PIN);

        edtPin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 4) userLogin();
            }
        });

    }

    private void userLogin() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();


        String user_pin = SharedPrefManager.getInstance(this).getKeyPin();
        String entered_pin = edtPin.getText().toString().trim();

        String md5_hashed_pin = convertPinMd5(entered_pin);

        if (md5_hashed_pin.equals(user_pin)) {
            String welcome_back = "Welcome " + SharedPrefManager.getInstance(this).getUserFirstName();
            progressDialog.dismiss();
            Toast.makeText(this, welcome_back, Toast.LENGTH_SHORT).show();
            startActivity(new Intent(PINActivity.this, MainActivity.class));
            finish();
        } else {
            progressDialog.dismiss();
            if (!md5_hashed_pin.equals(user_pin)) {
                tvError.setText(getString(R.string.incorrect_pin));
                tvError.setBackgroundResource(R.drawable.error_text);
                tvError.startAnimation(AnimationUtils.loadAnimation(PINActivity.this, android.R.anim.slide_in_left));
                Toast.makeText(getApplicationContext(), "Incorrect PIN", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private String convertPinMd5(String entered_pin) {
        String md5HashedPin = entered_pin;
        MessageDigest messageDigest;

        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(entered_pin.getBytes());
            BigInteger hash = new BigInteger(1, messageDigest.digest());

            md5HashedPin = hash.toString(16);
            if ((md5HashedPin.length() % 2) != 0) {
                md5HashedPin = "0" + md5HashedPin;
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();

        }

        return md5HashedPin;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_pin_enter:
                userLogin();
                break;
        }
    }
}
