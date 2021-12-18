package com.example.euskalmet.Room;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

import androidx.lifecycle.LiveData;
import androidx.room.Room;

import com.example.euskalmet.Room.DAO.StationDAO;
import com.example.euskalmet.Room.Entity.Station;

import java.util.List;

public class MeteoController {
    @SuppressLint("StaticFieldLeak")
    private static MeteoController meteoController;
    private LiveData<List<Station>> liveStationList;
    private StationDAO stationDAO;

    private MeteoController(Context context) {
        Context appContext = context.getApplicationContext();
        MeteoDatabase db = Room.databaseBuilder(
                        appContext,
                        MeteoDatabase.class,
                        "MeteoDB")
                .fallbackToDestructiveMigration()
                .build();
        stationDAO = db.stationDAO();
    }

    public static MeteoController getMeteoController(Context context) {
        if (meteoController == null) {
            meteoController = new MeteoController(context);
        }
        return meteoController;
    }

    public LiveData<List<Station>> getLiveStations() {
        HandlerThread getHandlerThread = new HandlerThread("GetHandlerThread");
        getHandlerThread.start();
        Looper getLooper = getHandlerThread.getLooper();
        Handler getHandler = new Handler(getLooper);
        getHandler.post(new Runnable() {
            @Override
            public void run() {
                liveStationList = stationDAO.getLiveStations();
            }
        });
        while (liveStationList == null) {
        }
        return liveStationList;
    }

    public void saveStations(List<Station> stationList) {
        HandlerThread insertHandlerThread = new HandlerThread("InsertHandlerThread");
        insertHandlerThread.start();
        Looper insertLooper = insertHandlerThread.getLooper();
        Handler insertHandler = new Handler(insertLooper);
        insertHandler.post(new Runnable() {
            @Override
            public void run() {
                stationDAO.insertStations(stationList);
            }
        });
    }
}
