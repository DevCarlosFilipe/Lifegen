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

    public void insertPlayer(String name, int maxHp) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(PlayerDatabaseHelper.COLUMN_NAME, name);
        values.put(PlayerDatabaseHelper.COLUMN_MAX_HP, maxHp);
        values.put(PlayerDatabaseHelper.COLUMN_CURRENT_HP, maxHp);

        db.insert(PlayerDatabaseHelper.TABLE_PLAYERS, null, values);
    }

    public void updatePlayer(Player player) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(PlayerDatabaseHelper.COLUMN_NAME, player.getName());
        values.put(PlayerDatabaseHelper.COLUMN_MAX_HP, player.getMaxHp());
        values.put(PlayerDatabaseHelper.COLUMN_CURRENT_HP, player.getCurrentHp());

        String selection = PlayerDatabaseHelper.COLUMN_ID + " = ?";
        String[] selectionArgs = { String.valueOf(player.getId()) };

        db.update(
                PlayerDatabaseHelper.TABLE_PLAYERS,
                values,
                selection,
                selectionArgs
        );
    }

    public void deletePlayer(Player player) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String selection = PlayerDatabaseHelper.COLUMN_ID + " = ?";
        String[] selectionArgs = { String.valueOf(player.getId()) };
        db.delete(PlayerDatabaseHelper.TABLE_PLAYERS, selection, selectionArgs);
    }

    public void deleteAllPlayers() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(PlayerDatabaseHelper.TABLE_PLAYERS, null, null);
    }

    @SuppressLint("Range")
    public List<Player> getAllPlayers() {
        List<Player> players = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(
                PlayerDatabaseHelper.TABLE_PLAYERS,
                null,
                null,
                null,
                null,
                null,
                PlayerDatabaseHelper.COLUMN_NAME + " ASC"
        );

        while (cursor.moveToNext()) {
            long id = cursor.getLong(cursor.getColumnIndex(PlayerDatabaseHelper.COLUMN_ID));
            String name = cursor.getString(cursor.getColumnIndex(PlayerDatabaseHelper.COLUMN_NAME));
            int maxHp = cursor.getInt(cursor.getColumnIndex(PlayerDatabaseHelper.COLUMN_MAX_HP));
            int currentHp = cursor.getInt(cursor.getColumnIndex(PlayerDatabaseHelper.COLUMN_CURRENT_HP));

            Player player = new Player(id, name, maxHp);
            player.setCurrentHp(currentHp);
            players.add(player);
        }
        cursor.close();

        return players;
    }
}
