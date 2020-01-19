package pe.com.apptama.apptamapedidos.data.local;

import android.content.Context;

import org.greenrobot.greendao.query.Query;

import java.util.List;

import pe.com.apptama.apptamapedidos.TamaPedidosApp;
import pe.com.apptama.apptamapedidos.data.entity.ArticuloEntity;
import pe.com.apptama.apptamapedidos.data.entity.ArticuloEntityDao;
import pe.com.apptama.apptamapedidos.data.entity.DaoSession;

public class ArticuloDAO {

    private final ArticuloEntityDao dao;


    public ArticuloDAO(Context context) {
        DaoSession daoSession = ((TamaPedidosApp) context.getApplicationContext()).getmDaoSession();
        this.dao = daoSession.getArticuloEntityDao();
    }

    public void saveArticuloList(List<ArticuloEntity> articuloEntities) {
        dao.insertOrReplaceInTx(articuloEntities);
    }

    public void deleteAll() {
        dao.deleteAll();
    }

    public List<ArticuloEntity> getArticuloAll() {
        return dao.loadAll();
    }

    public ArticuloEntity getArticuloById(String articuloId) {

        Query<ArticuloEntity> query = dao.queryBuilder().where(
                ArticuloEntityDao.Properties.ArticuloCodigo.eq(articuloId)
        ).build();

        return query.unique();
    }
}
