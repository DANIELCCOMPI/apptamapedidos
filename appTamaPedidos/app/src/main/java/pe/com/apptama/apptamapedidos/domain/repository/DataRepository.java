package pe.com.apptama.apptamapedidos.domain.repository;

import pe.com.apptama.apptamapedidos.data.entity.DeviceEntity;
import pe.com.apptama.apptamapedidos.data.entity.DownloadDataEntity;

public interface DataRepository {

    boolean insertLocal(DownloadDataEntity downloadDataEntity) throws Exception;

    DownloadDataEntity downloadData(DeviceEntity entity) throws Exception;

}
