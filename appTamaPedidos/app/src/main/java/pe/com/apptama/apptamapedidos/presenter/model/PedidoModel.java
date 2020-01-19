package pe.com.apptama.apptamapedidos.presenter.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class PedidoModel  implements Parcelable {

    private String pedidoNombre;
    private String pedidoTelefono;
    private String pedidoRuc;
    private String pedidoCodigo;

    private List<CarritoModel> carritoModelList;

    public PedidoModel() {
    }

    public PedidoModel(String pedidoNombre, String pedidoTelefono, String pedidoRuc, String pedidoCodigo, List<CarritoModel> carritoModelList) {

        this.pedidoNombre = pedidoNombre;
        this.pedidoTelefono = pedidoTelefono;
        this.pedidoRuc = pedidoRuc;
        this.pedidoCodigo = pedidoCodigo;
        this.carritoModelList = carritoModelList;
    }

    protected PedidoModel(Parcel in) {
        pedidoNombre = in.readString();
        pedidoTelefono = in.readString();
        pedidoRuc = in.readString();
        pedidoCodigo = in.readString();
    }

    public static final Creator<PedidoModel> CREATOR = new Creator<PedidoModel>() {
        @Override
        public PedidoModel createFromParcel(Parcel in) {
            return new PedidoModel(in);
        }

        @Override
        public PedidoModel[] newArray(int size) {
            return new PedidoModel[size];
        }
    };

    public String getPedidoNombre() {
        return pedidoNombre;
    }

    public void setPedidoNombre(String pedidoNombre) {
        this.pedidoNombre = pedidoNombre;
    }

    public String getPedidoTelefono() {
        return pedidoTelefono;
    }

    public void setPedidoTelefono(String pedidoTelefono) {
        this.pedidoTelefono = pedidoTelefono;
    }

    public String getPedidoRuc() {
        return pedidoRuc;
    }

    public void setPedidoRuc(String pedidoRuc) {
        this.pedidoRuc = pedidoRuc;
    }

    public String getPedidoCodigo() {
        return pedidoCodigo;
    }

    public void setPedidoCodigo(String pedidoCodigo) {
        this.pedidoCodigo = pedidoCodigo;
    }

    public List<CarritoModel> getCarritoModelList() {
        return carritoModelList;
    }

    public void setCarritoModelList(List<CarritoModel> carritoModelList) {
        this.carritoModelList = carritoModelList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(pedidoNombre);
        dest.writeString(pedidoTelefono);
        dest.writeString(pedidoRuc);
        dest.writeString(pedidoCodigo);
    }
}
