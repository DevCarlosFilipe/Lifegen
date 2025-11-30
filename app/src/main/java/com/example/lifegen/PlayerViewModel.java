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

    public void loadPlayers() {
        List<Player> players = repository.getAllPlayers();
        playersLiveData.setValue(players);
    }

    public void addPlayer(String name, int maxHp) {
        repository.insertPlayer(name, maxHp);
        loadPlayers();
    }

    public void endBattle() {
        repository.deleteAllPlayers();
        loadPlayers();
    }

    public void applyHealthChange(Player player, int amount) {
        int newHp = player.getMaxHp() + amount;
        // Garante que a vida não fique negativa
        if (newHp < 0) {
            newHp = 0;
        }

        Player updatedPlayer = new Player(player.getId(), player.getName(), newHp);
        repository.updatePlayer(updatedPlayer);
        loadPlayers();
    }
}
