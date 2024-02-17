package com.example.pm1e13454;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Interpolator;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import androidx.core.app.ActivityCompat;
import android.content.pm.PackageManager;
import Configuracion.SQLiteConexion;
import Configuracion.Transacciones;
import Models.Contactos;

public class ActivityList extends AppCompatActivity {
    GestureDetector gestureDetector;
    SQLiteConexion conexion;
    Button btnRegresarHome, btnVerImagen, btnEliminar, btnActualizar,btnCompartir;
    ListView listContact;
    ArrayList<Contactos> lista;
    ArrayList<String> Arreglo;
    Contactos contactoSelected;
    String idContacto = "0";
    SearchView txtSearch;
    SimpleAdapter adp;
    int selectedItem = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        txtSearch  = (SearchView) findViewById(R.id.txtSearch);

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
                if(contactoSelected == null){
                    Toast.makeText(getApplicationContext(), "No tiene ningun contacto seleccionado", Toast.LENGTH_LONG).show();
                }else
                if(contactoSelected.getImagen() == null || contactoSelected.getImagen().isEmpty()){
                    Toast.makeText(getApplicationContext(), "Este contacto no tiene imagen", Toast.LENGTH_LONG).show();
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
        btnCompartir= (Button) findViewById(R.id.btnCompartirContacto);
        btnCompartir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(contactoSelected == null){
                    Toast.makeText(getApplicationContext(), "No tiene ningun contacto seleccionado", Toast.LENGTH_LONG).show();
                }else{
                    String mensaje = "Datos del contacto:\n" +
                            "- Nombre: " + contactoSelected.getNombre() + "\n" +
                            "- Teléfono: +(" + contactoSelected.getPais() + ") " + contactoSelected.getTelefono();
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_TEXT, mensaje);
                    startActivity(Intent.createChooser(intent, "Compartir a través de:"));
                }

            }
        });
        listContact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                contactoSelected = lista.get(position);
                idContacto = contactoSelected.getId().toString();
                for (int i = 0; i < parent.getChildCount(); i++) {
                    parent.getChildAt(i).setBackgroundResource(com.google.android.material.R.color.m3_ref_palette_white);
                }
                view.setBackgroundResource(com.google.android.material.R.color.material_dynamic_neutral90);
            }
        });
        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            public boolean onDoubleTap(MotionEvent e) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ActivityList.this);
                builder.setTitle("¿Desea llamar a " + contactoSelected.getNombre() + "?")
                        .setMessage("Número de telefono +" + contactoSelected.getPais() + " " + contactoSelected.getTelefono())
                        .setPositiveButton("Llamar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String telefono = contactoSelected.getTelefono();
                                Intent intent = new Intent(Intent.ACTION_CALL);
                                intent.setData(Uri.parse("tel:" + telefono));

                                // Verificar si tienes permiso para realizar la llamada
                                if (ActivityCompat.checkSelfPermission(ActivityList.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                    // Si no tienes permiso, solicitar al usuario que lo conceda
                                    ActivityCompat.requestPermissions(ActivityList.this, new String[]{android.Manifest.permission.CALL_PHONE}, 1);
                                } else {
                                    // Si tienes permiso, iniciar la actividad del intent para realizar la llamada
                                    startActivity(intent);
                                }
                            }
                        })
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
                return true;
            }
        });

        listContact.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });

        btnEliminar = (Button) findViewById(R.id.btnEliminar);

        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(idContacto == "0"){
                    Toast.makeText(getApplicationContext(), "Favor seleccione un registro",
                            Toast.LENGTH_LONG).show();
                }else{
                    MensajeEliminar();
                }
            }
        });

        btnActualizar = (Button) findViewById(R.id.btnActualizar);
        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(idContacto == "0"){
                    Toast.makeText(getApplicationContext(), "Favor seleccione un registro",
                            Toast.LENGTH_LONG).show();
                }else{
                    EnviarInfoActualizar();
                }
            }
        });

        txtSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String texto) {
                adp.getFilter().filter(texto);
                return false;
            }
        });
    }

    private void MensajeEliminar(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("¿Seguro que desea eliminar este registro?")
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        EliminarContacto();
                        ObtenerContactos();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        builder.create().show();
    }

    private void EnviarInfoActualizar(){
        Intent intent = new Intent(ActivityList.this, MainActivity.class);

        intent.putExtra("Id", contactoSelected.getId().toString());
        intent.putExtra("Pais", contactoSelected.getPais());
        intent.putExtra("Nombre", contactoSelected.getNombre());
        intent.putExtra("Telefono", contactoSelected.getTelefono());
        intent.putExtra("Nota", contactoSelected.getNota());
        intent.putExtra("Imagen", contactoSelected.getImagen());
        startActivity(intent);
    }
    private void EliminarContacto(){
        SQLiteConexion conexion = new SQLiteConexion(this, Transacciones.DBName, null, 1);
        SQLiteDatabase db = conexion.getWritableDatabase();

        int rowDelete = db.delete(Transacciones.TableContactos, Transacciones.id + "=?", new String[]{idContacto});
        Toast.makeText(getApplicationContext(), "Registro Eliminado ", Toast.LENGTH_LONG).show();
        db.close();

    }
    private void ObtenerContactos() {
        SQLiteDatabase db = conexion.getReadableDatabase();
        Contactos contacts = null;
        lista = new ArrayList<Contactos>();

        Cursor cursor = db.rawQuery(Transacciones.SelectAllContacts, null);

        List<HashMap<String, String>> items = new ArrayList<>();
        adp = new SimpleAdapter(this, items, R.layout.listcontact_items,
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