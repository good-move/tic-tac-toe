package ru.nsu.ccfit.boltava.tictactoe.ui.main;

import ru.nsu.ccfit.boltava.tictactoe.ui.base.IBasePresenter;
import ru.nsu.ccfit.boltava.tictactoe.ui.base.IBaseView;


interface IMainContract {

    interface IPresenter extends IBasePresenter {
        void onPlayWithBotButtonClick();
        void onPlayOnlineButtonClick();
    }

    interface IView extends IBaseView<IPresenter> {
        void showErrorMessage(String message);
        void showLoading();
        void hideLoading();
    }

    interface IInteractor {

        boolean enqueuePlayer();
        void startBotGame();

    }

}
