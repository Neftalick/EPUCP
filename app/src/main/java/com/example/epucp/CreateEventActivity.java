package com.example.epucp;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.epucp.dto.Evento;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

public class CreateEventActivity extends AppCompatActivity {
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference();
    Evento evento;
    Uri imageUri;
    EditText nombre,responsable,descripcion,aula,fecha,hora;
    Spinner facultad;
    Button subirImagenbtn;
    FloatingActionButton crearEvento;
    ActivityResultLauncher<Intent> openImageLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result ->{
        if(result.getResultCode()== Activity.RESULT_OK){
            imageUri=result.getData().getData();
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        nombre = (EditText) findViewById(R.id.CEvent_Nombre);
        responsable = (EditText) findViewById(R.id.CEvent_Responsable);
        descripcion = (EditText) findViewById(R.id.CEvent_Descripcion);
        aula = (EditText) findViewById(R.id.CEvent_Aula);
        fecha = (EditText) findViewById(R.id.CEvent_Fecha);
        hora = (EditText) findViewById(R.id.CEEvent_Hora);
        facultad = (Spinner) findViewById(R.id.CEvent_Facultad);
        subirImagenbtn = (Button) findViewById(R.id.btn_subirImagen);
        crearEvento = (FloatingActionButton) findViewById(R.id.CEvento_btn);
        String[] lista = {"Item 1","Item 2","Item 3","Item 4"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,lista);
        facultad.setAdapter(adapter);
        subirImagenbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                subirImagen();
            }
        });
        crearEvento.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                String strDate = LocalDate.now().toString();
                Log.d("time",strDate);
                try {
                    SimpleDateFormat inSdf = new SimpleDateFormat("dd/MM/yyyy");
                    SimpleDateFormat outSdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date currentDate = outSdf.parse(strDate);
                    Date fechaIngresada = outSdf.parse(outSdf.format(inSdf.parse(fecha.getText().toString())));
                    if (fechaIngresada.after(currentDate)){
                        if(false){
                            /**String filename = "";
                            if(imageUri!=null){
                                String[] path= imageUri.toString().split("/");
                                filename = path[path.length-1];
                                StorageReference imageReference = storageReference.child("img/"+filename);
                                imageReference.putFile(imageUri).addOnSuccessListener(taskSnapshot ->
                                        Log.d("msg","Archivo Subido correctamente")).addOnFailureListener(e->Log.d("msg","Error",e.getCause()));
                            }
                            databaseReference.child(actividad.getKey()).child("descripcion").setValue(descripcion.getText().toString());
                            databaseReference.child(actividad.getKey()).child("fechaFin").setValue(fechaFin.getText().toString());
                            databaseReference.child(actividad.getKey()).child("fechaInicio").setValue(fechaInicio.getText().toString());
                            databaseReference.child(actividad.getKey()).child("horaFin").setValue(horaFin.getText().toString());
                            databaseReference.child(actividad.getKey()).child("horaInicio").setValue(horaInicio.getText().toString());
                            databaseReference.child(actividad.getKey()).child("titulo").setValue(titulo.getText().toString());
                            databaseReference.child(actividad.getKey()).child("filename").setValue(imageUri!=null?filename:actividad.getFilename());
                            Toast.makeText(ActualizaAgendaActivity.this, "Data Actualizada correctamente", Toast.LENGTH_SHORT).show();***/
                        }else{
                            //Creacion
                            if(imageUri!=null){
                                String[] path= imageUri.toString().split("/");
                                String filename = path[path.length-1];
                                StorageReference imageReference = storageReference.child("img/"+filename);
                                imageReference.putFile(imageUri).addOnSuccessListener(taskSnapshot ->
                                        Log.d("msg","Archivo Subido correctamente")).addOnFailureListener(e->Log.d("msg","Error",e.getCause()));
                                //Guardo el valor en la base de datos
                                String keyActividad = databaseReference.push().getKey();
                                //Actividad actividad = new Actividad(fechaFin.getText().toString(),horaFin.getText().toString(),fechaInicio.getText().toString(),horaInicio.getText().toString(),titulo.getText().toString(),descripcion.getText().toString(),filename,keyActividad);
                                Evento evento = new Evento(nombre.getText().toString(),responsable.getText().toString(),descripcion.getText().toString(),facultad.getSelectedItem().toString(),aula.getText().toString(),fecha.getText().toString(),hora.getText().toString());
                                databaseReference.child(keyActividad).setValue(evento);
                                nombre.setText("");
                                responsable.setText("");
                                descripcion.setText("");
                                //facultad
                                aula.setText("");
                                fecha.setText("");
                                hora.setText("");
                                imageUri = null;
                                Toast.makeText(CreateEventActivity.this, "Data Añadida correctamente", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(CreateEventActivity.this, "Debe Añadir una imagen!", Toast.LENGTH_SHORT).show();
                            }
                        }

                    }else{
                        if (!fechaIngresada.after(currentDate)){
                            fecha.setError("La fecha ingresada no es valida");
                            fecha.setTextColor(Color.RED);
                        }if (imageUri == null){
                            Toast.makeText(CreateEventActivity.this, "No se ha seleccionado una foto!!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }catch (ParseException e){
                    Toast.makeText(CreateEventActivity.this, "Se ingreso un formato de fecha incorrecto", Toast.LENGTH_SHORT).show();
                    System.out.println(e.getMessage());
                }
            }
        });
    }
    public void subirImagen(){
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*");
        openImageLauncher.launch(intent);
    }
}