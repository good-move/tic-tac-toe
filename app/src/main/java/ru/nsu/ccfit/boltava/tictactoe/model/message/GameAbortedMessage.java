package ru.nsu.ccfit.boltava.tictactoe.model.message;

/**
 * Created by alexey on 24.12.17.
 */

public class GameAbortedMessage extends Message {
    @Override
    public void handleBy(IGameMessageHandler handler) {
        handler.handle(this);
    }

    @Override
    public String toString() {
        return GameAbortedMessage.class.getSimpleName();
    }
}
