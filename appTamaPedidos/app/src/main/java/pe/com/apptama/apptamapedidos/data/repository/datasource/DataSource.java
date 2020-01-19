package pe.com.apptama.apptamapedidos.data.repository.datasource;

import pe.com.apptama.apptamapedidos.data.entity.DeviceEntity;
import pe.com.apptama.apptamapedidos.data.entity.DownloadDataEntity;

public interface DataSource {

    boolean insertLocal(DownloadDataEntity downloadDataEntity) throws Exception;

    DownloadDataEntity downloadData(DeviceEntity entity) throws Exception;

}
