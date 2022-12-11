package com.example.epucp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.epucp.dto.Evento;
import com.example.epucp.dto.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONException;
import org.json.JSONObject;

public class Estadisticas extends AppCompatActivity {
    TextView nombreEvento;
    TextView cantidadEvento;
    TextView usuario;
    TextView cantidadUsuario;
    ImageView fotoEvento;
    ImageView fotoUsuario;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estadisticas);
        RequestQueue queue = Volley.newRequestQueue(this);
        fotoEvento = findViewById(R.id.imageView_evento_estadistica);
        fotoUsuario = findViewById(R.id.imageView_usuario_estadistica);
        nombreEvento = findViewById(R.id.Nombre_evento_estadistica);
        usuario = findViewById(R.id.usuario_estadistica);
        String url = "https://us-central1-epucp-a998b.cloudfunctions.net/apiIOT";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String keyEvento = (String) response.get("Evento");
                    int cantidadEventos = (Integer) response.get("CantidadEvento");
                    cantidadEvento = findViewById(R.id.cantidad_evento_estadistica);
                    cantidadEvento.setText("Cantidad de asistentes: "+String.valueOf(cantidadEventos));
                    cantidadUsuario = findViewById(R.id.cantidad_usuario_estadistica);
                    String keyUsuario = (String) response.get("usuario");
                    int cantidadUsuarios = (Integer) response.get("CantidadEventosAsistidos");
                    cantidadUsuario.setText("Cantidad de eventos asistidos: "+String.valueOf(cantidadUsuarios));
                    System.out.println(keyEvento);
                    System.out.println(keyUsuario);
                    FirebaseDatabase.getInstance().getReference().child("eventos").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            System.out.println("aqui");
                            for (DataSnapshot children : snapshot.getChildren()){
                                Evento evento = children.getValue(Evento.class);
                                System.out.println(evento.getKey());
                                if (evento.getKey().equals(keyEvento)){
                                    System.out.println("llego aqui");
                                    StorageReference imageRef = FirebaseStorage.getInstance().getReference().child("img/"+evento.getFilename());
                                    Glide.with(Estadisticas.this).load(imageRef).into(fotoEvento);
                                    nombreEvento.setText(evento.getDetalleAImprimir());
                                    break;
                                }
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {}
                    });
                    FirebaseDatabase.getInstance().getReference().child("usuario").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot children : snapshot.getChildren()){
                                Usuario usuario1 = children.getValue(Usuario.class);
                                System.out.println(usuario1.getKey());
                                if (usuario1.getKey().equals(keyUsuario)){
                                    StorageReference imageRef = FirebaseStorage.getInstance().getReference().child("img/"+usuario1.getFotoFilename());
                                    Glide.with(Estadisticas.this).load(imageRef).into(fotoUsuario);
                                    usuario.setText(usuario1.imprimirInformacion());
                                    break;
                                }
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {}
                    });

                }catch (JSONException e){
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {}
        });
        queue.add(jsonObjectRequest);




    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(Estadisticas.this,AdminMainActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}