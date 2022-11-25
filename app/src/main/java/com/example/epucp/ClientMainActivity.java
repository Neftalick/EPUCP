package com.example.epucp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.epucp.dto.Evento;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ClientMainActivity extends AppCompatActivity {
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private List<Evento> eventoList = new ArrayList<Evento>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_main);
        //Definimos los botones
        Button historial = findViewById(R.id.btnClientHistory);
        Button Perfil = findViewById(R.id.btnClientPerfil);
        //Funcionamientos de los botones
        historial.setOnClickListener(view -> {
            Intent intent = new Intent(ClientMainActivity.this,ClientHistorial.class);
            startActivity(intent);
        });
        Perfil.setOnClickListener(view -> {
            Intent intent = new Intent(ClientMainActivity.this,ClientPerfil.class);
            startActivity(intent);
        });
        getItems();
    }

    private void getItems(){
        firebaseDatabase.getReference().child("events").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot children : snapshot.getChildren()){
                    Evento evento = children.getValue(Evento.class);
                    eventoList.add(evento);
                }
                ListEventClientAdapter eventClientAdapter = new ListEventClientAdapter();
                eventClientAdapter.setEventoList(eventoList);
                eventClientAdapter.setContext(ClientMainActivity.this);
                RecyclerView recyclerView = findViewById(R.id.recyclerView_client_main);
                recyclerView.setAdapter(eventClientAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(ClientMainActivity.this));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}