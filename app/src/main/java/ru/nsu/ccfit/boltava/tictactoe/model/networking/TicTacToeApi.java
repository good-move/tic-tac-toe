package ru.nsu.ccfit.boltava.tictactoe.model.networking;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import ru.nsu.ccfit.boltava.tictactoe.model.networking.binding.request.MoveRequestPayload;
import ru.nsu.ccfit.boltava.tictactoe.model.networking.binding.request.TokenizedRequestPayload;
import ru.nsu.ccfit.boltava.tictactoe.model.networking.binding.response.APIResponse;
import ru.nsu.ccfit.boltava.tictactoe.model.networking.binding.response.LoginResponse;
import ru.nsu.ccfit.boltava.tictactoe.model.networking.binding.response.StatisticsResponse;

/**
 * Created by alexey on 08.12.17.
 */

public interface TicTacToeApi {

    @POST("/register")
    Call<LoginResponse> register(@Body TokenizedRequestPayload payload);

    @POST("/login")
    Call<LoginResponse> login(@Body TokenizedRequestPayload payload);

    @GET("/logout")
    Call<APIResponse> logout(@Header("Authorization") String credentials);

    @GET("/stats/{userId}")
    Call<StatisticsResponse> getStatistics(@Path("userId") String userId, @Header("Authorization") String credentials);

    @POST("/game/{gameId}/abort")
    Call<APIResponse> abortGame(@Path("gameId") String gameId,
                     @Header("Authorization") String credentials,
                     @Body TokenizedRequestPayload payload);

    @POST("/game/{gameId}/move")
    Call<APIResponse> makeMove(@Path("gameId") String gameId,
                        @Header("Authorization") String credentials,
                        @Body MoveRequestPayload payload);

    @POST("/game/start")
    Call<APIResponse> startGame(@Header("Authorization") String credentials, @Body JsonObject object);

}
