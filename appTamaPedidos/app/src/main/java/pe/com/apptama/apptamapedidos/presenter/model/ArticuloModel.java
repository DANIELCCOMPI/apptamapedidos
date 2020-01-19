package pe.com.apptama.apptamapedidos.presenter.model;

public class ArticuloModel {

    private String key;
    private String articuloNombre;
    private String articuloPrecio;
    private String articuloUnidades;
    private String articuloPrecios;
    private String articuloEquivalentes;
    private int articuloPhoto;

    public ArticuloModel() {
    }


    public ArticuloModel(String key, String articuloNombre, String articuloPrecio, String articuloUnidades, String articuloPrecios, String articuloEquivalentes, int articuloPhoto) {
        this.key = key;
        this.articuloNombre = articuloNombre;
        this.articuloPrecio = articuloPrecio;
        this.articuloUnidades = articuloUnidades;
        this.articuloPrecios = articuloPrecios;
        this.articuloEquivalentes = articuloEquivalentes;
        this.articuloPhoto = articuloPhoto;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getArticuloNombre() {
        return articuloNombre;
    }

    public void setArticuloNombre(String articuloNombre) {
        this.articuloNombre = articuloNombre;
    }

    public String getArticuloPrecio() {
        return articuloPrecio;
    }

    public void setArticuloPrecio(String articuloPrecio) {
        this.articuloPrecio = articuloPrecio;
    }

    public String getArticuloUnidades() {
        return articuloUnidades;
    }

    public void setArticuloUnidades(String articuloUnidades) {
        this.articuloUnidades = articuloUnidades;
    }

    public String getArticuloPrecios() {
        return articuloPrecios;
    }

    public void setArticuloPrecios(String articuloPrecios) {
        this.articuloPrecios = articuloPrecios;
    }

    public String getArticuloEquivalentes() {
        return articuloEquivalentes;
    }

    public void setArticuloEquivalentes(String articuloEquivalentes) {
        this.articuloEquivalentes = articuloEquivalentes;
    }

    public int getArticuloPhoto() {
        return articuloPhoto;
    }

    public void setArticuloPhoto(int articuloPhoto) {
        this.articuloPhoto = articuloPhoto;
    }
}