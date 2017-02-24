package com.taxiconductor;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class Login extends AppCompatActivity {

    private EditText et_email;
    private EditText et_password;
    private Button btn_login;
    private final int REQUEST_ID_ACCESS_COURSE_FINE_LOCATION = 100;
    private ArrayList<String> user = new ArrayList<String>();
    private ArrayList<String> pass = new ArrayList<String>();

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
                // The Permissions to ask user.
                String[] permissions = new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION,
                        android.Manifest.permission.ACCESS_FINE_LOCATION};
                // Show a dialog asking the user to allow the above permissions.
                ActivityCompat.requestPermissions(Login.this, permissions,
                        REQUEST_ID_ACCESS_COURSE_FINE_LOCATION);
            }

        }
        makeCredentials();
        btn_login = (Button)findViewById(R.id.button_login);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_email = (EditText)findViewById(R.id.editText_email);
                et_password = (EditText)findViewById(R.id.editText_password);

                String usuario=et_email.getText().toString();
                String contrasenia=et_password.getText().toString();
                for(int j=0;j<user.size();j++){
                    System.out.println(user.get(j)+" "+pass.get(j));
                    System.out.println("\n"+usuario + " "+contrasenia);
                    if(usuario.equals(user.get(j))&& contrasenia.equals(pass.get(j))){
                        Intent intent = new Intent(Login.this, Home.class);
                        intent.putExtra("id", j);
                        startActivity(intent);
                        finish();
                        break;
                    }
                }
                Toast.makeText(getApplication(), "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                et_password.setText("");
                et_email.setText("");
            }
        });
    }
    private void makeCredentials(){
        user.add("usuario1");
        user.add("usuario2");
        user.add("usuario3");
        user.add("usuario4");
        user.add("usuario5");
        user.add("usuario6");
        user.add("usuario7");
        user.add("usuario8");
        user.add("usuario9");
        user.add("usuario10");
        user.add("usuario11");
        user.add("usuario12");
        user.add("usuario13");
        user.add("usuario14");
        user.add("usuario15");
        user.add("usuario16");
        user.add("usuario17");
        user.add("usuario18");
        user.add("usuario19");
        user.add("usuario20");
        pass.add("taxa%1");
        pass.add("taxa%2");
        pass.add("taxa%3");
        pass.add("taxa%4");
        pass.add("taxa%5");
        pass.add("taxa%6");
        pass.add("taxa%7");
        pass.add("taxa%8");
        pass.add("taxa%9");
        pass.add("taxa%10");
        pass.add("taxa%11");
        pass.add("taxa%12");
        pass.add("taxa%13");
        pass.add("taxa%14");
        pass.add("taxa%15");
        pass.add("taxa%16");
        pass.add("taxa%17");
        pass.add("taxa%18");
        pass.add("taxa%19");
        pass.add("taxa%20");



    }

}
