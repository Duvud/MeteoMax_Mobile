package com.example.euskalmet.Room.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.euskalmet.Room.Entity.Station;

import java.util.List;

@Dao
public interface StationDAO {
    @Query("SELECT * FROM station")
    List<Station> getStations();

    @Query("SELECT * FROM station")
    LiveData<List<Station>> getLiveStations();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertStations(List<Station> stationList);
}
