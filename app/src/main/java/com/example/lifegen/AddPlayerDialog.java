package com.example.lifegen;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

public class AddPlayerDialog extends DialogFragment {

    private final ViewHolder viewHolder = new ViewHolder();

    // Usando ViewHolder para organizar e referenciar as views do layout
    private static class ViewHolder {
        EditText etDialogName;
        EditText etDialogMaxHp;
        Button btnDialogAdd;
    }

    public interface AddPlayerDialogListener {
        void onAddPlayer(String name, int life);
    }

    private AddPlayerDialogListener listener;

    @Override
    public void onAttach(@NonNull android.content.Context context) {
        super.onAttach(context);
        if (context instanceof AddPlayerDialogListener) {
            listener = (AddPlayerDialogListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement AddPlayerDialogListener");
        }
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Construtor do diálogo
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Titulo
        builder.setTitle(R.string.add_player_npc);
        
        // Infla o layout personalizado para o diálogo
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_player, null);

        // Inicializa as views usando o ViewHolder.
        viewHolder.etDialogName = view.findViewById(R.id.etDialogName);
        viewHolder.etDialogMaxHp = view.findViewById(R.id.etDialogMaxHp);
        viewHolder.btnDialogAdd = view.findViewById(R.id.btnDialogAdd);

        // Exemplo de como adicionar a lógica do botão:
        viewHolder.btnDialogAdd.setOnClickListener(v -> {
             String name = viewHolder.etDialogName.getText().toString();
             String maxHp = viewHolder.etDialogMaxHp.getText().toString();

             if (name.isEmpty() || maxHp.isEmpty()) {
                 Toast.makeText(getContext(), R.string.fill_all_fields, Toast.LENGTH_SHORT).show();
                 return;
             }

             int life = Integer.parseInt(maxHp);
             listener.onAddPlayer(name, life);
             dismiss();
        });

        // Define a view personalizada para o diálogo
        builder.setView(view);

        // Cria e retorna o diálogo
        return builder.create();
    }
}
