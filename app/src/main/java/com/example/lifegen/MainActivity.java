package com.example.lifegen;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements AddPlayerDialog.AddPlayerDialogListener, PlayerAdapter.OnPlayerInteractionListener, AmountInputDialog.AmountDialogListener {

    private PlayerViewModel playerViewModel;
    private PlayerAdapter playerAdapter;

    private RecyclerView recyclerViewPlayers;
    private TextView txtNoBattlePlayers;
    private Button btnEndBattle;

    private Player selectedPlayer;
    private boolean isHealAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playerViewModel = new ViewModelProvider(this).get(PlayerViewModel.class);

        setupUI();
        observePlayers();
    }

    private void setupUI() {
        recyclerViewPlayers = findViewById(R.id.recyclerViewPlayers);
        txtNoBattlePlayers = findViewById(R.id.txtNoBattlePlayers);
        Button btnAddPlayer = findViewById(R.id.btnAddPlayer);
        btnEndBattle = findViewById(R.id.btnEndBattle);

        playerAdapter = new PlayerAdapter(this);
        recyclerViewPlayers.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewPlayers.setAdapter(playerAdapter);

        btnAddPlayer.setOnClickListener(view -> showAddPlayerDialog());
        btnEndBattle.setOnClickListener(view -> showEndBattleConfirmationDialog());
    }

    private void observePlayers() {
        playerViewModel.getPlayers().observe(this, players -> {
            boolean isListEmpty = players == null || players.isEmpty();
            recyclerViewPlayers.setVisibility(isListEmpty ? View.GONE : View.VISIBLE);
            txtNoBattlePlayers.setVisibility(isListEmpty ? View.VISIBLE : View.GONE);
            btnEndBattle.setVisibility(isListEmpty ? View.GONE : View.VISIBLE);
            playerAdapter.submitList(players);
        });
    }

    private void showAmountDialog(String title) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        // A verificação de segurança: só mostra o diálogo se não houver outro com a mesma tag.
        if (fragmentManager.findFragmentByTag("amountDialog") == null) {
            AmountInputDialog.newInstance(title).show(fragmentManager, "amountDialog");
        }
    }

    // --- Listeners da Interface ---

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
            playerViewModel.applyHealthChange(selectedPlayer, finalAmount);
        }
    }
    
    // --- Outros Métodos ---

    private void showEndBattleConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.end_battle)
                .setMessage(R.string.end_battle_confirmation)
                .setPositiveButton(R.string.yes, (dialog, which) -> {
                    playerViewModel.endBattle();
                    Toast.makeText(this, "Batalha encerrada!", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton(R.string.no, null)
                .show();
    }

    @Override
    public void onAddPlayer(String name, int life) {
        playerViewModel.addPlayer(name, life);
        Toast.makeText(this, "Jogador adicionado!", Toast.LENGTH_SHORT).show();
    }

    private void showAddPlayerDialog() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.findFragmentByTag("addPlayerDialog") == null) {
            AddPlayerDialog addPlayerDialog = new AddPlayerDialog();
            addPlayerDialog.show(fragmentManager, "addPlayerDialog");
        }
    }
}
