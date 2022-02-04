package com.example.euskalmet.EuskalmetData;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.euskalmet.Room.Entity.Reading;
import com.example.euskalmet.Room.Entity.Station;
import com.example.euskalmet.Room.MeteoController;

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


    public void updateReadings(List<Station> stationList) {
        if (stationList != null) {
            for (int i = 0; i < stationList.size(); i++) {
                getStationData(stationList.get(i).id);
            }
        }
    }

    public List getDateReadings(String date, String stationId, String dataType) {
        final List<List>[] readingList = new List[1];
        String stationDataUrl = String.format("https://euskalmet.euskadi.eus/vamet/stations/readings/%s/%s/readingsData.json", stationId, date);
        request = new StringRequest(Request.Method.GET, stationDataUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                readingList[0] = dataParser.parseDayStationData(response, stationId, dataType);
            }
        }, new ErrorListener());
        requestQueue.add(request);
        while (readingList[0] == null || readingList[0].size() == 0) {
        }
        return readingList[0];
    }

    public void getStationData(String stationId) {

        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Europe/Paris"));
        cal.setTime(new Date());
        int year = cal.get(Calendar.YEAR);
        String month =
                String.valueOf(cal.get(Calendar.MONTH) + 1).length() == 1 ?
                        "0" + (cal.get(Calendar.MONTH) + 1) : String.valueOf(cal.get(Calendar.MONTH) + 1);
        String day = cal.get(Calendar.DATE) < 10 ?
                "0" + (cal.get(Calendar.DATE)) : String.valueOf(cal.get(Calendar.DATE));
        String stationDataUrl = String.format("https://euskalmet.euskadi.eus/vamet/stations/readings/%s/%s/%s/%s/readingsData.json", stationId, year, month, day);
        request = new StringRequest(Request.Method.GET, stationDataUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dataParser.parseLastStationData(response, stationId);
            }
        }, new ErrorListener());
        requestQueue.add(request);
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
