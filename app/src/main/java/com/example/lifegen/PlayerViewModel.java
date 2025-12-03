package com.example.lifegen;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import java.util.List;

public class PlayerViewModel extends AndroidViewModel {
    private final PlayerRepository repository;
    private final MutableLiveData<List<Player>> playersLiveData = new MutableLiveData<>();

    public PlayerViewModel(@NonNull Application application) {
        super(application);
        repository = new PlayerRepository(application);
        loadPlayers();
    }

    public LiveData<List<Player>> getPlayers() {
        return playersLiveData;
    }

    private void loadPlayers() {
        List<Player> players = repository.getAllPlayers();
        playersLiveData.postValue(players);
    }

    public void addPlayer(String name, int maxHp) {
        repository.insertPlayer(name, maxHp);
        loadPlayers();
    }

    public void removePlayer(Player player) {
        repository.deletePlayer(player);
        loadPlayers();
    }

    public void updatePlayer(Player player, String newName, int newMaxHp) {
        player.setName(newName);
        player.setMaxHp(newMaxHp);

        if (player.getCurrentHp() > newMaxHp) {
            player.setCurrentHp(newMaxHp);
        }

        repository.updatePlayer(player);
        loadPlayers();
    }

    public void endBattle() {
        repository.deleteAllPlayers();
        loadPlayers();
    }

    public void applyHealthChange(Player player, int amount) {
        int newHp = player.getCurrentHp() + amount;

        if (newHp > player.getMaxHp()) {
            newHp = player.getMaxHp();
        } else if (newHp < 0) {
            newHp = 0;
        }

        player.setCurrentHp(newHp);
        repository.updatePlayer(player);
        loadPlayers();
    }
}
