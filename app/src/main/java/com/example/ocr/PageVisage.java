package com.example.ocr;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceContour;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetector;
import com.google.mlkit.vision.face.FaceDetectorOptions;

import java.io.IOException;
import java.util.List;

public class PageVisage extends AppCompatActivity {
    private InputImage inputImage;
    private ShapeableImageView image;
    private MaterialButton retourMenu;
    private ProgressDialog progressDialog;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visage);
        image = findViewById(R.id.image);
        retourMenu = findViewById(R.id.retourMenu);
        image.setImageURI(getIntent().getParcelableExtra("imageUri"));
        trouverVisage();

        retourMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PageVisage.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    public void trouverVisage() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Analyse");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        FaceDetectorOptions highAccuracyOpts =
                new FaceDetectorOptions.Builder()
                        .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
                        .setContourMode(FaceDetectorOptions.CONTOUR_MODE_ALL)
                        .build();
        try {
            inputImage = InputImage.fromFilePath(this,getIntent().getParcelableExtra("imageUri"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        FaceDetector detector = FaceDetection.getClient(highAccuracyOpts);
        Task<List<Face>> result = detector.process(inputImage).addOnSuccessListener(new OnSuccessListener<List<Face>>() {
            @Override
            public void onSuccess(List<Face> faces) {
                List<PointF> contourTete = null;
                List<PointF> contourOeilGauche = null;
                List<PointF> contourOeilDroit = null;
                List<PointF> nez = null;
                List<PointF> hautBouche = null;
                List<PointF> basBouche = null;
                List<PointF> HautsourcilsG = null;
                List<PointF> BasSourcilsG = null;
                List<PointF> hautsourcilsD = null;
                List<PointF> BassourcilsD = null;
                for (Face face : faces) {
                    contourOeilGauche = face.getContour(FaceContour.LEFT_EYE).getPoints();
                    contourOeilDroit = face.getContour(FaceContour.RIGHT_EYE).getPoints();
                    contourTete = face.getContour(FaceContour.FACE).getPoints();
                    nez = face.getContour(FaceContour.NOSE_BRIDGE).getPoints();
                    hautBouche = face.getContour(FaceContour.UPPER_LIP_BOTTOM).getPoints();
                    basBouche = face.getContour(FaceContour.LOWER_LIP_TOP).getPoints();
                    HautsourcilsG = face.getContour(FaceContour.LEFT_EYEBROW_TOP).getPoints();
                    BasSourcilsG = face.getContour(FaceContour.LEFT_EYEBROW_BOTTOM).getPoints();
                    hautsourcilsD = face.getContour(FaceContour.RIGHT_EYEBROW_TOP).getPoints();
                    BassourcilsD = face.getContour(FaceContour.RIGHT_EYEBROW_BOTTOM).getPoints();
                }

                Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();


                Bitmap imageWithPoints = bitmap.copy(bitmap.getConfig(), true);


                Canvas canvas = new Canvas(imageWithPoints);

                // Creation des couleurs
                Paint couleurTete = createPaint(Color.RED);
                Paint couleurYeux = createPaint(Color.BLUE);
                Paint couleurNez = createPaint(Color.GREEN);
                Paint couleurBouche = createPaint(Color.BLACK);
                Paint couleurSourcils = createPaint(Color.MAGENTA);

                Contour[] contours = {
                        new Contour(contourTete, couleurTete,"tete"),
                        new Contour(contourOeilGauche, couleurYeux,"oeilGauche"),
                        new Contour(contourOeilDroit, couleurYeux,"oeilDroit"),
                        new Contour(hautBouche, couleurBouche,"Hautbouche"),
                        new Contour(basBouche, couleurBouche,"Basbouche"),
                        new Contour(HautsourcilsG, couleurSourcils,"sourcilHautGauche"),
                        new Contour(BasSourcilsG, couleurSourcils, "sourcilBasGauche"),
                        new Contour(hautsourcilsD, couleurSourcils, "sourcilHautDroit"),
                        new Contour(BassourcilsD, couleurSourcils, "sourcilBasDroit"),
                };

                for (Contour contour : contours) {
                    for (PointF point : contour.point) {
                        canvas.drawCircle(point.x, point.y, 12, contour.couleur);
                    }
                }

                canvas.drawLine(nez.get(0).x, nez.get(0).y, nez.get(nez.size() - 1).x, nez.get(nez.size() - 1).y, couleurNez);

                // Affichage de l'image
                progressDialog.dismiss();
                image.setImageBitmap(imageWithPoints);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(PageVisage.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public Paint createPaint(int color) {
        Paint paint = new Paint();
        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(5);
        return paint;
    }
}
