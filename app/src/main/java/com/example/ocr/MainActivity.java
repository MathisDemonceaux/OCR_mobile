package com.example.ocr;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;

public class MainActivity extends AppCompatActivity {

    private MaterialButton imageEntree;
    private MaterialButton lireImage;
    private ShapeableImageView imagePrise;
    private EditText texteReconnue;

    private static final String TAG = "Main_TAG";
    private Uri imageUri = null;

    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int STORAGE_REQUEST_CODE = 101;

    private String[] cameraPermissions;
    private String[] stockagePermissions;

    private ProgressDialog progressDialog;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageEntree = findViewById(R.id.imageEntree);
        lireImage = findViewById(R.id.lireImage);
        imagePrise = findViewById(R.id.imagePrise);
        texteReconnue = findViewById(R.id.texteReconnue);

        //init liste
        cameraPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        stockagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_REQUEST_CODE);
        }
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, STORAGE_REQUEST_CODE);
        }
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Chargement");
        progressDialog.setCanceledOnTouchOutside(false);
        imageEntree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: ");
                voirListeChoix();
            }
        });
    }

    private void voirListeChoix() {
        PopupMenu popupMenu = new PopupMenu(this, imageEntree);
        popupMenu.getMenu().add(Menu.NONE,1,1,"CAMERA");
        popupMenu.getMenu().add(Menu.NONE,2,2,"GALERIE");

        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(android.view.MenuItem MenuItem) {
                int id = MenuItem.getItemId();
                if(id == 1){
                    imageCamera();
                } else if (id ==2) {
                    imageGallerie();
                }
                return true;
            }
        });

    }

    private void imageGallerie() {

        Intent intent = new Intent(Intent.ACTION_PICK);

        intent.setType("image/*");
        galleryActivityResultLauncher.launch(intent);
    }

    private ActivityResultLauncher<Intent> galleryActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == Activity.RESULT_OK) {
                        imageUri = result.getData().getData();
                        imagePrise.setImageURI(imageUri);
                    }else{
                        Toast.makeText(MainActivity.this, "Annulée", Toast.LENGTH_SHORT).show();
                    }

                }
            }
    );

    private void imageCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "NewPic");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Image to Text");

        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        cameraActivityResultLauncher.launch(intent);

    }

    private ActivityResultLauncher<Intent> cameraActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == Activity.RESULT_OK) {
                        imagePrise.setImageURI(imageUri);
                    }else{
                        Toast.makeText(MainActivity.this, "Annulée", Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case CAMERA_REQUEST_CODE:{
                if(grantResults.length>0){
                    boolean cameraPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean stockagePermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if(cameraPermission && stockagePermission){
                        imageCamera();
                    }else{
                        Toast.makeText(this, "Permission refusee", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(this, "Annulée", Toast.LENGTH_SHORT).show();
                }
            }

            case STORAGE_REQUEST_CODE:{
                if(grantResults.length>0){
                    boolean stockagePermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if(stockagePermission){
                        imageGallerie();
                    }else{
                        Toast.makeText(this, "Permission refusee", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(this, "Annulée", Toast.LENGTH_SHORT).show();
                }
            }
        }

    }
}
