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


public class AmountInputDialog extends DialogFragment {

    // Interface para comunicar a ação para a Activity
    public interface AmountDialogListener {
        void onAmountEntered(int amount);
    }

    private AmountDialogListener listener;
    private String title;

    public static AmountInputDialog newInstance(String title) {
        AmountInputDialog dialog = new AmountInputDialog();
        Bundle args = new Bundle();
        args.putString("title", title);
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            title = getArguments().getString("title");
        }

        // Define o listener. A Activity que chamar este diálogo DEVE implementar a interface.
        try {
            listener = (AmountDialogListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(requireActivity() + " must implement AmountDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_amount_input, null);

        final EditText etAmount = view.findViewById(R.id.etAmount);
        Button btnApply = view.findViewById(R.id.btnApply);

        btnApply.setOnClickListener(v -> {
            String amountStr = etAmount.getText().toString();
            if (!amountStr.isEmpty()) {
                int amount = Integer.parseInt(amountStr);
                listener.onAmountEntered(amount);
                dismiss();
            } else {
                Toast.makeText(getContext(), "Por favor, insira um valor", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setView(view).setTitle(title);
        return builder.create();
    }
}
