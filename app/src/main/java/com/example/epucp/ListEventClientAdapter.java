package com.example.epucp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.epucp.dto.Evento;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.InputStream;
import java.util.List;

public class ListEventClientAdapter extends RecyclerView.Adapter<ListEventClientAdapter.EventViewHolder> {
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference();
    private List<Evento> eventoList;
    private Context context;
    private String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public List<Evento> getEventoList() {
        return eventoList;
    }

    public void setEventoList(List<Evento> eventoList) {
        this.eventoList = eventoList;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public class EventViewHolder extends RecyclerView.ViewHolder{
        Evento evento;
        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.event_item_client, parent, false);
        return new EventViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder( EventViewHolder holder, int position) {
        Evento e = eventoList.get(position);
        //Rellenamos lo necesario
        ImageView imageView = holder.itemView.findViewById(R.id.imageView_cliente);
        TextView textView = holder.itemView.findViewById(R.id.tw_detalle_cliente);
        Button button = holder.itemView.findViewById(R.id.btn_asistencia_cliente);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(),InformacionEventoClient.class);
                System.out.println(getKey());
                intent.putExtra("key",getKey());
                intent.putExtra("evento",e);
                getContext().startActivity(intent);
            }
        });
        textView.setText(e.getDetalleAImprimir());
        StorageReference imageRef = storageReference.child("img/"+e.getFilename());
        Glide.with(getContext()).load(imageRef).into(imageView);
    }

    @Override
    public int getItemCount() {
        return eventoList.size();
    }
}
