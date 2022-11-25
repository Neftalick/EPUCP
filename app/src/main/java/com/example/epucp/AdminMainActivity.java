package com.example.epucp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.epucp.dto.Evento;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdminMainActivity extends AppCompatActivity {
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private List<Evento> eventoList = new ArrayList<Evento>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);
        FloatingActionButton floatingActionButton = findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(view -> {
            Intent intent = new Intent(AdminMainActivity.this,CreateEventActivity.class);
            startActivity(intent);
        });
        getItems();
    }

    public void getItems(){
        firebaseDatabase.getReference().child("events").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot children : snapshot.getChildren()){
                    Evento evento = children.getValue(Evento.class);
                    eventoList.add(evento);
                }
                ListEventAdminAdapter eventAdminAdapter = new ListEventAdminAdapter();
                eventAdminAdapter.setEventoList(eventoList);
                eventAdminAdapter.setContext(AdminMainActivity.this);
                RecyclerView recyclerView = findViewById(R.id.recview_admin_main);
                recyclerView.setAdapter(eventAdminAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(AdminMainActivity.this));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}