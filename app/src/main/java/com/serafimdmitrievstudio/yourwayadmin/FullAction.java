package com.serafimdmitrievstudio.yourwayadmin;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

class FullAction {
    private ArrayList<MarkerOptions> pointsToAddOptions = new ArrayList<>();
    private ArrayList<PolylineOptions> segmentsToAddOptions = new ArrayList<>();
    private ArrayList<PolylineOptions> segmentsToDeleteOptions = new ArrayList<>();

    private ArrayList<Marker> pointsToAddMarkers = new ArrayList<>();
    private ArrayList<Polyline> segmentsToAddPolylines = new ArrayList<>();
    private ArrayList<Polyline> segmentsToDeletePolylines = new ArrayList<>();

    private ArrayList<LatLng> LatsAndLngs;

    private TextView actionNumberTextView = null;
    private long actionId;

    private View actionSplash = null;

    private String streetName ="";

    FullAction(Context context, long actionId){
        LayoutInflater inflater;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        actionSplash =  inflater.inflate(R.layout.action_number, null);

        actionNumberTextView = (TextView) actionSplash.findViewById(R.id.actionNumberTextView);
        actionNumberTextView.setText(Long.toString(actionId));

        this.actionId = actionId;

        LatsAndLngs = new ArrayList<>();
    }


    String getStreetName() {
        return streetName;
    }

    void addPointToAdd(Short type, Double latitude, Double longitude) {
        if (type != null && latitude != null && longitude != null) {
            if (type == MapItemType.Lift)
                pointsToAddOptions.add(new MarkerOptions()
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_lift_small))
                        .anchor((float) 0.5, (float) 0.5)
                        .position(new LatLng(latitude, longitude)));
            if (type == MapItemType.Ramp)
                pointsToAddOptions.add(new MarkerOptions()
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_ramp_small))
                        .anchor((float) 0.5, (float) 0.5)
                        .position(new LatLng(latitude, longitude)));

            LatsAndLngs.add(new LatLng(latitude, longitude));
        }
    }

    void addSegmentToAdd(Short type, Double latitude1, Double longitude1, Double latitude2, Double longitude2, String streetName) {
        if (type != null && latitude1 != null && latitude2 != null && longitude1 != null && longitude2 != null) {

            segmentsToAddOptions.add(new PolylineOptions().width(15).color(Color.GREEN)
                    .add(new LatLng(latitude1, longitude1))
                    .add(new LatLng(latitude2, longitude2)));

            LatsAndLngs.add(new LatLng(latitude1, longitude1));
            LatsAndLngs.add(new LatLng(latitude2, longitude2));

            this.streetName = streetName;
        }
    }
    void addSegmentToDelete(Short type, Double latitude1, Double longitude1, Double latitude2, Double longitude2) {
        if (type != null && latitude1 != null && latitude2 != null && longitude1 != null && longitude2 != null) {

            segmentsToDeleteOptions.add(new PolylineOptions().width(15).color(Color.RED)
                    .add(new LatLng(latitude1, longitude1))
                    .add(new LatLng(latitude2, longitude2)));

            LatsAndLngs.add(new LatLng(latitude1, longitude1));
            LatsAndLngs.add(new LatLng(latitude2, longitude2));
        }
    }

    void removeDrownObjects() {
        for (Marker marker : pointsToAddMarkers) {
            marker.remove();
        }
        for (Polyline polyline : segmentsToAddPolylines) {
            polyline.remove();
        }
        for (Polyline polyline : segmentsToDeletePolylines) {
            polyline.remove();
        }
    }

    void drawObjectsToAdd(GoogleMap mMap) {
        removeDrownObjects();

        for (MarkerOptions options : pointsToAddOptions) {
            pointsToAddMarkers.add(mMap.addMarker(options));
        }
        for (PolylineOptions options : segmentsToAddOptions) {
            segmentsToAddPolylines.add(mMap.addPolyline(options));
        }
    }

    void drawObjectsToDelete(GoogleMap mMap) {
        removeDrownObjects();

        for (PolylineOptions options : segmentsToDeleteOptions) {
            segmentsToDeletePolylines.add(mMap.addPolyline(options));
        }
    }

    ArrayList<LatLng> getLatsAndLngs() {
        return LatsAndLngs;
    }

    View getView() {
        return actionSplash;
    }

    long getActionId() {
        return actionId;
    }
}
