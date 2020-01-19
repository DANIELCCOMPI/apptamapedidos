package pe.com.apptama.apptamapedidos.data.network;

import pe.com.apptama.apptamapedidos.data.entity.DownloadDataEntity;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {


    @GET("articulosventa")
    Call<DownloadDataEntity> downloadData(@Query("DeviceId") String  deviceId);

}
