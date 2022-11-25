package com.example.epucp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.epucp.dto.Evento;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ClientHistorial extends AppCompatActivity {
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private List<Evento> eventoList = new ArrayList<Evento>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_historial);
        getItems();
    }
    public void getItems(){
        firebaseDatabase.getReference("historial").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot children: snapshot.getChildren()){
                    Evento evento = children.getValue(Evento.class);
                    eventoList.add(evento);
                }
                HistorialEventClientAdapter eventClientAdapter = new HistorialEventClientAdapter();
                eventClientAdapter.setEventoList(eventoList);
                eventClientAdapter.setContext(ClientHistorial.this);
                RecyclerView recyclerView = findViewById(R.id.ClienteHistorial);
                recyclerView.setAdapter(eventClientAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(ClientHistorial.this));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}