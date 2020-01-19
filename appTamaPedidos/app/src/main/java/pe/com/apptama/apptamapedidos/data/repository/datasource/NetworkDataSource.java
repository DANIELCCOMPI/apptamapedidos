package pe.com.apptama.apptamapedidos.data.repository.datasource;

import pe.com.apptama.apptamapedidos.data.entity.DeviceEntity;
import pe.com.apptama.apptamapedidos.data.entity.DownloadDataEntity;
import pe.com.apptama.apptamapedidos.data.network.RestApi;

public class NetworkDataSource implements DataSource {
    private final RestApi restApi;
    private static final String OP_NO_PERM = "Operacion no permitida";

    public NetworkDataSource(RestApi restApi) {
        this.restApi = restApi;
    }

    @Override
    public boolean insertLocal(DownloadDataEntity downloadDataEntity) throws Exception {
        throw new Exception(OP_NO_PERM);
    }

    @Override
    public DownloadDataEntity downloadData(DeviceEntity entity) throws Exception {
        return restApi.downloadData(entity.getDeviceid().toString());
    }
}
