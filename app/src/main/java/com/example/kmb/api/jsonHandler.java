package com.example.kmb.api;
import com.example.kmb.model.busList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class jsonHandler {
    public static List<busList> parseJsonAll(JSONObject jsonObject) throws JSONException {
        List<busList> buslist = new ArrayList<>();
        JSONArray dataArray = jsonObject.getJSONArray("data");

        for (int i = 0; i < dataArray.length(); i++) {
            JSONObject dataObject = dataArray.getJSONObject(i);
            String route = dataObject.getString("route");
            String orig = dataObject.getString("orig_en");
            String dest = dataObject.getString("dest_en");
            buslist.add(new busList(route, orig, dest));
        }
        return buslist;
    }

//   public static List<stopIDList> parseJsonStopID(JSONObject jsonObject) throws JSONException{
//        List<stopIDList> stopIDlist = new ArrayList<>();
//        JSONArray dataArray = jsonObject.getJSONArray("data");
//
//        for (int i = 0; i < dataArray.length(); i++){
//            JSONObject dataObject = dataArray.getJSONObject(i);
//            String stopID = dataObject.getString("stop");
//            stopIDlist.add(new stopIDList(stopID));
//        }
//        return stopIDlist;
//   }
}
