package com.example.loginsample.data.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "edificacion")
public class Edificacion {
    @PrimaryKey(autoGenerate = true)
    private int EdId;
    private String EdNom;  // Nombre de la edificación
    private String EdDes;  // Descripción de la edificación
    private int EdIma;     // ID de la imagen
    private double EdLat;  // Latitud de la edificación
    private double EdLong; // Longitud de la edificación
    private int EdAuId;
    // Constructor, getters y setters
    public Edificacion(String EdNom, String EdDes, int EdIma, double EdLat, double EdLong, int EdAuId) {
        this.EdNom = EdNom;
        this.EdDes = EdDes;
        this.EdIma = EdIma;
        this.EdLat = EdLat;
        this.EdLong = EdLong;
        this.EdAuId = EdAuId;
    }
    // Getters y setters
    public void setEdAuId(int EdAuId) {
        this.EdAuId = EdAuId;
    }

    // Getters y setters
    public int getEdAuId() {
        return EdAuId;
    }

    // Getters y setters
    public void setEdId(int EdId) {
        this.EdId = EdId;
    }

    // Getters y setters
    public int getEdId() {
        return EdId;
    }

    public String getEdNom() {  // Get del nombre
        return EdNom;
    }

    public void setEdNom(String EdNom) {  // Set del nombre
        this.EdNom = EdNom;
    }

    public String getEdDes() {  // Get de la descripción
        return EdDes;
    }

    public void setEdDes(String EdDes) {  // Set de la descripción
        this.EdDes = EdDes;
    }

    public int getEdIma() {  // Get de la imagen
        return EdIma;
    }

    public void setEdIma(int EdIma) {  // Set de la imagen
        this.EdIma = EdIma;
    }

    public double getEdLat() {  // Get de la latitud
        return EdLat;
    }

    public void setEdLat(double EdLat) {  // Set de la latitud
        this.EdLat = EdLat;
    }

    public double getEdLong() {  // Get de la longitud
        return EdLong;
    }

    public void setEdLong(double EdLong) {  // Set de la longitud
        this.EdLong = EdLong;
    }
}
