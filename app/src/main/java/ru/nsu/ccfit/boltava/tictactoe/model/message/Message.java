package ru.nsu.ccfit.boltava.tictactoe.model.message;

/**
 * Created by alexey on 24.12.17.
 */

public abstract class Message {

    public class Type {
        public static final String GAME_STARTED = "GAME_STARTED";
        public static final String GAME_ABORTED = "GAME_ABORTED";
        public static final String GAME_FIELD_UPDATED = "GAME_FIELD_UPDATED";
        public static final String GAME_FINISHED = "GAME_FINISHED";
    }

    public abstract void handleBy(IGameMessageHandler handler);

}
