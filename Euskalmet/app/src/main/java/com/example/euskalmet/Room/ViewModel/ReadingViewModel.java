package com.example.euskalmet.Room.ViewModel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.euskalmet.Room.Entity.Reading;
import com.example.euskalmet.Room.MeteoController;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ReadingViewModel extends AndroidViewModel {
    private LiveData<List<Reading>> readingList;
    private MeteoController meteoController;

    public ReadingViewModel(@NonNull @NotNull Application application) {
        super(application);
    }

    public void setMeteoController(MeteoController meteoController) {
        this.meteoController = meteoController;
    }

    public LiveData<List<Reading>> getStationReadings(String stationID) {
        readingList = meteoController.getLiveReadings(stationID);
        if(readingList.getValue() != null && readingList.getValue().size() == 0){
            try {
                Thread.sleep(200);
                readingList = meteoController.getLiveReadings(stationID);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return readingList;
    }
}
