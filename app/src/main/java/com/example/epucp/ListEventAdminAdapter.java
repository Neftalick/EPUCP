package com.example.epucp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.annotation.GlideModule;
import com.example.epucp.dto.Evento;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class ListEventAdminAdapter extends RecyclerView.Adapter<ListEventAdminAdapter.EventViewHolder> {
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference();
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();
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
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_item_admin, parent, false);
        return new ListEventAdminAdapter.EventViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Evento e = eventoList.get(position);
        ImageView imageView = holder.itemView.findViewById(R.id.imageView_item_admin);
        TextView textView = holder.itemView.findViewById(R.id.textView_item_admin);
        Button editar = holder.itemView.findViewById(R.id.editar_item_admin);
        Button eliminar = holder.itemView.findViewById(R.id.elimanr_item_admin);
        //Editar
        editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(),CreateEventActivity.class);
                intent.putExtra("evento",e);
                getContext().startActivity(intent);
            }
        });
        //Eliminar
        eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sol = e.getKey();
                databaseReference.child("eventos").child(sol).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            storageReference.child("img/"+e.getFilename()).delete();
                            Toast.makeText(getContext(),"Borrado con exito",Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(getContext(), "Borrado fallido",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
        //Impresion de datos
        /*
        * titulo.setText(a.getTitulo());
        Descrip.setText(a.getDescripcion());
        detalle.setText(a.getDetalle());
        StorageReference imageRef = storageReference.child("img/"+a.getFilename());
        Glide.with(getContext()).load(imageRef).into(imageView);
        * */
        textView.setText(e.getDetalleAImprimir());
        StorageReference imageRef = storageReference.child("img/"+e.getFilename());
        Glide.with(getContext()).load(imageRef).into(imageView);
    }

    @Override
    public int getItemCount() {
        return eventoList.size();
    }
}
