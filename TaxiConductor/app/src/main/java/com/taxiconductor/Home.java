package com.taxiconductor;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.taxiconductor.DirectionMaps.DirectionFinder;
import com.taxiconductor.DirectionMaps.DirectionFinderListener;
import com.taxiconductor.DirectionMaps.Route;
import com.taxiconductor.RetrofitPetition.APIClient;
import com.taxiconductor.RetrofitPetition.APIService;
import com.taxiconductor.RetrofitPetition.MSG;
import com.taxiconductor.RetrofitPetition.Servicio;
import com.taxiconductor.RetrofitPetition.UpdateDriver;

import android.os.Handler;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,LocationListener, OnMapReadyCallback,DirectionFinderListener {

    public static GoogleMap mMap;
    public SupportMapFragment mapFragment;
    LocationManager locationManager;
    private TextView tv_usuario;
    private TextView tv_msj;
    static double latitude;
    static double longitude;
    static String direction;
    private Button btn_status;
    static int contador;
    private static Retrofit retrofit;
    private static GetTodos getTodos;
    static int id_var;

    TimerTask mTimerTask;
    final Handler handler = new Handler();
    Timer t = new Timer();
    public int nCounter = 0;

    TimerTask mTimerTask2;
    final Handler handler2 = new Handler();
    Timer t2 = new Timer();
    public int nCounter2 = 0;

    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMarkers = new ArrayList<>();
    private List<Polyline> polylinePaths = new ArrayList<>();
    private ProgressDialog progressDialog;

    public static String coordinates_origin="";
    public static String coordinates_destination="";

    static Servicio global;


    public TextView tv_distance, tv_duration;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        retrofit = new Retrofit.Builder()
                .baseUrl("http://seec.com.mx/taxaApp/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        getTodos = retrofit.create(GetTodos.class);
        id_var = (int)getIntent().getExtras().getSerializable("id");
        ExistSession(id_var);
        setContentView(R.layout.activity_home);
        contador=0;
        String usuario = (String)getIntent().getExtras().getSerializable("usuario");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tv_usuario = (TextView)findViewById(R.id.tv_usuario_chofer);
        tv_usuario.setText("Usted está logueado como: "+usuario);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        btn_status = (Button) findViewById(R.id.button_status);
        tv_distance = (TextView) findViewById(R.id.tvDistance);
        tv_duration = (TextView) findViewById(R.id.tvDuration);
        tv_msj = (TextView)findViewById(R.id.tv_msj);



        btn_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(contador==0){
                    contador++;
                    update_status(id_var,1);
                    btn_status.setBackgroundColor(Color.GREEN);
                    Toast.makeText(getApplication(),"Ahora está disponible, espere una solicitud de viaje",Toast.LENGTH_LONG).show();
                    mMap.clear();
                    tv_distance.setText("0 km");
                    tv_duration.setText("0 min");

                }
                else if(contador==1){
                    btn_status.setEnabled(false);
                    Toast.makeText(getApplication(),"No puede cambiar de estado hasta que tenga una solicitud de viaje",Toast.LENGTH_LONG).show();
                }
                else if(contador==2){
                    update_status(id_var,3);
                    contador++;
                    btn_status.setBackgroundColor(ContextCompat.getColor(getApplication(), R.color.colorOrange));
                    Toast.makeText(getApplication(),"Esperando a que el pasajero aborde la unidad",Toast.LENGTH_LONG).show();
                }
                else if(contador==3){
                    update_status(id_var,4);
                    contador=0;
                    String orig= global.getLATITUD_CLIENTE()+","+global.getLONGITUD_CLIENTE();
                    String dest =  global.getLATITUD_DESTINO()+","+global.getLONGITUD_DESTINO();
                    coordinates_origin = orig;
                    coordinates_destination = dest;
                    sendRequest(orig,dest);
                    btn_status.setBackgroundColor(Color.RED);
                    mapFragment.getMapAsync(Home.this);
                    tv_msj.setText("");
                    doTimerTask2();
                    Toast.makeText(getApplication(),"El pasajero ha abordado el taxi, estás dirigiéndote a su destino",Toast.LENGTH_LONG).show();
                }
            }
        });

        doTimerTask();
        doTimerTask2();
        insertLocation(id_var, latitude, longitude,0);
    }

    private void ExistSession(final int id) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Call<UpdateDriver> call = getTodos.verificar(id);
                try {
                    Response<UpdateDriver> response = call.execute();
                    final UpdateDriver result = response.body();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(result!=null){
                                Toast.makeText(getApplication(),"Hay una sesión abierta con esta cuenta, favor de iniciar sesión con una cuenta distinta",Toast.LENGTH_LONG).show();
                                deleteLocation(id);
                                Intent intent_login = new Intent(Home.this, Login.class);
                                startActivity(intent_login);
                                finish();
                            }
                        }
                    });
                } catch (IOException e) {}
            }
        }).start();
    }

    public void insertLocation(int id, double latitud, double longitud, int estatus){
        APIService service = APIClient.getClient().create(APIService.class);

        Call<MSG> userCall = service.insertLocation(id, latitud, longitud, estatus);

        userCall.enqueue(new Callback<MSG>() {
            @Override
            public void onResponse(Call<MSG> call, Response<MSG> response) {
            }
            @Override
            public void onFailure(Call<MSG> call, Throwable t) {
            }
        });
    }
    public void deleteLocation(int id){
        APIService service = APIClient.getClient().create(APIService.class);

        Call<MSG> userCall = service.deleteLocation(id);

        userCall.enqueue(new Callback<MSG>() {
            @Override
            public void onResponse(Call<MSG> call, Response<MSG> response) {
            }
            @Override
            public void onFailure(Call<MSG> call, Throwable t) {
            }
        });
    }
    public void getLoc(){
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String bestProvider = locationManager.getBestProvider(criteria, true);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) return;

        Location location = locationManager.getLastKnownLocation(bestProvider);
        if (location != null) {
            onLocationChanged(location);
            latitude = location.getLatitude();
            longitude =  location.getLongitude();
            update_data(id_var,latitude,longitude);
        }
        locationManager.requestLocationUpdates(bestProvider, 20000, 0, this);

    }

    public void setLocation(Location loc) {
        if (loc.getLatitude() != 0.0 && loc.getLongitude() != 0.0) {
            try {
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<android.location.Address> list = geocoder.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);
                if (!list.isEmpty()) {
                    android.location.Address DirCalle = list.get(0);
                    direction= DirCalle.getAddressLine(0);
                }

            } catch (IOException e) {
                System.out.println("No hay datos");
                e.printStackTrace();
            }
        }
    }

    private void escuchaPeticion(final int id_chofer) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Call<Servicio> call = getTodos.escucha(id_chofer);
                try {
                    final Response<Servicio> response = call.execute();
                    final Servicio result = response.body();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (result!=null){
                                stopTask2();
                                AlertDialog.Builder dialog = new AlertDialog.Builder(Home.this);
                                dialog.setCancelable(false);
                                dialog.setTitle("Solicitar viaje");
                                dialog.setMessage("¿Estás seguro de asignar este viaje al taxista?" );
                                Vibrator v = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                                MediaPlayer mp = MediaPlayer.create(getApplicationContext(),R.raw.samsung_galaxy_on_time);
                                mp.start();
                                v.vibrate(2000);
                                dialog.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int id) {
                                        global  = result;
                                        String orig = String.valueOf(latitude)+","+String.valueOf(longitude);
                                        coordinates_origin =orig;
                                        String dest =  result.getLATITUD_CLIENTE()+","+result.getLONGITUD_CLIENTE();
                                        coordinates_destination = dest;
                                        btn_status.setEnabled(true);
                                        btn_status.setBackgroundColor(Color.YELLOW);
                                        contador++;
                                        Toast.makeText(getApplication(),"Conductor en camino...",Toast.LENGTH_SHORT).show();
                                        tv_msj = (TextView)findViewById(R.id.tv_msj);
                                        tv_msj.setText(result.getMENSAJE().toString());
                                        update_status(id_var,2);
                                        delete_solicitud(id_var);
                                        sendRequest(orig,dest);
                                    }
                                }).setNegativeButton("Cancelar ", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //Action for "Cancel".
                                        contador=0;
                                        delete_solicitud(id_var);
                                        doTimerTask2();
                                        Toast.makeText(getApplication(),"Ha cancelado el viaje solicitado",Toast.LENGTH_SHORT).show();

                                    }
                                });

                                final AlertDialog alert = dialog.create();
                                if(!isFinishing()){
                                    alert.show();
                                }
                            }
                            rectificar(result);
                        }
                    });
                } catch (IOException e) {}
            }
        }).start();
    }

    private void rectificar(Servicio solicitud) {
        if (solicitud != null) {
            System.out.println(solicitud.getLATITUD_CLIENTE());
            System.out.println(solicitud.getLATITUD_DESTINO());
            System.out.println(solicitud.getLONGITUD_CLIENTE());
            System.out.println(solicitud.getLONGITUD_DESTINO());

        } else {Toast.makeText(getApplicationContext(), "Nombre de usuario incorrecto", Toast.LENGTH_SHORT).show();}
    }
    @Override
    public void onBackPressed() {
        deleteLocation(id_var);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            deleteLocation(id_var);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        setLocation(location);
       // mapFragment.getMapAsync(this);
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

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.clear();
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                 && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) return;
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
          Marker melbourne = mMap.addMarker(new MarkerOptions()
               .position(new LatLng(latitude,longitude))
               .title(direction));
            melbourne.showInfoWindow();
    }

    public void doTimerTask(){

        mTimerTask = new TimerTask() {
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        nCounter++;
                        // update TextView
                        getLoc();

                    }
                });
            }};

        // public void schedule (TimerTask task, long delay, long period)
        t.schedule(mTimerTask, 0, 10000);  //

    }
    public void doTimerTask2(){

        mTimerTask2 = new TimerTask() {
            public void run() {
                handler2.post(new Runnable() {
                    public void run() {
                        nCounter2++;
                        // update TextView

                        escuchaPeticion(id_var);

                    }
                });
            }};

        // public void schedule (TimerTask task, long delay, long period)
        t2.schedule(mTimerTask2, 0, 10000);  //

    }


    public void stopTask(){
        if(mTimerTask!=null){

            Log.e("Se ha cancelado", "la ubicación de los taxis");

            mTimerTask.cancel();
        }
    }

    public void stopTask2(){
        if(mTimerTask2!=null){

            Log.e("Se ha cancelado", "la ubicación de los taxis");

            mTimerTask2.cancel();
        }
    }

    private void update_data(int id, double latitud, double longitud) {


        APIService service = APIClient.getClient().create(APIService.class);
        //User user = new User(name, email, password);


        Call<MSG> userCall = service.updateCoordenadas(id, latitud, longitud);

        userCall.enqueue(new Callback<MSG>() {
            @Override
            public void onResponse(Call<MSG> call, Response<MSG> response) {
            }
            @Override
            public void onFailure(Call<MSG> call, Throwable t) {
            }
        });
    }

    private void update_status(int id, int status) {


        APIService service = APIClient.getClient().create(APIService.class);
        //User user = new User(name, email, password);


        Call<MSG> userCall = service.updateStatus(id, status);

        userCall.enqueue(new Callback<MSG>() {
            @Override
            public void onResponse(Call<MSG> call, Response<MSG> response) {
            }

            @Override
            public void onFailure(Call<MSG> call, Throwable t) {
                Log.d("onFailure", t.toString());
            }
        });
    }

    private void delete_solicitud(int id) {


        APIService service = APIClient.getClient().create(APIService.class);
        //User user = new User(name, email, password);


        Call<MSG> userCall = service.deleteSolicitud(id);

        userCall.enqueue(new Callback<MSG>() {
            @Override
            public void onResponse(Call<MSG> call, Response<MSG> response) {

            }

            @Override
            public void onFailure(Call<MSG> call, Throwable t) {
                Log.d("onFailure", t.toString());
            }
        });
    }


    private void sendRequest(String orign, String destin) {


        if (orign.isEmpty()) {
            Toast.makeText(this, "Por favor ingrese la dirección de origen!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (destin.isEmpty()) {
            Toast.makeText(this, "Por favor ingrese la dirección de destino!", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            new DirectionFinder(this, orign, destin).execute();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onDirectionFinderStart() {

        progressDialog = ProgressDialog.show(this, "Por favor espere.",
                "Localizando dirección..!", true);

        if (originMarkers != null) {
            for (Marker marker : originMarkers) {
                marker.remove();
            }
        }

        if (destinationMarkers != null) {
            for (Marker marker : destinationMarkers) {
                marker.remove();
            }
        }

        if (polylinePaths != null) {
            for (Polyline polyline : polylinePaths) {
                polyline.remove();
            }
        }


    }

    @Override
    public void onDirectionFinderSuccess(List<Route> routes) {

        try {
            progressDialog.dismiss();
            polylinePaths = new ArrayList<>();
            originMarkers = new ArrayList<>();
            destinationMarkers = new ArrayList<>();

            for (Route route : routes) {
                // mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(route.startLocation, 13));
                ((TextView) findViewById(R.id.tvDuration)).setText(route.duration.text);
                ((TextView) findViewById(R.id.tvDistance)).setText(route.distance.text);

                originMarkers.add(mMap.addMarker(new MarkerOptions()
                        //             .title(nombre)
                        .position(route.startLocation)));
                destinationMarkers.add(mMap.addMarker(new MarkerOptions()
                        .title(route.endAddress)
                        .position(route.endLocation)));

                PolylineOptions polylineOptions = new PolylineOptions().
                        geodesic(true).
                        color(Color.BLUE).
                        width(10);

                for (int i = 0; i < route.points.size(); i++)
                    polylineOptions.add(route.points.get(i));

                polylinePaths.add(mMap.addPolyline(polylineOptions));
            }

            Double origin_lat,origin_lng;
            Double destination_lat,destination_lng;

            String cadena[] = coordinates_origin.split(",");
            origin_lat = Double.parseDouble(cadena[0]);
            origin_lng = Double.parseDouble(cadena[1]);

            String cadena2[] = coordinates_destination.split(",");
            destination_lat = Double.parseDouble(cadena2[0]);
            destination_lng = Double.parseDouble(cadena2[1]);

            LatLng var_origen = new LatLng(origin_lat,origin_lng);
            LatLng var_destino = new LatLng(destination_lat,destination_lng);

            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            builder.include(var_origen);
            builder.include(var_destino);
            LatLngBounds bounds = builder.build();

            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 50);
            mMap.animateCamera(cu, new GoogleMap.CancelableCallback() {
                public void onCancel() {
                }

                public void onFinish() {
                    CameraUpdate zout = CameraUpdateFactory.zoomBy(-1.0f);
                    mMap.animateCamera(zout);
                }
            });

        } catch (Exception e) {
            Toast.makeText(getApplication(), "Hubo un error al obtener la ubicación " + e, Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void startActivityForResult(int requestCode, int resultCode, Intent data) {

    }

}
