package com.example.loginsample;

import android.graphics.PointF;

import java.util.ArrayList;
import java.util.List;

public class Room {
    private List<float[]> points;  // Lista de puntos representados por arrays de dos valores (x, y)

    public Room() {
        this.points = new ArrayList<>();
    }

    public List<float[]> getPoints() {
        return points;
    }

    // Método para agregar un punto al cuarto
    public void addPoint(float x, float y) {
        points.add(new float[]{x, y});
    }
}
