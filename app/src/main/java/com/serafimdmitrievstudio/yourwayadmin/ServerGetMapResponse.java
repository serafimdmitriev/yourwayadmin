package com.serafimdmitrievstudio.yourwayadmin;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.Expose;

class ServerGetMapResponse {
    @Expose
    private int id;
    private short type;
    private double latitude1;
    private double longitude1;
    private double latitude2;
    private double longitude2;
    private boolean wheelchairAccessible;
    private boolean electricWheelchairAccessible;
    private short grade;
    private short generalState;

    int getId() {
        return id;
    }
    short getType() {
        return type;
    }
    LatLng getLatLng1() {
        return new LatLng(latitude1, longitude1);
    }
    LatLng getLatLng2() {
        try {
            return new LatLng(latitude2, longitude2);
        } catch (Exception e) {
            Log.write("Nihuya sebe " + e.getMessage());
            return null;
        }
    }
}

