package com.example.f1fan;

import android.os.Looper;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        Looper.prepare();
        Toast.makeText(getApplicationContext(), message.getNotification().getBody(), Toast.LENGTH_LONG).show();
        Looper.loop();
    }
}
