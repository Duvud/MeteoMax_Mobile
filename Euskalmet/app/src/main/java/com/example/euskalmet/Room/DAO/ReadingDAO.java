package com.example.euskalmet.Room.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.example.euskalmet.Room.Entity.Reading;

import java.util.List;

@Dao
public interface ReadingDAO {

    @Query("SELECT * FROM reading WHERE stationId = :stationID")
    LiveData<List<Reading>> getLiveStationReadings(String stationID);

    @Query("DELETE FROM reading WHERE stationId = :stationID")
    void deleteReadingsFromStation(String stationID);

    @Query("SELECT * FROM reading")
    LiveData<List<Reading>> getAllLiveReadings();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertReadings(List<Reading> readingList);
}
