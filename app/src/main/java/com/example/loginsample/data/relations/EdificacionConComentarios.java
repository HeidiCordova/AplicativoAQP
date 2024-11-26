package com.example.loginsample.data.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.loginsample.data.entity.Edificacion;

import java.util.List;

public class EdificacionConComentarios {
    @Embedded
    public Edificacion edificacion;

    @Relation(
            parentColumn = "EdId",
            entityColumn = "EdId"
    )
    public List<ComentarioConUsuario> comentariosConUsuarios;
}

