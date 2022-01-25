package com.example.euskalmet.Activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.euskalmet.R;
import com.example.euskalmet.Room.MeteoController;
import com.example.euskalmet.databinding.ActivityMainBinding;


public class ReadingDataActivity extends AppCompatActivity {

    private MeteoController meteoController;
    private ActivityMainBinding binding;
    private String stationId;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        meteoController = MeteoController.getMeteoController(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            stationId = extras.getString("stationId");
        }
        setContentView(R.layout.activity_reading_data);
    }
}
