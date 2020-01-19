package pe.com.apptama.apptamapedidos.presenter.view.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import pe.com.apptama.apptamapedidos.data.entity.DeviceEntity;
import pe.com.apptama.apptamapedidos.data.entity.DownloadDataEntity;
import pe.com.apptama.apptamapedidos.data.repository.DataSourceRepository;
import pe.com.apptama.apptamapedidos.data.repository.datasource.DataSourceFactory;
import pe.com.apptama.apptamapedidos.data.util.NetUtils;
import pe.com.apptama.apptamapedidos.domain.repository.DataRepository;
import pe.com.apptama.apptamapedidos.presenter.view.activity.ListaArticulosActivity;

public class SyncReceiver extends BroadcastReceiver {


    private static final String TAG = "SyncReceiver";

    @Override
    public void onReceive(final Context context, Intent intent) {

        Log.d(TAG, "Se llamo a la sincronizacion");
        DataSourceFactory factory = new DataSourceFactory(context);
        final DataRepository repository = new DataSourceRepository(factory);
        if (NetUtils.hayInternet(context)) {

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    runSync(context, repository);
                }
            });
            thread.start();
        } else {
            notifSubida(context, false);
        }

    }

    private void notifSubida(Context context, boolean exito) {

    }

    private void runSync(Context context, DataRepository repository) {

        try {
            DownloadDataEntity downloadDataEntity = repository.downloadData(new DeviceEntity(3453454L));
            if (!downloadDataEntity.isEmpty()) {
                // guarda en local y devuelve true si se guardo correctamente
                boolean isSaveLocal = repository.insertLocal(downloadDataEntity);

                notifActualizacion(context);


            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void notifActualizacion(Context context) {
        Intent intent = new Intent(ListaArticulosActivity.REFRESH);
        context.sendBroadcast(intent);
    }


}
