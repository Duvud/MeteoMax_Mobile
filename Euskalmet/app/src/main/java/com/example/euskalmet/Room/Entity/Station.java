package com.example.euskalmet.Room.Entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "station")
public class Station {
    @PrimaryKey
    @NonNull
    public String id;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "altitude")
    public String altitude;

    @ColumnInfo(name = "x")
    public double x;

    @ColumnInfo(name = "y")
    public double y;

    @ColumnInfo(name = "municipality")
    public String municipality;

    @NonNull
    @ColumnInfo(name = "enabled", defaultValue = "false")
    public Boolean enabled;

    @NonNull
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAltitude() {
        return altitude;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public String getMunicipality() {
        return municipality;
    }
}
