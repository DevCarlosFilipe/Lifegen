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

public class PlayerAdapter extends ListAdapter<Player, PlayerAdapter.ViewHolder> {

    public interface OnPlayerInteractionListener {
        void onHealClick(Player player);
        void onDamageClick(Player player);
        void onEditClick(Player player);
    }

    private final OnPlayerInteractionListener listener;

    public PlayerAdapter(@NonNull OnPlayerInteractionListener listener) {
        super(DIFF_CALLBACK);
        this.listener = listener;
    }

    private static final DiffUtil.ItemCallback<Player> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<>() {
                @Override
                public boolean areItemsTheSame(@NonNull Player oldItem, @NonNull Player newItem) {
                    return oldItem.getId() == newItem.getId();
                }

                @Override
                public boolean areContentsTheSame(@NonNull Player oldItem, @NonNull Player newItem) {
                    return oldItem.getMaxHp() == newItem.getMaxHp() &&
                            oldItem.getCurrentHp() == newItem.getCurrentHp() &&
                            Objects.equals(oldItem.getName(), newItem.getName());
                }
            };

    public static class ViewHolder extends RecyclerView.ViewHolder {

        static class UI {
            TextView nameTextView;
            TextView hpTextView;
            ImageButton btnHeal;
            ImageButton btnDamage;
            ImageButton btnEdit;
        }

        UI ui;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ui = new UI();

            ui.nameTextView = itemView.findViewById(R.id.itemPlayerName);
            ui.hpTextView = itemView.findViewById(R.id.itemPlayerHp);
            ui.btnHeal = itemView.findViewById(R.id.btnHeal);
            ui.btnDamage = itemView.findViewById(R.id.btnDamage);
            ui.btnEdit = itemView.findViewById(R.id.btnEdit);
        }

        public void bind(Player player) {
            ui.nameTextView.setText(player.getName());
            String hpText = player.getCurrentHp() + "/" + player.getMaxHp();
            ui.hpTextView.setText(hpText);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_player, parent, false);
        return new ViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Player player = getItem(position);

        if (player == null) return;

        holder.bind(player);

        holder.ui.btnHeal.setOnClickListener(v -> listener.onHealClick(player));
        holder.ui.btnDamage.setOnClickListener(v -> listener.onDamageClick(player));
        holder.ui.btnEdit.setOnClickListener(v -> listener.onEditClick(player));
    }
}
