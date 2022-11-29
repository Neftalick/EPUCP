package com.example.epucp;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.bumptech.glide.Glide;
import com.example.epucp.dto.Evento;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.zxing.Result;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

public class InformacionEventoClient extends AppCompatActivity {
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference();
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    String key;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        key = intent.getStringExtra("key");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacion_evento_client);
        Evento evento = (Evento) intent.getSerializableExtra("evento");
        Button asistencia = findViewById(R.id.button2);
        TextView textView = findViewById(R.id.textViewClientInformacionDetalle);
        ImageView imageView = findViewById(R.id.ImageInformacionClient);
        textView.setText(evento.getDetalleAImprimir());
        StorageReference imageRef = storageReference.child("img/"+evento.getFilename());
        Glide.with(this).load(imageRef).into(imageView);
        asistencia.setOnClickListener(view -> {
            scanCode();
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void scanCode() {
        ScanOptions options = new ScanOptions();
        options.setPrompt("Volume up to set flash on");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);
        barLauncher.launch(options);
    }

    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(),result -> {
        System.out.println(result.getContents());
        if (result.getContents() != null){
            AlertDialog.Builder builder = new AlertDialog.Builder(InformacionEventoClient.this);
            builder.setTitle("Result");
            builder.setMessage(result.getContents());
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    System.out.println(key);
                    dialogInterface.dismiss();
                }
            }).show();
        }
    });



}