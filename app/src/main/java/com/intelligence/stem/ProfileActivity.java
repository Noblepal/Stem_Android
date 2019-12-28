package com.intelligence.stem;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    Button redeemBtn;
    TextView tvPoints, tvEmail, tvPhone, tvID, tvFullName, tvAmount, tvBalance;
    ImageView camera, profile_pic;
    Integer REQUEST_CAMERA = 1, SELECT_FILE = 0;
    private Bitmap bitmap;
    private String profile_pic_name = String.valueOf(SharedPrefManager.getInstance(this).getKeyUserId());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        camera = (ImageView) findViewById(R.id.camera);
        profile_pic = findViewById(R.id.profile_pic);
        loadProfilePhoto();

        tvFullName = findViewById(R.id.tv_profile_full_name);
        tvEmail = findViewById(R.id.tv_profile_email);
        tvPhone = findViewById(R.id.tv_profile_phone);
        tvID = findViewById(R.id.tv_profile_nationalID);

        String full_name = SharedPrefManager.getInstance(this).getUserFirstName() + " " + SharedPrefManager.getInstance(this).getUserLastName();
        tvFullName.setText(full_name);
        tvEmail.setText(SharedPrefManager.getInstance(this).getUserEmail());
        tvPhone.setText(SharedPrefManager.getInstance(this).getUserPhone());
        tvID.setText(SharedPrefManager.getInstance(this).getUserNationalID());


        tvPoints = findViewById(R.id.profile_points);
        tvAmount = findViewById(R.id.profile_amount);
        tvBalance = findViewById(R.id.profile_balance);

        CurrencyFormatter currencyFormatter = new CurrencyFormatter();

        String loan_amount = SharedPrefManager.getInstance(this).getKeyLoanAmount();
        String balance = SharedPrefManager.getInstance(this).getKeyLoanBalance();

        tvPoints.setText("201");
        String zero = currencyFormatter.FormatCurrencyFromString("0");
        if (loan_amount.contentEquals("null")) {
            tvAmount.setText(zero);
        } else {
            tvAmount.setText(currencyFormatter.FormatCurrencyFromString(loan_amount));
        }
        if (balance.contentEquals("null")) {
            tvBalance.setText(zero);
        } else {
            tvBalance.setText(currencyFormatter.FormatCurrencyFromString(balance));
        }

        redeemBtn = findViewById(R.id.btn_redeem);
        redeemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ProfileActivity.this, "Insufficient Points", Toast.LENGTH_SHORT).show();


            }
        });

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectImage();
            }
        });

    }

    private void loadProfilePhoto() {
        //TODO: Get image from sdcard - DONE
        //TODO: GEt image from URL to SDCard
        File photoFolder = new File(Environment.getExternalStorageDirectory() + File.separator + "Stem" + File.separator + "Profile Pictures");
        Uri file = Uri.fromFile(new File(photoFolder, "STEM_" + profile_pic_name + ".jpg"));

        if (file.toString() != null && file.toString().length() > 0) {
            Picasso.with(ProfileActivity.this).load(file).placeholder(R.drawable.profile).into(profile_pic);
            profile_pic.refreshDrawableState();
        } else {
            Toast.makeText(this, "Profile photo not found", Toast.LENGTH_SHORT).show();
        }
    }

    private void SelectImage() {

        final CharSequence[] items = {"Camera", "Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
        builder.setTitle("Add Image");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (items[i].equals("Camera")) {

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);

                } else if (items[i].equals("Gallery")) {

                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(intent.createChooser(intent, "Select File"), SELECT_FILE);

                } else if (items[i].equals("Cancel")) {

                    dialogInterface.dismiss();
                }

            }
        });

        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {

                Bundle bundle = data.getExtras();
                bitmap = (Bitmap) bundle.get("data");
                profile_pic.setImageBitmap(bitmap);

                uploadImage();
            } else if (requestCode == SELECT_FILE) {

                Uri path = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), path);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Uri selectedImageUri = data.getData();
                profile_pic.setImageURI(selectedImageUri);
                profile_pic.setImageBitmap(bitmap);
                uploadImage();
            }

        }
    }

    private void saveImageToSDCard(Bitmap bitmap) {
        OutputStream output;

        File filePath = new File(Environment.getExternalStorageDirectory() + File.separator + "Stem" + File.separator + "Profile Pictures");

        filePath.mkdir();

        if (!filePath.exists()) {
            filePath.mkdir();
        }

        String image_name = "STEM_" + profile_pic_name + ".jpg";
        File file = new File(filePath, image_name);

        try {
            output = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);
            output.flush();
            output.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void uploadImage() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading photo...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST
                , Constants.UPLOAD_IMAGE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Toast.makeText(ProfileActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    saveImageToSDCard(bitmap);
                    loadProfilePhoto();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.hide();
                Toast.makeText(ProfileActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                saveImageToSDCard(bitmap);
                loadProfilePhoto();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                //Get user ID
                int id = SharedPrefManager.getInstance(ProfileActivity.this).getKeyUserId();
                String user_id = String.valueOf(id);
                String image_name = "STEM_" + user_id;

                params.put("user_id", user_id);
                params.put("image_name", image_name);
                params.put("photo", imageToString(bitmap));

                return params;
            }
        };

        RequestHandler.getInstance(ProfileActivity.this).addToRequestQueue(stringRequest);
    }

    private String imageToString(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 10, byteArrayOutputStream);
        byte[] imgBytes = byteArrayOutputStream.toByteArray();

        return Base64.encodeToString(imgBytes, Base64.DEFAULT);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
