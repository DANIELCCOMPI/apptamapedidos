package pe.com.apptama.apptamapedidos.presenter.view.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import pe.com.apptama.apptamapedidos.R;
import pe.com.apptama.apptamapedidos.data.entity.ArticuloEntity;
import pe.com.apptama.apptamapedidos.data.local.ArticuloDAO;
import pe.com.apptama.apptamapedidos.presenter.model.ArticuloModel;
import pe.com.apptama.apptamapedidos.presenter.model.ArticuloModelHashTable;
import pe.com.apptama.apptamapedidos.presenter.model.CarritoModel;
import pe.com.apptama.apptamapedidos.presenter.model.CarritoModelHashTable;
import pe.com.apptama.apptamapedidos.presenter.model.PedidoModel;
import pe.com.apptama.apptamapedidos.presenter.model.PedidoModelHashTable;
import pe.com.apptama.apptamapedidos.presenter.util.DeviceUtils;
import pe.com.apptama.apptamapedidos.presenter.util.adapter.ListaArticuloAdapter;
import pe.com.apptama.apptamapedidos.presenter.util.adapter.ListaCarritoAdapter;
import pe.com.apptama.apptamapedidos.presenter.util.adapter.ListaPedidoAdapter;
import pe.com.apptama.apptamapedidos.presenter.view.fragment.IngresarCantidadFragmentDialog;
import pe.com.apptama.apptamapedidos.presenter.view.fragment.IngresarPedidoCabFragmentDialog;
import pe.com.apptama.apptamapedidos.presenter.view.receiver.SyncReceiver;
import pe.com.apptama.apptamapedidos.presenter.view.view.ListaArticulosView;
import pe.com.apptama.apptamapedidos.presenter.view.view.ListaCarritoView;
import pe.com.apptama.apptamapedidos.presenter.view.view.ListaPedidoView;

public class ListaArticulosActivity extends AppCompatActivity implements
        ListaArticulosView,
        ListaCarritoView,
        ListaPedidoView,
        IngresarCantidadFragmentDialog.IngresarCantidadDialogListener,
        IngresarPedidoCabFragmentDialog.IngresarPedidoCabDialogListener,
        BottomNavigationView.OnNavigationItemSelectedListener,
        View.OnClickListener {

    public static final String REFRESH = "REFRESH";
    private static final String ARG_ARTI = "ARG_ARTI";
    private static final String ARG_ID = "arg_id";
    private static final String ARG_DES = "arg_des";
    private static final String ARG_PRE = "arg_pre";
    private static final String ARG_CNT = "arg_cnt";
    private static final String PATH_SHOPPING = "shoppingcart";
    private static final String ARG_PQT = "arg_pqt";
    private static final String ARG_PQT_PRE = "arg_pqt_pre";
    private static final String ARG_EQUI = "arg_equiv";
    private static final String ARG_SELECT = "arg_select";
    private static final String PATH_ORDER = "purchaseorder";
    private static final String ARG_ID_FB = "arg_id_firebase";
    private static final String PATH_SELLER = "seller_company";
    private CoordinatorLayout cl_activity;
    private TextView tv_texto_carrito;
    private Button btn_save_pedido;
    private EditText et_busqueda;
    private MenuItem action_busca;
    private boolean bIsBusqueda = false;
    private Toolbar toolbar;
    private AppBarLayout abl_toolbar;
    private Double dTotalCarrito = 0D;
    BottomNavigationView navView;
    private ConstraintLayout inicio_layout, carrito_layout, pedidos_layout;
    private RecyclerView rv_lista_articulo, rv_lista_carrito, rv_lista_pedido;
    private ListaArticuloAdapter listaArticuloAdapter;
    private ListaCarritoAdapter listaCarritoAdapter;
    private ListaPedidoAdapter listaPedidoAdapter;
    private List<ArticuloModel> articuloModelList;
    private List<CarritoModel> carritoModelList;
    private List<PedidoModel> pedidoModelList;
    private List<String> sallerModelList;

    boolean isDark = false;
    private int indSel = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_articulos);

        if (savedInstanceState != null) {
            indSel = savedInstanceState.getInt(ARG_SELECT, 0);
        }

        navView = findViewById(R.id.nav_view);
        inicio_layout = findViewById(R.id.inicio_layout);
        carrito_layout = findViewById(R.id.carrito_layout);
        pedidos_layout = findViewById(R.id.pedidos_layout);
        rv_lista_articulo = findViewById(R.id.rv_lista_articulo);
        rv_lista_carrito = findViewById(R.id.rv_lista_carrito);
        rv_lista_pedido = findViewById(R.id.rv_lista_pedido);
        tv_texto_carrito = findViewById(R.id.tv_texto_carrito);
        btn_save_pedido = findViewById(R.id.btn_save_pedido);
        btn_save_pedido.setOnClickListener(this);
        cl_activity = findViewById(R.id.cl_activity);
        et_busqueda = findViewById(R.id.et_busqueda);
        abl_toolbar = findViewById(R.id.abl_toolbar);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(null);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (bIsBusqueda) {
                    et_busqueda.setVisibility(View.GONE);
                    ocultarTeclado(et_busqueda);
                    et_busqueda.setText("");
                    action_busca.setVisible(true);
                    toolbar.setNavigationIcon(null);
                    bIsBusqueda = false;
                }

            }
        });


        navView.setOnNavigationItemSelectedListener(this);
        isDark = getThemeStatePref();
        if (isDark) {
            et_busqueda.setBackgroundResource(R.drawable.search_input_dark_style);
            et_busqueda.setHintTextColor(getResources().getColor(R.color.content_text_gray_color));
            inicio_layout.setBackgroundColor(getResources().getColor(R.color.black));
            carrito_layout.setBackgroundColor(getResources().getColor(R.color.black));
            pedidos_layout.setBackgroundColor(getResources().getColor(R.color.black));
            toolbar.setBackgroundColor(getResources().getColor(R.color.black));
            abl_toolbar.getContext().setTheme(R.style.AppTheme_AppBarOverlayDark);
        } else {
            et_busqueda.setBackgroundResource(R.drawable.search_input_style);
            et_busqueda.setHintTextColor(getResources().getColor(R.color.content_text_color));
            inicio_layout.setBackgroundColor(getResources().getColor(R.color.white));
            carrito_layout.setBackgroundColor(getResources().getColor(R.color.white));
            pedidos_layout.setBackgroundColor(getResources().getColor(R.color.white));
            toolbar.setBackgroundColor(getResources().getColor(R.color.white));
            abl_toolbar.getContext().setTheme(R.style.AppTheme_AppBarOverlay);
        }

        applyNavigation_select(indSel);

        sallerModelList = new ArrayList<>();
        articuloModelList = ArticuloModelHashTable.getArticuloModelAll();
        carritoModelList = CarritoModelHashTable.getCarritoModelAll();
        pedidoModelList = new ArrayList<>();
        if (articuloModelList.size() > 0) {
            listaArticuloAdapter = new ListaArticuloAdapter(this, articuloModelList, isDark, this);
            rv_lista_articulo.setAdapter(listaArticuloAdapter);
            rv_lista_articulo.setLayoutManager(new LinearLayoutManager(this));

            listaCarritoAdapter = new ListaCarritoAdapter(this, carritoModelList, isDark, this);
            rv_lista_carrito.setAdapter(listaCarritoAdapter);
            rv_lista_carrito.setLayoutManager(new LinearLayoutManager(this));
            calculaTotalCarrito(carritoModelList);


            listaPedidoAdapter = new ListaPedidoAdapter(this, this, pedidoModelList, isDark);
            rv_lista_pedido.setLayoutManager(new LinearLayoutManager(this));
            rv_lista_pedido.setAdapter(listaPedidoAdapter);


        } else {
            actualizarLista();
        }

        et_busqueda.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                listaArticuloAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        FirebaseDatabase database1 = FirebaseDatabase.getInstance();
        DatabaseReference reference1 = database1.getReference(PATH_SELLER);

        reference1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                String codigo_saller = dataSnapshot.getValue(String.class);
                if (!sallerModelList.contains(codigo_saller)) {
                    sallerModelList.add(codigo_saller);
                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {


            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                String codigo_saller = dataSnapshot.getValue(String.class);
                if (sallerModelList.contains(codigo_saller)) {
                    sallerModelList.remove(codigo_saller);
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference(PATH_SHOPPING);

        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                String androidId = DeviceUtils.getAndroidId(getApplicationContext());
                CarritoModel carritoModel = dataSnapshot.getValue(CarritoModel.class);

                if (TextUtils.equals(androidId, carritoModel.getCarritoCodigo())) {
                    carritoModel.setCarritoCodigo(dataSnapshot.getKey());

                    if (!carritoModelList.contains(carritoModel)) {
                        carritoModelList.add(carritoModel);
                    }
                    rv_lista_carrito.getAdapter().notifyDataSetChanged();
                    calculaTotalCarrito(carritoModelList);
                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                String androidId = DeviceUtils.getAndroidId(getApplicationContext());
                CarritoModel carritoModel = dataSnapshot.getValue(CarritoModel.class);

                if (TextUtils.equals(androidId, carritoModel.getCarritoCodigo())) {
                    carritoModel.setCarritoCodigo(dataSnapshot.getKey());

                    for (CarritoModel model : carritoModelList) {

                        if (model.getCarritoCodigo().equals(carritoModel.getCarritoCodigo())) {
                            model.setCarritoDescripcion(carritoModel.getCarritoDescripcion());
                            model.setCarritoCantidad(carritoModel.getCarritoCantidad());
                            model.setCarritoPrecio(carritoModel.getCarritoPrecio());
                            model.setCarritoTotal(carritoModel.getCarritoTotal());
                        }
                    }

                    rv_lista_carrito.getAdapter().notifyDataSetChanged();
                    calculaTotalCarrito(carritoModelList);
                }

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                String androidId = DeviceUtils.getAndroidId(getApplicationContext());
                CarritoModel carritoModel = dataSnapshot.getValue(CarritoModel.class);

                if (TextUtils.equals(androidId, carritoModel.getCarritoCodigo())) {
                    carritoModel.setCarritoCodigo(dataSnapshot.getKey());

                    int i = 0;
                    for (CarritoModel model : carritoModelList) {
                        if (model.getCarritoCodigo().equals(carritoModel.getCarritoCodigo())) {
                            carritoModelList.remove(i);
                            break;
                        }
                        i++;
                    }


                    rv_lista_carrito.getAdapter().notifyDataSetChanged();
                    calculaTotalCarrito(carritoModelList);
                }

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        FirebaseDatabase database2 = FirebaseDatabase.getInstance();
        DatabaseReference reference2 = database2.getReference(PATH_ORDER);

        reference2.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                String androidId = DeviceUtils.getAndroidId(getApplicationContext());
                PedidoModel pedidoModel = dataSnapshot.getValue(PedidoModel.class);

                //if (TextUtils.equals(androidId, pedidoModel.getPedidoCodigo())) {
                pedidoModel.setPedidoCodigo(dataSnapshot.getKey());

                if (!pedidoModelList.contains(pedidoModel)) {
                    pedidoModelList.add(pedidoModel);
                }
                rv_lista_pedido.getAdapter().notifyDataSetChanged();
                //}

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    @Override
    protected void onStart() {
        super.onStart();

        IntentFilter filter = new IntentFilter();
        filter.addAction(REFRESH);
        registerReceiver(receiver, filter);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        if (articuloModelList != null) {
            ArticuloModelHashTable.quitarLineas();
            ArticuloModelHashTable.agregaLinea(1, articuloModelList);
        }

        if (carritoModelList != null) {
            CarritoModelHashTable.quitarLineas();
            CarritoModelHashTable.agregaLinea(1, carritoModelList);
        }

        outState.putInt(ARG_SELECT, indSel);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onStop() {
        unregisterReceiver(receiver);
        super.onStop();
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            if (REFRESH.equals(action)) {
                actualizarLista();
            }
        }
    };

    private void actualizarLista() {


        List<ArticuloEntity> articuloEntities = (new ArticuloDAO(this)).getArticuloAll();
        List<ArticuloModel> articuloModels = new ArrayList<>();
        for (ArticuloEntity entity : articuloEntities) {
            articuloModels.add(new ArticuloModel(entity.getArticuloCodigo(), entity.getArticuloDescripcion(), "S/. " + entity.getArticuloPrecio(), entity.getArticuloUnidades(), entity.getArticuloPrecios(), entity.getArticuloEquivalentes(), R.drawable.ic_desarmador2));
        }


        articuloModelList = articuloModels;
        listaArticuloAdapter = new ListaArticuloAdapter(getApplicationContext(), articuloModelList, isDark, this);
        rv_lista_articulo.setAdapter(listaArticuloAdapter);
        rv_lista_articulo.setLayoutManager(new LinearLayoutManager(this));



        carritoModelList = new ArrayList<>();
        listaCarritoAdapter = new ListaCarritoAdapter(getApplicationContext(), carritoModelList, isDark, this);
        rv_lista_carrito.setAdapter(listaCarritoAdapter);
        rv_lista_carrito.setLayoutManager(new LinearLayoutManager(this));
        calculaTotalCarrito(carritoModelList);

        pedidoModelList = new ArrayList<>();
        listaPedidoAdapter = new ListaPedidoAdapter(this, this, pedidoModelList, isDark);
        rv_lista_pedido.setLayoutManager(new LinearLayoutManager(this));
        rv_lista_pedido.setAdapter(listaPedidoAdapter);
    }


    private void calculaTotalCarrito(List<CarritoModel> carritoModelList) {

        dTotalCarrito = 0D;
        for (CarritoModel carritoModel : carritoModelList) {
            dTotalCarrito += Double.parseDouble(carritoModel.getCarritoTotal());
        }
        DecimalFormat formato = new DecimalFormat("#,###.00");
        String valorFormateado = formato.format(dTotalCarrito);
        String valorTexto = "Valor Total del Carrito S/." + valorFormateado.replace(",", " ");
        tv_texto_carrito.setText(valorTexto);
    }

    private void restartApp() {
        Intent intent = new Intent(getApplicationContext(), ListaArticulosActivity.class);
        startActivity(intent);
        finish();
    }

    private void saveThemeStatePref(boolean isDark) {
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("myPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("isDark", isDark);
        editor.apply();
    }

    private boolean getThemeStatePref() {
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("myPref", MODE_PRIVATE);
        boolean isDark = preferences.getBoolean("isDark", false);
        return isDark;
    }

    private void ocultarTeclado(EditText editText) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_lista_articulos, menu);
        action_busca = menu.findItem(R.id.action_busca);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_busca:
                et_busqueda.setVisibility(View.VISIBLE);
                et_busqueda.requestFocus();
                mostrarTeclado(et_busqueda);
                action_busca.setVisible(false);
                toolbar.setNavigationIcon(R.drawable.ic_keyboard_backspace_black_24dp);
                bIsBusqueda = true;
                break;
            case R.id.action_sincroniza:
                Intent intent = new Intent(this, SyncReceiver.class);
                sendBroadcast(intent);
                break;
            case R.id.action_modo:

                isDark = !isDark;
                if (isDark) {
                    et_busqueda.setBackgroundResource(R.drawable.search_input_dark_style);
                    et_busqueda.setHintTextColor(getResources().getColor(R.color.content_text_gray_color));
                    inicio_layout.setBackgroundColor(getResources().getColor(R.color.black));
                    toolbar.setBackgroundColor(getResources().getColor(R.color.black));
                    abl_toolbar.getContext().setTheme(R.style.AppTheme_AppBarOverlayDark);
                    restartApp();
                } else {
                    et_busqueda.setBackgroundResource(R.drawable.search_input_style);
                    et_busqueda.setHintTextColor(getResources().getColor(R.color.content_text_color));
                    inicio_layout.setBackgroundColor(getResources().getColor(R.color.white));
                    toolbar.setBackgroundColor(getResources().getColor(R.color.white));
                    abl_toolbar.getContext().setTheme(R.style.AppTheme_AppBarOverlay);
                    restartApp();
                }

                listaArticuloAdapter = new ListaArticuloAdapter(getApplicationContext(), articuloModelList, isDark, ListaArticulosActivity.this);
                rv_lista_articulo.setAdapter(listaArticuloAdapter);
                saveThemeStatePref(isDark);
                break;
            case R.id.action_acerca:

                TextView tv_acerca = new TextView(this);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
                );
                tv_acerca.setLayoutParams(params);
                tv_acerca.setGravity(Gravity.CENTER_HORIZONTAL);
                tv_acerca.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                tv_acerca.setText("Desarrollado por AppTama Software \n para Distribuidora Valois.\n \n Programador: Daniel Ccompi.\n Cel:960450835.");


                AlertDialog.Builder builder = new AlertDialog.Builder(this)
                        .setTitle(R.string.lista_articulo_dialog_title_acerca)
                        .setPositiveButton(R.string.lista_articulo_dialog_ok, null);
                builder.setView(tv_acerca);
                builder.show();

                break;

            case R.id.action_settings:

                TextView tv_setting = new TextView(this);

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
                );
                tv_setting.setLayoutParams(layoutParams);
                tv_setting.setGravity(Gravity.CENTER_HORIZONTAL);
                tv_setting.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                tv_setting.setText(DeviceUtils.getAndroidId(this));

                AlertDialog.Builder builder1 = new AlertDialog.Builder(this)
                        .setTitle(R.string.lista_articulo_dialog_title_setting)
                        .setPositiveButton(R.string.lista_articulo_dialog_ok, null);
                builder1.setView(tv_setting);
                builder1.show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void mostrarTeclado(EditText editText) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
    }


    @Override
    public void showDialogIngresarCantidad(String codigo, String descripcion, String precio, String cantidad, String unidades, String precios, String equivalentes) {

        Bundle bundle = new Bundle();
        bundle.putString(ARG_ID, codigo);
        bundle.putString(ARG_DES, descripcion);
        bundle.putString(ARG_PRE, precio.replace("S/. ", ""));
        bundle.putString(ARG_CNT, cantidad);
        bundle.putString(ARG_PQT, unidades);
        bundle.putString(ARG_PQT_PRE, precios);
        bundle.putString(ARG_EQUI, equivalentes);

        IngresarCantidadFragmentDialog fragmentDialog = new IngresarCantidadFragmentDialog();
        fragmentDialog.setArguments(bundle);
        fragmentDialog.show(getSupportFragmentManager(), "Cantidad Dialog");
    }

    @Override
    public void applyDeleteCarritoItem(String codigo) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference(PATH_SHOPPING);
        reference.child(codigo).removeValue();

        Snackbar snackbar = Snackbar.make(cl_activity,
                getResources().getString(R.string.lista_articulo_snackbar_elimina),
                Snackbar.LENGTH_SHORT)
                .setAction("Action", null);

        View sbView = snackbar.getView();
        sbView.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
        snackbar.show();
    }

    @Override
    public void applyEditaCarritoItem(String codigo, String articuloId, String cantidad) {

        ArticuloEntity articuloEntity = (new ArticuloDAO(this)).getArticuloById(articuloId);

        if (articuloEntity != null) {

            Bundle bundle = new Bundle();
            bundle.putString(ARG_ID, articuloId);
            bundle.putString(ARG_DES, articuloEntity.getArticuloDescripcion());
            bundle.putString(ARG_PRE, articuloEntity.getArticuloPrecio().replace("S/. ", ""));
            bundle.putString(ARG_CNT, cantidad);
            bundle.putString(ARG_PQT, articuloEntity.getArticuloUnidades());
            bundle.putString(ARG_PQT_PRE, articuloEntity.getArticuloPrecios());
            bundle.putString(ARG_EQUI, articuloEntity.getArticuloEquivalentes());
            bundle.putString(ARG_ID_FB, codigo);

            IngresarCantidadFragmentDialog fragmentDialog = new IngresarCantidadFragmentDialog();
            fragmentDialog.setArguments(bundle);
            fragmentDialog.show(getSupportFragmentManager(), "Cantidad Dialog");
        }


    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public Context context() {
        return this;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.navigation_home:

                indSel = 1;
                applyNavigation_select(indSel);
                return true;
            case R.id.navigation_carrito:

                indSel = 2;
                applyNavigation_select(indSel);
                return true;
            case R.id.navigation_pedidos:

                indSel = 3;
                applyNavigation_select(indSel);
                return true;
        }
        return false;
    }

    private void applyNavigation_select(int indSel) {

        switch (indSel) {
            case 1:
                inicio_layout.setVisibility(View.VISIBLE);
                carrito_layout.setVisibility(View.GONE);
                pedidos_layout.setVisibility(View.GONE);
                break;
            case 2:
                inicio_layout.setVisibility(View.GONE);
                carrito_layout.setVisibility(View.VISIBLE);
                pedidos_layout.setVisibility(View.GONE);
                break;
            case 3:
                inicio_layout.setVisibility(View.VISIBLE);
                carrito_layout.setVisibility(View.GONE);
                pedidos_layout.setVisibility(View.VISIBLE);
                break;
        }
    }


    @Override
    public void applyCantidad(String codigo, String descripcion, String precio, String cantidad, String total, String sFirebaseId) {

        CarritoModel carritoModel = new CarritoModel(DeviceUtils.getAndroidId(this), codigo, descripcion, precio, cantidad, total);

        if (TextUtils.equals(sFirebaseId, "")) {

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference reference = database.getReference(PATH_SHOPPING);
            reference.push().setValue(carritoModel);


            Snackbar snackbar = Snackbar.make(cl_activity,
                    getResources().getString(R.string.lista_articulo_snackbar_agregar1) + cantidad + "UND. " + descripcion + " " +
                            getResources().getString(R.string.lista_articulo_snackbar_agregar2),
                    Snackbar.LENGTH_SHORT)
                    .setAction("Action", null);

            View sbView = snackbar.getView();
            sbView.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
            snackbar.show();
        } else {


            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference reference = database.getReference(PATH_SHOPPING);
            reference.child(sFirebaseId).setValue(carritoModel);

            Snackbar snackbar = Snackbar.make(cl_activity,
                    getResources().getString(R.string.lista_articulo_snackbar_modifico),
                    Snackbar.LENGTH_SHORT)
                    .setAction("Action", null);

            View sbView = snackbar.getView();
            sbView.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
            snackbar.show();
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_save_pedido:

                if (carritoModelList.size() > 0) {

                    IngresarPedidoCabFragmentDialog fragmentDialog = new IngresarPedidoCabFragmentDialog();
                    fragmentDialog.show(getSupportFragmentManager(), "Pedido Dialog");
                } else {
                    TextView tv_setting = new TextView(this);

                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
                    );
                    tv_setting.setLayoutParams(layoutParams);
                    tv_setting.setGravity(Gravity.CENTER_HORIZONTAL);
                    tv_setting.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                    tv_setting.setText(getResources().getString(R.string.lista_articulo_dialog_msj_carrito_vacio));

                    AlertDialog.Builder builder1 = new AlertDialog.Builder(this)
                            .setTitle(R.string.lista_articulo_dialog_title_info)
                            .setPositiveButton(R.string.lista_articulo_dialog_ok, null);
                    builder1.setView(tv_setting);
                    builder1.show();
                }

                break;
        }
    }

    @Override
    public void applySaveDatosPedido(String nombre, String telefono, String ruc) {

        String androidId = DeviceUtils.getAndroidId(this);
        PedidoModel pedidoModel = new PedidoModel(nombre, telefono, ruc, androidId, carritoModelList);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference(PATH_ORDER);
        reference.push().setValue(pedidoModel);

        FirebaseDatabase database1 = FirebaseDatabase.getInstance();
        DatabaseReference reference1 = database1.getReference(PATH_SHOPPING);
        for (CarritoModel carritoModel : carritoModelList) {
            reference1.child(carritoModel.getCarritoCodigo()).removeValue();
        }

        carritoModelList.clear();
        rv_lista_carrito.getAdapter().notifyDataSetChanged();

        Snackbar snackbar = Snackbar.make(cl_activity,
                getResources().getString(R.string.lista_articulo_snackbar_savepedido),
                Snackbar.LENGTH_SHORT)
                .setAction("Action", null);

        View sbView = snackbar.getView();
        sbView.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
        snackbar.show();

    }

    @Override
    public void goToListaPedido(PedidoModel pedidoModel) {

        PedidoModelHashTable.quitarLineas();
        PedidoModelHashTable.agregaLinea(1, pedidoModel);
        Intent intent = new Intent(this, ListaPedidoActivity.class);
        intent.putExtra("isDark", isDark);
        startActivity(intent);


    }
}
