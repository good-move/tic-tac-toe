package ru.nsu.ccfit.boltava.tictactoe.model.networking.binding.request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by alexey on 23.12.17.
 */

public class Move {

    @SerializedName("x")
    int x;

    @SerializedName("y")
    int y;

    @SerializedName("mark")
    String mark;


    public Move(int x, int y, String mark) {
        this.x = x;
        this.y = y;
        this.mark = mark;
    }

}
