package com.example.pm1e13454;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

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
    Button btnRegresarHome;
    ListView listContact;
    ArrayList<Contactos> lista;
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
    }

    private void ObtenerContactos() {
        SQLiteDatabase db = conexion.getReadableDatabase();
        Contactos contacts = null;
        lista = new ArrayList<Contactos>();

        Cursor cursor = db.rawQuery(Transacciones.SelectAllContacts, null);


        /*HashMap<String, String> contacts = new HashMap<>();
        contacts.put("Joshua", "8819-8991");
        contacts.put("Keyla", "8523-8991");
        contacts.put("Marcela Aguilar", "9955-2145");
        contacts.put("Cristel Lones", "1475-9632");*/

        List<HashMap<String, String>> items = new ArrayList<>();
        SimpleAdapter adp = new SimpleAdapter(this, items, R.layout.listcontact_items,
                new String[]{"First Line", "Second Line"},
                new int[]{R.id.txtItems, R.id.txtSubItems});

        while(cursor.moveToNext()){
            HashMap<String, String> result = new HashMap<>();
            result.put("First Line", cursor.getString(1));
            result.put("Second Line", cursor.getString(2));
            items.add(result);
        }

        listContact.setAdapter(adp);
    }
}