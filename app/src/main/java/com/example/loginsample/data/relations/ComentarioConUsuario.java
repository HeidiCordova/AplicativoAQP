package com.example.loginsample.data.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.loginsample.data.entity.Comentario;
import com.example.loginsample.data.entity.Usuario;

public class ComentarioConUsuario {
    @Embedded
    public Comentario comentario;

    @Relation(
            parentColumn = "UsId",
            entityColumn = "UsId"
    )
    public Usuario usuario;

}
