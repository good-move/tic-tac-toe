package ru.nsu.ccfit.boltava.tictactoe.ui.main;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Created by alexey on 23.12.17.
 */

class MainScreenPresenter implements IMainContract.IPresenter {

    private final IMainContract.IView view;
    private final IMainContract.IInteractor interactor;

    MainScreenPresenter(IMainContract.IView view, IMainContract.IInteractor interactor) {
        this.view = view;
        this.interactor = interactor;
        view.setPresenter(this);
    }

    @Override
    public void onPlayWithBotButtonClick() {

    }

    @Override
    public void onPlayOnlineButtonClick() {
        view.showLoading();
        boolean enqueuedSuccessfully = interactor.enqueuePlayer();

        if (!enqueuedSuccessfully) {
            final String errorMessage = "Failed to start online game session";
            view.hideLoading();
            view.showErrorMessage(errorMessage);
        }
    }

}
