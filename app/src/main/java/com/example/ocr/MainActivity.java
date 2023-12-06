package com.example.ocr;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private Uri imageUri;
    private ActivityResultLauncher<Intent> prendrePhotoLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate");

        prendrePhotoLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    Log.d(TAG, "ActivityResultCallback");
                    if (result.getResultCode() == RESULT_OK) {
                        // L'image a été capturée et peut être traitée ici
                        // Utilisez l'Uri de l'image (imageUri) pour accéder au fichier
                        Log.d(TAG, "Résultat OK");
                    }
                }
        );

        findViewById(R.id.buttonOpenCamera).setOnClickListener(openCameraClickListener);
    }

    private View.OnClickListener openCameraClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.d(TAG, "onClick - Ouverture de la caméra");
            ouvrirCamera();
        }
    };

    private void ouvrirCamera() {
        Log.d(TAG, "Ouverture de la caméra");

        Intent prendrePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (prendrePhotoIntent.resolveActivity(getPackageManager()) != null) {
            try {
                File photoFile = createImageFile();
                if (photoFile != null) {
                    imageUri = FileProvider.getUriForFile(
                            this,
                            "com.example.ocr.fileprovider",
                            photoFile
                    );
                    prendrePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    prendrePhotoLauncher.launch(prendrePhotoIntent);
                }
            } catch (IOException ex) {
                // Gérer l'exception
                Log.e(TAG, "Erreur lors de la création du fichier temporaire: " + ex.getMessage());
            }
        } else {
            Log.e(TAG, "Aucune application d'appareil photo disponible");
        }
    }

    private File createImageFile() throws IOException {
        Log.d(TAG, "Création du fichier temporaire");

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imageFile = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        return imageFile;
    }
}
