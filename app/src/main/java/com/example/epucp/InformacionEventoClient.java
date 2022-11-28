package com.example.epucp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.epucp.dto.Evento;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class InformacionEventoClient extends AppCompatActivity {
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference();
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacion_evento_client);
        Intent intent = getIntent();
        Evento evento = (Evento) intent.getSerializableExtra("evento");
        Button asistencia = findViewById(R.id.button2);
        asistencia.setOnClickListener(view -> {
            //Logica de escaneo de codigo QR
        });
        TextView textView = findViewById(R.id.textViewClientInformacionDetalle);
        ImageView imageView = findViewById(R.id.ImageInformacionClient);
        textView.setText(evento.getDetalleAImprimir());
        StorageReference imageRef = storageReference.child("img/"+evento.getFilename());
        Glide.with(this).load(imageRef).into(imageView);
    }
}