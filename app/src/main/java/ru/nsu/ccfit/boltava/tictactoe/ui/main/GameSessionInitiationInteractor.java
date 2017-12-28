package ru.nsu.ccfit.boltava.tictactoe.ui.main;

import com.google.firebase.iid.FirebaseInstanceId;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import ru.nsu.ccfit.boltava.tictactoe.model.networking.ServerInteractor;
import ru.nsu.ccfit.boltava.tictactoe.model.networking.binding.response.APIResponse;
import ru.nsu.ccfit.boltava.tictactoe.model.networking.binding.response.ErrorResponse;
import ru.nsu.ccfit.boltava.tictactoe.model.networking.binding.response.LoginResponse;
import ru.nsu.ccfit.boltava.tictactoe.model.storage.AccessTokenStorage;

public class GameSessionInitiationInteractor implements IMainContract.IInteractor {

    private final AccessTokenStorage accessTokenStorage;
    private GameSessionInitiationInteractor self;

    public GameSessionInitiationInteractor(AccessTokenStorage storage) {
        accessTokenStorage = storage;
        self = this;
    }

    @Override
    public boolean enqueuePlayer() {
        int responseCode = tryEnqueuePlayer();
        boolean successfullyEnqueued = false;
        if (responseCode == 200) {
            successfullyEnqueued = true;
        } else if (responseCode == 401) {
            // token expired
            responseCode = updateToken();
            if (responseCode == 200) {
                responseCode = tryEnqueuePlayer();
                if (responseCode == 200) {
                    successfullyEnqueued = true;
                }
            }
        }

        return successfullyEnqueued;
    }

    private int updateToken() {
        int responseCode = 500;
        Future<APIResponse> responseFuture = ServerInteractor.login(FirebaseInstanceId.getInstance().getToken());
        try {
            APIResponse response = responseFuture.get();
            if (response.getClass().equals(ErrorResponse.class)) {
                ErrorResponse error = (ErrorResponse) response;
                System.err.println("Failed to login: " + error.getMessage());
                responseCode = error.getCode();
            } else {
                responseCode = 200;
                accessTokenStorage.setAccessToken(((LoginResponse) response).getAccessToken());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return responseCode;

    }

    @Override
    public void startBotGame() {

    }

    private int tryEnqueuePlayer() {
        int responseCode = 500;

        String token = accessTokenStorage.getAccessToken();
        System.out.println("TOKEN: " + token);
        Future<APIResponse> responseFuture = ServerInteractor.startOnlineGame(token);
        try {
            APIResponse response = responseFuture.get();
            if (response.getClass().equals(ErrorResponse.class)) {
                ErrorResponse error = (ErrorResponse) response;
                System.err.println("Failed to start game: " + error.getMessage());
                responseCode = error.getCode();
            } else {
                responseCode = 200;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return responseCode;
    }

}
