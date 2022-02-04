package com.example.euskalmet.EuskalmetData;

import android.content.Context;
import static java.nio.charset.StandardCharsets.*;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import com.example.euskalmet.Room.Entity.Reading;
import com.example.euskalmet.Room.Entity.Station;
import com.example.euskalmet.Room.MeteoController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

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

    public List<List> parseDayStationData(String stationData, String stationID, String dataType) {
                byte[] byteData = stationData.getBytes(ISO_8859_1);
                String stationUtf8Data = new String(byteData, UTF_8);
                List<List> encapsulatedDataArray = new ArrayList<>();
                try {
                    JSONObject stationJsonObject = new JSONObject(stationUtf8Data);
                    for(int i=0; i<stationJsonObject.names().length(); i++) {
                        String dataSectionType = stationJsonObject.
                                getJSONObject((String) stationJsonObject.names().get(i))
                                .getString("name");
                        JSONObject dataJSONObject = stationJsonObject.
                                getJSONObject((String) stationJsonObject.names().get(i)).getJSONObject("data");
                        if(dataSectionType.equals(dataType)){
                            encapsulatedDataArray = parseStationDayData(dataJSONObject);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
        return encapsulatedDataArray;
    }

    public void parseLastStationData(String stationData, String stationID) {
        HandlerThread handlerThread = new HandlerThread("ParseStationDataHandlerThread");
        handlerThread.start();
        Looper looper = handlerThread.getLooper();
        Handler handler = new Handler(looper);
        handler.post(new Runnable() {
            @Override
            public void run() {
                byte[] byteData = stationData.getBytes(ISO_8859_1);
                String stationUtf8Data = new String(byteData, UTF_8);
                List<Reading> readingList = new ArrayList<>();
                try {
                    JSONObject stationJsonObject = new JSONObject(stationUtf8Data);
                    for(int i=0; i<stationJsonObject.names().length(); i++) {
                        String dataSectionType = stationJsonObject.
                                getJSONObject((String) stationJsonObject.names().get(i))
                                .getString("name");
                        JSONObject dataJSONObject = stationJsonObject.
                                getJSONObject((String) stationJsonObject.names().get(i)).getJSONObject("data");
                        switch (dataSectionType) {
                            case "temperature" :
                                ArrayList lastTemperatureArray = parseStationJsonData(dataJSONObject);
                                Reading newReading = new Reading();
                                newReading.readingData = (double) lastTemperatureArray.get(1);
                                newReading.readingDateTime = (String) lastTemperatureArray.get(0);
                                newReading.stationId = stationID;
                                newReading.readingType = "temperature";
                                newReading.readingId = stationID+"T";
                                readingList.add(newReading);
                                break;
                            case "precipitation":
                                ArrayList lastPrecipitationArray = parseStationJsonData(dataJSONObject);
                                Reading precipitationReading = new Reading();
                                precipitationReading.readingData = (double) lastPrecipitationArray.get(1);
                                precipitationReading.readingDateTime = (String) lastPrecipitationArray.get(0);
                                precipitationReading.stationId = stationID;
                                precipitationReading.readingType = "precipitation";
                                precipitationReading.readingId = stationID+"P";
                                readingList.add(precipitationReading);
                                break;
                            case "humidity":
                                ArrayList lastHumidityArray = parseStationJsonData(dataJSONObject);
                                Reading humidityReading = new Reading();
                                humidityReading.readingData = (double) lastHumidityArray.get(1);
                                humidityReading.readingDateTime = (String) lastHumidityArray.get(0);
                                humidityReading.stationId = stationID;
                                humidityReading.readingType = "humidity";
                                humidityReading.readingId = stationID+"H";
                                readingList.add(humidityReading);
                                break;
                            case "mean_speed":
                                ArrayList lastSpeedArray = parseStationJsonData(dataJSONObject);
                                Reading speedReading = new Reading();
                                speedReading.readingData = (double) lastSpeedArray.get(1);
                                speedReading.readingDateTime = (String) lastSpeedArray.get(0);
                                speedReading.stationId = stationID;
                                speedReading.readingType = "speed";
                                speedReading.readingId = stationID+"S";
                                readingList.add(speedReading);
                                break;
                        }
                    }
                    meteoController.saveReadings(readingList);
                    System.out.println("Reading list " + readingList.size() + " aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public ArrayList parseStationDayData(JSONObject dataJSONObject) throws JSONException {
        JSONObject rawDataJSONObject = dataJSONObject.
                getJSONObject((String)dataJSONObject.names().get(0));
        ArrayList<String> lectureTimeArray = new ArrayList<>();
        ArrayList<Double> lectureDataArray = new ArrayList<>();
        for(int z = 0 ; z< rawDataJSONObject.names().length(); z++){
            lectureTimeArray.add((String) rawDataJSONObject.names().get(z));
        }
        Collections.sort(lectureTimeArray);
        for(int z = 0 ; z < lectureTimeArray.size() ; z++) {
            lectureDataArray.add(rawDataJSONObject.getDouble(lectureTimeArray.get(z)));
        }
        ArrayList encapsulationArray = new ArrayList();
        encapsulationArray.add(lectureTimeArray);
        encapsulationArray.add(lectureDataArray);
        return encapsulationArray;
    }

    public ArrayList parseStationJsonData(JSONObject dataJSONObject) throws JSONException {
        JSONObject rawDataJSONObject = dataJSONObject.
                getJSONObject((String)dataJSONObject.names().get(0));
        ArrayList<String> lectureTimeArray = new ArrayList<>();
        ArrayList<Double> lectureDataArray = new ArrayList<>();
        for(int z = 0 ; z< rawDataJSONObject.names().length(); z++){
            lectureTimeArray.add((String) rawDataJSONObject.names().get(z));
        }
        Collections.sort(lectureTimeArray);
        for(int z = 0 ; z < lectureTimeArray.size() ; z++) {
            lectureDataArray.add(rawDataJSONObject.getDouble(lectureTimeArray.get(z)));
        }
        ArrayList lastDataArrayList = new ArrayList();
        lastDataArrayList.add(lectureTimeArray.get(lectureTimeArray.size()-1));
        lastDataArrayList.add(lectureDataArray.get(lectureDataArray.size()-1));
        return lastDataArrayList;
    }

    public void parseStationList(String stationList) {
        HandlerThread handlerThread = new HandlerThread("ParseStationListHandlerThread");

        handlerThread.start();
        Looper looper = handlerThread.getLooper();
        Handler handler = new Handler(looper);
        handler.post(new Runnable() {
            @Override
            public void run() {
                DataParser.this.stationListText = stationList;
                try {
                    stationJsonArray = new JSONArray(stationList);
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
                            newStation.enabled = false;
                            stationArrayList.add(newStation);
                        }
                    }
                    meteoController.saveStations(stationArrayList);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public boolean checkExists(String stationId) {
        if(oldStationList == null){
            return false;
        }
        for (int i=0; i<oldStationList.size(); i++) {
            if(oldStationList.get(i).id.equals(stationId)) {
                return true;
            }
        }
        return false;
    }
}
