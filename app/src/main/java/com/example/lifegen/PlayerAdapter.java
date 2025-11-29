package com.example.lifegen;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

// O Adapter é a ponte entre os seus dados (a lista de jogadores) e a UI (o RecyclerView)
public class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.PlayerViewHolder> {

    private List<Player> players = new ArrayList<>();

    @NonNull
    @Override
    public PlayerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Infla o layout do item da lista (item_player.xml) e cria o ViewHolder
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_player, parent, false);
        return new PlayerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayerViewHolder holder, int position) {
        // Pega o jogador na posição atual e exibe seus dados usando o ViewHolder
        Player currentPlayer = players.get(position);
        holder.nameTextView.setText(currentPlayer.getName());
        holder.hpTextView.setText(String.valueOf(currentPlayer.getMaxHp()));
    }

    @Override
    public int getItemCount() {
        return players.size();
    }

    // Método para atualizar a lista de jogadores no adapter
    public void setPlayers(List<Player> players) {
        this.players = players;
        notifyDataSetChanged(); // Notifica o RecyclerView que os dados mudaram
    }

    // Esta é a implementação do padrão ViewHolder para CADA ITEM da sua lista.
    static class PlayerViewHolder extends RecyclerView.ViewHolder {
        private final TextView nameTextView;
        private final TextView hpTextView;

        public PlayerViewHolder(@NonNull View itemView) {
            super(itemView);
            // Encontra as views DENTRO do layout do item (item_player.xml)
            nameTextView = itemView.findViewById(R.id.itemPlayerName);
            hpTextView = itemView.findViewById(R.id.itemPlayerHp);
        }
    }
}
