package com.intelligence.stem;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class LoanRepaymentActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView c1, c2, c3, c4;
    TextView tv1, tv2, tv3, tv4, tv5, tv6;
    TextView tvr1, tvr2, tvr3, tvr4, repayment_days;
    Button repayLoan;
    String loan_amount, subTotal, total, paid_amount, bal;
    Float totalAmountSelected = 0.00f;
    String[] totals;

    CurrencyFormatter currencyFormatter = new CurrencyFormatter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan_repayment);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        c1 = findViewById(R.id.repayment_complete_1);
        c2 = findViewById(R.id.repayment_complete_2);
        c3 = findViewById(R.id.repayment_complete_3);
        c4 = findViewById(R.id.repayment_complete_4);

        tv1 = findViewById(R.id.repayment_amt_first);
        tv2 = findViewById(R.id.repayment_amt_second);
        tv3 = findViewById(R.id.repayment_amt_third);
        tv4 = findViewById(R.id.repayment_amt_fourth);
        tv5 = findViewById(R.id.repayment_amt_total);
        tv6 = findViewById(R.id.repayment_loan_balance);

        tvr1 = findViewById(R.id.tv_repayment_date_one);
        tvr2 = findViewById(R.id.tv_repayment_date_two);
        tvr3 = findViewById(R.id.tv_repayment_date_three);
        tvr4 = findViewById(R.id.tv_repayment_date_four);
        repayment_days = findViewById(R.id.tv_repayment_info);

        initData();

        repayLoan = findViewById(R.id.repayment_btn_pay_loan);
        repayLoan.setOnClickListener(this);
    }

    private void initData() {
        String application_date = SharedPrefManager.getInstance(LoanRepaymentActivity.this).getKeyLoanApplicationDate();
        loan_amount = SharedPrefManager.getInstance(LoanRepaymentActivity.this).getKeyLoanAmount();
        paid_amount = SharedPrefManager.getInstance(LoanRepaymentActivity.this).getKeyPaidAmount();
        bal = SharedPrefManager.getInstance(LoanRepaymentActivity.this).getKeyLoanBalance();

        if (!paid_amount.contentEquals("null") && !paid_amount.contentEquals("null")) {
            float x = Float.parseFloat(paid_amount);
            float y = (Float.parseFloat(loan_amount) / 4);

            //Ticks
            if (x - y > 0) {
                mSetVisibility(1);
            } else if (x - (y * 2) >= 0) {
                mSetVisibility(2);

            } else if (x - (y * 3) >= 0) {
                mSetVisibility(3);

            } else if (x - (y * 4) >= 0) {
                mSetVisibility(4);

            }
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        if (application_date.length() > 0) {
            String strDate, strDate2, strDate3, strDate4, days = "";
            long diff;
            try {
                Date date = simpleDateFormat.parse(application_date);
                Calendar calendar = Calendar.getInstance();
                Calendar calendar1 = Calendar.getInstance();
                calendar.setTime(date);

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM, yyyy");

                addWeekToCalendar(calendar);
                strDate = dateFormat.format(calendar.getTime());

                addWeekToCalendar(calendar);
                strDate2 = dateFormat.format(calendar.getTime());

                addWeekToCalendar(calendar);
                strDate3 = dateFormat.format(calendar.getTime());

                addWeekToCalendar(calendar);
                strDate4 = dateFormat.format(calendar.getTime());

                tvr1.setText(strDate);
                tvr2.setText(strDate2);
                tvr3.setText(strDate3);
                tvr4.setText(strDate4);

                Date date1 = dateFormat.parse(strDate);
                Date date2 = dateFormat.parse(strDate2);
                Date date3 = dateFormat.parse(strDate3);
                Date date4 = dateFormat.parse(strDate4);
                Date date0 = calendar1.getTime();

                if (date0.getTime() <= date1.getTime()) {
                    if (date0.getTime() == date1.getTime()) {
                        days = "Your next payment is today";
                    } else if (date1.getTime() - date0.getTime() == 1) {
                        days = "Your next payment is tomorrow";
                    } else {
                        diff = date1.getTime() - date0.getTime();
                        days = "Your next payment is in " + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) + " Days";
                    }
                } else if (date0.getTime() <= date2.getTime() && date0.getTime() > date1.getTime()) {
                    if (date0.getTime() == date2.getTime()) {
                        days = "Your next payment is today";
                    } else if (date2.getTime() - date0.getTime() == 1) {
                        days = "Your next payment is tomorrow";
                    } else {
                        diff = date2.getTime() - date0.getTime();
                        days = "Your next payment is in " + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) + " Days";
                    }
                } else if (date0.getTime() <= date3.getTime() && date0.getTime() > date2.getTime()) {
                    if (date0.getTime() == date3.getTime()) {
                        days = "Your next payment is today";
                    } else if (date3.getTime() - date0.getTime() == 1) {
                        days = "Your next payment is tomorrow";
                    } else {
                        diff = date3.getTime() - date0.getTime();
                        days = "Your next payment is in " + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) + " Days";
                    }
                } else if (date0.getTime() <= date4.getTime() && date0.getTime() > date3.getTime()) {
                    if (date0.getTime() == date4.getTime()) {
                        days = "Your next payment is today";
                    } else if (date4.getTime() - date0.getTime() == 1) {
                        days = "Your next payment is tomorrow";
                    } else {
                        diff = date4.getTime() - date0.getTime();
                        days = "Your next payment is in " + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) + " Days";
                    }
                } else {
                    days = "No loan";
                }

                repayment_days.setText(days);

            } catch (ParseException e) {
                e.printStackTrace();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Error");
            builder.setMessage("Error retrieving your loan status");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(LoanRepaymentActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            });

            Dialog dialog = builder.create();
            dialog.show();

        }

        loan_amount = SharedPrefManager.getInstance(LoanRepaymentActivity.this).getKeyLoanAmount();
        total = currencyFormatter.FormatCurrencyFromString(loan_amount);

        if (loan_amount.length() != 0) {
            subTotal = String.valueOf(calculateCumulativeTotals(Float.parseFloat(loan_amount)));
        } else {
            subTotal = "Ksh. 0.00";
        }

        tv1.setText(currencyFormatter.FormatCurrencyFromString(subTotal));
        tv2.setText(currencyFormatter.FormatCurrencyFromString(subTotal));
        tv3.setText(currencyFormatter.FormatCurrencyFromString(subTotal));
        tv4.setText(currencyFormatter.FormatCurrencyFromString(subTotal));
        tv5.setText(currencyFormatter.FormatCurrencyFromString(loan_amount));
        tv6.setText(currencyFormatter.FormatCurrencyFromString(bal));

    }

    private void mSetVisibility(int i) {
        switch (i) {
            case 1:
                c1.setVisibility(View.VISIBLE);
                break;
            case 2:
                c1.setVisibility(View.VISIBLE);
                c2.setVisibility(View.VISIBLE);
                break;
            case 3:
                c1.setVisibility(View.VISIBLE);
                c2.setVisibility(View.VISIBLE);
                c3.setVisibility(View.VISIBLE);
                break;
            case 4:
                c1.setVisibility(View.VISIBLE);
                c2.setVisibility(View.VISIBLE);
                c3.setVisibility(View.VISIBLE);
                c4.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void addWeekToCalendar(Calendar calendar) {
        calendar.add(Calendar.WEEK_OF_MONTH, 1);
    }

    public float calculateCumulativeTotals(float total) {
        return total / 4;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.repayment_btn_pay_loan:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                LayoutInflater inflater = this.getLayoutInflater();
                View rootView = inflater.inflate(R.layout.layout_amount_slider, null);
                builder.setView(rootView);
                SeekBar seekBar = rootView.findViewById(R.id.seekBar);
                final TextView ksh = rootView.findViewById(R.id.ksh);
                final TextView sliderAmount = rootView.findViewById(R.id.tv_slider_amount);
                final int max = Integer.parseInt(SharedPrefManager.getInstance(this).getKeyLoanBalance());
                int min = max / 4;

                sliderAmount.setTextColor(getResources().getColor(R.color.myGreen));
                sliderAmount.setText(" " + max);
                seekBar.setMax(max);
                seekBar.setProgress(max);

                if (seekBar.getProgress() <= max / 4) {
                    sliderAmount.setTextColor(Color.RED);
                    ksh.setTextColor(Color.RED);
                }

                seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        sliderAmount.setText(" " + progress);
                        totalAmountSelected = Float.parseFloat(sliderAmount.getText().toString().trim());
                        if (progress <= max / 4) {
                            sliderAmount.setTextColor(Color.RED);
                            ksh.setTextColor(Color.RED);
                        } else if (progress <= max / 2) {
                            sliderAmount.setTextColor(getResources().getColor(R.color.colorPrimary));
                            ksh.setTextColor(getResources().getColor(R.color.colorPrimary));
                        } else {
                            sliderAmount.setTextColor(getResources().getColor(R.color.myGreen));
                            ksh.setTextColor(getResources().getColor(R.color.myGreen));
                        }
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        totalAmountSelected = Float.parseFloat(sliderAmount.getText().toString().trim());
                    }
                });

                builder.setCancelable(false);
                builder.setPositiveButton("Pay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        repayLoan();
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(LoanRepaymentActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();
                    }
                });

                Dialog dialog = builder.create();
                dialog.show();

                break;
        }
    }

    private void repayLoan() {
        if (!checkNetworkStatus()) {//Check if internet NOT available
            AlertDialog.Builder builder = new AlertDialog.Builder(LoanRepaymentActivity.this);
            builder.setTitle("No Internet");
            builder.setMessage("Unable to connect to the internet\nPlease check your network settings");
            builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    repayLoan();
                }
            });

            builder.setNeutralButton("Check Settings", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(LoanRepaymentActivity.this, "Opening settings...", Toast.LENGTH_SHORT).show();
                }
            });

            Dialog dialog = builder.create();
            dialog.show();

        } else {
            final ProgressDialog progressDialog;
            progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Processing payment...");
            progressDialog.setMessage("Please wait");
            progressDialog.show();
            StringRequest stringRequest = new StringRequest(Request.Method.POST,
                    Constants.REPAY_LOAN,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();

                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                Toast.makeText(LoanRepaymentActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                                //Save user loan information locally - shared preferences
                                if (jsonObject.getString("message").toLowerCase().contains("successful")) {
                                    SharedPrefManager.getInstance(LoanRepaymentActivity.this).repayLoan(
                                            jsonObject.getString("loan_amount"),
                                            jsonObject.getString("lend_date"),
                                            jsonObject.getString("expected_date"),
                                            jsonObject.getString("balance"),
                                            jsonObject.getString("paid_amount"),
                                            jsonObject.getString("status")
                                    );

                                    AlertDialog.Builder mBuilder = new AlertDialog.Builder(LoanRepaymentActivity.this);
                                    mBuilder.setTitle("Payment successful");
                                    mBuilder.setIcon(R.drawable.check);

                                    //Perform subtraction on initial balance and paid amount
                                    Float initialBalance = Float.parseFloat(bal);
                                    Float newBalance = initialBalance - totalAmountSelected;
                                    String printedBalance = "";

                                    if (newBalance <= 0) {
                                        mBuilder.setMessage("You have successfully cleared your loan. Thank you");
                                        mBuilder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                finish();
                                            }
                                        });
                                    } else {
                                        printedBalance = currencyFormatter.FormatCurrencyFromString(String.valueOf(newBalance));
                                        mBuilder.setMessage("You have successfully paid Ksh. " + totalAmountSelected + "\n Your balance is: " +
                                                printedBalance);
                                        mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                initData();
                                            }
                                        });
                                    }

                                    Dialog alertDialog = mBuilder.create();
                                    alertDialog.show();

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    Toast.makeText(LoanRepaymentActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    String user_id = String.valueOf(SharedPrefManager.getInstance(LoanRepaymentActivity.this).getKeyUserId());
                    params.put("user_id", user_id);
                    params.put("paid_amount", String.valueOf(totalAmountSelected));

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
