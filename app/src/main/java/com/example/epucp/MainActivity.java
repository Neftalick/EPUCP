package com.example.epucp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button buttonClient = findViewById(R.id.btn_client);
        buttonClient.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this,ClientMainActivity.class);
            startActivity(intent);
        });
        Button buttonAdmin = findViewById(R.id.btn_admin);
        buttonAdmin.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this,AdminMainActivity.class);
            startActivity(intent);
        });



    }
}