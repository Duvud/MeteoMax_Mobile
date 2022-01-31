package com.example.euskalmet.Room.Entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.sql.Date;

@Entity(tableName = "reading")
public class Reading {
    @PrimaryKey
    @NonNull
    public String readingId;
    public String stationId;
    public String readingType;
    public double readingData;
    public String readingDateTime;
}

