package com.example.lifegen;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PlayerDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "players.db";
    private static final int DATABASE_VERSION = 1;

    // Nomes da tabela e colunas
    public static final String TABLE_PLAYERS = "players";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_HP = "max_hp";

    private static final String CREATE_TABLE_PLAYERS =
        "CREATE TABLE " + TABLE_PLAYERS + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_NAME + " TEXT NOT NULL, " +
            COLUMN_HP + " INTEGER NOT NULL" +
            ");";

    public PlayerDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_PLAYERS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Política de atualização simples: apaga a tabela antiga e cria uma nova
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLAYERS);
        onCreate(db);
    }
}
