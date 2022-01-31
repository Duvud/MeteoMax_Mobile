package com.example.euskalmet.EuskalmetData;

import com.android.volley.Response;
import com.android.volley.VolleyError;

public class ErrorListener implements Response.ErrorListener{
    @Override
    public void onErrorResponse(VolleyError error) {
        System.out.println(
                "REQUEST ERROR " + "\n" +
                "Ha ocurrido un error con el request :" +
                "\n" +
                error.getMessage() + "\n" +
                error.networkResponse
                );
    }
}
