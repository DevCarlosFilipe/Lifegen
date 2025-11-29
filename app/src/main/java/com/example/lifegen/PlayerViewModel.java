package com.example.lifegen;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import java.util.List;

public class PlayerViewModel extends AndroidViewModel {
    private final PlayerRepository repository;
    // LiveData que observaremos na Activity. É privado para que só o ViewModel possa alterá-lo.
    private final MutableLiveData<List<Player>> playersLiveData = new MutableLiveData<>();

    public PlayerViewModel(@NonNull Application application) {
        super(application);
        repository = new PlayerRepository(application);
        // Carrega os dados iniciais
        loadPlayers();
    }

    // Método público para a View (Activity) observar os dados.
    public LiveData<List<Player>> getPlayers() {
        return playersLiveData;
    }

    // Carrega ou recarrega a lista de jogadores do repositório
    public void loadPlayers() {
        List<Player> players = repository.getAllPlayers();
        playersLiveData.setValue(players);
    }

    // Adiciona um novo jogador
    public void addPlayer(String name, int maxHp) {
        long result = repository.insertPlayer(name, maxHp);
        if (result > 0) {
            // Se o jogador foi inserido com sucesso, recarrega a lista
            loadPlayers();
        }
    }

    // Encerra a batalha, deletando todos os jogadores
    public void endBattle() {
        repository.deleteAllPlayers();
        // Recarrega a lista (que agora estará vazia) para notificar a UI
        loadPlayers();
    }
}
