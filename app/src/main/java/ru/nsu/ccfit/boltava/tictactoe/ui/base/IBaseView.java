package ru.nsu.ccfit.boltava.tictactoe.ui.base;

/**
 * Created by alexey on 23.12.17.
 */

public interface IBaseView<Presenter extends IBasePresenter> {

    void setPresenter(Presenter presenter);

}
