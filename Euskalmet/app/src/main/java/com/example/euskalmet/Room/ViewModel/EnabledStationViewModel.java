package com.example.euskalmet.Room.ViewModel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.euskalmet.Room.Entity.Station;
import com.example.euskalmet.Room.MeteoController;

import java.util.List;

public class EnabledStationViewModel extends AndroidViewModel {
    private LiveData<List<Station>> enabledStationList;
    private MeteoController meteoController;

    public EnabledStationViewModel(@NonNull Application application) {
        super(application);
    }

    public void setMeteoController(MeteoController meteoController) {
        this.meteoController = meteoController;
    }


    public LiveData<List<Station>> getEnabledStations() {
        enabledStationList = meteoController.getEnabledLiveStations();
        if(enabledStationList.getValue() != null && enabledStationList.getValue().size() == 0){
            try {
                Thread.sleep(200);
                enabledStationList = meteoController.getEnabledLiveStations();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return enabledStationList;
    }
}
