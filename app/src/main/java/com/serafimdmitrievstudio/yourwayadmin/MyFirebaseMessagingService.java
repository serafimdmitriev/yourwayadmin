package com.serafimdmitrievstudio.yourwayadmin;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by Serafim on 22.02.2018.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    String id = null;

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.write("BLA");
    }


    //@Override
    public void sendNotification(String messageBody) {
        Log.write("BLABLA");
    }
}
