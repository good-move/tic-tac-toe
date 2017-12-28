package ru.nsu.ccfit.boltava.tictactoe.ui.main;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;

import org.xml.sax.helpers.LocatorImpl;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import ru.nsu.ccfit.boltava.tictactoe.BuildConfig;
import ru.nsu.ccfit.boltava.tictactoe.R;
import ru.nsu.ccfit.boltava.tictactoe.model.firebase.BasicFirebaseService;
import ru.nsu.ccfit.boltava.tictactoe.model.message.GameAbortedMessage;
import ru.nsu.ccfit.boltava.tictactoe.model.message.GameFinishedMessage;
import ru.nsu.ccfit.boltava.tictactoe.model.message.GameStartedMessage;
import ru.nsu.ccfit.boltava.tictactoe.model.message.IGameMessageHandler;
import ru.nsu.ccfit.boltava.tictactoe.model.message.PlaygroundUpdatedMessage;
import ru.nsu.ccfit.boltava.tictactoe.model.networking.ServerInteractor;
import ru.nsu.ccfit.boltava.tictactoe.model.networking.binding.response.ErrorResponse;
import ru.nsu.ccfit.boltava.tictactoe.model.networking.binding.response.APIResponse;
import ru.nsu.ccfit.boltava.tictactoe.model.networking.binding.response.LoginResponse;
import ru.nsu.ccfit.boltava.tictactoe.model.storage.AccessTokenStorage;
import ru.nsu.ccfit.boltava.tictactoe.ui.game.PlaygroundActivity;
import ru.nsu.ccfit.boltava.tictactoe.ui.statistics.StatisticsActivity;

public class MainActivity extends AppCompatActivity implements IGameMessageHandler, ServerInteractor.IRequestHandler<LoginResponse> {

    private IMainContract.IView view;
    private IMainContract.IPresenter presenter;
    private IMainContract.IInteractor interactor;
    private AccessTokenStorage accessTokenStorage;


    RelativeLayout layout;
    private Button openSettingsButton;

    private boolean isFirstRun = false;

    // *************************** Activity Lifecycle Controls ***************************

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        removeToolbars();
        checkFirstRun();

        accessTokenStorage = new AccessTokenStorage(this);
        registerOrLogin();
        subscribeToFirebaseUpdates();

        setContentView(R.layout.activity_main);

        bindViews();
        setEventListeners();
        initControllers();
        showChooseUsernameDialogOnFirstRun();
    }

    @Override
    public void onDestroy() {
        unsubscribeFromFirebaseUpdates();
        logout();
        super.onDestroy();
    }

    //    ************************ Firebase interactions Routines ************************

    private void subscribeToFirebaseUpdates() {
        BasicFirebaseService.addMessageHandler(this);
    }

    private void unsubscribeFromFirebaseUpdates() {
        BasicFirebaseService.removeMessageHandler(this);
    }

    private void checkFirstRun() {
        final String PREFS_NAME = "MyPrefsFile";
        final String PREF_VERSION_CODE_KEY = "version_code";
        final int DOESNT_EXIST = -1;

        // Get current version code
        int currentVersionCode = BuildConfig.VERSION_CODE;

        // Get saved version code
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        int savedVersionCode = prefs.getInt(PREF_VERSION_CODE_KEY, DOESNT_EXIST);

        if (savedVersionCode == DOESNT_EXIST) {
            isFirstRun = true;
        }

        // Update the shared preferences with the current version code
        prefs.edit().putInt(PREF_VERSION_CODE_KEY, currentVersionCode).apply();
    }

    //    ************************ Activity Specific Routines ************************

    private void removeToolbars() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

    }

    private void setEventListeners() {
        final Context context = this;
        openSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // show Settings activity
                System.out.println("on statistics button click");
                startActivity(StatisticsActivity.getCallingIntent(context));
            }
        });
    }

    private void bindViews() {
        openSettingsButton = findViewById(R.id.openSettingsButton);
        layout = findViewById(R.id.mainLayout);
        view = new MainView(this, layout);
    }

    private void showChooseUsernameDialogOnFirstRun() {
        if (isFirstRun) {
            // show <Choose Username> popup
        }
    }

    private void initControllers() {
        interactor = new GameSessionInitiationInteractor(accessTokenStorage);
        presenter = new MainScreenPresenter(view, interactor);
    }

    //    ************************ Server API Usage Routines ************************

    private void registerOrLogin() {
        Future<APIResponse>  responseFuture;
        if (isFirstRun) {
            responseFuture = ServerInteractor.register(FirebaseInstanceId.getInstance().getToken());
        } else {
            if (accessTokenStorage.hasToken()) {
                return;
            }
            responseFuture = ServerInteractor.login(FirebaseInstanceId.getInstance().getToken());
        }

        try {
            APIResponse response = responseFuture.get();
            if (response instanceof LoginResponse) {
                onAccessTokenReceived((LoginResponse) response);
            } else {
                System.err.println("Failed to receive access token: " + ((ErrorResponse) response).getMessage());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }

    private void onAccessTokenReceived(LoginResponse response) {
        System.out.println("Received new access token: " + response.getAccessToken());
        accessTokenStorage.setAccessToken(response.getAccessToken());
    }

    private void logout() {
        Future<APIResponse> future = ServerInteractor.logout(accessTokenStorage.getAccessToken());
        try {
            future.get();
            System.out.println("Successfully logged out");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } finally {
            accessTokenStorage.removeAccessToken();
        }
    }

//    ***************************** Interface Implementation *****************************

    @Override
    public void handle(GameStartedMessage message) {
        view.hideLoading();
        startActivity(PlaygroundActivity.getCallingIntent(this,
                                                          message.isShouldMakeFirstMove(),
                                                          message.getGameId(),
                                                          PlaygroundActivity.GameSessionType.ONLINE));
    }

    @Override
    public void handle(GameFinishedMessage message) {
        System.out.println("Main activity received " + message.toString());
    }

    @Override
    public void handle(GameAbortedMessage message) {
        System.out.println("Main activity received " + message.toString());
    }

    @Override
    public void handle(PlaygroundUpdatedMessage message) {
        System.out.println("Main activity received " + message.toString());
    }

    //    ***************************** IRequestHandler Interface Implementation *****************************


    @Override
    public void onSuccess(LoginResponse response) {
        System.out.println("Fetched access token: " + response.getAccessToken());
        accessTokenStorage.setAccessToken(response.getAccessToken());
    }

    @Override
    public void onError(ErrorResponse error) {
        Toast.makeText(this, error.getMessage() + " " + error.getStatus(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onFailure(Throwable t) {
        System.err.println("Failed to connect to the server");
        t.printStackTrace();
    }
}
