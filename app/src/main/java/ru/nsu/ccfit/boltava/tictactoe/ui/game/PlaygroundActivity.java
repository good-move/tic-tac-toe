package ru.nsu.ccfit.boltava.tictactoe.ui.game;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.google.firebase.messaging.FirebaseMessagingService;

import ru.nsu.ccfit.boltava.tictactoe.R;
import ru.nsu.ccfit.boltava.tictactoe.model.firebase.BasicFirebaseService;
import ru.nsu.ccfit.boltava.tictactoe.model.message.GameAbortedMessage;
import ru.nsu.ccfit.boltava.tictactoe.model.message.GameFinishedMessage;
import ru.nsu.ccfit.boltava.tictactoe.model.message.GameStartedMessage;
import ru.nsu.ccfit.boltava.tictactoe.model.message.IGameMessageHandler;
import ru.nsu.ccfit.boltava.tictactoe.model.message.PlaygroundUpdatedMessage;
import ru.nsu.ccfit.boltava.tictactoe.model.storage.AccessTokenStorage;

public class PlaygroundActivity extends AppCompatActivity implements IGameMessageHandler {

    private static final String IS_FIRST_PLAYER = "__IS_FIRST_PLAYER__";
    private static final String GAME_SESSION_TYPE = "__GAME_SESSION_TYPE__";
    private static final String GAME_ID = "__GAME_ID__";

    private LinearLayout layout;
    private IPlaygroundContract.IView playgroundView;
    private IPlaygroundContract.IPresenter playgroundPresenter;
    private IPlaygroundContract.IPlaygroundUpdater playgroundInteractor;
    private AccessTokenStorage accessTokenStorage;

    public enum GameSessionType {
        ONLINE,
        BOT
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playground);

        Intent callingIntent = getIntent();
        boolean isFirstPlayer = callingIntent.getBooleanExtra(IS_FIRST_PLAYER, false);
        String gameId = callingIntent.getStringExtra(GAME_ID);

        layout = findViewById(R.id.playgroundMainLayout);
        createControls(gameId, isFirstPlayer);
        subscribeToFirebaseUpdates();
    }

    @Override
    public void onDestroy() {
        unsubscribeFromFirebaseUpdates();
        super.onDestroy();
    }


    private void subscribeToFirebaseUpdates() {
        BasicFirebaseService.addMessageHandler(this);
    }

    private void unsubscribeFromFirebaseUpdates() {
        BasicFirebaseService.removeMessageHandler(this);
    }

    private void createControls(String gameId, boolean hasFirstTurn) {
        accessTokenStorage = new AccessTokenStorage(this);
        playgroundView = new PlaygroundView(this, layout);
        playgroundInteractor = new PlaygroundInteractor(accessTokenStorage, gameId, hasFirstTurn);
        playgroundPresenter = new PlaygroundPresenter(playgroundView, playgroundInteractor);
    }

    @Override
    public void handle(GameStartedMessage message) {
        System.out.println("Playground Activity received GameStarted message");
    }

    @Override
    public void handle(final GameFinishedMessage message) {
        System.out.println("Playground Activity received " + message.toString());
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                playgroundInteractor.gameFinished(message.isWinner());
            }
        });
        System.out.println("Game must be finished by now");
    }

    @Override
    public void handle(GameAbortedMessage message) {
        System.out.println("Playground Activity received " + message.toString());
        System.out.println("HERE");
    }

    @Override
    public void handle(final PlaygroundUpdatedMessage message) {
        System.out.println("Playground Activity: received PlaygroundUpdatedMessage message");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                playgroundInteractor.updatePlayground(
                        message.getXPosition(),
                        message.getYPosition(),
                        message.getNewPlaygroundState()
                );
            }
        });
        System.out.println("Playground Activity: updated playground");
    }

    public static Intent getCallingIntent(Context context,
                                          boolean isFirstPlayer,
                                          String gameId,
                                          GameSessionType sessionType) {
        Intent intent = new Intent(context, PlaygroundActivity.class);
        intent.putExtra(IS_FIRST_PLAYER, isFirstPlayer);
        intent.putExtra(GAME_SESSION_TYPE, sessionType);
        intent.putExtra(GAME_ID, gameId);

        return intent;
    }

}
