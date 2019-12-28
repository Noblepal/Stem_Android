package com.intelligence.stem;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {

    //TODO: ADD back arrow to all activities

    TextView about_email;
    EditText mEditTextTo, mEditTextSubject, mEditTextMessage;
    Button buttonSend;
    FloatingActionButton actionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater layoutInflater = this.getLayoutInflater();
        View dialogView = layoutInflater.inflate(R.layout.activity_contact, null);
        builder.setView(dialogView);
        mEditTextTo = dialogView.findViewById(R.id.contact_edit_text_to);
        mEditTextSubject = dialogView.findViewById(R.id.contact_edit_text_subject);
        mEditTextMessage = dialogView.findViewById(R.id.contact_edit_text_message);
        final Dialog dialog = builder.create();
        dialog.setCancelable(true);

        buttonSend = dialogView.findViewById(R.id.button_send);
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMail();
                dialog.dismiss();
            }
        });

        actionButton = findViewById(R.id.about_fab);
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });
    }

    private void sendMail() {
        String recipientList = mEditTextTo.getText().toString().trim();
        String[] recipients = recipientList.split(",");
        String subject = mEditTextSubject.getText().toString().trim();
        String message = mEditTextMessage.getText().toString().trim();

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL, recipients);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, message);

        intent.setType("message/rfc822");
        startActivity(Intent.createChooser(intent, "Choose your client"));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}