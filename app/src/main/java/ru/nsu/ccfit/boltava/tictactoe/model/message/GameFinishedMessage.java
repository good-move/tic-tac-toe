package ru.nsu.ccfit.boltava.tictactoe.model.message;

import java.util.Locale;

/**
 * Created by alexey on 24.12.17.
 */

public class GameFinishedMessage extends Message {
    private final boolean isWinner;

    public GameFinishedMessage(boolean isWinner) {
        this.isWinner = isWinner;
    }

    public boolean isWinner() {
        return isWinner;
    }

    @Override
    public String toString() {
        return String.format(Locale.ENGLISH, "%s <isWinner=%b>", GameFinishedMessage.class.getSimpleName(), isWinner);
    }

    @Override
    public void handleBy(IGameMessageHandler handler) {
        handler.handle(this);
    }
}

