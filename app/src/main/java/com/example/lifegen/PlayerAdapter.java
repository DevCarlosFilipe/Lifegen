package com.example.lifegen;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import java.util.Objects;

// Agora estendemos de ListAdapter, que tem DiffUtil embutido
public class PlayerAdapter extends ListAdapter<Player, PlayerAdapter.PlayerViewHolder> {

    public PlayerAdapter() {
        super(DIFF_CALLBACK);
    }

    // O ListAdapter usa um ItemCallback, que é mais simples que o Callback anterior
    private static final DiffUtil.ItemCallback<Player> DIFF_CALLBACK = new DiffUtil.ItemCallback<>() {
        @Override
        public boolean areItemsTheSame(@NonNull Player oldItem, @NonNull Player newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Player oldItem, @NonNull Player newItem) {
            return oldItem.getMaxHp() == newItem.getMaxHp() &&
                    Objects.equals(oldItem.getName(), newItem.getName());
        }
    };

    @NonNull
    @Override
    public PlayerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_player, parent, false);
        return new PlayerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayerViewHolder holder, int position) {
        Player currentPlayer = getItem(position);
        if (currentPlayer != null) {
            holder.bind(currentPlayer);
        }
    }

    // ViewHolder precisa ser público para ser usado na assinatura da classe do Adapter
    public static class PlayerViewHolder extends RecyclerView.ViewHolder {
        private final TextView nameTextView;
        private final TextView hpTextView;

        public PlayerViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.itemPlayerName);
            hpTextView = itemView.findViewById(R.id.itemPlayerHp);
        }

        public void bind(Player player) {
            nameTextView.setText(player.getName());
            hpTextView.setText(String.valueOf(player.getMaxHp()));
        }
    }
}
