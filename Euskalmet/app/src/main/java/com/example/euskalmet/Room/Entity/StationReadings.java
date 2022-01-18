package com.example.euskalmet.Room.Entity;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class StationReadings {
    @Embedded public Station station;
    @Relation(
            parentColumn = "id",
            entityColumn = "stationId"
    )
    public List<Reading> readingList;
}
