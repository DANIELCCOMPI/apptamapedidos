package pe.com.apptama.apptamapedidos.presenter.util.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import pe.com.apptama.apptamapedidos.R;
import pe.com.apptama.apptamapedidos.presenter.model.CarritoModel;
import pe.com.apptama.apptamapedidos.presenter.view.view.ListaCarritoView;

public class ListaCarritoAdapter extends RecyclerView.Adapter<ListaCarritoAdapter.ListaCarritoViewHolder> implements Filterable {

    private Context mContext;
    private ListaCarritoView mView;
    private List<CarritoModel> carritoModelList;
    private List<CarritoModel> carritoModelListFiltered;
    boolean isDark = false;

    public ListaCarritoAdapter(Context mContext, List<CarritoModel> carritoModelList, boolean isDark, ListaCarritoView view) {
        this.mContext = mContext;
        this.mView = view;
        this.carritoModelList = carritoModelList;
        this.carritoModelListFiltered = carritoModelList;
        this.isDark = isDark;
    }

    @NonNull
    @Override
    public ListaCarritoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View layout;
        layout = LayoutInflater.from(mContext).inflate(R.layout.item_articulo, viewGroup, false);

        return new ListaCarritoViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull final ListaCarritoViewHolder listaCarritoViewHolder, int i) {


        // seteamos animacion
        listaCarritoViewHolder.iv_arti.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.fade_transition_animation));
        listaCarritoViewHolder.rl_content.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.fade_scale_animation));
        listaCarritoViewHolder.iv_opcion.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.fade_scale_animation));

        // seteamos los datos
        listaCarritoViewHolder.tv_title.setText(carritoModelListFiltered.get(i).getCarritoDescripcion());
        listaCarritoViewHolder.tv_precio.setText(carritoModelListFiltered.get(i).getCarritoCantidad() + " UND");
        listaCarritoViewHolder.tv_content1.setText("Precio S/. " + carritoModelListFiltered.get(i).getCarritoPrecio());
        listaCarritoViewHolder.tv_content2.setText("Total S/." + carritoModelListFiltered.get(i).getCarritoTotal());
        String nro_item = String.valueOf(i + 1);
        listaCarritoViewHolder.tv_nro_item.setText(nro_item);

        listaCarritoViewHolder.iv_opcion.setVisibility(View.VISIBLE);
        listaCarritoViewHolder.tv_nro_item.setVisibility(View.VISIBLE);
        listaCarritoViewHolder.iv_agregar_carrito.setVisibility(View.GONE);
        final String codigoFirebase = carritoModelListFiltered.get(i).getCarritoCodigo();
        final String articuloId = carritoModelListFiltered.get(i).getCarritoArticuloId();
        final String cantitdad = carritoModelListFiltered.get(i).getCarritoCantidad();

        listaCarritoViewHolder.iv_opcion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PopupMenu popup = new PopupMenu(mContext, listaCarritoViewHolder.iv_opcion);
                popup.getMenuInflater().inflate(R.menu.menu_item_carrito, popup.getMenu());
                popup.getMenu().findItem(R.id.action_opciones).setChecked(true);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()) {

                            case R.id.action_editar:

                                mView.applyEditaCarritoItem(codigoFirebase, articuloId, cantitdad);
                                break;
                            case R.id.action_eliminar:

                                mView.applyDeleteCarritoItem(codigoFirebase);

                                break;
                        }

                        return true;
                    }
                });

                popup.show();

            }
        });

    }

    @Override
    public int getItemCount() {
        return carritoModelListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                String key = constraint.toString();
                if (key.isEmpty()) {
                    carritoModelListFiltered = carritoModelList;
                } else {
                    List<CarritoModel> lstFiltered = new ArrayList<>();
                    for (CarritoModel row : carritoModelList) {
                        if (row.getCarritoDescripcion().toLowerCase().contains(key.toLowerCase())) {
                            lstFiltered.add(row);
                        }
                    }
                    carritoModelListFiltered = lstFiltered;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = carritoModelListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                carritoModelListFiltered = (List<CarritoModel>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ListaCarritoViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout rl_content;
        TextView tv_title;
        TextView tv_content1;
        TextView tv_content2;
        TextView tv_precio;
        TextView tv_nro_item;
        ImageView iv_arti, iv_opcion, iv_agregar_carrito;

        public ListaCarritoViewHolder(@NonNull View itemView) {
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
