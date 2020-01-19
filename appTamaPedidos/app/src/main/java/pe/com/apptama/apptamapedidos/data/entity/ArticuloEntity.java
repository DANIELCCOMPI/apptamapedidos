package pe.com.apptama.apptamapedidos.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Tabla de Articulos
 */

@Entity(nameInDb = "articulo")
public class ArticuloEntity {

    @Id
    @Expose
    @SerializedName("ArticuloCodigo")
    @Property(nameInDb = "idarticulo")
    private String articuloCodigo;

    @Expose
    @SerializedName("ArticuloDescripcion")
    @Property(nameInDb = "descripcion")
    private String articuloDescripcion;

    @Expose
    @SerializedName("ArticuloPrecio")
    @Property(nameInDb = "precio")
    private String articuloPrecio;

    @Expose
    @SerializedName("ArticuloUnidades")
    @Property(nameInDb = "unidades")
    private String articuloUnidades;

    @Expose
    @SerializedName("ArticuloPrecios")
    @Property(nameInDb = "precios")
    private String articuloPrecios;

    @Expose
    @SerializedName("ArticuloEquivalentes")
    @Property(nameInDb = "equivalentes")
    private String articuloEquivalentes;

    @Generated(hash = 1619144080)
    public ArticuloEntity(String articuloCodigo, String articuloDescripcion,
            String articuloPrecio, String articuloUnidades, String articuloPrecios,
            String articuloEquivalentes) {
        this.articuloCodigo = articuloCodigo;
        this.articuloDescripcion = articuloDescripcion;
        this.articuloPrecio = articuloPrecio;
        this.articuloUnidades = articuloUnidades;
        this.articuloPrecios = articuloPrecios;
        this.articuloEquivalentes = articuloEquivalentes;
    }

    @Generated(hash = 600935253)
    public ArticuloEntity() {
    }

    public String getArticuloCodigo() {
        return this.articuloCodigo;
    }

    public void setArticuloCodigo(String articuloCodigo) {
        this.articuloCodigo = articuloCodigo;
    }

    public String getArticuloDescripcion() {
        return this.articuloDescripcion;
    }

    public void setArticuloDescripcion(String articuloDescripcion) {
        this.articuloDescripcion = articuloDescripcion;
    }

    public String getArticuloPrecio() {
        return this.articuloPrecio;
    }

    public void setArticuloPrecio(String articuloPrecio) {
        this.articuloPrecio = articuloPrecio;
    }

    public String getArticuloUnidades() {
        return this.articuloUnidades;
    }

    public void setArticuloUnidades(String articuloUnidades) {
        this.articuloUnidades = articuloUnidades;
    }

    public String getArticuloPrecios() {
        return this.articuloPrecios;
    }

    public void setArticuloPrecios(String articuloPrecios) {
        this.articuloPrecios = articuloPrecios;
    }

    public String getArticuloEquivalentes() {
        return this.articuloEquivalentes;
    }

    public void setArticuloEquivalentes(String articuloEquivalentes) {
        this.articuloEquivalentes = articuloEquivalentes;
    }

    

}
