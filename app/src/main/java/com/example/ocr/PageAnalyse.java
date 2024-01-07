package com.example.ocr;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PageAnalyse extends AppCompatActivity {

    private EditText magasinReconnue;
    private EditText total;
    private EditText texte;
    private MaterialButton retourMenu;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analyse);
        magasinReconnue = findViewById(R.id.magasinReconnue);
        total = findViewById(R.id.total);
        texte = findViewById(R.id.texte);
        retourMenu = findViewById(R.id.retourMenu);
        texte.setText(getIntent().getStringExtra("texte"));

        retourMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PageAnalyse.this, MainActivity.class);
                startActivity(intent);
            }
        });
        trouverMagasin();
        trouverTotal();
    }

    public void trouverTotal() {
        String total = getIntent().getStringExtra("texte").toLowerCase();
        Pattern pattern = Pattern.compile("\\d+(?:\\s*[,.]{0,}?\\s*\\d{1,})?\\s*€");
        if (magasinReconnue.getText().toString().equals("Le magasin est Leclerc")) {
            pattern = Pattern.compile("\\d+(?:\\s*[,.]{0,}?\\s*\\d{1,})?\\s*€?");
        }
        Matcher matcher = pattern.matcher(total);
        float plusGrand = 0;
        while(matcher.find()) {
            String group = matcher.group();
            group = group.replace(" ", "");
            group = group.replace(",", ".");
            group = group.replace("..", ".");
            group = group.replace("€", "");
            Log.e("group", group.toString());
            Log.e("totalite",total);
            if(Float.parseFloat(group) > plusGrand && Float.parseFloat(group) < 1000) {
                plusGrand = Float.parseFloat(group);
            }
        }
        this.total.setText(String.valueOf(plusGrand));

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