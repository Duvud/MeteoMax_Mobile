package com.example.euskalmet.EuskalmetData;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
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
import java.util.*;

public class ServerRequest {
    private static ServerRequest serverRequest;
    private String stationListUrl =
            "https://www.euskalmet.euskadi.eus/vamet/stations/stationList/stationList.json";

    private RequestQueue requestQueue;
    private StringRequest request;
    private DataParser dataParser;
    private Context mainContext;
    private MeteoController meteoController;

    public ServerRequest(Context mainContext, List<Station> oldStationList) {
        this.mainContext = mainContext;
        meteoController = MeteoController.getMeteoController(this.mainContext);
        requestQueue = Volley.newRequestQueue(this.mainContext);
        dataParser = new DataParser(this.mainContext, oldStationList);
    }

    public static ServerRequest getServerRequest(Context context, List<Station> oldStationList) {
        if (serverRequest == null) {
            serverRequest = new ServerRequest(context, oldStationList);
        }
        return serverRequest;
    }

    public void getStationData(String stationId) {
        HandlerThread insertHandlerThread = new HandlerThread("stationDataHandlerThread");
        insertHandlerThread.start();
        Looper insertLooper = insertHandlerThread.getLooper();
        Handler insertHandler = new Handler(insertLooper);
        insertHandler.post(new Runnable() {
            @Override
            public void run() {
                Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Europe/Paris"));
                cal.setTime(new Date());
                int year = cal.get(Calendar.YEAR);
                String month =
                        String.valueOf(cal.get(Calendar.MONTH) + 1).length() == 1 ?
                                "0" + (cal.get(Calendar.MONTH) + 1) : String.valueOf(cal.get(Calendar.MONTH) + 1);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                String stationDataUrl = String.format("https://euskalmet.euskadi.eus/vamet/stations/readings/%s/%s/%s/%s/readingsData.json",stationId, year, month, day);
                request = new StringRequest(Request.Method.GET, stationDataUrl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dataParser.parseStationData(response, stationId);
                    }
                }, new ErrorListener());
                requestQueue.add(request);
            }
        });

    }

    public void getStationList() {
        HandlerThread insertHandlerThread = new HandlerThread("stationListHandlerThread");
        insertHandlerThread.start();
        Looper insertLooper = insertHandlerThread.getLooper();
        Handler insertHandler = new Handler(insertLooper);
        insertHandler.post(new Runnable() {
            @Override
            public void run() {
                request = new StringRequest(Request.Method.GET, stationListUrl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dataParser.parseStationList(response);
                    }
                }, new ErrorListener());
                requestQueue.add(request);
            }
        });

    }
}
