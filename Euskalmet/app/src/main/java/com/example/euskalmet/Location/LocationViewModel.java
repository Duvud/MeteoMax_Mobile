package com.example.euskalmet.Location;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

public class LocationViewModel extends AndroidViewModel {
    private MutableLiveData<List<Double>> location;
    private LocationController locationController;

    public LocationViewModel(@NonNull Application application) {
        super(application);
    }

    public void setLocationRequest (LocationController locationController) {
        this.locationController = locationController;
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
                while(!locationController.locationInited) {

                }
                List<Double> coordinatesList = new ArrayList<>();
                coordinatesList.add(locationController.latitude);
                coordinatesList.add(locationController.longitude);
                location.postValue(coordinatesList);
            }
        }).start();
    }

}
