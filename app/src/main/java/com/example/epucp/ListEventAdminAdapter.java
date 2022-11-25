package com.example.epucp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.epucp.dto.Evento;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class ListEventAdminAdapter extends RecyclerView.Adapter<ListEventAdminAdapter.EventViewHolder> {
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
    public class EventViewHolder extends RecyclerView.ViewHolder{
        Evento evento;
        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.event_item_admin, parent, false);
        return new ListEventAdminAdapter.EventViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Evento e = eventoList.get(position);
        ImageView imageView = holder.itemView.findViewById(R.id.imageView_item_admin);
        TextView textView = holder.itemView.findViewById(R.id.textView_item_admin);
        Button editar = holder.itemView.findViewById(R.id.editar_item_admin);
        Button eliminar = holder.itemView.findViewById(R.id.elimanr_item_admin);

    }

    @Override
    public int getItemCount() {
        return eventoList.size();
    }
}
