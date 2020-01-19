package pe.com.apptama.apptamapedidos.presenter.view.receiver;

import android.app.AlarmManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {
    private static final String TAG = "AlarmReceiver";
    private static final int ALARM_ID = 2031;

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.d(TAG, "Se recibio accion");
        if (action != null) {

            switch (action) {
                case Intent.ACTION_BOOT_COMPLETED:
                    iniciarSincro(context);
                    return;
            }
        }
        reiniciarSincro(context);
    }

    private void reiniciarSincro(Context context) {
    }

    private void iniciarSincro(Context context) {
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Long time = null;



    }
}
