package ru.nsu.ccfit.boltava.tictactoe.model.message;

import java.util.Locale;

/**
 * Created by alexey on 24.12.17.
 */

public class GameStartedMessage extends Message {

    private final boolean shouldMakeFirstMove;
    private final String gameId;

    public GameStartedMessage(boolean shouldMakeFirstMove, String gameId) {
        this.shouldMakeFirstMove = shouldMakeFirstMove;
        this.gameId = gameId;
    }

    public boolean isShouldMakeFirstMove() {
        return shouldMakeFirstMove;
    }

    @Override
    public void handleBy(IGameMessageHandler handler) {
        handler.handle(this);
    }

    @Override
    public String toString() {
        return String.format(Locale.ENGLISH, "%s <FirstMove=%b>",
                GameStartedMessage.class.getSimpleName(), shouldMakeFirstMove);
    }

    public String getGameId() {
        return gameId;
    }

}
