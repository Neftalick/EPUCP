package com.example.epucp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.epucp.dto.Evento;
import com.example.epucp.dto.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth =  FirebaseAuth.getInstance();
    TextInputLayout correo, password;
    Button btnIniciasSesion, btnRegistrarUsuario;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        correo = findViewById(R.id.inputCorreo_iniSesion);
        password = findViewById(R.id.inputPassword_iniSesion);
        btnIniciasSesion = findViewById(R.id.btn_inicio);
        btnRegistrarUsuario = findViewById(R.id.btn_registrarUsuario);
        btnIniciasSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (verificarEstadoInternet()) {
                    //Iniciar sesion
                    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z.]+";
                    boolean correoValido = true;
                    if (correo.getEditText().getText().toString() != null && !correo.getEditText().getText().toString().equals("")) {
                        if (!correo.getEditText().getText().toString().matches(emailPattern)) {
                            correo.setError("Ingrese un correo válido");
                            correoValido = false;
                        } else {
                            correo.setErrorEnabled(false);
                        }
                    } else {
                        correo.setError("Ingrese un correo");
                        correoValido = false;
                    }

                    boolean passwordValido = true;
                    if (password.getEditText().getText().toString() != null && !password.getEditText().getText().toString().equals("")) {
                        password.setErrorEnabled(false);
                    } else {
                        password.setError("Ingrese una contraseña");
                        passwordValido = false;
                    }

                    if (correoValido && passwordValido) {
                        firebaseAuth.signInWithEmailAndPassword(correo.getEditText().getText().toString(), password.getEditText().getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Log.d("task", "EXITO EN REGISTRO");

                                    firebaseAuth.getCurrentUser().reload().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (firebaseAuth.getCurrentUser().isEmailVerified()) {
                                                Log.d("mes", correo.getEditText().getText().toString());
                                                //Verificamos si es administrador
                                                databaseReference.child("usuario").addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        if (snapshot.getValue() != null){
                                                            for (DataSnapshot children : snapshot.getChildren()){
                                                                Usuario usuario = children.getValue(Usuario.class);
                                                                if (usuario.getCorreo().equals(correo.getEditText().getText().toString())){
                                                                    if(usuario.getRol().equals("administrador")){
                                                                        Intent intent = new Intent(MainActivity.this, AdminMainActivity.class);
                                                                        startActivity(intent);
                                                                    }else{
                                                                        Intent intent = new Intent(MainActivity.this,ClientMainActivity.class);
                                                                        intent.putExtra("key",usuario.getKey());
                                                                        startActivity(intent);
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                    }
                                                });
                                            } else {
                                                Snackbar.make(findViewById(R.id.activity_inicio_sesion), "Su cuenta no ha sido verificada. Verifíquela para poder ingresar", Snackbar.LENGTH_LONG).show();
                                            }
                                        }
                                    });

                                } else {
                                    Log.d("task", "ERROR EN REGISTRO - " + task.getException().getMessage());
                                    Snackbar.make(findViewById(R.id.activity_inicio_sesion), task.getException().getMessage(), Snackbar.LENGTH_LONG).show();
                                }
                            }
                        });

                    }
                } else {
                    MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(MainActivity.this);
                    builder.setMessage("Verifique su conexión a internet para poder ingresar a la aplicación");
                    builder.setPositiveButton("Aceptar",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    builder.show();
                }
            }
        });
        btnRegistrarUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (verificarEstadoInternet()) {
                    //registrar Usuario
                    Intent intent = new Intent(MainActivity.this,RegistrarUsuario.class);
                    startActivity(intent);
                } else {
                    MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(MainActivity.this);
                    builder.setMessage("Verifique su conexión a internet para poder ingresar a la aplicación");
                    builder.setPositiveButton("Aceptar",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    builder.show();
                }
            }
        });
    }

    public boolean verificarEstadoInternet(){
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


}