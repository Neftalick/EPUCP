package com.example.epucp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.epucp.dto.Evento;
import com.example.epucp.dto.HistoryEvent;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ClientHistorial extends AppCompatActivity {
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

    private String userKeyOwn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        userKeyOwn = intent.getStringExtra("key");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_historial);
        System.out.println(userKeyOwn);
        getItems();
    }
    public void getItems(){

        List<Evento> eventoList = new ArrayList<Evento>();
        List<HistoryEvent> historyEventList = new ArrayList<HistoryEvent>();
        firebaseDatabase.getReference().child("historial").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                eventoList.clear();
                historyEventList.clear();
                for (DataSnapshot children: snapshot.getChildren()){
                    HistoryEvent historyEvent = children.getValue(HistoryEvent.class);
                    System.out.println("Clave del evento");
                    System.out.println(historyEvent.getEventKey());
                    System.out.println("clave del usuario");
                    System.out.println(historyEvent.getUserKey());
                    if(historyEvent.getUserKey().equals(userKeyOwn)){
                        historyEventList.add(historyEvent);
                    }
                }
                firebaseDatabase.getReference().child("eventos").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        eventoList.clear();
                        for (DataSnapshot children: snapshot.getChildren()){
                            Evento evento = children.getValue(Evento.class);
                            System.out.println("aqui");
                            System.out.println(evento.getKey());
                            for (HistoryEvent historyEvent : historyEventList)
                                if (historyEvent.getEventKey().equals(evento.getKey())){
                                    System.out.println("aqui");
                                    eventoList.add(evento);
                            }
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
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(ClientHistorial.this,ClientMainActivity.class);
                intent.putExtra("key",userKeyOwn);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(ClientHistorial.this);
        builder.setMessage("¿Seguro que desea regresar a la pantalla de inicio? Perderá los datos ingresados");
        builder.setPositiveButton("Aceptar",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(ClientHistorial.this,ClientMainActivity.class);
                        intent.putExtra("key",userKeyOwn);
                        startActivity(intent);
                    }
                });
        builder.setNegativeButton("Cancelar",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        builder.show();
    }
}