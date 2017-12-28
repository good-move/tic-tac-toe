package ru.nsu.ccfit.boltava.tictactoe.ui.game;

import ru.nsu.ccfit.boltava.tictactoe.ui.base.IBasePresenter;
import ru.nsu.ccfit.boltava.tictactoe.ui.base.IBaseView;

/**
 * Created by alexey on 24.12.17.
 */

interface IPlaygroundContract {

    enum CellEntity {
        CROSS,
        CIRCLE
    }

    enum GameStatus {
        MY_TURN,
        OPPONENT_TURN,
        VICTORY,
        LOSS
    }

    interface IView extends IBaseView<IPresenter> {
        void updateCell(int x, int y, CellEntity entity);
        void updateGameStatus(GameStatus status);
        void showErrorMessage(String message);
        void showStartingMessage(String message);
        void showFinishingMessage(String message);
    }

    interface IPresenter extends IBasePresenter {
        void onCellClick(int x, int y);
        void onAbortGameClick();
    }

    interface IInteractor {
        void abortGame();

        boolean canMakeMove(int x, int y);
        boolean makeMove(int x, int y);
        boolean hasTurn();
        CellEntity getPlayerMark();

        void setPlaygroundStateListener(IPlaygroundStateListener listener);
        void removePlaygroundUpdateListener();

        String getPlayerStringMark();
    }


    interface IPlaygroundStateListener {
        void onPlaygroundUpdate(int x, int y, CellEntity cellEntity);
        void onGameFinished(boolean isWinner);
    }


    interface IPlaygroundUpdater extends IInteractor {
        void updatePlayground(int x, int y, String nextFieldState);
        void gameFinished(boolean isWinner);
    }

}
