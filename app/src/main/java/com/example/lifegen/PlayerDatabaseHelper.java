package com.example.lifegen;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PlayerDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "lifegen.db";
    private static final int DATABASE_VERSION = 2; // Versão incrementada

    public static final String TABLE_PLAYERS = "players";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_MAX_HP = "max_hp";
    public static final String COLUMN_CURRENT_HP = "current_hp";

    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_PLAYERS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME + " TEXT, " +
                    COLUMN_MAX_HP + " INTEGER, " +
                    COLUMN_CURRENT_HP + " INTEGER);";

    public PlayerDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            // Adiciona as novas colunas e migra os dados
            db.execSQL("ALTER TABLE " + TABLE_PLAYERS + " ADD COLUMN " + COLUMN_MAX_HP + " INTEGER DEFAULT 0");
            db.execSQL("ALTER TABLE " + TABLE_PLAYERS + " ADD COLUMN " + COLUMN_CURRENT_HP + " INTEGER DEFAULT 0");
            db.execSQL("UPDATE " + TABLE_PLAYERS + " SET " + COLUMN_MAX_HP + " = hp, " + COLUMN_CURRENT_HP + " = hp");
        }
    }
}
