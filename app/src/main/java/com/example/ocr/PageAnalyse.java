package com.example.ocr;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class PageAnalyse extends AppCompatActivity {

    private EditText magasinReconnue;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analyse);
        magasinReconnue = findViewById(R.id.magasinReconnue);
        trouverMagasin();
    }

    public void trouverMagasin() {
        String magasin = getIntent().getStringExtra("texte").toLowerCase();
        if(magasin.contains("auchan")) {
            magasinReconnue.setText("Le magasin est Auchan");
        }
        else if(magasin.contains("carrefour")) {
            magasinReconnue.setText("Le magasin est Carrefour");
        }
        else if(magasin.contains("leclerc")) {
            magasinReconnue.setText("Le magasin est Leclerc");
        }
        else {
            magasinReconnue.setText("Le magasin n'est pas reconnu");
        }

    }
}