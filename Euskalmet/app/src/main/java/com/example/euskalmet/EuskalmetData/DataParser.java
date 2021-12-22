package com.example.euskalmet.EuskalmetData;

import android.app.Activity;
import android.content.Context;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import com.example.euskalmet.Room.Entity.Station;
import com.example.euskalmet.Room.MeteoController;

import com.example.euskalmet.Room.StationViewModel;
import com.example.euskalmet.ui.main.MapsFragment;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DataParser {
    private String stationListText;
    private JSONArray stationJsonArray;
    private ArrayList<Station> stationArrayList = new ArrayList<>();
    private List<Station> oldStationList ;
    private Context mainContext;
    private MeteoController meteoController;

    public DataParser(Context mainContext, List<Station> oldStationList) {
        this.mainContext = mainContext;
        this.oldStationList = oldStationList;
        meteoController = MeteoController.getMeteoController(mainContext);
    }

    public DataParser(String response) {
    }

    public void parseStationList(String stationList) {
        HandlerThread handlerThread = new HandlerThread("ParseHandlerThread");
        handlerThread.start();
        Looper looper = handlerThread.getLooper();
        Handler handler = new Handler(looper);
        handler.post(new Runnable() {
            @Override
            public void run() {
                DataParser.this.stationListText = stationList;
                try {
                    stationJsonArray = new JSONArray(stationListText);
                    for(int i=0; i<stationJsonArray.length(); i++) {
                        JSONObject stationJsonObject = stationJsonArray.getJSONObject(i);
                        if(stationJsonObject.getString("stationType").equals("METEOROLOGICAL")){
                            Station newStation = new Station();
                            newStation.id = stationJsonObject.getString("id");
                            newStation.name = stationJsonObject.getString("name");
                            newStation.altitude = stationJsonObject.getString("altitude");
                            newStation.x = stationJsonObject.getDouble("x");
                            newStation.y = stationJsonObject.getDouble("y");
                            newStation.municipality = stationJsonObject.getString("municipality");
                            newStation.enabled = checkEnabled(newStation.id);
                            stationArrayList.add(newStation);
                            System.out.println(checkEnabled(newStation.id) + " checkenabled return");
                        }
                    }
                    meteoController.saveStations(stationArrayList);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public boolean checkEnabled(String stationId) {
        for (int i=0; i<oldStationList.size(); i++) {
            if(oldStationList.get(i).id.equals(stationId)) {
                System.out.println(oldStationList.get(i).enabled);
                return oldStationList.get(i).enabled;
            }
        }
        return false;
    }
}
