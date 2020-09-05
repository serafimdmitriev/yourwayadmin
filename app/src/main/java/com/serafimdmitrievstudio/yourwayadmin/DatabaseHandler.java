package com.serafimdmitrievstudio.yourwayadmin;

import android.graphics.Color;
import android.os.StrictMode;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


class DatabaseHandler {
    static boolean loadDatabase(GoogleMap mMap) {

        try {
            Log.write("RETROFIT 0");
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            Log.write("RETROFIT 1");
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://accesspassed.com:8080/") //Базовая часть адреса
                    .addConverterFactory(GsonConverterFactory.create()) //Конвертер, необходимый для преобразования JSON'а в объекты
                    .build();

            Log.write("RETROFIT 2");
            ServerApi serverApi = retrofit.create(ServerApi.class);

            Log.write("RETROFIT 3");

            List<ServerGetMapResponse> response;

            try {
                Log.write("RETROFIT 4");
                response = serverApi.getMap("getMap").execute().body();
            } catch (Exception e) {
                Log.write("Sorry, map loading was failed :( Please, check your Internet connection.");

                Log.write(e.getMessage());
                for (StackTraceElement el : e.getStackTrace()) {
                    Log.write(el.toString());
                }
                return false;
            }

            for (int WaitingTime = 0; ; ) {
                if (response != null) {
                    break;
                } else {
                    if (WaitingTime < 15000) {
                        try {
                            Thread.sleep(1);
                            WaitingTime++;
                        } catch (Exception e) {
                            Log.write(e.getMessage());
                        }
                    } else {
                        Log.write("Sorry, map loading was failed :( Please, check your Internet connection.");
                        return false;
                    }
                }
            }

            for (int i = 0; i < response.size(); i++) {
                switch (response.get(i).getType()) {
                    case MapItemType.Road:
                    case MapItemType.Passage:
                    case MapItemType.OverheadPassage:
                    case MapItemType.UndergroundPassage: {
                        mMap.addPolyline(new PolylineOptions().width(15).color(Color.CYAN)
                                .add(response.get(i).getLatLng1())
                                .add(response.get(i).getLatLng2()));
                    }
                    break;
                    case MapItemType.Lift: {
                        mMap.addMarker(new MarkerOptions()
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_lift_small))
                                .anchor((float) 0.5, (float) 0.5)
                                .visible(false)
                                .position(new LatLng(0, 0)));
                    }
                    break;
                    case MapItemType.Ramp: {
                        mMap.addMarker(new MarkerOptions()
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_ramp_small))
                                .anchor((float) 0.5, (float) 0.5)
                                .visible(false)
                                .position(new LatLng(0, 0)));

                        Log.write("Point added");
                    }
                    break;
                }
            }

        } catch (Exception e) {
            Log.write("RETROFIT 10");
            Log.write(e.getMessage());
            for (StackTraceElement el : e.getStackTrace()) {
                Log.write(el.toString());
            }
        }

        return true;
    }
}
