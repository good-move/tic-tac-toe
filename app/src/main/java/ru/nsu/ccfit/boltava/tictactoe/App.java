package ru.nsu.ccfit.boltava.tictactoe;

import android.app.Application;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.nsu.ccfit.boltava.tictactoe.model.networking.TicTacToeApi;

/**
 * Created by alexey on 08.12.17.
 */

public class App extends Application {

    private static final int SERVER_PORT = 9000;
    private static final String DEV_MACHINE_LOCALHOST = "10.0.2.2";
    private static final String SERVER_BASE_URL = "http://" + DEV_MACHINE_LOCALHOST + ":" + String.valueOf(SERVER_PORT);

    private static TicTacToeApi api;
    private static Retrofit retrofit;

    @Override
    public void onCreate() {
        super.onCreate();

        retrofit = new Retrofit.Builder()
                .baseUrl(SERVER_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(TicTacToeApi.class);
    }

    public static TicTacToeApi getApi() {
        return api;
    }

    public static Retrofit retrofit() {
        return retrofit;
    }

}
