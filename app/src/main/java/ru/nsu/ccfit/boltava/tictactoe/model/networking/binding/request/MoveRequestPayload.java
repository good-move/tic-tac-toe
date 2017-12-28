package ru.nsu.ccfit.boltava.tictactoe.model.networking.binding.request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by alexey on 23.12.17.
 */

public class MoveRequestPayload {

    @SerializedName("move")
    private Move move;

    @SerializedName("field_state")
    private String fieldState;

    public MoveRequestPayload(int xCoordinate, int yCoordinate, String mark, String fieldState) {
        this.move = new Move(xCoordinate, yCoordinate, mark);
        this.fieldState = fieldState;
    }

}
