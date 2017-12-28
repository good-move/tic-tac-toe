package ru.nsu.ccfit.boltava.tictactoe.model.networking;

import com.google.gson.JsonObject;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

import retrofit2.Call;
import retrofit2.Response;
import ru.nsu.ccfit.boltava.tictactoe.App;
import ru.nsu.ccfit.boltava.tictactoe.model.networking.binding.request.MoveRequestPayload;
import ru.nsu.ccfit.boltava.tictactoe.model.networking.binding.request.TokenizedRequestPayload;
import ru.nsu.ccfit.boltava.tictactoe.model.networking.binding.response.ErrorResponse;
import ru.nsu.ccfit.boltava.tictactoe.model.networking.binding.response.APIResponse;
import ru.nsu.ccfit.boltava.tictactoe.model.networking.binding.response.ErrorUtils;
import ru.nsu.ccfit.boltava.tictactoe.model.networking.binding.response.LoginResponse;
import ru.nsu.ccfit.boltava.tictactoe.model.networking.binding.response.StatisticsResponse;


public class ServerInteractor {

    public interface IRequestHandler<ResponseType> {
        void onSuccess(ResponseType response);
        void onError(ErrorResponse response);
        void onFailure(Throwable t);
    }

    private static final Executor executor = Executors.newFixedThreadPool(2);

    private static final String TAG = ServerInteractor.class.getCanonicalName();

    public static Future<APIResponse> register(final String userId) {
        return runLoginOrRegisterRequest(userId, RequestType.REGISTER);
    }

    public static Future<APIResponse> login(final String userId) {
        return runLoginOrRegisterRequest(userId, RequestType.LOGIN);
    }

    private static Future<APIResponse> runLoginOrRegisterRequest(final String authToken,
                                                                 final RequestType type) {
        final FutureTask<APIResponse> future = new FutureTask<>(new Callable<APIResponse>() {
            @Override
            public APIResponse call() throws Exception {
                Response<LoginResponse> response;
                if (type == RequestType.LOGIN) {
                    response = App.getApi().login(new TokenizedRequestPayload(authToken)).execute();
                } else {
                    response = App.getApi().register(new TokenizedRequestPayload(authToken)).execute();
                }

                if (response.isSuccessful()) {
                    System.out.println("Successfully received response");
                    return response.body() ;
                } else {
                    System.out.println("Error response received");
                    return ErrorUtils.parseError(response);
                }
            }

        });

        executor.execute(new Runnable() {
            @Override
            public void run() {
                future.run();
            }
        });

        return future;
    }

    public static Future<APIResponse> logout(final String accessToken) {
        return makeBaseApiCall(App.getApi().logout(accessToken));
    }

    public static Future<APIResponse> startOnlineGame(final String accessToken) {
        return makeBaseApiCall(App.getApi().startGame(accessToken, new JsonObject()));
    }

    private static  Future<APIResponse> makeBaseApiCall(final Call<APIResponse> call) {
        final FutureTask<APIResponse> future = new FutureTask<>(new Callable<APIResponse>() {
            @Override
            public APIResponse call() throws Exception {
                Response<APIResponse> response = call.execute();
                if (response.isSuccessful()) {
                    System.out.println("Successfully received response");
                    return response.body() ;
                } else {
                    System.out.println("Error response received");
                    return ErrorUtils.parseError(response);
                }
            }

        });

        executor.execute(new Runnable() {
            @Override
            public void run() {
                future.run();
            }
        });

        return future;
    }

    private enum RequestType {
        LOGIN,
        REGISTER
    }

    public static Future<APIResponse> makeMove(final String accessToken, final String gameId, GameMove move) {
        return makeBaseApiCall(App.getApi().makeMove(
                gameId,
                accessToken,
                new MoveRequestPayload(move.x, move.y, move.mark, move.currentFieldState))
        );
    }

    public static class GameMove {

        int x;
        int y;
        String currentFieldState;
        String mark;

        public GameMove(int x, int y, String currentFieldState, String mark) {
            this.x = x;
            this.y = y;
            this.currentFieldState = currentFieldState;
            this.mark = mark;
        }

    }

    public static Future<APIResponse> getStatistics(final String accessToken, final String userId) {
        final FutureTask<APIResponse> future = new FutureTask<>(new Callable<APIResponse>() {
            @Override
            public APIResponse call() throws Exception {
                Response<StatisticsResponse> response = App.getApi().getStatistics(userId, accessToken).execute();
                if (response.isSuccessful()) {
                    System.out.println("Successfully received response");
                    return response.body() ;
                } else {
                    System.out.println("Error response received");
                    return ErrorUtils.parseError(response);
                }
            }

        });

        executor.execute(new Runnable() {
            @Override
            public void run() {
                future.run();
            }
        });

        return future;
    }

}
