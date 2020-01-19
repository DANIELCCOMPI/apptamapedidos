package pe.com.apptama.apptamapedidos.presenter.view.activity;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;


import pe.com.apptama.apptamapedidos.R;

public class UbicacionPedidoActivity extends FragmentActivity implements
        OnMapReadyCallback,
        View.OnClickListener,
        LocationListener {

    private static final String TAG = "mapsActivity";
    private static final int LOCATION_REQUEST_CODE = 1108;
    private static final float IND_ZOOM = 16;
    private static final int RP_COURSE_LOCATION = 21;
    private static final int RP_FINE_LOCATION = 22;
    private GoogleMap mMap;
    private CameraPosition mCameraPosition;
    private FloatingActionButton fab_myubicacion;
    private Button btn_listo;
    private LocationManager locationManager;
    private Location myLocation;
    private boolean bIsIniMylocation = false;
    private List<Address> addresses;
    private Geocoder geocoder;


    private static final int REQUEST_CHECK_SETTINGS = 100;
    private static final int ACCESS_FINE_LOCATION_INTENT = 3;
    private static final String BROADCAST_ACTION = "android.location.PROVIDERS_CHANGED";
    private GoogleApiClient googleApiClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubicacion_pedido);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        fab_myubicacion = findViewById(R.id.fab_myubicacion);
        btn_listo = findViewById(R.id.btn_listo);

        fab_myubicacion.setOnClickListener(this);
        btn_listo.setOnClickListener(this);

    }


    private boolean checkPermissionToApp(String strPermission, int requestPermission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, strPermission) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{strPermission}, requestPermission);
                return false;
            }
        }

        /*
        switch (requestPermission){
            case RP_FINE_LOCATION:
                mMap.setMyLocationEnabled(true);
                locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 10, this);
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10, this);

                break;
        }
        */

        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            switch (requestCode) {
                case RP_FINE_LOCATION:
                    initAndroidGps();
                    break;

            }
        }
    }

    private boolean checkPermiso() {
        boolean coarse_location = ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        boolean fine_location = ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        return coarse_location && fine_location;
    }


    /*
    private void solicPermiso() {
        List<String> permisos = new LinkedList<>();
        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permisos.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }

        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permisos.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }

        int iSize = permisos.size();
        ActivityCompat.requestPermissions(
                this, permisos.toArray(new String[iSize]), PERMISOS_RESULT);

    }
    */

    /*
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISOS_RESULT) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initGoogleAPIClient();
                //requestLocationPermission();
                //openDialog();
                initAndroidGps();
            }
        }
    }
    */

    private void initAndroidGps() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 10, this);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10, this);

    }

    private void initGoogleAPIClient() {
        // Sin el diálogo de ubicación automática del cliente API de Google no funcionará
        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();
    }

    /*
    private void checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestLocationPermission();
            } else {
                openDialog();
            }
        } else {
            openDialog();
        }
    }
    */

    /*
    private void requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    ACCESS_FINE_LOCATION_INTENT);

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    ACCESS_FINE_LOCATION_INTENT);
        }
    }
    */
    // Broadcast  para verificar el estado del GPS
    private BroadcastReceiver gpsLocationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                if (Objects.requireNonNull(intent.getAction()).matches(BROADCAST_ACTION)) {
                    LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
                    // Compruebe si el GPS está activado o desactivado
                    if (locationManager != null) {
                        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                            Toast.makeText(context, "GPS está habilitado en su dispositivo", Toast.LENGTH_SHORT).show();
                        } else {
                            // Si el GPS está apagado, muestre el diálogo de ubicación
                            new Handler().postDelayed(sendUpdatesToUI, 10);
                        }
                    }
                }
            }
        }
    };


    private Runnable sendUpdatesToUI = new Runnable() {
        public void run() {
            openDialog();
        }
    };


    private void openDialog() {
        LocationRequest mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(30 * 1000);
        mLocationRequest.setFastestInterval(5 * 1000); //5 sec Time interval for location update
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY); //Setting priority of Location request to high

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest);

        Task<LocationSettingsResponse> task = LocationServices.getSettingsClient(this).checkLocationSettings(builder.build());

        task.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(Task<LocationSettingsResponse> task) {
                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                    // All location settings are satisfied. The client can initialize location
                    // requests here.
                } catch (ApiException exception) {
                    switch (exception.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            // Location settings are not satisfied. But could be fixed by showing the
                            // user a dialog.
                            try {
                                // Cast to a resolvable exception.
                                ResolvableApiException resolvable = (ResolvableApiException) exception;
                                // Show the dialog by calling startResolutionForResult(),
                                // and check the result in onActivityResult().
                                resolvable.startResolutionForResult(UbicacionPedidoActivity.this, REQUEST_CHECK_SETTINGS);
                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                            } catch (ClassCastException e) {
                                // Ignore, should be an impossible error.
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            // Location settings are not satisfied. However, we have no way to fix the
                            // settings so we won't show the dialog.
                            break;
                    }
                }
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        // Todos los cambios requeridos se realizaron con éxito.
                        Toast.makeText(getApplicationContext(), "GPS habilitado", Toast.LENGTH_LONG).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        // Se le pidió al usuario que cambiara la configuración, pero decidió no hacerlo.
                        Toast.makeText(getApplicationContext(), "GPS no esta habilitado", Toast.LENGTH_LONG).show();
                        //openDialog();
                        break;
                    default:
                        break;
                }
                break;
        }
    }


    /*

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case ACCESS_FINE_LOCATION_INTENT: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Mostrar el cuadro de diálogo de ubicación
                    if (googleApiClient == null) {
                        initGoogleAPIClient();
                        openDialog();
                    } else {
                        openDialog();
                    }
                } else {
                    Toast.makeText(this, "Permiso de ubicación denegado", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case LOCATION_REQUEST_CODE: {
                // ¿Permisos asignados?
                if (permissions.length > 0 &&
                        permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION) &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    habilitaLocalizacion();
                } else {
                    Toast.makeText(this, "Error de permisos", Toast.LENGTH_LONG).show();
                }
                break;
            }
        }
    }

    */


    @Override
    protected void onResume() {
        super.onResume();
        // Registra el BroadcastReceiver para verificar el estado del GPS
        registerReceiver(gpsLocationReceiver, new IntentFilter(BROADCAST_ACTION));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Anular el BroadcastReceiver al destruir
        if (gpsLocationReceiver != null)
            unregisterReceiver(gpsLocationReceiver);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (checkPermissionToApp(Manifest.permission.ACCESS_FINE_LOCATION, RP_FINE_LOCATION)) {
            initAndroidGps();
        }

        /*
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);

            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 10, this);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10, this);

        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Mostrar diálogo explicativo
            } else {
                // Solicitar permiso
                ActivityCompat.requestPermissions(
                        this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        LOCATION_REQUEST_CODE);

            }
        }
        */

        // Controles UI
        mMap.getUiSettings().setCompassEnabled(false);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);

        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.json_mapstyle_standard));

            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }


        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-12.0006545, -77.073914);
        mCameraPosition = new CameraPosition.Builder()
                .target(sydney)
                .zoom(IND_ZOOM)
                .tilt(45)
                .build();

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(mCameraPosition));

        mMap.setOnCameraMoveStartedListener(new GoogleMap.OnCameraMoveStartedListener() {
            @Override
            public void onCameraMoveStarted(int i) {
                //Toast.makeText(UbicacionPedidoActivity.this, "onCameraMove", Toast.LENGTH_LONG).show();
                fab_myubicacion.show();
            }
        });

        geocoder = new Geocoder(this, Locale.getDefault());


    }


    /*
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == LOCATION_REQUEST_CODE) {
            // ¿Permisos asignados?
            if (permissions.length > 0 &&
                    permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION) &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                habilitaLocalizacion();
            } else {
                Toast.makeText(this, "Error de permisos", Toast.LENGTH_LONG).show();
            }

        }
    }
    */

    private void habilitaLocalizacion() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            return;
        }

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.fab_myubicacion:

                moverCameraMyPosition(myLocation);
                fab_myubicacion.hide();
                break;
            case R.id.btn_listo:
                if (mMap != null) {
                    double myLat = mMap.getCameraPosition().target.latitude;
                    double myLng = mMap.getCameraPosition().target.longitude;
                    LatLng markerPoint = new LatLng(myLat, myLng);
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_posicion_seleccionada));
                    markerOptions.position(markerPoint);
                    mMap.addMarker(markerOptions).setTitle("new marker");

                    if (geocoder.isPresent()) {
                        try {
                            addresses = geocoder.getFromLocation(myLat, myLng, 1);

                            if (addresses.size() > 0) {
                                Toast.makeText(this, "direccion: " + addresses.get(0).getAddressLine(0), Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(this, "No se encontró la dirección.Intente nuevamente.", Toast.LENGTH_LONG).show();
                            }

                        } catch (IOException e) {
                            Toast.makeText(this, "No se encontró la dirección.Intente nuevamente.", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(this, "No se encontró la dirección.Intente nuevamente.", Toast.LENGTH_LONG).show();
                    }

                }

                break;
        }

    }

    @Override
    public void onLocationChanged(Location location) {
        //Toast.makeText(this, "changed mediante provider:" + location.getProvider(), Toast.LENGTH_LONG).show();
        myLocation = location;
        if (!bIsIniMylocation) {
            bIsIniMylocation = true;
            moverCameraMyPosition(myLocation);
        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private void moverCameraMyPosition(Location myLocation) {
        if (myLocation != null) {
            mCameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(myLocation.getLatitude(), myLocation.getLongitude()))
                    .zoom(IND_ZOOM)
                    //.tilt(45)
                    .build();
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(mCameraPosition));
        }

    }

}
