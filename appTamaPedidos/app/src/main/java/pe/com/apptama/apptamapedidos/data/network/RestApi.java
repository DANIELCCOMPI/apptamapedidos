package pe.com.apptama.apptamapedidos.data.network;

import pe.com.apptama.apptamapedidos.data.entity.DeviceEntity;
import pe.com.apptama.apptamapedidos.data.entity.DownloadDataEntity;

public interface RestApi {

    DownloadDataEntity downloadData(String  deviceId) throws Exception;

}
