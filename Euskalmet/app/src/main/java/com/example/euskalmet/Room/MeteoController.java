package com.example.euskalmet.Room;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

import androidx.lifecycle.LiveData;
import androidx.room.Room;

import com.example.euskalmet.EuskalmetData.ServerRequest;
import com.example.euskalmet.Room.DAO.ReadingDAO;
import com.example.euskalmet.Room.DAO.StationDAO;
import com.example.euskalmet.Room.Entity.Reading;
import com.example.euskalmet.Room.Entity.Station;

import java.util.List;

public class MeteoController {
    @SuppressLint("StaticFieldLeak")
    private static MeteoController meteoController;
    private LiveData<List<Station>> liveStationList;
    private LiveData<List<Station>> liveEnabledStations;
    private LiveData<List<Reading>> liveReadingList;
    private List<Station> stationList;
    private ServerRequest serverRequest;
    private StationDAO stationDAO;
    private ReadingDAO readingDAO;
    private Context appContext;

    private MeteoController(Context context) {
        appContext = context.getApplicationContext();
        MeteoDatabase db = Room.databaseBuilder(
                        appContext,
                        MeteoDatabase.class,
                        "MeteoDB")
                .fallbackToDestructiveMigration()
                .build();
        stationDAO = db.stationDAO();
        readingDAO = db.readingDAO();
    }

    public static MeteoController getMeteoController(Context context) {
        if (meteoController == null) {
            meteoController = new MeteoController(context);
        }
        return meteoController;
    }

    public void updateReadings() {
        HandlerThread getHandlerThread = new HandlerThread("UpdateReadingsHandlerThread");
        getHandlerThread.start();
        Looper getLooper = getHandlerThread.getLooper();
        Handler getHandler = new Handler(getLooper);
        getHandler.post(new Runnable() {
            @Override
            public void run() {
                while (stationList == null) {
                }
                serverRequest = ServerRequest.getServerRequest(appContext, stationList);
                serverRequest.updateReadings(stationList);
            }
        });
    }

    public LiveData<List<Station>> getLiveStations() {
        HandlerThread getHandlerThread = new HandlerThread("GetHandlerThread");
        getHandlerThread.start();
        Looper getLooper = getHandlerThread.getLooper();
        Handler getHandler = new Handler(getLooper);
        getHandler.post(new Runnable() {
            @Override
            public void run() {
                stationList = stationDAO.getEnabledStations();
                liveStationList = stationDAO.getLiveStations();
            }
        });
        while (liveStationList == null) {
        }
        return liveStationList;
    }

    public List<List> getDateReadings(String date, String stationd, String dataType) {
        serverRequest = ServerRequest.getServerRequest(appContext, stationList);
        List dateReadings =  serverRequest.getDateReadings(date, stationd, dataType);
        return dateReadings;
    }

    public LiveData<List<Station>> getEnabledLiveStations() {
        HandlerThread getHandlerThread = new HandlerThread("GetEnabledHandlerThread");
        getHandlerThread.start();
        Looper getLooper = getHandlerThread.getLooper();
        Handler getHandler = new Handler(getLooper);
        getHandler.post(new Runnable() {
            @Override
            public void run() {
                liveEnabledStations = stationDAO.getEnabledLiveStations();
            }
        });
        while (liveEnabledStations == null) {
        }
        return liveEnabledStations;
    }

    public LiveData<List<Reading>> getLiveStationReadings(String stationID) {
        HandlerThread getHandlerThread = new HandlerThread("GetReadingsHandlerThread");
        getHandlerThread.start();
        Looper getLooper = getHandlerThread.getLooper();
        Handler getHandler = new Handler(getLooper);
        getHandler.post(new Runnable() {
            @Override
            public void run() {
                liveReadingList = readingDAO.getLiveStationReadings(stationID);
            }
        });
        while (liveReadingList == null) {
        }
        return liveReadingList;
    }

    public LiveData<List<Reading>> getLivenReadings() {
        HandlerThread getHandlerThread = new HandlerThread("GetReadingsHandlerThread");
        getHandlerThread.start();
        Looper getLooper = getHandlerThread.getLooper();
        Handler getHandler = new Handler(getLooper);
        getHandler.post(new Runnable() {
            @Override
            public void run() {
                liveReadingList = readingDAO.getAllLiveReadings();
            }
        });
        while (liveReadingList == null) {
        }
        return liveReadingList;
    }

    public void saveReadings(List<Reading> readingListList) {
        HandlerThread insertHandlerThread = new HandlerThread("InsertReadingsHandlerThread");
        insertHandlerThread.start();
        Looper insertLooper = insertHandlerThread.getLooper();
        Handler insertHandler = new Handler(insertLooper);
        insertHandler.post(new Runnable() {
            @Override
            public void run() {
                readingDAO.insertReadings(readingListList);
            }
        });
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

    public void deleteReadingsFromStation(String stationID) {
        HandlerThread insertHandlerThread = new HandlerThread("DeleteHandlerThread");
        insertHandlerThread.start();
        Looper insertLooper = insertHandlerThread.getLooper();
        Handler insertHandler = new Handler(insertLooper);
        insertHandler.post(new Runnable() {
            @Override
            public void run() {
                readingDAO.deleteReadingsFromStation(stationID);
            }
        });
    }


    public void changeEnabled(String stationId, Boolean enabled) {
        HandlerThread updateHandlerThread = new HandlerThread("UpdateHandlerThread");
        updateHandlerThread.start();
        Looper updateLooper = updateHandlerThread.getLooper();
        Handler insertHandler = new Handler(updateLooper);
        insertHandler.post(new Runnable() {
            @Override
            public void run() {
                stationDAO.changeStationEnabled(stationId, enabled);
            }
        });
    }
}
