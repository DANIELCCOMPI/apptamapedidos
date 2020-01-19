package pe.com.apptama.apptamapedidos.presenter.view.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;

import java.util.regex.Pattern;

import pe.com.apptama.apptamapedidos.R;

public class IngresarPedidoCabFragmentDialog extends AppCompatDialogFragment {


    IngresarPedidoCabDialogListener listener;
    private TextInputEditText tie_nombre, tie_telefono, tie_ruc;
    private TextInputLayout til_nombre, til_telefono, til_ruc;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_dialog_ingresar_pedidocab, null);

        builder.setView(view)
                .setTitle("Ingresar datos del Pedido")
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.applySaveDatosPedido(String.valueOf(tie_nombre.getText()).trim(), String.valueOf(tie_telefono.getText()).trim(), String.valueOf(tie_ruc.getText()).trim());
                    }
                });

        tie_nombre = view.findViewById(R.id.tie_nombre);
        tie_telefono = view.findViewById(R.id.tie_telefono);
        tie_ruc = view.findViewById(R.id.tie_ruc);
        til_nombre = view.findViewById(R.id.til_nombre);
        til_telefono = view.findViewById(R.id.til_telefono);
        til_ruc = view.findViewById(R.id.til_ruc);

        tie_nombre.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                esNombreValido(String.valueOf(s));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        tie_telefono.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                esTelefonoValido(String.valueOf(s));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        tie_ruc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                esRucValido(String.valueOf(s));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        return builder.create();
    }

    private void esTelefonoValido(String telefono) {
        if (!Patterns.PHONE.matcher(telefono).matches()) {
            til_telefono.setError("Teléfono inválido");
        } else {
            til_telefono.setError(null);
        }

    }

    private void esNombreValido(String nombre) {
        Pattern patron = Pattern.compile("^[a-zA-Z ]+$");
        if (!patron.matcher(nombre).matches() || nombre.length() > 30) {
            til_nombre.setError("Nombre inválido");
        } else {
            til_nombre.setError(null);
        }

    }

    private void esRucValido(String ruc) {
        if (ruc.length() < 11) {
            til_ruc.setError("Ruc inválido");
        } else {
            til_ruc.setError(null);
        }

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (IngresarPedidoCabDialogListener) context;
        } catch (Exception e) {
            throw new ClassCastException(context.toString() + "de implementar IngresarCantidadDialog");
        }
    }

    public interface IngresarPedidoCabDialogListener {

        void applySaveDatosPedido(String nombre, String telefono, String ruc);
    }

}

