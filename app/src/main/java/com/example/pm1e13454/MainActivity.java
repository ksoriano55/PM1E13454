package com.example.pm1e13454;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
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
    Button btnListaContacto, btnGuardarContacto, btnAgregarPais;
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

        btnAgregarPais = (Button) findViewById(R.id.btnAgregarPais);
        btnAgregarPais.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builderPais = new AlertDialog.Builder(MainActivity.this);
                LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
                View viewPais = inflater.inflate(R.layout.dialogo_paises, null);

                builderPais.setView(viewPais);
                AlertDialog dialogPais = builderPais.create();
                dialogPais.show();

                EditText txtCodPais = viewPais.findViewById(R.id.txtCodPais);
                EditText txtPais = viewPais.findViewById(R.id.txtPais);

                viewPais.findViewById(R.id.btnGuardarPais).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String txtCodPaisVal = txtCodPais.getText().toString();
                        String txtPaisVal = txtPais.getText().toString();

                        if(txtCodPaisVal.isEmpty() || txtPaisVal.isEmpty()){
                            Toast.makeText(getApplicationContext(), "*Campos Obligatorios", Toast.LENGTH_LONG).show();
                        }else{
                            ///Agregar netodo de guardar pais:


                            Toast.makeText(getApplicationContext(), "Pais guardado con exito!", Toast.LENGTH_LONG).show();
                            dialogPais.dismiss();
                        }
                    }
                });

                viewPais.findViewById(R.id.btnCancelarPais).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogPais.dismiss();
                    }
                });
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