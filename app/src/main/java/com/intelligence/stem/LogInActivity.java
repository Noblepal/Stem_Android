package com.intelligence.stem;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LogInActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnLogin;
    TextView linkCreate;
    EditText edtEmail;
    EditText edtPin;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, MainActivity.class));

        }

        edtEmail = findViewById(R.id.edt_login_email);
        edtPin = findViewById(R.id.edt_login_pin);

        btnLogin = findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(this);

        linkCreate = findViewById(R.id.link_login);
        linkCreate.setOnClickListener(this);
    }

    private void userLogin() {
        final String email = edtEmail.getText().toString().trim();
        final String pin = edtPin.getText().toString().trim();

        final AlertDialog.Builder builder = new AlertDialog.Builder(LogInActivity.this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.URL_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (!jsonObject.getBoolean("error")) {
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
                                                //TODO: Fill remaining data -Login
                                        );

                                Toast.makeText(getApplicationContext(), "Logged in as " + jsonObject.getString("firstname") + " " +
                                        jsonObject.getString("lastname"), Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                finish();
                            } else {
                                builder.setIcon(R.drawable.error);
                                builder.setTitle("Error");
                                builder.setMessage(jsonObject.getString("message"));
                                AlertDialog dialog = builder.create();
                                builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                                dialog.show();
                                //Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();

                builder.setIcon(R.drawable.error);
                builder.setTitle("Network Error");
                builder.setMessage(error.getMessage());
                builder.setPositiveButton("Try again", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        userLogin();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("pin", pin);

                return params;
            }
        };

        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }

    @Override
    public void onClick(View v) {
        if (v == linkCreate) {
            startActivity(new Intent(getApplicationContext(), SignUpActivity.class));
            finish();
        }
        if (v == btnLogin) {
            userLogin();
        }
    }
}
