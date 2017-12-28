package ru.nsu.ccfit.boltava.tictactoe.ui.game;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import ru.nsu.ccfit.boltava.tictactoe.R;


public class PlaygroundView extends View implements IPlaygroundContract.IView {

    private final static int SIDE_SIZE = 3;

    private final ViewGroup rootView;
    private final Context context;

    private GridView playground;
    private TextView gameStatusMessage;
    private TextView additionalMessage;

    private PlaygroundAdapter adapter;
    private IPlaygroundContract.IPresenter presenter;
    private PlaygroundAdapter.Cell[] cells;

    public PlaygroundView(Context context, ViewGroup rootView) {
        super(context);
        this.rootView = rootView;
        this.context = context;

        initDefaultCells();
        initViews(context);
        setEventListeners();
    }

    private void initDefaultCells() {
        cells = new PlaygroundAdapter.Cell[SIDE_SIZE * SIDE_SIZE];
        for (int i = 0; i < cells.length; ++i) {
            cells[i] = new PlaygroundAdapter.Cell();
        }
    }

    private void initViews(Context context) {
        adapter = new PlaygroundAdapter(context, cells, SIDE_SIZE);

        playground = rootView.findViewById(R.id.playgroundTable);
        playground.setAdapter(adapter);

        gameStatusMessage = rootView.findViewById(R.id.gameMoveHint);
        additionalMessage = rootView.findViewById(R.id.gameStatusMessage);
    }

    private void setEventListeners() {
        playground.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("PlaygroundView: Cell clicked");
                presenter.onCellClick(position % SIDE_SIZE, position / SIDE_SIZE);
            }
        });

        playground.setOnTouchListener(new OnTouchListener(){

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return event.getAction() == MotionEvent.ACTION_MOVE;
            }

        });
    }

    @Override
    public void updateCell(int x, int y, IPlaygroundContract.CellEntity entity) {
        if (entity == IPlaygroundContract.CellEntity.CIRCLE) {
            cells[y * SIDE_SIZE + x].setMark(PlaygroundAdapter.PlaygroundMark.CIRCLE);
        } else {
            cells[y * SIDE_SIZE + x].setMark(PlaygroundAdapter.PlaygroundMark.CROSS);
        }

        adapter.notifyDataSetChanged();
    }

    @Override
    public void updateGameStatus(IPlaygroundContract.GameStatus status) {
        switch (status) {
            case MY_TURN:
                gameStatusMessage.setText(R.string.my_turn_string);
                break;
            case OPPONENT_TURN:
                gameStatusMessage.setText(R.string.opponent_turn_string);
                break;
            case VICTORY:
                gameStatusMessage.setText(R.string.victory_message_string);
                break;
            case LOSS:
                gameStatusMessage.setText(R.string.loss_message_string);
                break;
        }
    }

    @Override
    public void showErrorMessage(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showStartingMessage(String message) {
        this.additionalMessage.setText(message);
    }

    @Override
    public void showFinishingMessage(String message) {
        this.additionalMessage.setText(message);
    }

    @Override
    public void setPresenter(IPlaygroundContract.IPresenter presenter) {
        this.presenter = presenter;
    }

}
