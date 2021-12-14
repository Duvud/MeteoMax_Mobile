package com.example.euskalmet.ui.main;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.example.euskalmet.R;
import com.example.euskalmet.Room.Entity.Station;
import com.example.euskalmet.Room.MeteoController;
import com.example.euskalmet.Room.StationViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;


public class MapsFragment extends Fragment {
    private StationViewModel stationViewModel;
    private MeteoController meteoController;
    private GoogleMap googleMap;
    private List<Station> stationList;

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        @Override
        public void onMapReady(GoogleMap googleMap) {
            MapsFragment.this.googleMap = googleMap;
            MapsFragment.this.addMarkers(MapsFragment.this.stationList);
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViewModel();
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }

    public void initViewModel() {
        meteoController = MeteoController.getMeteoController(getContext());
        stationViewModel = new ViewModelProvider(requireActivity()).get(StationViewModel.class);
        stationViewModel.setMeteoController(this.meteoController);
        final Observer<List<Station>> stationListObserver = new Observer<List<Station>>() {
            @Override
            public void onChanged(@Nullable final List<Station> stationList) {
                MapsFragment.this.stationList = stationList;
                System.out.println("Size from maps fragment inside class");
                System.out.println(MapsFragment.this.stationList.size());
            }
        };
        stationViewModel.getStations().observe(getViewLifecycleOwner(), stationListObserver);
    }

    public void addMarkers(List<Station> stationList) {
        for(int i=0; i< stationList.size(); i++) {
            Station currentStation = stationList.get(i);
            LatLng newMarker = new LatLng(currentStation.getY(),currentStation.getX());
            googleMap.addMarker(new MarkerOptions().position(newMarker).title(currentStation.getName()));
        }
        Station lastStation = stationList.get(stationList.size()-1);
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lastStation.getY(),lastStation.getX()), 12.0f));
    }
}