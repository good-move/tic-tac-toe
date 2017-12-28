package ru.nsu.ccfit.boltava.tictactoe.ui.game;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import ru.nsu.ccfit.boltava.tictactoe.R;


public class PlaygroundAdapter extends BaseAdapter {

    private final Context context;
    private final int sideWidth;
    final private Cell[] cells;

    PlaygroundAdapter(Context context, Cell[] cells, int gridSideWidth) {
        this.context = context;
        this.sideWidth = gridSideWidth;
        this.cells = cells;
    }

    @Override
    public int getCount() {
        return sideWidth * sideWidth;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.playground_cell, null);
        }

        ImageView imageView = convertView.findViewById(R.id.playgroundCell);

        switch (cells[position].mark) {
            case CROSS:
                imageView.setImageResource(R.drawable.cross);
                break;
            case CIRCLE:
                imageView.setImageResource(R.drawable.circle);
                break;
            case EMPTY:
                imageView.setImageResource(R.drawable.empty_cell);
                break;
        }

        return convertView;
    }

    enum PlaygroundMark {
        EMPTY,
        CIRCLE,
        CROSS
    }

    static class Cell {
        PlaygroundMark mark;

        Cell() {
            mark = PlaygroundMark.EMPTY;
        }

        public void setMark(PlaygroundMark mark) {
            this.mark = mark;
        }
    }

}


