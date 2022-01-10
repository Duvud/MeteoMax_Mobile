package com.example.euskalmet.ui.main;

import android.app.AlertDialog;
import android.content.DialogInterface;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.example.euskalmet.Location.LocationController;
import com.example.euskalmet.Location.LocationViewModel;
import com.example.euskalmet.R;
import com.example.euskalmet.Room.Entity.Station;
import com.example.euskalmet.Room.MeteoController;
import com.example.euskalmet.Room.StationViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.*;

import java.util.HashMap;
import java.util.List;

import static java.sql.DriverManager.println;


public class MapsFragment extends Fragment implements GoogleMap.OnMarkerClickListener  {
    private StationViewModel stationViewModel;
    private LocationViewModel locationViewModel;
    private MeteoController meteoController;
    private HashMap<String, String> stationIDs = new HashMap<>();
    private MarkerOptions locationMarker;
    private GoogleMap googleMap;
    private List<Station> stationList;
    private LocationController locationController;


    private OnMapReadyCallback callback = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {
            MapsFragment.this.googleMap = googleMap;
            MapsFragment.this.addMarkers(MapsFragment.this.stationList);
            MapsFragment.this.googleMap.setOnMarkerClickListener(MapsFragment.this);
        }
    };

    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        String stationId = stationIDs.get(marker.getTitle());
        System.out.println(stationId);
        return false;
    }

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
        locationController = LocationController.getLocationRequest(getContext());
        meteoController = MeteoController.getMeteoController(getContext());
        locationViewModel = new ViewModelProvider(requireActivity()).get(LocationViewModel.class);
        locationViewModel.setLocationRequest(this.locationController);
        final Observer<List<Double>> locationObserver = new Observer<List<Double>>() {
            @Override
            public void onChanged(List<Double> doubles) {
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(doubles.get(0), doubles.get(1)), 11.0f));
                locationMarker = new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)).position(new LatLng(doubles.get(0), doubles.get(1))).title("Tú");
                googleMap.addMarker(locationMarker);
            }
        };
        locationViewModel.getLocation().observe(getViewLifecycleOwner(), locationObserver);
        stationViewModel = new ViewModelProvider(requireActivity()).get(StationViewModel.class);
        stationViewModel.setMeteoController(this.meteoController);
        final Observer<List<Station>> stationListObserver = new Observer<List<Station>>() {
            @Override
            public void onChanged(@Nullable final List<Station> stationList) {
                googleMap.clear();
                MapsFragment.this.stationList = stationList;
                addMarkers(stationList);
            }
        };
        stationViewModel.getStations().observe(getViewLifecycleOwner(), stationListObserver);
    }

    public void addMarkers(List<Station> stationList) {
        if(stationList != null){
            for(int i=0; i< stationList.size(); i++) {
                Station currentStation = stationList.get(i);
                LatLng newMarker = new LatLng(currentStation.getY(),currentStation.getX());
                BitmapDescriptor markerColor = currentStation.enabled ?
                        BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN) :
                        BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE);
                googleMap.addMarker(new MarkerOptions().icon(markerColor).position(newMarker).title(currentStation.getName()));
                stationIDs.put(currentStation.getName(), currentStation.getId());
            }
            if(locationMarker != null){
                googleMap.addMarker(locationMarker);
            }
            if (!locationController.locationInited) {
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(43.0345685061911, -2.417053097254997), 8.0f));
            }
        }
    }
}