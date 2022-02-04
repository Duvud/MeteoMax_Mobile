package com.example.euskalmet.ui.main.fragment;

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
import com.example.euskalmet.R;
import com.example.euskalmet.Room.Entity.Station;
import com.example.euskalmet.Room.MeteoController;
import com.example.euskalmet.Room.ViewModel.StationViewModel;
import com.example.euskalmet.ui.main.recyclerAdapter.StationListRecyclerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class StationListFragment extends Fragment {

    private StationViewModel stationViewModel;
    private Context mainContext;
    private MeteoController meteoController;
    List<Station> stationList = new ArrayList<>();
    StationListRecyclerAdapter stationListRecyclerAdapter;
    RecyclerView recyclerView;

    public StationListFragment(Context mainContext) {
        super();
        this.mainContext = mainContext;
        meteoController = MeteoController.getMeteoController(mainContext);
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        View view = inflater.inflate(R.layout.station_list_fragment, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        stationListRecyclerAdapter = new StationListRecyclerAdapter(view.getContext(), this.stationList);
        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(),LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(stationListRecyclerAdapter);
        recyclerView.setHasFixedSize(true);
        initViewModel();
    }

    public void initViewModel() {
        stationViewModel = new ViewModelProvider(requireActivity()).get(StationViewModel.class);
        stationViewModel.setMeteoController(this.meteoController);
        final Observer<List<Station>> stationListObserver = new Observer<List<Station>>() {
            @Override
            public void onChanged(@Nullable final List<Station> stationList) {
                stationListRecyclerAdapter.updateMessageList(stationList);
            }
        };
        stationViewModel.getStations().observe(getViewLifecycleOwner(), stationListObserver);
    }
}