package com.example.loginsample.data.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "comentarios",
        foreignKeys = {
                @ForeignKey(entity = Edificacion.class, parentColumns = "EdId", childColumns = "EdId"),
                @ForeignKey(entity = Usuario.class, parentColumns = "UsId", childColumns = "UsId")
        },
        indices = {@Index(value = "EdId"), @Index(value = "UsId")}
)
public class Comentario {

    @PrimaryKey(autoGenerate = true)
    public int ComId;

    @ColumnInfo(name = "EdId")
    public int EdId;

    @ColumnInfo(name = "UsId")
    public int UsId;

    @ColumnInfo(name = "ComTex")
    public String ComTex;

    @ColumnInfo(name = "ComCal")
    public int ComCal;

    @ColumnInfo(name = "ComFec")
    public String ComFec; // Puede ser String en formato ISO 8601

    // Getters y Setters

    public int getComId() {
        return ComId;
    }

    public void setComId(int comId) {
        this.ComId = comId;
    }

    public int getEdId() {
        return EdId;
    }

    public void setEdId(int edId) {
        this.EdId = edId;
    }

    public int getUsId() {
        return UsId;
    }

    public void setUsId(int usId) {
        this.UsId = usId;
    }

    public String getComTex() {
        return ComTex;
    }

    public void setComTex(String comTex) {
        this.ComTex = comTex;
    }

    public int getComCal() {
        return ComCal;
    }

    public void setComCal(int comCal) {
        this.ComCal = comCal;
    }

    public String getComFec() {
        return ComFec;
    }

    public void setComFec(String comFec) {
        this.ComFec = comFec;
    }
}
