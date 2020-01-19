package pe.com.apptama.apptamapedidos;

import android.app.Application;

import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.EncryptedDatabase;

import pe.com.apptama.apptamapedidos.data.entity.DaoMaster;
import pe.com.apptama.apptamapedidos.data.entity.DaoSession;
import pe.com.apptama.apptamapedidos.data.local.DbOpenHelper;

public class TamaPedidosApp extends Application {

    private final static  String DB_NAME = "pedidosvalois.db";
    private DaoSession mDaoSession;

    @Override
    public void onCreate() {
        super.onCreate();

        DbOpenHelper helper = new DbOpenHelper(this, DB_NAME);
        Database database =  helper.getWritableDb();

        mDaoSession = new DaoMaster(database).newSession();

    }

    public DaoSession getmDaoSession() {
        return mDaoSession;
    }

}
