package com.example.loginsample.data.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "usuarios")
public class Usuario {
    @PrimaryKey(autoGenerate = true)
    public int UsId;

    @ColumnInfo(name = "UsNom")
    public String UsNom;

    @ColumnInfo(name = "UsApe")
    public String UsApe;

    @ColumnInfo(name = "UsCor")
    public String UsCor;

    @ColumnInfo(name = "UsCon")
    public String UsCon;

    @ColumnInfo(name = "UsUsu")
    public String UsUsu;

    @ColumnInfo(name = "UsTel")
    public String UsTel;

}
