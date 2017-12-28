package ru.nsu.ccfit.boltava.tictactoe.ui.game;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import ru.nsu.ccfit.boltava.tictactoe.model.networking.ServerInteractor;
import ru.nsu.ccfit.boltava.tictactoe.model.networking.binding.response.APIResponse;
import ru.nsu.ccfit.boltava.tictactoe.model.networking.binding.response.ErrorResponse;
import ru.nsu.ccfit.boltava.tictactoe.model.storage.AccessTokenStorage;

/**
 * Created by alexey on 25.12.17.
 */

public class PlaygroundInteractor implements IPlaygroundContract.IPlaygroundUpdater {

    private final AccessTokenStorage accessTokenStorage;
    private final String gameId;
    private boolean hasTurn;
    private boolean isGameFinished = false;

    private static final String DEFAULT_FIELD_STATE = "---------";
    private static final char EMPTY_CELL_MARK = '-';
    private static final char CROSS_CELL_MARK = 'X';
    private static final char CIRCLE_CELL_MARK = 'O';

    private StringBuilder currentFieldState = new StringBuilder(DEFAULT_FIELD_STATE);

    private IPlaygroundContract.CellEntity playerMark;

    private IPlaygroundContract.IPlaygroundStateListener playgroundStateListener;

    private final Object lock = new Object();

    PlaygroundInteractor(AccessTokenStorage storage, String gameId, boolean hasFirstTurn) {
        this.accessTokenStorage = storage;
        this.gameId = gameId;
        hasTurn = hasFirstTurn;

        if (hasFirstTurn) {
            playerMark = IPlaygroundContract.CellEntity.CROSS;
        } else {
            playerMark = IPlaygroundContract.CellEntity.CIRCLE;
        }
    }

    @Override
    public boolean makeMove(int x, int y) {
        if (!canMakeMove(x,y)) {
            return false;
        }

        Future<APIResponse> future = ServerInteractor.makeMove(
                accessTokenStorage.getAccessToken(),
                gameId,
                new ServerInteractor.GameMove(x, y,
                        currentFieldState.toString(), new String(new char[]{getCharForMark(playerMark)})
                )
        );

        try {
            APIResponse response = future.get();
            if (response instanceof ErrorResponse) {
                ErrorResponse error = (ErrorResponse) response;
                System.err.println(error.getMessage());
                return false;
            }

            toggleHasTurn();
            updateFieldState(x, y, playerMark);

            return true;
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean hasTurn() {
        return hasTurn;
    }

    @Override
    public IPlaygroundContract.CellEntity getPlayerMark() {
        return playerMark;
    }

    @Override
    public void setPlaygroundStateListener(IPlaygroundContract.IPlaygroundStateListener listener) {
        playgroundStateListener = listener;
    }

    @Override
    public void removePlaygroundUpdateListener() {
        playgroundStateListener = null;
    }

    @Override
    public String getPlayerStringMark() {
//        return new String(new char[]{ getCharForMark(playerMark) });
        return playerMark.name();
    }

    @Override
    public void abortGame() {

    }

    @Override
    public boolean canMakeMove(int x, int y) {
        return !isGameFinished && hasTurn && currentFieldState.charAt(y * 3 + x) == EMPTY_CELL_MARK;

    }

    private void updateFieldState(int x, int y, IPlaygroundContract.CellEntity entity) {
        currentFieldState.setCharAt(y * 3 + x, getCharForMark(entity));
    }

    private void toggleHasTurn() {
        hasTurn = !hasTurn;
    }


    private char getCharForMark(IPlaygroundContract.CellEntity entity) {
        if (entity == IPlaygroundContract.CellEntity.CIRCLE) {
            return CIRCLE_CELL_MARK;
        } else {
            return CROSS_CELL_MARK;
        }
    }

    private IPlaygroundContract.CellEntity getMarkForChar(char charMark) {
        switch (charMark){
            case CIRCLE_CELL_MARK:
                return IPlaygroundContract.CellEntity.CIRCLE;
            case CROSS_CELL_MARK:
                return IPlaygroundContract.CellEntity.CROSS;
            default:
                throw new RuntimeException("Invalid char " + charMark);
        }
    }

    @Override
    public void updatePlayground(int x, int y, String nextFieldState) {
        System.out.println("Interactor: updatePlayground triggered");
        char charMark = nextFieldState.charAt(y*3+x);
        updateFieldState(x, y, getMarkForChar(charMark));
        toggleHasTurn();
        System.out.println("Interactor: updated local playground state");
        if (playgroundStateListener != null) {
            System.out.println("Interactor: calling presenter");
            playgroundStateListener.onPlaygroundUpdate(x, y, getMarkForChar(charMark));
        }
    }

    @Override
    public void gameFinished(boolean isWinner) {
        System.out.println("Interactor: gameFinished triggered");
        isGameFinished = true;
        if (playgroundStateListener != null) {
            System.out.println("Interactor: calling presenter");
            playgroundStateListener.onGameFinished(isWinner);
        }
    }

}
