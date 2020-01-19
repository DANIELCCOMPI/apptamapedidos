package pe.com.apptama.apptamapedidos.data.repository.datasource;

import android.content.Context;

import pe.com.apptama.apptamapedidos.data.network.RestApi;
import pe.com.apptama.apptamapedidos.data.network.RestApiImpl;

public class DataSourceFactory {
    private final Context context;

    public DataSourceFactory(Context context) {
        this.context = context;
    }


    public DataSource crear(boolean forzarRed) {
        return forzarRed ? createNetworkDatasource() : createLocalDataSource();
    }

    private DataSource createLocalDataSource() {
        return new LocalDataSource(context);
    }

    private DataSource createNetworkDatasource() {
        RestApi restApi = new RestApiImpl(context);
        return new NetworkDataSource(restApi);
    }

    public Context getContext() {
        return context;
    }
}
