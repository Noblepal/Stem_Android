package com.intelligence.stem;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    public EditText f_name, phone, l_name, confirm_pin, input_pin, national, input_email;
    private Button btnSignUp;
    private TextView login;
    private AwesomeValidation awesomeValidation;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        awesomeValidation = new AwesomeValidation(ValidationStyle.UNDERLABEL);
        awesomeValidation.setContext(this);

        login = findViewById(R.id.link_login);
        login.setOnClickListener(this);

        progressDialog = new ProgressDialog(this);
        updateUI();
    }

    private void updateUI() {
        f_name = findViewById(R.id.edt_signup_firstname);
        l_name = findViewById(R.id.edt_signup_lastname);
        national = findViewById(R.id.edt_signup_nationalID);
        phone = findViewById(R.id.edt_signup_phone);
        input_email = findViewById(R.id.edt_signup_email);
        input_pin = findViewById(R.id.edt_signup_pin);
        confirm_pin = findViewById(R.id.edt_signup_confirm_pin);
        btnSignUp = findViewById(R.id.btn_signup);

        awesomeValidation.addValidation(SignUpActivity.this, R.id.edt_signup_firstname, RegexTemplate.NOT_EMPTY, R.string.fname_error);
        awesomeValidation.addValidation(SignUpActivity.this, R.id.edt_signup_lastname, RegexTemplate.NOT_EMPTY, R.string.lname_error);
        awesomeValidation.addValidation(SignUpActivity.this, R.id.edt_signup_nationalID, RegexTemplate.NOT_EMPTY, R.string.national_id_error);
        awesomeValidation.addValidation(SignUpActivity.this, R.id.edt_signup_phone, RegexTemplate.TELEPHONE, R.string.phone_error);
        awesomeValidation.addValidation(SignUpActivity.this, R.id.edt_signup_phone, RegexTemplate.NOT_EMPTY, R.string.phone_empty_error);
        awesomeValidation.addValidation(SignUpActivity.this, R.id.edt_signup_email, Patterns.EMAIL_ADDRESS, R.string.email_error);

        String regexPassword = ".{4,}";
        awesomeValidation.addValidation(SignUpActivity.this, R.id.edt_signup_pin, regexPassword, R.string.pin_error);
        awesomeValidation.addValidation(SignUpActivity.this, R.id.edt_signup_confirm_pin, regexPassword, R.string.pin_error);
        awesomeValidation.addValidation(SignUpActivity.this, R.id.edt_signup_confirm_pin, R.id.edt_signup_pin, R.string.confirm_pin_error);

        btnSignUp.setOnClickListener(this);

    }

    private void registerUser() {
        final String fname = f_name.getText().toString();
        final String lname = l_name.getText().toString();
        final String phone_no = phone.getText().toString();
        final String email = input_email.getText().toString();
        final String national_id = national.getText().toString();
        final String pin = input_pin.getText().toString();

        final AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);

        progressDialog.setTitle("Sign Up");
        progressDialog.setMessage("Registering...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.URL_REGISTER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.getBoolean("error")) {
                                builder.setTitle("Error");
                                builder.setIcon(R.drawable.error);
                                builder.setMessage(jsonObject.getString("message"));
                                builder.setNegativeButton("Check Details", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });

                                builder.setNeutralButton("Login", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        startActivity(new Intent(SignUpActivity.this, LogInActivity.class));
                                        finish();
                                    }
                                });

                                builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Toast.makeText(SignUpActivity.this, "Retrying...", Toast.LENGTH_SHORT).show();
                                        registerUser();
                                    }
                                });

                                AlertDialog dialog = builder.create();
                                dialog.show();
                            } else {
                                SharedPrefManager.getInstance(getApplicationContext())
                                        .userLogin(
                                                jsonObject.getInt("user_id"),
                                                jsonObject.getString("firstname"),
                                                jsonObject.getString("lastname"),
                                                jsonObject.getString("phone"),
                                                jsonObject.getString("email"),
                                                jsonObject.getString("nationalID"),
                                                jsonObject.getString("pin"),
                                                jsonObject.getString("loan_amount"),
                                                jsonObject.getString("lend_date"),
                                                jsonObject.getString("expected_date"),
                                                jsonObject.getString("balance"),
                                                jsonObject.getString("status")
                                        );

                                Toast.makeText(SignUpActivity.this, "Enter your PIN to continue", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(SignUpActivity.this, PINActivity.class));
                                finish();

                                AlertDialog dialog = builder.create();
                                dialog.show();

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.hide();

                builder.setIcon(R.drawable.error);
                builder.setTitle("Network Error");
                builder.setMessage("Connect to the internet and try again");
                builder.setPositiveButton("Try again", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        registerUser();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("firstname", fname);
                params.put("lastname", lname);
                params.put("phone", phone_no);
                params.put("email", email);
                params.put("nationalID", national_id);
                params.put("pin", pin);

                return params;

            }
        };

        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_signup:

                break;
        }
        if (v == btnSignUp) {
            if (awesomeValidation.validate()) {
                registerUser();
            }
        }

    }
}
