package pe.com.apptama.apptamapedidos.data.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.greenrobot.greendao.database.Database;

import pe.com.apptama.apptamapedidos.data.entity.DaoMaster;

public class DbOpenHelper extends DaoMaster.OpenHelper {

    private Context context;

    public DbOpenHelper(Context context, String name) {
        super(context, name);
        this.context = context;

    }

    @Override
    public void onCreate(Database db) {
        super.onCreate(db);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        super.onUpgrade(db, oldVersion, newVersion);
        Log.d("DEBUG", "DB_OLD_VERSION : " + oldVersion + ", DB_NEW_VERSION : " + newVersion);
    }
}
