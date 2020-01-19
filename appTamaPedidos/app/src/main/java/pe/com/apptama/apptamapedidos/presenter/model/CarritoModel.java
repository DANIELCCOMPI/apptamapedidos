package pe.com.apptama.apptamapedidos.presenter.model;

public class CarritoModel {

    private String carritoCodigo;
    private String carritoArticuloId;
    private String carritoDescripcion;
    private String carritoPrecio;
    private String carritoCantidad;
    private String carritoTotal;

    public CarritoModel() {
    }

    public CarritoModel(String carritoCodigo, String carritoArticuloId, String carritoDescripcion, String carritoPrecio, String carritoCantidad, String carritoTotal) {
        this.carritoCodigo = carritoCodigo;
        this.carritoArticuloId = carritoArticuloId;
        this.carritoDescripcion = carritoDescripcion;
        this.carritoPrecio = carritoPrecio;
        this.carritoCantidad = carritoCantidad;
        this.carritoTotal = carritoTotal;
    }

    public String getCarritoCodigo() {
        return carritoCodigo;
    }

    public void setCarritoCodigo(String carritoCodigo) {
        this.carritoCodigo = carritoCodigo;
    }

    public String getCarritoArticuloId() {
        return carritoArticuloId;
    }

    public void setCarritoArticuloId(String carritoArticuloId) {
        this.carritoArticuloId = carritoArticuloId;
    }

    public String getCarritoDescripcion() {
        return carritoDescripcion;
    }

    public void setCarritoDescripcion(String carritoDescripcion) {
        this.carritoDescripcion = carritoDescripcion;
    }

    public String getCarritoPrecio() {
        return carritoPrecio;
    }

    public void setCarritoPrecio(String carritoPrecio) {
        this.carritoPrecio = carritoPrecio;
    }

    public String getCarritoCantidad() {
        return carritoCantidad;
    }

    public void setCarritoCantidad(String carritoCantidad) {
        this.carritoCantidad = carritoCantidad;
    }

    public String getCarritoTotal() {
        return carritoTotal;
    }

    public void setCarritoTotal(String carritoTotal) {
        this.carritoTotal = carritoTotal;
    }
}
