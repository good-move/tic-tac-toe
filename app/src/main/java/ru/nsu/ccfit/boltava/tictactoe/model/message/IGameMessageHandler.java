package ru.nsu.ccfit.boltava.tictactoe.model.message;

/**
 * Created by alexey on 24.12.17.
 */

public interface IGameMessageHandler {

    void handle(GameStartedMessage message);
    void handle(GameFinishedMessage message);
    void handle(GameAbortedMessage message);
    void handle(PlaygroundUpdatedMessage message);

}
