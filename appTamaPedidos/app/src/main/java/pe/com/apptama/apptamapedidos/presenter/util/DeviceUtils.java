package pe.com.apptama.apptamapedidos.presenter.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.provider.Settings.Secure;

public class DeviceUtils {

    @SuppressLint("HardwareIds")
    public static String getAndroidId(Context context) {

        return Secure.getString(context.getContentResolver(),
                Secure.ANDROID_ID);
    }

}
