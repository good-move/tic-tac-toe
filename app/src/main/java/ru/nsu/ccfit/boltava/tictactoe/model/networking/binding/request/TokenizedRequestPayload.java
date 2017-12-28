package ru.nsu.ccfit.boltava.tictactoe.model.networking.binding.request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by alexey on 23.12.17.
 */

public class TokenizedRequestPayload {

    @SerializedName("user_id")
    String authenticationToken;

    public TokenizedRequestPayload(String authenticationToken) {
        this.authenticationToken = authenticationToken;
    }

}
