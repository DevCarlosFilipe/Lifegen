package com.example.lifegen;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.List;

public class PlayerRepository {
    private final PlayerDatabaseHelper dbHelper;

    public PlayerRepository(Context context) {
        dbHelper = new PlayerDatabaseHelper(context);
    }

    public long insertPlayer(String name, int hp) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(PlayerDatabaseHelper.COLUMN_NAME, name);
        values.put(PlayerDatabaseHelper.COLUMN_HP, hp);

        return db.insert(PlayerDatabaseHelper.TABLE_PLAYERS, null, values);
    }

    public void deleteAllPlayers() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        // O argumento 'null' para 'whereClause' e 'whereArgs' deleta todas as linhas.
        db.delete(PlayerDatabaseHelper.TABLE_PLAYERS, null, null);
    }

    @SuppressLint("Range")
    public List<Player> getAllPlayers() {
        List<Player> players = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(
                PlayerDatabaseHelper.TABLE_PLAYERS,
                null, // null para retornar todas as colunas
                null,
                null,
                null,
                null,
                PlayerDatabaseHelper.COLUMN_NAME + " ASC"
        );

        // Itera sobre o cursor e cria um objeto Player para cada linha
        if (cursor != null) {
            while (cursor.moveToNext()) {
                long id = cursor.getLong(cursor.getColumnIndex(PlayerDatabaseHelper.COLUMN_ID));
                String name = cursor.getString(cursor.getColumnIndex(PlayerDatabaseHelper.COLUMN_NAME));
                int maxHp = cursor.getInt(cursor.getColumnIndex(PlayerDatabaseHelper.COLUMN_HP));
                players.add(new Player(id, name, maxHp));
            }
            cursor.close(); // Sempre feche o cursor!
        }

        return players;
    }
}
