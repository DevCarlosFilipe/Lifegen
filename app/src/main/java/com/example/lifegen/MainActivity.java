package com.example.lifegen;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity
        implements AddPlayerDialog.AddPlayerDialogListener,
        PlayerAdapter.OnPlayerInteractionListener,
        AmountInputDialog.AmountDialogListener {

    // ------------------------------
    //   VIEW HOLDER INTERNO
    // ------------------------------
    private static class ViewHolder {
        PlayerViewModel playerViewModel;
        PlayerAdapter playerAdapter;

        RecyclerView recyclerViewPlayers;
        TextView txtNoBattlePlayers;
        Button btnEndBattle;
        Button btnAddPlayer;
    }

    private ViewHolder mViewHolder = new ViewHolder();  // INSTÂNCIA DO VIEWHOLDER

    private Player selectedPlayer;
    private boolean isHealAction;

    // ------------------------------
    //   ACTIVITY
    // ------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mViewHolder = new ViewHolder();  // IMPORTANTE!!!

        initViews();
        initViewModel();
        setupRecycler();
        setListeners();
        observePlayers();
    }

    // ------------------------------
    //   VIEWHolder Bind
    // ------------------------------
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

    // ------------------------------
    //   OBSERVER
    // ------------------------------
    private void observePlayers() {
        mViewHolder.playerViewModel.getPlayers().observe(this, players -> {

            boolean isListEmpty = players == null || players.isEmpty();

            mViewHolder.recyclerViewPlayers.setVisibility(isListEmpty ? View.GONE : View.VISIBLE);
            mViewHolder.txtNoBattlePlayers.setVisibility(isListEmpty ? View.VISIBLE : View.GONE);
            mViewHolder.btnEndBattle.setVisibility(isListEmpty ? View.GONE : View.VISIBLE);

            mViewHolder.playerAdapter.submitList(players);
        });
    }

    // ------------------------------
    //   DIALOGS
    // ------------------------------
    private void showAmountDialog(String title) {
        FragmentManager fm = getSupportFragmentManager();
        if (fm.findFragmentByTag("amountDialog") == null) {
            AmountInputDialog.newInstance(title).show(fm, "amountDialog");
        }
    }

    private void showAddPlayerDialog() {
        FragmentManager fm = getSupportFragmentManager();
        if (fm.findFragmentByTag("addPlayerDialog") == null) {
            AddPlayerDialog dialog = new AddPlayerDialog();
            dialog.show(fm, "addPlayerDialog");
        }
    }

    // ------------------------------
    //   LISTENER: Heal / Damage
    // ------------------------------
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
    public void onAmountEntered(int amount) {
        if (selectedPlayer != null) {
            int finalAmount = isHealAction ? amount : -amount;
            mViewHolder.playerViewModel.applyHealthChange(selectedPlayer, finalAmount);
        }
    }

    // ------------------------------
    //   FINALIZAR BATALHA
    // ------------------------------
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

    // ------------------------------
    //   CALLBACK DO DIALOG DE ADD PLAYER
    // ------------------------------
    @Override
    public void onAddPlayer(String name, int life) {
        mViewHolder.playerViewModel.addPlayer(name, life);
        Toast.makeText(this, R.string.added_player, Toast.LENGTH_SHORT).show();
    }
}
