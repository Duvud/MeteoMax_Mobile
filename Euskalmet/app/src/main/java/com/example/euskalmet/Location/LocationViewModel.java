package com.example.euskalmet.Location;

import android.app.Application;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.euskalmet.Room.Entity.Station;
import com.example.euskalmet.Room.MeteoController;

import java.util.ArrayList;
import java.util.List;

public class LocationViewModel extends AndroidViewModel {
    private MutableLiveData<List<Double>> location;
    private LocationRequest locationRequest;

    public LocationViewModel(@NonNull Application application) {
        super(application);
    }

    public void setLocationRequest (LocationRequest locationRequest) {
        this.locationRequest = locationRequest;
    }

    public LiveData<List<Double>> getLocation() {
        if(location == null){
                location = new MutableLiveData<>();
                loadLocation();
        }
        return location;
    }
    private void loadLocation() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(!locationRequest.locationInited) {

                }
                List<Double> coordinatesList = new ArrayList<>();
                coordinatesList.add(locationRequest.latitude);
                coordinatesList.add(locationRequest.longitude);
                location.postValue(coordinatesList);
            }
        }).start();
    }

}
