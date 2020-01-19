package pe.com.apptama.apptamapedidos.presenter.view.view;

public interface ListaCarritoView extends  LoadingView{

    void applyDeleteCarritoItem(String codigo);

    void applyEditaCarritoItem(String codigo, String articuloId , String cantidad);
}
