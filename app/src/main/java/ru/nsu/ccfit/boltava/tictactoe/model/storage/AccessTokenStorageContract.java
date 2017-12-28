package ru.nsu.ccfit.boltava.tictactoe.model.storage;

import android.provider.BaseColumns;

/**
 * Created by alexey on 24.12.17.
 */

class AccessTokenStorageContract {

    private AccessTokenStorageContract(){}

    static class AccessTokenStorageEntry implements BaseColumns {
        static final String TABLE_NAME = "Token";
        static final String COLUMN_TOKEN = "token";
    }

}
