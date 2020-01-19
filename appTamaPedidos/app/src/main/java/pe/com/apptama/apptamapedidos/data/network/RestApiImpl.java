package pe.com.apptama.apptamapedidos.data.network;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import pe.com.apptama.apptamapedidos.R;
import pe.com.apptama.apptamapedidos.data.entity.DeviceEntity;
import pe.com.apptama.apptamapedidos.data.entity.DownloadDataEntity;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestApiImpl implements RestApi {


    private final ApiService apiService;
    private final Context context;

    public RestApiImpl(Context context) {
        String url = context.getString(R.string.server_url);

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.excludeFieldsWithoutExposeAnnotation();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gsonBuilder.create()))
                .build();

        this.apiService = retrofit.create(ApiService.class);
        this.context = context;
    }


    @Override
    public DownloadDataEntity downloadData(String  deviceId) throws Exception {
        while (true) {
            Call<DownloadDataEntity> call = apiService.downloadData(deviceId);
            try {
                Response<DownloadDataEntity> response = call.execute();
                if (response.isSuccessful()) {
                    return response.body();
                } else {
                    throw new Exception("Response code: " + response.code());
                }
            } catch (Exception e) {
                manejarError(e);
            }

        }
    }

    private void manejarError(Exception e) throws Exception {
        if (!TextUtils.equals("unexpected end of stream", e.getMessage())
                && !e.toString().contains("java.io.EOFException")) {
            if (e.getMessage().contains("failed to connect"))
                throw new Exception("failed");
            throw e;
        } else Log.e("ERROR", "Error en respuesta, reintentando");

    }

}
