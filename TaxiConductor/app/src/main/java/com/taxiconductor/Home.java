package com.taxiconductor;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.taxiconductor.RetrofitPetition.APIClient;
import com.taxiconductor.RetrofitPetition.APIService;
import com.taxiconductor.RetrofitPetition.MSG;
import com.taxiconductor.RetrofitPetition.Servicio;
import com.taxiconductor.RetrofitPetition.getDriverCredential;

import java.io.IOException;
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
        implements NavigationView.OnNavigationItemSelectedListener,LocationListener, OnMapReadyCallback {

    private GoogleMap mMap;
    public SupportMapFragment mapFragment;
    LocationManager locationManager;
    private TextView tv_usuario;
    static double latitude;
    static double longitude;
    static String direction;
    private Button btn_status;
    static String contador="";
    private static Retrofit retrofit;
    private static GetTodos getTodos;
    private boolean boton=false;
    static int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        id = (int)getIntent().getExtras().getSerializable("id");
        String usuario = (String)getIntent().getExtras().getSerializable("usuario");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        retrofit = new Retrofit.Builder()
                .baseUrl("http://taxa.pe.hu/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        getTodos = retrofit.create(GetTodos.class);
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

        //id = (int)getIntent().getExtras().getSerializable("id");

        btn_status = (Button) findViewById(R.id.button_status);

        btn_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(contador.equals("")){
                    update_status(id,1);
                    btn_status.setBackgroundColor(Color.GREEN);
                    contador= "2";
                }
                else if(contador.equals("2")){
                    btn_status.setEnabled(boton);
                }
                else if(contador.equals("3")){
                    update_status(id,3);
                    btn_status.setBackgroundColor(ContextCompat.getColor(getApplication(), R.color.colorOrange));
                    contador = "4";
                }
                else if(contador.equals("4")){
                    update_status(id,4);
                    btn_status.setBackgroundColor(Color.RED);
                    contador = "";
                }
            }
        });

        final android.os.Handler handler = new android.os.Handler();
        Timer timer = new Timer();

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {

                        getLoc();
                        escuchaPeticion(id);

                    }
                });
            }
        };
        timer.schedule(task, 0, 5000);


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
            update_data(id,latitude,longitude);
        }
        locationManager.requestLocationUpdates(bestProvider, 20000, 0, this);
        update_data(id,latitude,longitude);

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
                    Response<Servicio> response = call.execute();
                    final Servicio result = response.body();
                    System.out.println(result);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
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
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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
            return true;
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
        mapFragment.getMapAsync(this);
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
                //onSignupSuccess();
                Log.d("onResponse", "" + response.body().getMessage());


                if(response.body().getSuccess() == 1) {
                    Log.d("onResponse", "" + "Se ha actualizado las coordenadas");

                    // startActivity(new Intent(SignupActivity.this, MainActivity.class));
                }else {

                    Log.d("onResponse", "" + "Hubo un error al actualizar las coordenadas");
                    Toast.makeText(Home.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MSG> call, Throwable t) {
                Log.d("onFailure", t.toString());
            }
        });
    }

}
