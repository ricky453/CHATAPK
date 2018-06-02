package com.chatapp.cdliii.mychatapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText txtUsuario;
    private EditText txtPassword;
    private Button btnIngresar;
    private Button btnRegistrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setBackgroundDrawableResource(R.drawable.background);

        txtUsuario = (EditText) findViewById(R.id.username);
        txtPassword = (EditText) findViewById(R.id.password);

        btnIngresar = (Button) findViewById(R.id.btnIniciar);
        btnRegistrar = (Button) findViewById(R.id.btnRegistrarse);

        btnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 VerificarLogin(txtUsuario.getText().toString(), txtPassword.getText().toString());
            }
        });

    }

    public void VerificarLogin(String username, String password){
        Toast.makeText(this,"El usuario es "+username+" y la contrasena "+password, Toast.LENGTH_SHORT).show();
    }
}
