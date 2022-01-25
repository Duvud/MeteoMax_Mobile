package com.example.euskalmet.Activity;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import com.example.euskalmet.BackgroundService.UpdateReadingsUtil;
import com.example.euskalmet.EuskalmetData.ServerRequest;
import com.example.euskalmet.Room.Entity.Station;
import com.example.euskalmet.Room.MeteoController;
import com.example.euskalmet.Room.ViewModel.EnabledStationViewModel;
import com.example.euskalmet.Room.ViewModel.StationViewModel;
import com.google.android.material.tabs.TabLayout;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import com.example.euskalmet.ui.main.recyclerAdapter.SectionsPagerAdapter;
import com.example.euskalmet.databinding.ActivityMainBinding;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private ServerRequest serverRequest;
    private StationViewModel stationViewModel;
    private EnabledStationViewModel enabledStationViewModel;
    private MeteoController meteoController;
    private boolean listenInited = false;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        meteoController = MeteoController.getMeteoController(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        stationViewModel = new ViewModelProvider(this).get(StationViewModel.class);
        stationViewModel.setMeteoController(this.meteoController);
        enabledStationViewModel = new ViewModelProvider(this).get(EnabledStationViewModel.class);
        enabledStationViewModel.setMeteoController(this.meteoController);
        final Observer<List<Station>> stationListObserver = new Observer<List<Station>>() {
            @Override
            public void onChanged(@Nullable final List<Station> stationList) {
                if(!listenInited){
                    serverRequest = ServerRequest.getServerRequest(MainActivity.this, stationList);
                    serverRequest.getStationList();
                    listenInited = true;
                    SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(MainActivity.this, getSupportFragmentManager());
                    ViewPager viewPager = binding.viewPager;
                    viewPager.setAdapter(sectionsPagerAdapter);
                    TabLayout tabs = binding.tabs;
                    tabs.setupWithViewPager(viewPager);
                }
            }
        };
        stationViewModel.getStations().observe(this, stationListObserver);
        UpdateReadingsUtil.scheduleJob(getApplicationContext());
    }
}