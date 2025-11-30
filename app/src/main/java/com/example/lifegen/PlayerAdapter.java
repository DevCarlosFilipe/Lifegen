package com.example.lifegen;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import java.util.Objects;

public class PlayerAdapter extends ListAdapter<Player, PlayerAdapter.PlayerViewHolder> {

    public interface OnPlayerInteractionListener {
        void onHealClick(Player player);
        void onDamageClick(Player player);
    }

    private final OnPlayerInteractionListener listener;

    // O listener é essencial, então o marcamos como NonNull para garantir que seja sempre fornecido.
    public PlayerAdapter(@NonNull OnPlayerInteractionListener listener) {
        super(DIFF_CALLBACK);
        this.listener = listener;
    }

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
        // O ViewHolder não precisa mais do listener em seu construtor.
        return new PlayerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayerViewHolder holder, int position) {
        Player currentPlayer = getItem(position);
        if (currentPlayer != null) {
            holder.bind(currentPlayer);

            // A lógica do listener agora fica aqui: o local mais seguro e limpo.
            holder.btnHeal.setOnClickListener(v -> listener.onHealClick(currentPlayer));
            holder.btnDamage.setOnClickListener(v -> listener.onDamageClick(currentPlayer));
        }
    }

    // ViewHolder agora é mais simples: sua única função é segurar as views e preenchê-las.
    public static class PlayerViewHolder extends RecyclerView.ViewHolder {
        private final TextView nameTextView;
        private final TextView hpTextView;
        private final ImageButton btnHeal;
        private final ImageButton btnDamage;

        public PlayerViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.itemPlayerName);
            hpTextView = itemView.findViewById(R.id.itemPlayerHp);
            btnHeal = itemView.findViewById(R.id.btnHeal);
            btnDamage = itemView.findViewById(R.id.btnDamage);
        }

        public void bind(Player player) {
            nameTextView.setText(player.getName());
            hpTextView.setText(String.valueOf(player.getMaxHp()));
        }
    }
}
