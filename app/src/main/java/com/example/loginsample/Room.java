package com.example.loginsample;

import android.graphics.PointF;

import java.util.ArrayList;
import java.util.List;

public class Room {
    String name;
    float x1, y1, x2, y2;
    public Room(String name, float x1, float y1, float x2, float y2) {
        this.name = name;
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }
    public String getName() {
        return name;
    }
    public float getX1() {
        return x1;
    }
    public float getY1() {
        return y1;
    }
    public float getX2() {
        return x2;
    }
    public float getY2() {
        return y2;
    }
}