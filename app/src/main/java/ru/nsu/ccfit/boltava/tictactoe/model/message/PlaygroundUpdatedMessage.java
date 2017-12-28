package ru.nsu.ccfit.boltava.tictactoe.model.message;

import java.util.Locale;

/**
 * Created by alexey on 24.12.17.
 */

public class PlaygroundUpdatedMessage extends Message {

    private final int xPosition;
    private final int yPosition;
    private final String newPlaygroundState;

    public PlaygroundUpdatedMessage(int xPosition, int yPosition, String newPlaygroundState) {
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.newPlaygroundState = newPlaygroundState;
    }


    public String getNewPlaygroundState() {
        return newPlaygroundState;
    }

    public int getYPosition() {
        return yPosition;
    }

    public int getXPosition() {
        return xPosition;
    }

    @Override
    public void handleBy(IGameMessageHandler handler) {
        handler.handle(this);
    }

    @Override
    public String toString() {
        return String.format(Locale.ENGLISH, "%s <NextState=%s; x=%d, y=%d>",
                PlaygroundUpdatedMessage.class.getSimpleName(), newPlaygroundState, xPosition, yPosition);
    }
}
