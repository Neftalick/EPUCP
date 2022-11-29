package com.example.epucp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.example.epucp.dto.Evento;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
        Intent intent1 = getIntent();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_main);
        //Definimos los botones
        Button historial = findViewById(R.id.btnClientHistory);
        Button Perfil = findViewById(R.id.btnClientPerfil);
        //Funcionamientos de los botones
        historial.setOnClickListener(view -> {
            Intent intent = new Intent(ClientMainActivity.this,ClientHistorial.class);
            intent.putExtra("key",intent1.getStringExtra("key"));
            startActivity(intent);
        });
        Perfil.setOnClickListener(view -> {
            Intent intent = new Intent(ClientMainActivity.this,ClientPerfil.class);

            startActivity(intent);
        });
        getItems();
    }

    private void getItems(){
        firebaseDatabase.getReference().child("eventos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot children : snapshot.getChildren()){
                    Evento evento = children.getValue(Evento.class);
                    eventoList.add(evento);
                }
                ListEventClientAdapter eventClientAdapter = new ListEventClientAdapter();
                eventClientAdapter.setKey(getIntent().getStringExtra("key"));
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.cerrar_sesion:
                AuthUI.getInstance().signOut(this).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d("Sistema","Logout exitoso");
                        Intent intent = new Intent(ClientMainActivity




                                .this, MainActivity.class);
                        startActivity(intent);
                    }
                });
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}