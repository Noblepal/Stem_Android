package com.intelligence.stem;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class LoanApplicationActivity extends AppCompatActivity implements CustomInterface, View.OnClickListener {

    String mData = "";
    ProgressDialog progressDialog;
    private String RAW_AMOUNT = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan_application);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Button btnLoanApply = findViewById(R.id.btn_apply_loan);
        btnLoanApply.setOnClickListener(this);

    }

    @Override
    public void setAmount(String data) {
        FragmentManager manager = getFragmentManager();
        FragmentLoanDetails loanDetails = (FragmentLoanDetails) manager.findFragmentById(R.id.fragment2);
        //Initialize string RAW_MOUNT with data
        RAW_AMOUNT = data;
        loanDetails.initData(data);

        CurrencyFormatter currencyFormatter = new CurrencyFormatter();
        getAmount(currencyFormatter.FormatCurrencyFromString(data));
    }

    public void getAmount(String data) {
        mData = data;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_apply_loan:
                applyLoan();
                break;
        }
    }

    //TODO: Retrieve loan data from database to fill table...

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void applyLoan() {
        if (!checkNetworkStatus()) {//Check if internet NOT available
            AlertDialog.Builder builder = new AlertDialog.Builder(LoanApplicationActivity.this);
            builder.setTitle("No Internet");
            builder.setMessage("Unable to connect to the internet\nPlease check your network settings");
            builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //retrieveLoanHistory();
                }
            });

            builder.setNeutralButton("Check Settings", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(LoanApplicationActivity.this, "Opening settings...", Toast.LENGTH_SHORT).show();
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();

        } else {

            progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Processing...");
            progressDialog.setMessage("Please wait as your loan application is being processed\nDo not close the app");
            progressDialog.show();
            StringRequest stringRequest = new StringRequest(Request.Method.POST,
                    Constants.APPLY_LOAN,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();

                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                //Save user loan information locally - shared preferences
                                if (jsonObject.getString("message").toLowerCase().contains("successful")) {
                                    SharedPrefManager.getInstance(LoanApplicationActivity.this).applyLoan(
                                            jsonObject.getString("loan_amount"),
                                            jsonObject.getString("lend_date"),
                                            jsonObject.getString("expected_date"),
                                            jsonObject.getString("balance"),
                                            jsonObject.getString("status")
                                    );

                                    AlertDialog.Builder builder = new AlertDialog.Builder(LoanApplicationActivity.this);
                                    builder.setIcon(R.drawable.check);
                                    builder.setTitle("Success");
                                    builder.setMessage("Your loan application is successful");
                                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            startActivity(new Intent(LoanApplicationActivity.this, LoanRepaymentActivity.class));
                                            finish();
                                        }
                                    });
                                    AlertDialog mDialog = builder.create();
                                    mDialog.show();
                                } else {
                                    AlertDialog.Builder mBuilder = new AlertDialog.Builder(LoanApplicationActivity.this);
                                    mBuilder.setIcon(R.drawable.error);
                                    mBuilder.setTitle("Failed");
                                    mBuilder.setMessage(jsonObject.getString("message"));
                                    mBuilder.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                                    AlertDialog dialog = mBuilder.create();
                                    dialog.show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    Toast.makeText(LoanApplicationActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    FragmentManager manager = getFragmentManager();
                    FragmentLoanDetails loanDetails = (FragmentLoanDetails) manager.findFragmentById(R.id.fragment2);

                    MyCalendar myCalendar = new MyCalendar();
                    int id = SharedPrefManager.getInstance(LoanApplicationActivity.this).getKeyUserId();

                    String user_id = String.valueOf(id);
                    String loan_amount = String.valueOf(loanDetails.calculateInterestAndTotal(Float.parseFloat(RAW_AMOUNT)));
                    String lend_date = myCalendar.getCurrentDate();
                    String expected_date = myCalendar.addMonthToCalendar();

                    Map<String, String> params = new HashMap<>();
                    params.put("user_id", user_id);
                    params.put("loan_amount", loan_amount);
                    params.put("lend_date", lend_date);
                    params.put("expected_date", expected_date);

                    return params;

                }
            };

            RequestHandler.getInstance(this).addToRequestQueue(stringRequest);

            //TODO: Disable button and re-enable on loan selected

        }
    }

    //Checking for internet connectivity
    private boolean checkNetworkStatus() {
        boolean wifiConnected, dataConnected;
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            wifiConnected = networkInfo.getType() == ConnectivityManager.TYPE_WIFI;
            dataConnected = networkInfo.getType() == ConnectivityManager.TYPE_MOBILE;

            return wifiConnected || dataConnected;

        } else {
            return false;
        }
    }

    public class MyCalendar {
        public MyCalendar() {
        }

        public String getCurrentDate() {
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            return dateFormat.format(calendar.getTime());
        }

        public String addMonthToCalendar() {
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            calendar.add(Calendar.WEEK_OF_MONTH, 4);

            return dateFormat.format(calendar.getTime());
        }
    }

}
