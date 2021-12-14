package com.example.euskalmet;

import android.os.Bundle;

import com.example.euskalmet.EuskalmetData.ServerRequest;
import com.example.euskalmet.Room.Entity.Station;
import com.example.euskalmet.Room.MeteoController;
import com.example.euskalmet.Room.StationViewModel;
import com.google.android.material.tabs.TabLayout;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import com.example.euskalmet.ui.main.SectionsPagerAdapter;
import com.example.euskalmet.databinding.ActivityMainBinding;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private ServerRequest serverRequest;
    private StationViewModel stationViewModel;
    private MeteoController meteoController;
    private List<Station> stationList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        meteoController = MeteoController.getMeteoController(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        serverRequest = new ServerRequest(this);
        serverRequest.getStationList();
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager);
    }
}