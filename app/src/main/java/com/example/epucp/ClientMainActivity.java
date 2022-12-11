package com.example.epucp;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.epucp.dto.Evento;
import com.example.epucp.dto.HistoryEvent;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ClientMainActivity extends AppCompatActivity {
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private List<Evento> eventoList = new ArrayList<Evento>();
    String key;
    Spinner facultad;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent1 = getIntent();
        key = intent1.getStringExtra("key");
        super.onCreate(savedInstanceState);
        FirebaseMessaging.getInstance().subscribeToTopic("EPUCP").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.d("TAG", "onComplete: Suscribed");
            }
        });
        setContentView(R.layout.activity_client_main);
        //Definimos los botones
        Button historial = findViewById(R.id.btnClientHistory);
        facultad = findViewById(R.id.spinner_main_client);
        Button filtrar = findViewById(R.id.button_main_client);
        Button perfil = findViewById(R.id.btnPerfil);
        String[] lista = {"filtrar por facultad","Ciencias e Ingenieria","Generales Ciencias","Generales Letras","Sociales","Ingenieria de telecomunicaciones","Ingenieria electronica","Ingenieria informatica","Ingenieria Mecanica","Ingenieria Industrial"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,lista);
        facultad.setAdapter(adapter);
        //Funcionamientos de los botones
        perfil.setOnClickListener(view -> {
            Intent intent = new Intent(ClientMainActivity.this,ClientPerfil.class);
            intent.putExtra("key",intent1.getStringExtra("key"));
            startActivity(intent);
        });
        historial.setOnClickListener(view -> {
            Intent intent = new Intent(ClientMainActivity.this,ClientHistorial.class);
            intent.putExtra("key",intent1.getStringExtra("key"));
            startActivity(intent);
        });
        filtrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!facultad.getSelectedItem().toString().equals("filtrar por facultad")){
                    getFiltredItems(facultad.getSelectedItem().toString());
                }else{
                    getItems();
                }
            }
        });
        FloatingActionButton floatingActionButton = findViewById(R.id.btn_asistencia);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanCode();
            }
        });
        getItems();
    }
    private void getFiltredItems(String filtro){
        eventoList.clear();
        firebaseDatabase.getReference().child("eventos").addValueEventListener(new ValueEventListener() {
            String strDate = LocalDate.now().toString();
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                eventoList.clear();
                for (DataSnapshot children : snapshot.getChildren()){
                    Evento evento = children.getValue(Evento.class);
                    if (evento.getFacultad().equals(filtro)){
                        SimpleDateFormat inSdf = new SimpleDateFormat("dd/MM/yyyy");
                        SimpleDateFormat outSdf = new SimpleDateFormat("yyyy-MM-dd");
                        try {
                            Date currentDate = outSdf.parse(strDate);
                            Date fechaIngresada = outSdf.parse(outSdf.format(inSdf.parse(evento.getFecha())));
                            if (fechaIngresada.compareTo(currentDate) >= 0){eventoList.add(evento);}
                        } catch (ParseException e) {e.printStackTrace();}
                    }

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

    private void getItems(){
        eventoList.clear();
        firebaseDatabase.getReference().child("eventos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                eventoList.clear();
                String strDate = LocalDate.now().toString();
                for (DataSnapshot children : snapshot.getChildren()){
                    Evento evento = children.getValue(Evento.class);
                    //Validamos que la fecha del evento sea igual o mayor que el día que se realiza el listado
                    SimpleDateFormat inSdf = new SimpleDateFormat("dd/MM/yyyy");
                    SimpleDateFormat outSdf = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        Date currentDate = outSdf.parse(strDate);
                        Date fechaIngresada = outSdf.parse(outSdf.format(inSdf.parse(evento.getFecha())));
                        if (fechaIngresada.compareTo(currentDate) >= 0){eventoList.add(evento);}
                    } catch (ParseException e) {e.printStackTrace();}
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
                FirebaseMessaging.getInstance().unsubscribeFromTopic("EPUCP");
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

    private void scanCode() {
        ScanOptions options = new ScanOptions();
        options.setPrompt("Volume up to set flash on");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);
        barLauncher.launch(options);
    }

    public boolean primeraAsistencia =true;
    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(), result -> {
        System.out.println(result.getContents());
        if (result.getContents() != null){
            //Validamos que sea la primera asistencia del usuario
            firebaseDatabase.getReference().child("historial").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    for (DataSnapshot children: task.getResult().getChildren()){
                        HistoryEvent historyEvent = children.getValue(HistoryEvent.class);
                        if (historyEvent.getEventKey().equals(result.getContents()) && historyEvent.getUserKey().equals(key)){
                            primeraAsistencia = false;
                        }
                    }
                    if (primeraAsistencia){
                        RequestQueue queue = Volley.newRequestQueue(ClientMainActivity.this);
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("eventKey", result.getContents());
                            jsonObject.put("userKey", key);
                        } catch (JSONException e) {
                            System.out.println(e);
                        }
                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, "https://epucp-a998b-default-rtdb.firebaseio.com/historial.json", jsonObject, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Toast.makeText(ClientMainActivity.this, "Asistencia marcada", Toast.LENGTH_SHORT).show();
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(ClientMainActivity.this, "Error en la marca", Toast.LENGTH_SHORT).show();
                            }
                        });
                        queue.add(jsonObjectRequest);
                    }else {
                        Toast.makeText(ClientMainActivity.this, "Usted ya marco asistencia en este evento, gracias!",Toast.LENGTH_SHORT).show();
                    }
                }
            });
            primeraAsistencia = true;
        }
    });

    @Override
    protected void onStop() {
        super.onStop();
    }
}