package pe.com.apptama.apptamapedidos.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RespEntity {

    @Expose
    @SerializedName("status")
    private String status;

    @Expose
    @SerializedName("msj")
    private String msj;
}
