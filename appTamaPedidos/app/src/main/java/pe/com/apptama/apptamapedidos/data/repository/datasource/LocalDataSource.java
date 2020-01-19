package pe.com.apptama.apptamapedidos.data.repository.datasource;

import android.content.Context;

import java.util.List;

import pe.com.apptama.apptamapedidos.data.entity.ArticuloEntity;
import pe.com.apptama.apptamapedidos.data.entity.DeviceEntity;
import pe.com.apptama.apptamapedidos.data.entity.DownloadDataEntity;
import pe.com.apptama.apptamapedidos.data.local.ArticuloDAO;

public class LocalDataSource implements DataSource {

    private static final String OP_NO_PERM = "Operacion no permitida";
    private final Context context;

    public LocalDataSource(Context context) {
        this.context = context;
    }


    @Override
    public boolean insertLocal(DownloadDataEntity downloadDataEntity) throws Exception {
        boolean isSaveLocal = false;
        List<ArticuloEntity> articuloEntities = downloadDataEntity.getArticuloEntityList();

        if (articuloEntities != null && articuloEntities.size() > 0) {
            (new ArticuloDAO(context)).deleteAll();
            (new ArticuloDAO(context)).saveArticuloList(articuloEntities);
            isSaveLocal = true;
        }

        return isSaveLocal;
    }

    @Override
    public DownloadDataEntity downloadData(DeviceEntity entity) throws Exception {
        throw new Exception(OP_NO_PERM);
    }
}
