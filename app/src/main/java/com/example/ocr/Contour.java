package com.example.ocr;

import android.graphics.Paint;
import android.graphics.PointF;

import java.util.List;

public class Contour {
    List<PointF> point;
    Paint couleur;
    String type;
    public Contour(List<PointF> point, Paint couleur, String type) {
        this.point = point;
        this.couleur = couleur;
        this.type = type;
    }

    public int getCouleur() {
        return couleur.getColor();
    }
}
