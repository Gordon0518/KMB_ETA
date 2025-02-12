package com.example.kmb;
import static com.example.kmb.api.ApiService.getbus;

import com.example.kmb.adapter.busAdapter;
import com.example.kmb.model.busList;
import com.example.kmb.api.ApiService;
import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.appcompat.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements busAdapter.OnItemClickListener {

    private RecyclerView recyclerView;
    private busAdapter adapter;
    private ApiService apiService;
    private SearchView searchView;
    private TextView emptyState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        apiService = new ApiService(this);


        recyclerView = findViewById(R.id.busList);
        searchView = findViewById(R.id.searchView);
        emptyState = findViewById(R.id.emptyState);


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new busAdapter(new ArrayList<>(), this);  // Start with empty list
        recyclerView.setAdapter(adapter);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return true;
            }
        });

        loadBusData();


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void loadBusData() {
        apiService.getbus(new ApiService.BusCallback() {
            @Override
            public void onBusesReceived(List<busList> buses) {
                runOnUiThread(() -> {
                    adapter.updateFullList(buses);
                    emptyState.setVisibility(buses.isEmpty() ? View.VISIBLE : View.GONE);
                });
            }

            @Override
            public void onError(String errorMessage) {
                runOnUiThread(() -> {
                    Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                    adapter.updateFullList(new ArrayList<>());
                    emptyState.setVisibility(View.VISIBLE);
                });
            }
        });
    }

    // In your onItemClick method
    @Override
    public void onItemClick(int position) {
        busList clickedItem = adapter.busList.get(position);
        Intent intent = new Intent(MainActivity.this, busETA.class);
        intent.putExtra("BUS_NUMBER", clickedItem.getBusNum());
        startActivity(intent);
    }
}