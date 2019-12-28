package com.intelligence.stem;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class CreatePINActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnPin;
    EditText edtPin;
    TextView tvError;
    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_pin);

        edtPin = findViewById(R.id.edt_pin);
        btnPin = findViewById(R.id.btn_pin_enter);
        btnPin.setOnClickListener(this);
        tvError = findViewById(R.id.error_PIN);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_pin_enter:
                String USER_PIN = SharedPrefManager.getInstance(this).getKeyPin();
                String ENTERED_PIN = edtPin.getText().toString().trim();

                if (USER_PIN.equals("_PIN") || USER_PIN.length() <= 0) {
                    Toast.makeText(getApplicationContext(), "Please log in first", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), LogInActivity.class));
                    finish();
                } else if (!ENTERED_PIN.equals(USER_PIN)) {
                    tvError.setText(getString(R.string.incorrect_pin));
                    tvError.startAnimation(AnimationUtils.loadAnimation(CreatePINActivity.this, android.R.anim.slide_in_left));
                    Toast.makeText(getApplicationContext(), "Incorrect PIN", Toast.LENGTH_SHORT).show();

                }

                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                break;
        }
    }
}
