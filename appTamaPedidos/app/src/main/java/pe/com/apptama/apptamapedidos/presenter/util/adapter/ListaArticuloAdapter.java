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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import pe.com.apptama.apptamapedidos.R;
import pe.com.apptama.apptamapedidos.presenter.model.ArticuloModel;
import pe.com.apptama.apptamapedidos.presenter.view.view.ListaArticulosView;

public class ListaArticuloAdapter extends RecyclerView.Adapter<ListaArticuloAdapter.ListaArticuloViewHolder> implements Filterable {

    private Context mContext;
    private ListaArticulosView mView;
    private List<ArticuloModel> articuloModelList;
    private List<ArticuloModel> articuloModelListFiltered;
    boolean isDark = false;


    public ListaArticuloAdapter(Context mContext, List<ArticuloModel> articuloModelList, boolean isDark, ListaArticulosView view) {
        this.mContext = mContext;
        this.articuloModelList = articuloModelList;
        this.articuloModelListFiltered = articuloModelList;
        this.isDark = isDark;
        this.mView = view;
    }


    @NonNull
    @Override
    public ListaArticuloViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View layout;
        layout = LayoutInflater.from(mContext).inflate(R.layout.item_articulo, viewGroup, false);

        return new ListaArticuloViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull ListaArticuloViewHolder listaArticuloViewHolder, int i) {

        // seteamos animacion
        listaArticuloViewHolder.iv_arti.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.fade_transition_animation));
        listaArticuloViewHolder.rl_content.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.fade_scale_animation));
        listaArticuloViewHolder.iv_agregar_carrito.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.fade_scale_animation));

        // seteamos los datos
        listaArticuloViewHolder.tv_title.setText(articuloModelListFiltered.get(i).getArticuloNombre());
        listaArticuloViewHolder.tv_precio.setText(articuloModelListFiltered.get(i).getArticuloPrecio());
        listaArticuloViewHolder.tv_content1.setText(articuloModelListFiltered.get(i).getArticuloUnidades().replace(",", "\n"));
        listaArticuloViewHolder.tv_content2.setText(articuloModelListFiltered.get(i).getArticuloPrecios().replace(" ", ": S/. ").replace(",", "\n"));
        listaArticuloViewHolder.iv_arti.setImageResource(articuloModelListFiltered.get(i).getArticuloPhoto());
        final String codigo = articuloModelListFiltered.get(i).getKey(), descripcion = articuloModelListFiltered.get(i).getArticuloNombre(), precio = articuloModelListFiltered.get(i).getArticuloPrecio(), cantidad = "1";
        final String unidades = articuloModelListFiltered.get(i).getArticuloUnidades();
        final String precios = articuloModelListFiltered.get(i).getArticuloPrecios();
        final String equivalentes = articuloModelListFiltered.get(i).getArticuloEquivalentes();
        listaArticuloViewHolder.iv_agregar_carrito.setVisibility(View.VISIBLE);
        listaArticuloViewHolder.iv_opcion.setVisibility(View.GONE);
        listaArticuloViewHolder.tv_nro_item.setVisibility(View.GONE);

        listaArticuloViewHolder.iv_agregar_carrito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mView.showDialogIngresarCantidad(codigo, descripcion, precio, cantidad, unidades, precios, equivalentes);
            }
        });


    }

    @Override
    public int getItemCount() {
        return articuloModelListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                String key = constraint.toString();
                if (key.isEmpty()) {
                    articuloModelListFiltered = articuloModelList;
                } else {
                    List<ArticuloModel> lstFiltered = new ArrayList<>();
                    for (ArticuloModel row : articuloModelList) {
                        if (row.getArticuloNombre().toLowerCase().contains(key.toLowerCase())) {
                            lstFiltered.add(row);
                        }
                    }
                    articuloModelListFiltered = lstFiltered;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = articuloModelListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                articuloModelListFiltered = (List<ArticuloModel>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ListaArticuloViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout rl_content;
        TextView tv_title;
        TextView tv_content1;
        TextView tv_content2;
        TextView tv_precio;
        TextView tv_nro_item;
        ImageView iv_arti, iv_opcion, iv_agregar_carrito;

        public ListaArticuloViewHolder(@NonNull View itemView) {
            super(itemView);

            rl_content = itemView.findViewById(R.id.rl_content);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_content1 = itemView.findViewById(R.id.tv_content1);
            tv_content2 = itemView.findViewById(R.id.tv_content2);
            tv_precio = itemView.findViewById(R.id.tv_precio);
            tv_nro_item = itemView.findViewById(R.id.tv_nro_item);
            iv_arti = itemView.findViewById(R.id.iv_arti);
            iv_opcion = itemView.findViewById(R.id.iv_opcion);
            iv_agregar_carrito = itemView.findViewById(R.id.iv_agregar_carrito);

            if (isDark) {
                setDarkTheme();
            }
        }

        private void setDarkTheme() {
            rl_content.setBackgroundResource(R.drawable.card_dark_bg);
        }
    }


}
