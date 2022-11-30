package com.example.epucp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.epucp.dto.Evento;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class HistorialEventClientAdapter extends RecyclerView.Adapter<HistorialEventClientAdapter.HEventHolder> {
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference();
    private List<Evento> eventoList;
    private Context context;

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
    public class HEventHolder extends RecyclerView.ViewHolder{
        Evento evento;
        public HEventHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    @NonNull
    @Override
    public HEventHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.historialclienteitem, parent, false);
        return new HEventHolder(itemView);
    }

    @Override
    public void onBindViewHolder(HEventHolder holder, int position) {
        Evento e = eventoList.get(position);
        ImageView imageView = holder.itemView.findViewById(R.id.imageViewHistorialClient);
        TextView textView = holder.itemView.findViewById(R.id.textViewHistorialClient);
        textView.setText(e.getDetalleAImprimir());
        StorageReference imageRef = storageReference.child("img/"+e.getFilename());
        Glide.with(getContext()).load(imageRef).into(imageView);
    }

    @Override
    public int getItemCount() {
        return eventoList.size();
    }
}
