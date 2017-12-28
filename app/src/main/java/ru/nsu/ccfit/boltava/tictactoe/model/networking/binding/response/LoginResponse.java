package ru.nsu.ccfit.boltava.tictactoe.model.networking.binding.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by alexey on 24.12.17.
 */

public class LoginResponse extends APIResponse {

    @SerializedName("token")
    private String accessToken;


    public String getAccessToken() {
        return accessToken;
    }

}
