package com.intelligence.stem;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {
    //User login/Signup details
    private static final String SHARED_PREF_NAME = "stem_shared_prefs";
    private static final String KEY_FIRSTNAME = "_firstname";
    private static final String KEY_LASTNAME = "_lastname";
    private static final String KEY_PHONE = "_phone";
    private static final String KEY_EMAIL = "_email";
    private static final String KEY_NATIONAL_ID = "_nationalID";
    private static final String KEY_USER_ID = "_user_id";
    private static final String KEY_PIN = "_PIN";
    //Fields for loan application and repayment
    private static final String KEY_EXPECTED_DATE = "_expected_date";
    private static final String KEY_LOAN_AMOUNT = "_loan_amount";
    private static final String KEY_PAID_AMOUNT = "_paid_amount";
    private static final String KEY_LOAN_BALANCE = "_loan_balance";
    private static final String KEY_LOAN_APPLICATION_DATE = "_application_date";
    private static final String KEY_LOAN_REPAYMENT_DATE = "_repayment_date";
    private static final String KEY_STATUS = "_status";
    private static SharedPrefManager mInstance;
    private static Context mCtx;

    private SharedPrefManager(Context context) {
        mCtx = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }


    public boolean userLogin(int user_id, String firstname, String lastname, String phone,
                             String email, String nationalID, String pin, String loanAmount, String applicationDate, String expectedDate,
                             String loanBalance, String status) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt(KEY_USER_ID, user_id);
        editor.putString(KEY_FIRSTNAME, firstname);
        editor.putString(KEY_LASTNAME, lastname);
        editor.putString(KEY_PHONE, phone);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_NATIONAL_ID, nationalID);
        editor.putString(KEY_PIN, pin);
        editor.putString(KEY_LOAN_AMOUNT, loanAmount);
        editor.putString(KEY_LOAN_APPLICATION_DATE, applicationDate);
        editor.putString(KEY_EXPECTED_DATE, expectedDate);
        editor.putString(KEY_LOAN_BALANCE, loanBalance);
        editor.putString(KEY_STATUS, status);

        editor.apply();

        return true;
    }

    public boolean applyLoan(String loanAmount, String applicationDate, String expectedDate,
                             String loanBalance, String status) {

        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(KEY_LOAN_AMOUNT, loanAmount);
        editor.putString(KEY_LOAN_APPLICATION_DATE, applicationDate);
        editor.putString(KEY_EXPECTED_DATE, expectedDate);
        editor.putString(KEY_LOAN_BALANCE, loanBalance);
        editor.putString(KEY_STATUS, status);

        editor.apply();

        return true;
    }

    public boolean repayLoan(String loanAmount, String applicationDate, String expectedDate,
                             String loanBalance, String paid_amount, String status) {

        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(KEY_LOAN_AMOUNT, loanAmount);
        editor.putString(KEY_LOAN_APPLICATION_DATE, applicationDate);
        editor.putString(KEY_EXPECTED_DATE, expectedDate);
        editor.putString(KEY_LOAN_BALANCE, loanBalance);
        editor.putString(KEY_PAID_AMOUNT, paid_amount);
        editor.putString(KEY_STATUS, status);

        editor.apply();

        return true;
    }

    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        //Simplified if statement
        return sharedPreferences.getString(KEY_FIRSTNAME, null) != null;
    }

    public boolean logout() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        return true;
    }

    public int getKeyUserId() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(KEY_USER_ID, 0);
    }

    public String getUserFirstName() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_FIRSTNAME, null);
    }

    public String getUserLastName() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_LASTNAME, null);
    }

    public String getUserEmail() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_EMAIL, null);
    }

    public String getUserPhone() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_PHONE, null);
    }

    public String getUserNationalID() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_NATIONAL_ID, null);
    }

    public String getKeyPin() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_PIN, null);
    }

    public String getKeyStatus() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_STATUS, null);
    }

    public String getKeyExpectedDate() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_EXPECTED_DATE, null);
    }

    public String getKeyLoanAmount() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_LOAN_AMOUNT, null);
    }

    public String getKeyLoanBalance() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_LOAN_BALANCE, null);
    }

    public String getKeyPaidAmount() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_LOAN_BALANCE, null);
    }

    public String getKeyLoanApplicationDate() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_LOAN_APPLICATION_DATE, null);
    }

    public String getKeyLoanRepaymentDate() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_LOAN_REPAYMENT_DATE, null);
    }

}
