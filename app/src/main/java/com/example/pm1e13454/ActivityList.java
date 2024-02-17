package com.example.pm1e13454;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import Configuracion.SQLiteConexion;
import Configuracion.Transacciones;
import Models.Contactos;

public class ActivityList extends AppCompatActivity {
    SQLiteConexion conexion;
    Button btnRegresarHome, btnVerImagen;
    ListView listContact;
    ArrayList<Contactos> lista;
    ArrayList<String> Arreglo;
    Contactos contactoSelected;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        conexion = new SQLiteConexion(this, Transacciones.DBName, null, 1);
        listContact = (ListView) findViewById(R.id.listView);
        ObtenerContactos();

        btnRegresarHome = (Button) findViewById(R.id.btnRegresarHome);
        btnRegresarHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        btnVerImagen = (Button) findViewById(R.id.btnVerImagen);
        btnVerImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(contactoSelected.getImagen().isEmpty() || contactoSelected.getImagen() == null){
                    Toast.makeText(getApplicationContext(), "No se puede visualizar la imagen", Toast.LENGTH_LONG).show();
                }
                else{
                    // Convierte la cadena Base64 a un Bitmap
                    byte[] decodedString = Base64.decode(contactoSelected.getImagen(), Base64.DEFAULT);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                    AlertDialog.Builder builderFoto = new AlertDialog.Builder(ActivityList.this);
                    LayoutInflater inflater = LayoutInflater.from(ActivityList.this);
                    View viewFotoContacto = inflater.inflate(R.layout.foto_contacto, null);

                    // Obtiene la referencia a la vista ImageView dentro del layout inflado
                    ImageView imageView = viewFotoContacto.findViewById(R.id.imgFotoContacto);
                    imageView.setImageBitmap(bitmap);

                    builderFoto.setView(viewFotoContacto);
                    AlertDialog dialogFoto = builderFoto.create();
                    dialogFoto.show();
                }
            }
        });


        listContact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                contactoSelected = lista.get(position);
            }
        });
    }

    private void ObtenerContactos() {
        SQLiteDatabase db = conexion.getReadableDatabase();
        Contactos contacts = null;
        lista = new ArrayList<Contactos>();

        Cursor cursor = db.rawQuery(Transacciones.SelectAllContacts, null);

        List<HashMap<String, String>> items = new ArrayList<>();
        SimpleAdapter adp = new SimpleAdapter(this, items, R.layout.listcontact_items,
                new String[]{"First Line", "Second Line"},
                new int[]{R.id.txtItems, R.id.txtSubItems});

        while(cursor.moveToNext()){
            HashMap<String, String> result = new HashMap<>();
            result.put("First Line", cursor.getString(1));
            result.put("Second Line", cursor.getString(2));
            items.add(result);

            contacts = new Contactos();
            contacts.setId(cursor.getInt(0));
            contacts.setNombre(cursor.getString(1));
            contacts.setTelefono(cursor.getString(2));
            contacts.setPais(cursor.getString(3));
            contacts.setNota(cursor.getString(4));
            contacts.setImagen(cursor.getString(5));

            lista.add(contacts);
        }

        listContact.setAdapter(adp);
    }
}