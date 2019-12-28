package com.intelligence.stem;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LoanHistoryActivity extends AppCompatActivity {

    RecyclerView.LayoutManager mLayoutManager;
    TextView acc_name, acc_phone, acc_points;
    ProgressDialog progressDialog;
    CurrencyFormatter currencyFormatter;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private ArrayList<LoanHistoryItem> loanHistoryItemsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan_history);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        acc_name = findViewById(R.id.history_account_name);
        acc_phone = findViewById(R.id.history_phone);
        acc_points = findViewById(R.id.history_points);

        String full_name = SharedPrefManager.getInstance(this).getUserFirstName() + " " + SharedPrefManager.getInstance(this).getUserLastName();
        String phone = SharedPrefManager.getInstance(this).getUserPhone();
        String points = "10000";
        acc_name.setText(full_name);
        acc_phone.setText(phone);
        acc_points.setText(points);

        retrieveLoanHistory();
    }

    private void retrieveLoanHistory() { //Retrieve user loan history from server...
        if (!checkNetworkStatus()) {//Check if internet NOT available
            AlertDialog.Builder builder = new AlertDialog.Builder(LoanHistoryActivity.this);
            builder.setTitle("No Internet");
            builder.setMessage("Unable to connect to the internet\nPlease check your network settings");
            builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    retrieveLoanHistory();
                }
            });

            builder.setNeutralButton("Check Settings", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(LoanHistoryActivity.this, "Opening settings...", Toast.LENGTH_SHORT).show();
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();

        } else {

            mRecyclerView = findViewById(R.id.my_recyclerView);

            mRecyclerView.setHasFixedSize(false);
            mLayoutManager = new LinearLayoutManager(this);
            mAdapter = new LoanHistoryAdapter(loanHistoryItemsList);

            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.setAdapter(mAdapter);

            progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Please wait...");
            progressDialog.setMessage("Retrieving your loan history");
            progressDialog.show();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.LOAN_HISTORY, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    progressDialog.dismiss();

                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            try {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                LoanHistoryItem historyItem = new LoanHistoryItem();
                                CurrencyFormatter currencyFormatter = new CurrencyFormatter();

                                //TODO: Double check json object with PHP json object from server - DONE

                                historyItem.setmTitleDate(jsonObject.getString("lend_date"));
                                historyItem.setmStatus(jsonObject.getString("status"));
                                historyItem.setmLoanAmount(currencyFormatter.FormatCurrencyFromString(jsonObject.getString("loan_amount")));
                                historyItem.setmLendDate(jsonObject.getString("lend_date"));
                                historyItem.setmRepayDate(jsonObject.getString("paid_full_date"));

                                if (historyItem.getmStatus().contentEquals("null")) {
                                    historyItem.setmStatus("Active");
                                }

                                if (historyItem.getmRepayDate().contentEquals("null")) {
                                    historyItem.setmRepayDate("Pending");
                                }

                                loanHistoryItemsList.add(historyItem);
                                mAdapter.notifyDataSetChanged();

                            } catch (JSONException e) {
                                e.printStackTrace();

                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    Log.e("Error", "Error fetching data");
                    Toast.makeText(LoanHistoryActivity.this, "Error fetching data: " + error.getMessage(), Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    int id = SharedPrefManager.getInstance(LoanHistoryActivity.this).getKeyUserId();
                    String user_id = String.valueOf(id);
                    params.put("user_id", user_id);
                    return params;
                }
            };

            RequestHandler.getInstance(LoanHistoryActivity.this).addToRequestQueue(stringRequest);

            //TODO: Retrieve data from database to recyclerview - DONE
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
