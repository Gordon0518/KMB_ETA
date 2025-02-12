package com.example.kmb;
import  com.example.kmb.adapter.EtaAdapter;
import com.example.kmb.model.stopETA;


import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kmb.api.ApiService;
import com.example.kmb.model.stopETA;
import com.example.kmb.model.stopList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class busETA extends AppCompatActivity {

    private RecyclerView rvStopETAs;
    private EtaAdapter etaAdapter;
    private ApiService apiService;
    private List<stopETA> stopETAList = new ArrayList<>();
    private String busNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_bus_eta);

        busNumber = getIntent().getStringExtra("BUS_NUMBER");
        TextView tvRouteNumber = findViewById(R.id.tvRouteNumber);
        tvRouteNumber.setText(busNumber);

        rvStopETAs = findViewById(R.id.rvStopETAs);
        rvStopETAs.setLayoutManager(new LinearLayoutManager(this));
        etaAdapter = new EtaAdapter(stopETAList);
        rvStopETAs.setAdapter(etaAdapter);

        apiService = new ApiService(this);
        fetchRouteStopsAndETAs();




        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void fetchRouteStopsAndETAs() {
        apiService.getStopIds(busNumber, new ApiService.StopCallback() {
            @Override
            public void onStopsReceived(List<stopList> stops) {
                fetchETAsForStops(stops);
            }

            @Override
            public void onError(String errorMessage) {
                Toast.makeText(busETA.this, errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void fetchETAsForStops(List<stopList> stops) {

        List<stopList> sortedStops = new ArrayList<>(stops);
        Collections.sort(sortedStops, (a, b) -> a.getSeq() - b.getSeq());
        AtomicInteger counter = new AtomicInteger(stops.size());

        for(stopList stop : stops) {
            apiService.getETAs(stop.getId(), busNumber, 1, new ApiService.ETACallback() {
                @Override
                public void onETAsReceived(List<String> etas) {
                    synchronized(stopETAList) {
                        stopETAList.add(new stopETA(stop.getName_en(), etas, stop.getSeq()));

                        if(counter.decrementAndGet() == 0) {

                            Collections.sort(stopETAList, (a, b) -> a.getSeq() - b.getSeq());
                            runOnUiThread(() -> {
                                Collections.sort(stopETAList, Comparator.comparingInt(a -> stops.indexOf(a.getStopName())));
                                etaAdapter.notifyDataSetChanged();
                            });
                        }
                    }
                }

                @Override
                public void onError(String errorMessage) {

                    counter.decrementAndGet();
                }
            });
        }
    }
}