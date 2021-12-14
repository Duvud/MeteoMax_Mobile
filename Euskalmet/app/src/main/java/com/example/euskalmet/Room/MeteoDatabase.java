package com.example.euskalmet.Room;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import com.example.euskalmet.Room.DAO.StationDAO;
import com.example.euskalmet.Room.Entity.Station;

@Database(entities = {Station.class}, version = 2)
public abstract class MeteoDatabase extends RoomDatabase {
    public abstract StationDAO stationDAO();
}
