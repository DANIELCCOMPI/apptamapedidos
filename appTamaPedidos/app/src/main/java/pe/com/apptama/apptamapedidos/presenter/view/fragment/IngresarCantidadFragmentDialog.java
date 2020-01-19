package pe.com.apptama.apptamapedidos.presenter.view.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;

import pe.com.apptama.apptamapedidos.R;

public class IngresarCantidadFragmentDialog extends AppCompatDialogFragment implements View.OnClickListener {

    TextInputEditText tie_cantidad;
    CardView cv_restar_uno, cv_sumar_uno;
    IngresarCantidadDialogListener listener;
    private static final String ARG_ID = "arg_id";
    private static final String ARG_DES = "arg_des";
    private static final String ARG_PRE = "arg_pre";
    private static final String ARG_CNT = "arg_cnt";
    private static final String ARG_PQT = "arg_pqt";
    private static final String ARG_PQT_PRE = "arg_pqt_pre";
    private static final String ARG_EQUI = "arg_equiv";
    private static final String ARG_ID_FB = "arg_id_firebase";
    String sCodigo, sDescripcion, sPrecio, sCantidad, sPaquetes, sPaquetes_Precios, sEquivalente, sFirebaseId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {
            sCodigo = bundle.getString(ARG_ID, "");
            sDescripcion = bundle.getString(ARG_DES, "");
            sPrecio = bundle.getString(ARG_PRE, "");
            sCantidad = bundle.getString(ARG_CNT, "");
            sPaquetes = bundle.getString(ARG_PQT, "");
            sPaquetes_Precios = bundle.getString(ARG_PQT_PRE, "");
            sEquivalente = bundle.getString(ARG_EQUI, "");
            sFirebaseId = bundle.getString(ARG_ID_FB, "");
        }

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_dialog_ingresar_cantidad, null);

        builder.setView(view)
                .setTitle("Ingresar cantidad")
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String[] paquetesPrecioLst = sPaquetes_Precios.split(",");
                        String[] equivalentesLst = sEquivalente.split(",");
                        String precioReal, equivaReal;
                        String cantidad = String.valueOf(tie_cantidad.getText()).trim();
                        String cantidadReal;
                        String totalReal;

                        if (!cantidad.isEmpty()) {
                            if (paquetesPrecioLst.length > 0 && !sPaquetes_Precios.trim().isEmpty()) {

                                int positionEqu = -1, i = 0;
                                for (String equivalente : equivalentesLst) {
                                    equivalente = equivalente.trim();
                                    if (Integer.parseInt(cantidad) >= Integer.parseInt(equivalente)) {
                                        if ((Integer.parseInt(cantidad) % Integer.parseInt(equivalente)) == 0) {
                                            positionEqu = i;
                                        }
                                    }
                                    i++;
                                }

                                if (positionEqu > -1) {
                                    equivaReal = equivalentesLst[positionEqu].trim();
                                    cantidadReal = String.valueOf(Integer.parseInt(cantidad) / Integer.parseInt(equivaReal));
                                    precioReal = paquetesPrecioLst[positionEqu].trim();
                                } else {
                                    precioReal = sPrecio;
                                    cantidadReal = cantidad;
                                }
                            } else {
                                precioReal = sPrecio;
                                cantidadReal = cantidad;
                            }
                            totalReal = String.valueOf(Integer.parseInt(cantidadReal) * Double.parseDouble(precioReal));
                            precioReal = String.valueOf(Double.parseDouble(totalReal) / Integer.parseInt(cantidad));
                            listener.applyCantidad(sCodigo, sDescripcion, precioReal, cantidad, totalReal,sFirebaseId);
                        }
                    }
                });

        cv_restar_uno = view.findViewById(R.id.cv_restar_uno);
        cv_sumar_uno = view.findViewById(R.id.cv_sumar_uno);
        tie_cantidad = view.findViewById(R.id.tie_cantidad);

        cv_restar_uno.setOnClickListener(this);
        cv_sumar_uno.setOnClickListener(this);

        tie_cantidad.setText(sCantidad);

        return builder.create();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (IngresarCantidadDialogListener) context;
        } catch (Exception e) {
            throw new ClassCastException(context.toString() + "de implementar IngresarCantidadDialog");
        }
    }

    @Override
    public void onClick(View v) {
        String texto;
        switch (v.getId()) {
            case R.id.cv_restar_uno:
                texto = tie_cantidad.getText().toString().trim();
                if (!texto.isEmpty()) {
                    int number = Integer.parseInt(texto);
                    number -= 1;
                    if (number > 0) {
                        tie_cantidad.setText(String.valueOf(number));
                    }
                }

                break;
            case R.id.cv_sumar_uno:
                texto = tie_cantidad.getText().toString().trim();
                if (!texto.isEmpty()) {
                    int number = Integer.parseInt(texto);
                    number += 1;
                    tie_cantidad.setText(String.valueOf(number));
                }
                break;
        }

    }

    public interface IngresarCantidadDialogListener {
        void applyCantidad(String codigo, String descripcion, String precio, String cantidad, String total, String sFirebaseId);
    }

}
