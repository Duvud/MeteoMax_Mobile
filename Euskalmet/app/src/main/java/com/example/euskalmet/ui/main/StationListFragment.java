package com.example.euskalmet.ui.main;

import android.content.Context;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.euskalmet.MainActivity;
import com.example.euskalmet.R;
import com.example.euskalmet.Room.Entity.Station;
import com.example.euskalmet.Room.MeteoController;
import com.example.euskalmet.Room.StationViewModel;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class StationListFragment extends Fragment {

    private StationViewModel stationViewModel;
    private Context mainContext;
    private MeteoController meteoController;
    List<Station> stationList = new ArrayList<>();
    RecyclerAdapter recyclerAdapter;
    RecyclerView recyclerView;

    public static StationListFragment newInstance(Context mainContext) {
        return new StationListFragment(mainContext);
    }

    StationListFragment(Context mainContext) {
        this.mainContext = mainContext;
        meteoController = MeteoController.getMeteoController(mainContext);
    }


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        View view = inflater.inflate(R.layout.station_list_fragment, container, false);
        Station newStation = new Station();
        newStation.id= "AAAAA";
        newStation.name= "AAAAA";
        stationList.add(newStation);
        recyclerAdapter = new RecyclerAdapter(view.getContext(), this.stationList);
        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(),LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.setHasFixedSize(true);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViewModel();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void initViewModel() {
        stationViewModel = new ViewModelProvider(requireActivity()).get(StationViewModel.class);
        stationViewModel.setMeteoController(this.meteoController);
        final Observer<List<Station>> stationListObserver = new Observer<List<Station>>() {
            @Override
            public void onChanged(@Nullable final List<Station> stationList) {
                System.out.println("Size from fragment");
                System.out.println(stationList.size());
                StationListFragment.this.stationList = stationList;
                recyclerAdapter.updateMessageList(stationList);
                recyclerView.scrollToPosition(stationList.size()-1);
            }
        };
        stationViewModel.getStations().observe(getViewLifecycleOwner(), stationListObserver);
    }

}