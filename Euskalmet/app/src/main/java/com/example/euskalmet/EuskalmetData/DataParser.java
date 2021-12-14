package com.example.euskalmet.EuskalmetData;

import android.content.Context;

import com.example.euskalmet.Room.Entity.Station;
import com.example.euskalmet.Room.MeteoController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DataParser {
    private String stationListText;
    private JSONArray stationJsonArray;
    private ArrayList<Station> stationArrayList = new ArrayList<>();
    private Context mainContext;
    private MeteoController meteoController;

    public DataParser(Context mainContext) {
        this.mainContext = mainContext;
        meteoController = MeteoController.getMeteoController(mainContext);
    }

    public DataParser(String response) {
    }


    public void parseStationList(String stationList) {
        this.stationListText = stationList;
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
                    stationArrayList.add(newStation);
                }
            }
            meteoController.saveStations(stationArrayList);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void parseStationData(String response) {
        System.out.println(response);
    }
}
