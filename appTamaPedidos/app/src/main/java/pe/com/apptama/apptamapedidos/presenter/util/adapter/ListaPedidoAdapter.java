package pe.com.apptama.apptamapedidos.presenter.util.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;

import pe.com.apptama.apptamapedidos.R;
import pe.com.apptama.apptamapedidos.presenter.model.CarritoModel;
import pe.com.apptama.apptamapedidos.presenter.model.PedidoModel;
import pe.com.apptama.apptamapedidos.presenter.view.view.ListaPedidoView;

public class ListaPedidoAdapter extends RecyclerView.Adapter<ListaPedidoAdapter.ListaPedidoViewHolder> implements Filterable {


    private Context mContext;
    private ListaPedidoView mView;
    private List<PedidoModel> pedidoModelList;
    private List<PedidoModel> pedidoModelListFiltered;
    boolean isDark = false;

    public ListaPedidoAdapter(Context context, ListaPedidoView mView, List<PedidoModel> pedidoModelList, boolean isDark) {
        this.mContext = context;
        this.mView = mView;
        this.pedidoModelList = pedidoModelList;
        this.pedidoModelListFiltered = pedidoModelList;
        this.isDark = isDark;
    }

    @NonNull
    @Override
    public ListaPedidoAdapter.ListaPedidoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View layout;
        layout = LayoutInflater.from(mContext).inflate(R.layout.item_pedido, viewGroup, false);
        return new ListaPedidoViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull ListaPedidoAdapter.ListaPedidoViewHolder listaPedidoViewHolder, int i) {

        // seteamos animacion
        listaPedidoViewHolder.rl_content.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.fade_transition_animation));

        // seteamos los datos
        listaPedidoViewHolder.tv_nombre.setText(pedidoModelListFiltered.get(i).getPedidoNombre());
        listaPedidoViewHolder.tv_content1.setText("Ruc: " + pedidoModelListFiltered.get(i).getPedidoRuc());
        listaPedidoViewHolder.tv_content2.setText("Teléfono: " + pedidoModelListFiltered.get(i).getPedidoTelefono());
        listaPedidoViewHolder.tv_total.setText(obtenerTotal(pedidoModelListFiltered.get(i).getCarritoModelList()));

        final PedidoModel model = pedidoModelListFiltered.get(i);

        listaPedidoViewHolder.rl_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mView.goToListaPedido(model);


            }
        });

    }

    private String obtenerTotal(List<CarritoModel> carritoModelList) {

        double total = 0D;
        for (CarritoModel carritoModel : carritoModelList) {
            total += Double.parseDouble(carritoModel.getCarritoTotal());
        }

        DecimalFormat formato = new DecimalFormat("#,###.00");
        String valorFormateado = formato.format(total);
        return "S/. " + valorFormateado;
    }

    @Override
    public int getItemCount() {
        return pedidoModelListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return null;
    }

    public class ListaPedidoViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout rl_content;
        TextView tv_nombre;
        TextView tv_total;
        TextView tv_content1;
        TextView tv_content2;

        public ListaPedidoViewHolder(@NonNull View itemView) {
            super(itemView);

            rl_content = itemView.findViewById(R.id.rl_content);
            tv_nombre = itemView.findViewById(R.id.tv_nombre);
            tv_total = itemView.findViewById(R.id.tv_total);
            tv_content1 = itemView.findViewById(R.id.tv_content1);
            tv_content2 = itemView.findViewById(R.id.tv_content2);

            if (isDark) {
                setDarkTheme();
            }

        }

        private void setDarkTheme() {
            rl_content.setBackgroundResource(R.drawable.card_dark_bg);
        }
    }
}