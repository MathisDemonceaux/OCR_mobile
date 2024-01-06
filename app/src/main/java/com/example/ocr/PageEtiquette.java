package com.example.ocr;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.label.ImageLabel;
import com.google.mlkit.vision.label.ImageLabeler;
import com.google.mlkit.vision.label.ImageLabeling;
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions;

import java.io.IOException;
import java.util.List;

public class PageEtiquette  extends AppCompatActivity {
    private EditText imageType;
    private ShapeableImageView image;
    private InputImage inputImage;

    @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_etiquette);
        imageType = findViewById(R.id.imageType);
        image = findViewById(R.id.image);
        image.setImageURI(getIntent().getParcelableExtra("imageUri"));
        try {
            classifierImage();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void classifierImage() throws IOException {
        inputImage = InputImage.fromFilePath(this,getIntent().getParcelableExtra("imageUri"));
        ImageLabeler labeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS);
        labeler.process(inputImage)
                .addOnSuccessListener(new OnSuccessListener<List<ImageLabel>>() {
                    @Override
                    public void onSuccess(List<ImageLabel> labels) {
                        StringBuilder labelBuilder = new StringBuilder();
                        for(ImageLabel label : labels) {
                            labelBuilder.append(label.getText()).append(" : ").append(label.getConfidence()).append("\n");
                        }
                        imageType.setText(labelBuilder.toString());
                    }
                });
    }
}
