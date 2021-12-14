package com.example.euskalmet.EuskalmetData;

import com.android.volley.Response;

public class ResponseListener implements Response.Listener<String> {
    @Override
    public void onResponse(String response) {
        new DataParser(response);
    }
}
