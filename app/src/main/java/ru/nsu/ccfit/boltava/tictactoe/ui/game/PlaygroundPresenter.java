package ru.nsu.ccfit.boltava.tictactoe.ui.game;

/**
 * Created by alexey on 24.12.17.
 */

public class PlaygroundPresenter implements IPlaygroundContract.IPresenter, IPlaygroundContract.IPlaygroundStateListener {

    private final IPlaygroundContract.IView view;
    private final IPlaygroundContract.IInteractor interactor;

    PlaygroundPresenter(IPlaygroundContract.IView view, IPlaygroundContract.IInteractor interactor) {
        this.view = view;
        this.interactor = interactor;
        this.interactor.setPlaygroundStateListener(this);
        view.setPresenter(this);
        updateTurnStatus();
        view.showStartingMessage("You are playing with " + interactor.getPlayerStringMark());
    }

    @Override
    public void onCellClick(int x, int y) {
        // if can make move, make move
        if (interactor.canMakeMove(x, y)) {
            boolean successfulMove = interactor.makeMove(x, y);
            if (successfulMove) {
                updateTurnStatus();
                updateTurnStatus();
                view.updateCell(x, y, interactor.getPlayerMark());
            } else {
                view.showErrorMessage("Failed to perform your move");
            }
        }
    }

    private void updateTurnStatus() {
        if (interactor.hasTurn()) {
            view.updateGameStatus(IPlaygroundContract.GameStatus.MY_TURN);
        } else {
            view.updateGameStatus(IPlaygroundContract.GameStatus.OPPONENT_TURN);
        }
    }

    @Override
    public void onAbortGameClick() {

    }

    @Override
    public void onPlaygroundUpdate(int x, int y, IPlaygroundContract.CellEntity cellEntity) {
        System.out.println("Presenter: calling view");
        view.updateCell(x, y, cellEntity);
        updateTurnStatus();


    }

    @Override
    public void onGameFinished(boolean isWinner) {
        if (isWinner) {
            view.updateGameStatus(IPlaygroundContract.GameStatus.VICTORY);
            view.showFinishingMessage("Game over. You're the champion!");
        } else {
            view.updateGameStatus(IPlaygroundContract.GameStatus.LOSS);
            view.showFinishingMessage("Game over. You lost.");
        }
    }
}
