package com.example.loginsample.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.loginsample.data.entity.Comentario;

import java.util.List;

@Dao
public interface ComentarioDAO {
    @Insert
    long insertComentario(Comentario comentario);


    @Query("SELECT * FROM comentarios WHERE UsId = :idUsuario")
    List<Comentario> getComentariosByUsuario(int idUsuario);

    @Query("SELECT * FROM comentarios WHERE EdId = :edificacionId")
    List<Comentario> getComentariosByEdificacion(int edificacionId);
}
