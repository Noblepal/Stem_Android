package com.intelligence.stem;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    TextView accountName, loanStatus, points, due_date, history, user, about, apply_repay_loan;
    private CardView card_apply_loan, card_loan_history, card_user_profile, card_about_us;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        accountName = findViewById(R.id.dash_account_name);
        loanStatus = findViewById(R.id.tv_loan_status);
        points = findViewById(R.id.tv_loan_points);

        String full_name = SharedPrefManager.getInstance(this).getUserFirstName() + " " + SharedPrefManager.getInstance(this).getUserLastName();
        accountName.setText(full_name);


        Typeface fontAwesomeFont = Typeface.createFromAsset(getAssets(), "fonts/fontawesome_webfont.ttf");
        due_date = findViewById(R.id.tv_due_date);
        history = findViewById(R.id.tv_loan_history);
        user = findViewById(R.id.tv_user_profile);
        about = findViewById(R.id.tv_about);
        apply_repay_loan = findViewById(R.id.tv_dash_apply_repay_loan);

        //loan_balance.setTypeface(fontAwesomeFont);
        due_date.setTypeface(fontAwesomeFont);
        history.setTypeface(fontAwesomeFont);
        user.setTypeface(fontAwesomeFont);
        about.setTypeface(fontAwesomeFont);

        card_apply_loan = findViewById(R.id.dash_card_apply_loan);
        card_loan_history = findViewById(R.id.dash_loan_history);
        card_user_profile = findViewById(R.id.dash_user_profile);
        card_about_us = findViewById(R.id.dash_about_us);

        /*Intents from dashboard to other activities*/
        card_apply_loan.setOnClickListener(this);
        card_loan_history.setOnClickListener(this);
        card_user_profile.setOnClickListener(this);
        card_about_us.setOnClickListener(this);

        initData();
    }

    private void initData() {
        String loan_status = SharedPrefManager.getInstance(this).getKeyStatus();
        if (loan_status.equals("Active")) {
            loanStatus.setText(loan_status);
        } else {
            loanStatus.setText("Not applied");
        }

        final int min = 50;
        final int max = 1000;
        final int random = new Random().nextInt((max - min) + 1) + min;
        String pts = String.valueOf(random);
        points.setText(pts);

        //Check if user already has active loan
        String status = SharedPrefManager.getInstance(MainActivity.this).getKeyStatus();
        if (status.equals("Active")) {
            due_date.setText(R.string.due_date_icon);
            due_date.setTextColor(R.color.colorAccent);
            apply_repay_loan.setText(R.string.repay_loan);
        } else {
            due_date.setText(R.string.apply_loan_icon);
            due_date.setTextColor(R.color.myGreen);
            apply_repay_loan.setText(R.string.apply_for_a_loan);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_account) {
            startActivity(new Intent(MainActivity.this, ProfileActivity.class));

        } else if (id == R.id.nav_messages) {
            Toast.makeText(this, "Coming soon...", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_points) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Your STEM bonus points");
            builder.setMessage("You have 25 points\n\nReach 100 points to start redeeming\n\n" +
                    "Pay your loans on time to earn bonus points\n\nRefer your friends to earn referral points");
            builder.setIcon(R.mipmap.ic_launcher);
            Dialog dialog = builder.create();
            dialog.show();
        } else if (id == R.id.nav_share) {
            Intent mIntent = new Intent(Intent.ACTION_SEND);
            mIntent.setType("text/plain");
            String shareBody = getString(R.string.invite);
            String shareSub = getString(R.string.app_name);
            mIntent.putExtra(Intent.EXTRA_SUBJECT, shareSub);
            mIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(mIntent, "Share using"));

        } else if (id == R.id.nav_code) {
            Toast.makeText(this, "Coming soon...", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_logout) {
            final AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
            mBuilder.setTitle("Sure?");
            mBuilder.setMessage("Are you sure you want to logout");

            mBuilder.setPositiveButton("Yes, logout", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    SharedPrefManager.getInstance(MainActivity.this).logout();
                    Toast.makeText(MainActivity.this, "You are now logged out", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });

            mBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(MainActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();
                }
            });
            AlertDialog mDialog = mBuilder.create();
            mDialog.show();
        }


        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.dash_card_apply_loan:
                String mStatus = SharedPrefManager.getInstance(this).getKeyStatus();
                if (mStatus.equals("Active")) {
                    startActivity(new Intent(MainActivity.this, LoanRepaymentActivity.class));
                } else {
                    startActivity(new Intent(MainActivity.this, LoanApplicationActivity.class));
                }
                break;

            case R.id.dash_loan_history:
                startActivity(new Intent(MainActivity.this, LoanHistoryActivity.class));
                break;

            case R.id.dash_user_profile:
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                break;

            case R.id.dash_about_us:
                startActivity(new Intent(MainActivity.this, AboutActivity.class));
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }
}
