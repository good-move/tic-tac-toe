package ru.nsu.ccfit.boltava.tictactoe.model.networking.binding.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by alexey on 25.12.17.
 */

public class StatisticsResponse extends APIResponse {

    @SerializedName("victory")
    private int victoryCount;

    @SerializedName("loss")
    private int lossCount;


    public int getVictories() {
        return victoryCount;
    }

    public int getLosses() {
        return lossCount;
    }
}
