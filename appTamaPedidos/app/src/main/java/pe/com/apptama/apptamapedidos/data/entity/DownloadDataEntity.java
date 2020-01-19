package pe.com.apptama.apptamapedidos.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DownloadDataEntity extends RespEntity {

    @Expose
    @SerializedName("articulos")
    private List<ArticuloEntity> articuloEntityList;

    public List<ArticuloEntity> getArticuloEntityList() {
        return articuloEntityList;
    }

    public void setArticuloEntityList(List<ArticuloEntity> articuloEntityList) {
        this.articuloEntityList = articuloEntityList;
    }

    public boolean isEmpty() {
        boolean res = true;
        if (articuloEntityList != null && articuloEntityList.size() > 0) res = false;
        return res;
    }

}
