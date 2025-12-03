package com.example.lifegen;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;

public class MainActivity extends AppCompatActivity
        implements AddPlayerDialog.AddPlayerDialogListener,
        PlayerAdapter.OnPlayerInteractionListener,
        AmountInputDialog.AmountDialogListener {

    private static class ViewHolder {
        PlayerViewModel playerViewModel;
        PlayerAdapter playerAdapter;

        RecyclerView recyclerViewPlayers;
        TextView txtNoBattlePlayers;
        Button btnEndBattle;
        Button btnAddPlayer;
    }

    private final ViewHolder mViewHolder = new ViewHolder();

    private Player selectedPlayer;
    private boolean isHealAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        initViewModel();
        setupRecycler();
        setListeners();
        observePlayers();
    }

    private void initViews() {
        mViewHolder.recyclerViewPlayers = findViewById(R.id.recyclerViewPlayers);
        mViewHolder.txtNoBattlePlayers = findViewById(R.id.txtNoBattlePlayers);
        mViewHolder.btnAddPlayer = findViewById(R.id.btnAddPlayer);
        mViewHolder.btnEndBattle = findViewById(R.id.btnEndBattle);
    }

    private void initViewModel() {
        mViewHolder.playerViewModel = new ViewModelProvider(this).get(PlayerViewModel.class);
    }

    private void setupRecycler() {
        mViewHolder.playerAdapter = new PlayerAdapter(this);
        mViewHolder.recyclerViewPlayers.setLayoutManager(new LinearLayoutManager(this));
        mViewHolder.recyclerViewPlayers.setAdapter(mViewHolder.playerAdapter);
    }

    private void setListeners() {
        mViewHolder.btnAddPlayer.setOnClickListener(v -> showAddPlayerDialog());
        mViewHolder.btnEndBattle.setOnClickListener(v -> showEndBattleConfirmationDialog());
    }

    private void observePlayers() {
        mViewHolder.playerViewModel.getPlayers().observe(this, players -> {
            boolean isListEmpty = players == null || players.isEmpty();
            mViewHolder.recyclerViewPlayers.setVisibility(isListEmpty ? View.GONE : View.VISIBLE);
            mViewHolder.txtNoBattlePlayers.setVisibility(isListEmpty ? View.VISIBLE : View.GONE);
            mViewHolder.btnEndBattle.setVisibility(isListEmpty ? View.GONE : View.VISIBLE);
            mViewHolder.playerAdapter.submitList(players);
        });
    }

    private void showAmountDialog(String title) {
        FragmentManager fm = getSupportFragmentManager();
        if (fm.findFragmentByTag("amountDialog") == null) {
            AmountInputDialog.newInstance(title).show(fm, "amountDialog");
        }
    }

    private void showAddPlayerDialog() {
        FragmentManager fm = getSupportFragmentManager();
        if (fm.findFragmentByTag("addPlayerDialog") == null) {
            new AddPlayerDialog().show(fm, "addPlayerDialog");
        }
    }

    @Override
    public void onHealClick(Player player) {
        selectedPlayer = player;
        isHealAction = true;
        showAmountDialog(getString(R.string.enter_heal_amount));
    }

    @Override
    public void onDamageClick(Player player) {
        selectedPlayer = player;
        isHealAction = false;
        showAmountDialog(getString(R.string.enter_damage_amount));
    }

    @Override
    public void onEditClick(Player player) {
        showEditPlayerDialog(player);
    }

    private void showEditPlayerDialog(Player player) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_edit_player, null);

        final TextInputEditText editPlayerName = dialogView.findViewById(R.id.editPlayerName);
        final TextInputEditText editPlayerMaxHp = dialogView.findViewById(R.id.editPlayerMaxHp);
        Button btnDeletePlayer = dialogView.findViewById(R.id.btnDeletePlayer);

        editPlayerName.setText(player.getName());
        editPlayerMaxHp.setText(String.valueOf(player.getMaxHp()));

        builder.setView(dialogView)
                .setPositiveButton("Salvar", (dialog, id) -> {
                    String newName = editPlayerName.getText().toString();
                    int newMaxHp = Integer.parseInt(editPlayerMaxHp.getText().toString());
                    mViewHolder.playerViewModel.updatePlayer(player, newName, newMaxHp);
                })
                .setNegativeButton("Cancelar", (dialog, id) -> dialog.dismiss());

        AlertDialog alertDialog = builder.create();

        btnDeletePlayer.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Excluir Jogador")
                    .setMessage("Tem certeza que deseja excluir " + player.getName() + "?")
                    .setPositiveButton("Sim", (dialog, which) -> {
                        mViewHolder.playerViewModel.removePlayer(player);
                        alertDialog.dismiss();
                    })
                    .setNegativeButton("Não", null)
                    .show();
        });

        alertDialog.show();
    }

    @Override
    public void onAmountEntered(int amount) {
        if (selectedPlayer != null) {
            int finalAmount = isHealAction ? amount : -amount;
            mViewHolder.playerViewModel.applyHealthChange(selectedPlayer, finalAmount);
        }
    }

    private void showEndBattleConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.end_battle)
                .setMessage(R.string.end_battle_confirmation)
                .setPositiveButton(R.string.yes, (dialog, which) -> {
                    mViewHolder.playerViewModel.endBattle();
                    Toast.makeText(this, R.string.battle_over, Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton(R.string.no, null)
                .show();
    }

    @Override
    public void onAddPlayer(String name, int life) {
        mViewHolder.playerViewModel.addPlayer(name, life);
        Toast.makeText(this, R.string.added_player, Toast.LENGTH_SHORT).show();
    }
}
