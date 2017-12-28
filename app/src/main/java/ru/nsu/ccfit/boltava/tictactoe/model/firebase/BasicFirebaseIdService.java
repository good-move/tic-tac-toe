package ru.nsu.ccfit.boltava.tictactoe.model.firebase;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.io.IOException;

import ru.nsu.ccfit.boltava.tictactoe.App;
import ru.nsu.ccfit.boltava.tictactoe.model.networking.binding.request.TokenizedRequestPayload;

/**
 * Created by alexey on 07.12.17.
 */

public class BasicFirebaseIdService extends FirebaseInstanceIdService {

    private final String TAG = BasicFirebaseIdService.class.getCanonicalName();

    @Override
    public void onTokenRefresh() {
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "-----------");
        Log.d(TAG, "New token received: " + token);
        Log.d(TAG, "-----------");

        try {
            TokenizedRequestPayload tokenPayload = new TokenizedRequestPayload(token);
            App.getApi().register(tokenPayload).execute();
        } catch (IOException e) {
            System.err.println("Failed to register FCM token");
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
