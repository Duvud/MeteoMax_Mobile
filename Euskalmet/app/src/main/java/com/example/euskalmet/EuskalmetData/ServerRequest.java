package com.example.euskalmet.EuskalmetData;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.provider.ContactsContract;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.euskalmet.R;
import com.example.euskalmet.Room.Entity.Station;
import com.example.euskalmet.Room.MeteoController;

import java.sql.SQLOutput;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ServerRequest {
    private String stationListUrl =
            "https://www.euskalmet.euskadi.eus/vamet/stations/stationList/stationList.json";

    private RequestQueue requestQueue;
    private StringRequest request;
    private DataParser dataParser;
    private Context mainContext;
    private MeteoController meteoController;

    public ServerRequest(Context mainContext) {
        this.mainContext = mainContext;
        meteoController = MeteoController.getMeteoController(this.mainContext);
        requestQueue = Volley.newRequestQueue(this.mainContext);
        dataParser = new DataParser(this.mainContext);
    }

    public void getStationList() {
        request = new StringRequest(Request.Method.GET, stationListUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dataParser.parseStationList(response);
            }
        }, new ErrorListener());
        requestQueue.add(request);
    }
}
