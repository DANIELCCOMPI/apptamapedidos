package pe.com.apptama.apptamapedidos.presenter.view.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;

import pe.com.apptama.apptamapedidos.R;
import pe.com.apptama.apptamapedidos.presenter.model.CarritoModel;
import pe.com.apptama.apptamapedidos.presenter.model.PedidoModel;
import pe.com.apptama.apptamapedidos.presenter.model.PedidoModelHashTable;
import pe.com.apptama.apptamapedidos.presenter.util.adapter.ListaCarritoAdapter;
import pe.com.apptama.apptamapedidos.presenter.util.adapter.ListaPedidoDetalleAdapter;
import pe.com.apptama.apptamapedidos.presenter.view.view.ListaCarritoView;
import pe.com.apptama.apptamapedidos.presenter.view.view.ListaPedidoDetalleView;

public class ListaPedidoActivity extends AppCompatActivity implements ListaPedidoDetalleView {

    private Toolbar toolbar;
    private RecyclerView rv_lista_pedidodet;
    private ListaPedidoDetalleAdapter listaCarritoAdapter;
    private ConstraintLayout pedido_detalle_layout;
    private AppBarLayout abl_toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_pedido);


        TextView tv_nombre = findViewById(R.id.tv_nombre);
        pedido_detalle_layout = findViewById(R.id.pedido_detalle_layout);
        abl_toolbar = findViewById(R.id.abl_toolbar);
        rv_lista_pedidodet = findViewById(R.id.rv_lista_pedidodet);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });


        Bundle data = getIntent().getExtras();
        if (data != null) {
            boolean isDark = data.getBoolean("isDark", false);
            PedidoModel pedidoModel = PedidoModelHashTable.getPedidoModelAll();
            if (pedidoModel != null) {
                tv_nombre.setText(pedidoModel.getPedidoNombre());

                listaCarritoAdapter = new ListaPedidoDetalleAdapter(this, pedidoModel.getCarritoModelList(), isDark, this);
                rv_lista_pedidodet.setAdapter(listaCarritoAdapter);
                rv_lista_pedidodet.setLayoutManager(new LinearLayoutManager(this));
                calculaTotalCarrito(pedidoModel.getCarritoModelList());
            }

        }

        boolean isDark = getThemeStatePref();
        if (isDark) {
            pedido_detalle_layout.setBackgroundColor(getResources().getColor(R.color.black));
            toolbar.setBackgroundColor(getResources().getColor(R.color.black));
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
            abl_toolbar.getContext().setTheme(R.style.AppTheme_AppBarOverlayDark);
        } else {
            ;
            pedido_detalle_layout.setBackgroundColor(getResources().getColor(R.color.white));
            toolbar.setBackgroundColor(getResources().getColor(R.color.white));
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
            abl_toolbar.getContext().setTheme(R.style.AppTheme_AppBarOverlay);
        }
    }

    private boolean getThemeStatePref() {
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("myPref", MODE_PRIVATE);
        boolean isDark = preferences.getBoolean("isDark", false);
        return isDark;
    }

    private void calculaTotalCarrito(List<CarritoModel> carritoModelList) {
        double dTotalCarrito = 0D;
        dTotalCarrito = 0D;
        for (CarritoModel carritoModel : carritoModelList) {
            dTotalCarrito += Double.parseDouble(carritoModel.getCarritoTotal());
        }
        DecimalFormat formato = new DecimalFormat("#,###.00");
        String valorFormateado = formato.format(dTotalCarrito);
        String valorTexto = "Valor Total del Carrito S/." + valorFormateado.replace(",", " ");
        //tv_texto_carrito.setText(valorTexto);
    }

}
