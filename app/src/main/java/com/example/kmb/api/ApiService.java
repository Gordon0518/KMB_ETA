package com.example.kmb.api;
import static com.example.kmb.api.jsonHandler.parseJsonAll;
import com.example.kmb.model.stopSeq;


import android.content.Context;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.kmb.model.busList;
import com.example.kmb.model.stopList;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ApiService {
    private static  Context context;
    public interface BusCallback {
        void onBusesReceived(List<busList> buses);
        void onError(String errorMessage);
    }

    public interface StopCallback {
        void onStopsReceived(List<stopList> stops);
        void onError(String errorMessage);
    }

    public interface ETACallback {
        void onETAsReceived(List<String> etas);
        void onError(String errorMessage);
    }

    public interface StopDetailsCallback {
        void onStopDetailsReceived(List<stopList> stopDetails);
        void onError(String errorMessage);
    }





    public ApiService(Context context) {
        this.context = context.getApplicationContext();  // Use application context
    }

    public static void getbus(BusCallback callback) {
        String url = "https://data.etabus.gov.hk/v1/transport/kmb/route/";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,

                response -> {
                    try {
                        List<busList> buses = parseJsonAll(response);
                        callback.onBusesReceived(buses);
                    } catch (Exception e) {
                        callback.onError("Parsing error: " + e.getMessage());
                    }
                },
                error -> callback.onError("Network error: " + error.getMessage()));
        Volley.newRequestQueue(context).add(request);
    }



    public void getStopIds(String routeNumber, StopCallback callback) {
        String url = "https://data.etabus.gov.hk/v1/transport/kmb/route-stop/"
                + routeNumber + "/outbound/1";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONArray data = response.getJSONArray("data");
                        List<stopSeq> stops = new ArrayList<>();

                        for (int i = 0; i < data.length(); i++) {
                            JSONObject item = data.getJSONObject(i);
                            stops.add(new stopSeq(item.getString("stop"), item.getInt("seq")));
                        }

                        stops.sort((a, b) -> a.seq - b.seq);
                        getStopDetails(stops, callback);
                    } catch (JSONException e) {
                        callback.onError("Parsing error: " + e.getMessage());
                    }
                },
                error -> callback.onError("Network error: " + error.getMessage()));

        Volley.newRequestQueue(context).add(request);
    }

    private void getStopDetails(List<stopSeq> stopIds, StopCallback callback) {
        List<stopList> stopDetails = new ArrayList<>();
        AtomicInteger counter = new AtomicInteger(stopIds.size());

        if (stopIds.isEmpty()) {
            callback.onError("No stop IDs found");
            return;
        }

        for (stopSeq stopseq : stopIds) {
            String url = "https://data.etabus.gov.hk/v1/transport/kmb/stop/" + stopseq.getStopid();

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                    response -> {
                        try {
                            JSONObject data = response.getJSONObject("data");
                            stopDetails.add(new stopList(
                                    stopseq.getStopid(),
                                    data.getString("name_tc"),
                                    data.getString("name_en"),
                                    stopseq.getSeq()
                            ));

                            if (counter.decrementAndGet() == 0) {
                                Collections.sort(stopDetails, (a, b) -> a.getSeq() - b.getSeq());
                                callback.onStopsReceived(stopDetails);
                            }
                        } catch (JSONException e) {
                            callback.onError("Stop details error: " + e.getMessage());
                        }
                    },
                    error -> callback.onError("Network error: " + error.getMessage()));

            Volley.newRequestQueue(context).add(request);
        }
    }

    public void getETAs(String stopId, String routeNumber, int serviceType, ETACallback callback) {
        String url = "https://data.etabus.gov.hk/v1/transport/kmb/eta/"
                + stopId + "/" + routeNumber + "/" + serviceType;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        List<String> etas = new ArrayList<>();
                        JSONArray data = response.getJSONArray("data");

                        for(int i = 0; i < Math.min(3, data.length()); i++) {
                            JSONObject etaItem = data.getJSONObject(i);
                            String etaTime = etaItem.getString("eta");
                            etas.add(formatEtaTime(etaTime));
                        }

                        callback.onETAsReceived(etas);
                    } catch (JSONException e) {
                        callback.onError("ETA parsing error: " + e.getMessage());
                    }
                },
                error -> callback.onError("ETA request error: " + error.getMessage()));

        Volley.newRequestQueue(context).add(request);
    }

    private String formatEtaTime(String isoTime) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");
            Date date = inputFormat.parse(isoTime);
            SimpleDateFormat outputFormat = new SimpleDateFormat("HH:mm");
            return outputFormat.format(date);
        } catch (Exception e) {
            return "N/A";
        }
    }

}
