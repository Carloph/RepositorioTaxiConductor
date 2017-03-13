package com.taxiconductor;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.taxiconductor.RetrofitPetition.Servicio;
import com.taxiconductor.RetrofitPetition.UpdateDriver;
import com.taxiconductor.RetrofitPetition.getDriverCredential;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Login extends AppCompatActivity {


    private EditText et_usuario;
    private EditText et_password;
    private Button btn_login;
    private final int REQUEST_ID_ACCESS_COURSE_FINE_LOCATION = 100;
    private ArrayList<String> user = new ArrayList<String>();
    private ArrayList<String> pass = new ArrayList<String>();
    private static String username;
    private static String contra;
    private Retrofit retrofit;
    private static GetTodos getTodos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (Build.VERSION.SDK_INT >= 23) {
            int accessCoarsePermission
                    = ContextCompat.checkSelfPermission(Login.this, android.Manifest.permission.ACCESS_COARSE_LOCATION);
            int accessFinePermission
                    = ContextCompat.checkSelfPermission(Login.this, android.Manifest.permission.ACCESS_FINE_LOCATION);
            if (accessCoarsePermission != PackageManager.PERMISSION_GRANTED
                    || accessFinePermission != PackageManager.PERMISSION_GRANTED) {

                String[] permissions = new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION,
                        android.Manifest.permission.ACCESS_FINE_LOCATION};
                ActivityCompat.requestPermissions(Login.this, permissions,
                        REQUEST_ID_ACCESS_COURSE_FINE_LOCATION);
            }
        }
        retrofit = new Retrofit.Builder()
                .baseUrl("http://seec.com.mx/taxaApp/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        getTodos = retrofit.create(GetTodos.class);

        btn_login = (Button)findViewById(R.id.button_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_password = (EditText)findViewById(R.id.editText_password);
                et_usuario = (EditText)findViewById(R.id.editText_usuario);
                username = et_usuario.getText().toString();
                contra=et_password.getText().toString();

                if(et_usuario.getText().equals(null)||et_usuario.getText().toString().equals("")){
                    et_usuario.setError("Ingrese usuario");
                }
                else if(et_password.getText().equals(null) || et_password.getText().toString().equals("")){
                    et_password.setError("Ingrese contraseña");
                }
                else{loadSession(username,contra);}
            }
        });
    }
    private void loadSession(final String usuario, final String password) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Call<getDriverCredential> call = getTodos.getLogin(usuario);
                try {
                    Response<getDriverCredential> response = call.execute();
                    final getDriverCredential result = response.body();
                    System.out.println(result);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            compareSession(result,usuario, password);
                        }
                    });
                } catch (IOException e) {

                }
            }
        }).start();
    }
    private void compareSession(getDriverCredential credential, String usuario, String password) {
        if (credential != null) {
                if(password.equals(credential.getCONTRASENIA())){
                    Intent intent_home = new Intent(Login.this, Home.class);
                    intent_home.putExtra("id", credential.getID_CHOFER());
                    intent_home.putExtra("usuario", usuario);
                    startActivity(intent_home);
                    finish();
                } else{
                    et_password = (EditText)findViewById(R.id.editText_password);
                    et_usuario.setText("");
                    et_password.setText("");
                    Toast.makeText(getApplicationContext(), "Usuario o contraseña incorrecta", Toast.LENGTH_SHORT).show();
                }
        } else {
            Toast.makeText(getApplicationContext(), "Nombre de usuario incorrecto", Toast.LENGTH_SHORT).show();
        }
    }

}
