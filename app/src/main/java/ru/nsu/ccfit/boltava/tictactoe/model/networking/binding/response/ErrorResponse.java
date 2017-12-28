package ru.nsu.ccfit.boltava.tictactoe.model.networking.binding.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by alexey on 24.12.17.
 */

public class ErrorResponse extends APIResponse {

    @SerializedName("code")
    private int code;

    @SerializedName("message")
    private String message;

    @SerializedName("status")
    private String status;


    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getStatus() {
        return status;
    }

}
