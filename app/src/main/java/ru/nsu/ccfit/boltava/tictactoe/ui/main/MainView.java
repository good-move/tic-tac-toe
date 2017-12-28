package ru.nsu.ccfit.boltava.tictactoe.ui.main;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import ru.nsu.ccfit.boltava.tictactoe.R;

/**
 * Created by alexey on 23.12.17.
 */

class MainView extends View implements IMainContract.IView {

    private IMainContract.IPresenter presenter;

    private Button playOnlineButton;
    private Button playWithBotButton;
    private ViewGroup rootView;

    public MainView(Context context, ViewGroup rootView) {
        super(context);
        this.rootView = rootView;
        bindLayoutElements();
        setEventListeners();
    }

    private void bindLayoutElements() {
        playOnlineButton = rootView.findViewById(R.id.playOnlineButton);
        playWithBotButton = rootView.findViewById(R.id.playWithBotButton);
    }

    private void setEventListeners() {
        playWithBotButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onPlayWithBotButtonClick();
            }
        });

        playOnlineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Play online button clicked");
                presenter.onPlayOnlineButtonClick();
            }
        });
    }

    @Override
    public void setPresenter(IMainContract.IPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showErrorMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoading() {
        System.out.println("Show loading triggered");
    }

    @Override
    public void hideLoading() {
        System.out.println("Hide loading triggered");
    }
}
