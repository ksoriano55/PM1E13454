package com.example.pm1e13454;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import Configuracion.SQLiteConexion;
import Configuracion.Transacciones;

public class MainActivity extends AppCompatActivity {

    EditText nombre, telefono, nota, imagen;
    Spinner cbxPais;
    Button btnListaContacto, btnGuardarContacto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nombre = (EditText) findViewById(R.id.txtNombre);
        telefono = (EditText) findViewById(R.id.txtTelefono);
        cbxPais = (Spinner) findViewById(R.id.spinnerPais);
        nota = (EditText) findViewById(R.id.txtNota);

        btnListaContacto = (Button) findViewById(R.id.btnListaContacto);
        btnGuardarContacto = (Button) findViewById(R.id.btnGuardarContacto);

        btnListaContacto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ActivityList.class);
                startActivity(intent);
            }
        });
        btnGuardarContacto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AgregarContacto();
            }
        });
    }

    private void AgregarContacto()
    {
        SQLiteConexion conexion = new SQLiteConexion(this, Transacciones.DBName, null, 1);
        SQLiteDatabase db = conexion.getWritableDatabase();

        ContentValues valores = new ContentValues();
        valores.put(Transacciones.nombre, nombre.getText().toString());
        valores.put(Transacciones.telefono, telefono.getText().toString());
        valores.put(Transacciones.pais, "504");
        valores.put(Transacciones.nota, nota.getText().toString());
        valores.put(Transacciones.imagen, "");

        Long resultado = db.insert(Transacciones.TableContactos, Transacciones.id, valores);

        Toast.makeText(getApplicationContext(), "Registro Ingresado con exito " + resultado.toString(),
                Toast.LENGTH_LONG).show();

        db.close();

    }
}