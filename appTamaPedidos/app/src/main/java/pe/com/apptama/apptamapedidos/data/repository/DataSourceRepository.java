package pe.com.apptama.apptamapedidos.data.repository;


import android.net.NetworkInfo;

import pe.com.apptama.apptamapedidos.data.entity.DeviceEntity;
import pe.com.apptama.apptamapedidos.data.entity.DownloadDataEntity;
import pe.com.apptama.apptamapedidos.data.repository.datasource.DataSource;
import pe.com.apptama.apptamapedidos.data.repository.datasource.DataSourceFactory;
import pe.com.apptama.apptamapedidos.data.util.NetUtils;
import pe.com.apptama.apptamapedidos.domain.repository.DataRepository;

public class DataSourceRepository implements DataRepository {

    private final DataSourceFactory factory;

    public DataSourceRepository(DataSourceFactory factory) {
        this.factory = factory;
    }

    @Override
    public boolean insertLocal(DownloadDataEntity downloadDataEntity) throws Exception {
        final DataSource local = factory.crear(false);
        return local.insertLocal(downloadDataEntity);
    }

    @Override
    public DownloadDataEntity downloadData(DeviceEntity entity) throws Exception {
        if (NetUtils.hayInternet(factory.getContext())) {
            final DataSource network = factory.crear(true);
            return network.downloadData(entity);
        } else {
            throw new Exception("No hay conexión");
        }
    }


}
