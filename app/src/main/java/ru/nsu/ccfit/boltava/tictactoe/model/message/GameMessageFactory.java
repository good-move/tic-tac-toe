package ru.nsu.ccfit.boltava.tictactoe.model.message;

import java.util.Map;

/**
 * Created by alexey on 24.12.17.
 */

public class GameMessageFactory {

    public static Message create(Map<String, String> messageData) {
        String messageType = messageData.get("type");

        switch (messageType) {
            case Message.Type.GAME_STARTED:
                return createGameStartedMessage(messageData);
            case Message.Type.GAME_FINISHED:
                return createGameFinishedMessage(messageData);
            case Message.Type.GAME_ABORTED:
                return createGameAbortedMessage(messageData);
            case Message.Type.GAME_FIELD_UPDATED:
                return createPlaygroundUpdatedMessage(messageData);
            default:
                System.err.println("Unknown message type: " + messageType);
        }

        return new GameAbortedMessage();
    }

    private static PlaygroundUpdatedMessage createPlaygroundUpdatedMessage(Map<String, String> messageData) {
        int xPosition = Integer.valueOf(messageData.get("x"));
        int yPosition = Integer.valueOf(messageData.get("y"));
        String playgroundState = messageData.get("field_state");

        return new PlaygroundUpdatedMessage(xPosition, yPosition, playgroundState);
    }

    private static GameAbortedMessage createGameAbortedMessage(Map<String, String> messageData) {
        return new GameAbortedMessage();
    }

    private static GameFinishedMessage createGameFinishedMessage(Map<String, String> messageData) {
        String firstMove = messageData.get("winner");
        if (firstMove == null) {
            throw new NullPointerException("Missing field <winner> in firebase message");
        }
        boolean isWinner = false;
        if (firstMove.equals("true")) {
            isWinner = true;
        }
        
        return new GameFinishedMessage(isWinner);
    }

    private static GameStartedMessage createGameStartedMessage(Map<String, String> messageData) {
        String firstMove = messageData.get("first_move");
        if (firstMove == null) {
            throw new NullPointerException("Missing field <first_move> in firebase message");
        }
        boolean shouldMakeFirstMove = false;
        if (firstMove.equals("true")) {
            shouldMakeFirstMove = true;
        }

        String gameId = messageData.get("game_id");
        
        return new GameStartedMessage(shouldMakeFirstMove, gameId);
    }
    
    

}
