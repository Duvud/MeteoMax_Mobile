package com.example.euskalmet.ui.main;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.euskalmet.MainActivity;
import com.example.euskalmet.R;
import com.example.euskalmet.Room.Entity.Reading;
import com.example.euskalmet.Room.Entity.Station;
import com.example.euskalmet.Room.MeteoController;
import com.example.euskalmet.Room.ViewModel.EnabledStationViewModel;
import com.example.euskalmet.ui.main.recyclerAdapter.EnabledStationsAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class EnabledStationsFragment extends Fragment {

    private EnabledStationViewModel enabledStationViewModel;
    private Context mainContext;
    private MeteoController meteoController;
    List<Station> enabledStationList = new ArrayList<>();
    EnabledStationsAdapter enabledStationsAdapter;
    RecyclerView recyclerView;

    EnabledStationsFragment(Context mainContext) {
        this.mainContext = mainContext;
        meteoController = MeteoController.getMeteoController(mainContext);
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        View view = inflater.inflate(R.layout.enabled_station_fragment, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        enabledStationsAdapter = new EnabledStationsAdapter(view.getContext(), this.enabledStationList);
        recyclerView = view.findViewById(R.id.enabled_stations_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(),LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(enabledStationsAdapter);
        recyclerView.setHasFixedSize(true);
        initViewModel();
    }

    public void initViewModel() {
        enabledStationViewModel = new ViewModelProvider(requireActivity()).get(EnabledStationViewModel.class);
        enabledStationViewModel.setMeteoController(this.meteoController);
        final Observer<List<Station>> enabledStationsObserver = new Observer<List<Station>>() {
            @Override
            public void onChanged(@Nullable final List<Station> stationList) {
                enabledStationsAdapter.updateEnabledStations(stationList);
            }
        };
        final Observer<List<Reading>> readingsObserver = new Observer<List<Reading>>() {
            @Override
            public void onChanged(List<Reading> readings) {
                enabledStationsAdapter.updateReadings(readings);
            }
        };
        enabledStationViewModel.getReadings().observe(getViewLifecycleOwner(), readingsObserver);
        enabledStationViewModel.getEnabledStations().observe(getViewLifecycleOwner(), enabledStationsObserver);
    }
}
