package com.example.epucp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrarUsuario extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference().child("usuario");

    boolean correoValido = true;
    boolean passwordValido = true;
    boolean verifyPasswordValido = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_usuario);
        getSupportActionBar().setTitle("Registro de usuario");
        firebaseAuth = FirebaseAuth.getInstance();
        ;
    }

    public void validarRegistro(View view) {
        TextInputLayout correo = findViewById(R.id.inputCorreo_registro);
        TextInputLayout password = findViewById(R.id.inputPassword_registro);
        TextInputLayout verifyPassword = findViewById(R.id.inputVerifyPassword_registro);

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

        if (verifyPassword.getEditText().getText().toString() != null && !verifyPassword.getEditText().getText().toString().equals("")) {

            verifyPassword.setErrorEnabled(false);
        } else {
            verifyPassword.setError("Debe verificar su contraseña");
            verifyPasswordValido = false;
        }

        if (correoValido && passwordValido && verifyPasswordValido) {
            Log.d("task", "Registro valido");

            firebaseAuth.createUserWithEmailAndPassword(correo.getEditText().getText().toString(), password.getEditText().getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Log.d("task", "EXITO EN REGISTRO");
                        firebaseAuth.getCurrentUser().sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Log.d("task", "EXITO EN ENVIO DE CORREO DE VERIFICACION");
                                String key = databaseReference.push().getKey();
                                databaseReference.child(key).child("correo").setValue(correo.getEditText().getText().toString());
                                databaseReference.child(key).child("rol").setValue("administrador");
                                databaseReference.child(key).child("key").setValue(key);
                                Intent intent = new Intent(RegistrarUsuario.this, MainActivity.class);
                                intent.putExtra("exito", "Se ha enviado un correo para la verificación de su cuenta");
                                startActivity(intent);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("task", "ERROR EN ENVIO DE CORREO DE VERIFICACION - " + e.getMessage());
                            }
                        });

                    } else {
                        Log.d("task", "ERROR EN REGISTRO - " + task.getException().getMessage());
                    }
                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(RegistrarUsuario.this);
                builder.setMessage("¿Seguro que desea regresar a la pantalla de inicio? Perderá los datos ingresados");
                builder.setPositiveButton("Aceptar",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(RegistrarUsuario.this,MainActivity.class);
                                startActivity(intent);
                            }
                        });
                builder.setNegativeButton("Cancelar",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                builder.show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(RegistrarUsuario.this);
        builder.setMessage("¿Seguro que desea regresar a la pantalla de inicio? Perderá los datos ingresados");
        builder.setPositiveButton("Aceptar",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(RegistrarUsuario.this,MainActivity.class);
                        startActivity(intent);
                    }
                });
        builder.setNegativeButton("Cancelar",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        builder.show();
    }

}