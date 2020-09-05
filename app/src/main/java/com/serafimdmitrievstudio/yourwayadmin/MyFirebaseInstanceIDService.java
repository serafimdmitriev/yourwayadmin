package com.serafimdmitrievstudio.yourwayadmin;

import android.support.annotation.Nullable;
import android.view.View;
import android.view.animation.Animation;
import android.widget.RadioButton;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Serafim on 22.02.2018.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService{

    static String token = null;

    static public void setToken(String Token) {
        token = Token;
    }

    @Nullable
    static String getToken() {
        return token;
    }

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.write("Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(String newToken) {
        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://accesspassed.com:8080/") //Базовая часть адреса
                    .addConverterFactory(GsonConverterFactory.create()) //Конвертер, необходимый для преобразования JSON'а в объекты
                    .build();

            ServerApi serverApi = retrofit.create(ServerApi.class);

            Call<ServerSimpleResponse> call = serverApi.changeFirebaseToken("changeFirebaseToken"
                    , token
                    , newToken
            );

            call.enqueue(new Callback<ServerSimpleResponse>() {
                @Override
                public void onResponse(Call<ServerSimpleResponse> call, Response<ServerSimpleResponse> response) {


                }

                @Override
                public void onFailure(Call<ServerSimpleResponse> call, Throwable t) {
                    Log.write(call.toString());
                    Log.write(t.toString());
                }
            });

        } catch (Exception e) {
            Log.write(e.getMessage());
            for (StackTraceElement el : e.getStackTrace()) {
                Log.write(el.toString());
            }
        }
    }
}
