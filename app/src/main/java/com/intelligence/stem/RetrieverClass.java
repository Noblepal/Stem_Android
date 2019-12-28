package com.intelligence.stem;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;


//TODO: Implment single class to retrieve all user Data for better synchronization between activities

public class RetrieverClass {
    StringRequest stringRequest = new StringRequest(Request.Method.POST,
            Constants.USER_DATA, new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {

        }
    }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {

        }
    });
}
