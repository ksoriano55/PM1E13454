package com.example.pm1e13454;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import Configuracion.PaisesTrans;
import Configuracion.SQLiteConexion;
import Configuracion.Transacciones;
import Models.Paises;

public class MainActivity extends AppCompatActivity {
    EditText nombre, telefono, nota;
    static final int peticion_camara = 100;
    static final int  peticion_foto = 101;
    Spinner cbxPais;
    Button btnListaContacto, btnGuardarContacto, btnAgregarPais, btnTomarFoto;
    String imagenBase64, idContacto, codPaisSelected;
    Integer codigoPais;
    ImageView imageView;
    ArrayList<Paises> lista;
    ArrayList<String> PaisesList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nombre = (EditText) findViewById(R.id.txtNombre);
        telefono = (EditText) findViewById(R.id.txtTelefono);
        cbxPais = (Spinner) findViewById(R.id.spinnerPais);
        nota = (EditText) findViewById(R.id.txtNota);
        imageView = (ImageView) findViewById(R.id.fotoContacto);

        //**********EDITAR***********//
        Intent intent = getIntent();
        if(intent.getExtras() != null){
            idContacto = intent.getStringExtra("Id");
            codPaisSelected = intent.getStringExtra("Pais");
            String nombreVal = intent.getStringExtra("Nombre");
            String telefonoVal = intent.getStringExtra("Telefono");
            String notaVal = intent.getStringExtra("Nota");
            String imagenBase64 = intent.getStringExtra("Imagen");

            nombre.setText(nombreVal);
            telefono.setText(String.valueOf(telefonoVal));
            nota.setText(notaVal);
            if(imagenBase64 != null){
                VerImagen(imagenBase64);
            }
        }
        //**********BOTONES**********//
        btnListaContacto = (Button) findViewById(R.id.btnListaContacto);
        btnGuardarContacto = (Button) findViewById(R.id.btnGuardarContacto);
        btnAgregarPais = (Button) findViewById(R.id.btnAgregarPais);
        btnTomarFoto = (Button)  findViewById(R.id.btnTomarFoto);

        ///----------------------------------------------------------------------///
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
                if(intent.getExtras() != null){
                    EditarContacto();
                }else{
                    AgregarContacto();
                }
            }
        });
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
                            AgregarPais(txtCodPaisVal,txtPaisVal);
                            llenarCombo();
                           // Toast.makeText(getApplicationContext(), "Pais guardado con exito!", Toast.LENGTH_LONG).show();
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
        btnTomarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ObtenerPermisos();
            }
        });

        //**--------------------ComboBox Paises----------------------**//
        llenarCombo();
        cbxPais.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try
                {
                    codigoPais = lista.get(position).getCodigo();
                }
                catch (Exception ex)
                {
                    ex.toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ///********  Fin Combo Paises------------------*****************////////////
    }

    private void VerImagen(String img) {
        byte[] decodedString = Base64.decode(img, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        imageView.setImageBitmap(bitmap);
    }

    private void EditarContacto(){
        Pattern p = Pattern.compile("[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]");
        if(nombre.getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(), "¡Es obligatorio el campo nombre!", Toast.LENGTH_LONG).show();
        }
        else if(telefono.getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(), "¡Es obligatorio el campo telefono!", Toast.LENGTH_LONG).show();
        }else if(nota.getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(), "¡Es obligatorio el campo nota!", Toast.LENGTH_LONG).show();
        }else if(!p.matcher(telefono.getText().toString()).matches()){
            Toast.makeText(getApplicationContext(), "¡Formato invalido para campo telefono!", Toast.LENGTH_LONG).show();
        }
        else{
            SQLiteConexion conexion = new SQLiteConexion(this, Transacciones.DBName, null, 1);
            SQLiteDatabase db = conexion.getWritableDatabase();
            ContentValues valores = new ContentValues();
            valores.put(Transacciones.nombre, nombre.getText().toString());
            valores.put(Transacciones.telefono, telefono.getText().toString());
            valores.put(Transacciones.pais, codigoPais);
            valores.put(Transacciones.nota, nota.getText().toString());
            valores.put(Transacciones.imagen, imagenBase64);

            int resultado = db.update(Transacciones.TableContactos,valores, Transacciones.id+"=?", new String[]{idContacto});

            Toast.makeText(getApplicationContext(), "Registro Ingresado con exito ",
                    Toast.LENGTH_LONG).show();

            db.close();
        }
    }
    private void llenarCombo(){
        ObtenerPaises();
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, PaisesList);

        cbxPais.setAdapter(adapter);

        int position = getPositionByCode(PaisesList, codPaisSelected);

        if(position != -1){
            cbxPais.setSelection(position);
        }
    }

    private void ClearTexts(){
        nombre.setText(""); telefono.setText(""); nota.setText("");
    }

    private int getPositionByCode(List<String> list, String code) {
        for (int i = 0; i < list.size(); i++) {
            String item = list.get(i);
            String[] parts = item.split(" - ");
            if (parts.length == 2 && parts[1].equals(code)) {
                return i;
            }
        }
        return -1;
    }

    private void ObtenerPaises()
    {
        SQLiteConexion conexion = new SQLiteConexion(this, Transacciones.DBName, null, 1);
        SQLiteDatabase db = conexion.getReadableDatabase();
        Paises paises = null;
        lista = new ArrayList<Paises>();

        // Cursor de base de datos para recorrer los datos
        Cursor cursor = db.rawQuery(PaisesTrans.SelectAllPaises, null);

        while (cursor.moveToNext())
        {
            paises = new Paises();
            paises.setId(cursor.getInt(0));
            paises.setCodigo(cursor.getInt(1));
            paises.setNombre(cursor.getString(2));

            lista.add(paises);
        }

        cursor.close();

        FillData();
    }

    private void FillData()
    {
        PaisesList = new ArrayList<String>();
        for(int i = 0; i < lista.size(); i ++)
        {
            PaisesList.add(lista.get(i).getNombre()  +" - "+ lista.get(i).getCodigo()) ;
        }
    }
    private void AgregarContacto()
    {
        Pattern p = Pattern.compile("[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]");
        if(nombre.getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(), "¡Es obligatorio el campo nombre!", Toast.LENGTH_LONG).show();
        }
        else if(telefono.getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(), "¡Es obligatorio el campo telefono!", Toast.LENGTH_LONG).show();
        }else if(nota.getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(), "¡Es obligatorio el campo nota!", Toast.LENGTH_LONG).show();
        }else if(!p.matcher(telefono.getText().toString()).matches()){
            Toast.makeText(getApplicationContext(), "¡Formato invalido para campo telefono!", Toast.LENGTH_LONG).show();
        } else if (codigoPais == null) {
            Toast.makeText(getApplicationContext(), "¡Debe registrar un pais para registrar un contacto!", Toast.LENGTH_LONG).show();
        } else{
            SQLiteConexion conexion = new SQLiteConexion(this, Transacciones.DBName, null, 1);
            SQLiteDatabase db = conexion.getWritableDatabase();
            ContentValues valores = new ContentValues();
            valores.put(Transacciones.nombre, nombre.getText().toString());
            valores.put(Transacciones.telefono, telefono.getText().toString());
            valores.put(Transacciones.pais, codigoPais);
            valores.put(Transacciones.nota, nota.getText().toString());
            valores.put(Transacciones.imagen, imagenBase64);

            Long resultado = db.insert(Transacciones.TableContactos, Transacciones.id, valores);

            Toast.makeText(getApplicationContext(), "Registro Ingresado con exito " + resultado.toString(),
                    Toast.LENGTH_LONG).show();

            db.close();
            ClearTexts();
        }

    }
    private void AgregarPais(String codigo, String nombre)
    {
        SQLiteConexion conexion = new SQLiteConexion(this, Transacciones.DBName, null, 1);
        SQLiteDatabase db = conexion.getWritableDatabase();

        ContentValues valores = new ContentValues();
        valores.put(PaisesTrans.codigo, codigo);
        valores.put(PaisesTrans.nombre, nombre);

        Long resultado = db.insert(PaisesTrans.TablePaises, Transacciones.id, valores);

        Toast.makeText(getApplicationContext(), "Registro Ingresado con exito " + resultado.toString(),
                Toast.LENGTH_LONG).show();

        db.close();
    }
    private void ObtenerPermisos()
    {
        if(ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},
                    peticion_camara);
        }
        else
        {
            CapturarFoto();
        }
    }
    private void CapturarFoto()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if(intent.resolveActivity(getPackageManager()) != null)
        {
            startActivityForResult(intent, peticion_foto);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull
    int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == peticion_camara)
        {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                CapturarFoto();
            }
            else
            {
                Toast.makeText(getApplicationContext(), "!Alto ahi!, Permiso denegado", Toast.LENGTH_LONG).show();
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == peticion_foto && resultCode == RESULT_OK)
        {
            Bundle extras = data.getExtras();
            Bitmap imagen = (Bitmap) extras.get("data");
            imageView.setImageBitmap(imagen);

            /*---------Convertir imagen a base64-------*/
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            imagen.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            imagenBase64 = Base64.encodeToString(byteArray, Base64.DEFAULT);
        }
    }
}