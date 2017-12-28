package ru.nsu.ccfit.boltava.tictactoe.ui.statistics;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import ru.nsu.ccfit.boltava.tictactoe.R;
import ru.nsu.ccfit.boltava.tictactoe.model.networking.ServerInteractor;
import ru.nsu.ccfit.boltava.tictactoe.model.networking.binding.response.APIResponse;
import ru.nsu.ccfit.boltava.tictactoe.model.networking.binding.response.ErrorResponse;
import ru.nsu.ccfit.boltava.tictactoe.model.networking.binding.response.StatisticsResponse;
import ru.nsu.ccfit.boltava.tictactoe.model.storage.AccessTokenStorage;

public class StatisticsActivity extends AppCompatActivity {

    private AccessTokenStorage accessTokenStorage;

    private RelativeLayout layout;
    private TextView victoriesTextView;
    private TextView lossesTextView;
    private TextView winRateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        accessTokenStorage = new AccessTokenStorage(this);
        bindViews();
        loadStatistics();

    }

    private void bindViews() {
        layout = findViewById(R.id.statisticsLayout);
        victoriesTextView = layout.findViewById(R.id.victoryTextView);
        lossesTextView = layout.findViewById(R.id.lossTextView);
        winRateTextView = layout.findViewById(R.id.winRateTextView);
    }

    private void loadStatistics() {

        Future<APIResponse> future = ServerInteractor.getStatistics(
                accessTokenStorage.getAccessToken(),
                FirebaseInstanceId.getInstance().getToken()
        );

        try {
            APIResponse response = future.get();
            if (response instanceof ErrorResponse) {
                ErrorResponse errorResponse = (ErrorResponse) response;
                System.err.println(errorResponse.getMessage());
            } else {
                StatisticsResponse r = (StatisticsResponse) response;
                lossesTextView.setText("Losses: " + String.valueOf(r.getLosses()));
                victoriesTextView.setText("Victories: " + String.valueOf(r.getVictories()));
                int gamesCount = r.getLosses() + r.getVictories();
                if (gamesCount == 0) {
                    winRateTextView.setText("WinRate: No data yet.");
                } else {
                    winRateTextView.setText("WinRate: " + String.valueOf((double)r.getVictories() / gamesCount));
                }

            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, StatisticsActivity.class);
    }
}
