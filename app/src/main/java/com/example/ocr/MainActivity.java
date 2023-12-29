package com.example.ocr;

import android.os.Bundle;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;

public class MainActivity extends AppCompatActivity {

    private MaterialButton imageEntree;
    private MaterialButton lireImage;
    private ShapeableImageView imagePrise;
    private EditText texteReconnue;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageEntree = findViewById(R.id.imageEntree);
        lireImage = findViewById(R.id.lireImage);
        imagePrise = findViewById(R.id.imagePrise);
        texteReconnue = findViewById(R.id.texteReconnue);
    }
}
