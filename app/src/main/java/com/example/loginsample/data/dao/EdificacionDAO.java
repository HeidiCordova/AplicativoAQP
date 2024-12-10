package com.example.loginsample.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.loginsample.data.entity.Edificacion;

import java.util.List;

@Dao
public interface EdificacionDAO {
    // Inserta una sola edificación y devuelve el ID generado
    @Insert
    long insertEdificacion(Edificacion edificacion);

    // Inserta varias edificaciones y devuelve una lista de IDs generados
    @Insert
    List<Long> insertAll(List<Edificacion> edificaciones);

    // Recupera todas las edificaciones
    @Query("SELECT * FROM edificacion")
    List<Edificacion> getAllEdificaciones();

    // Método para eliminar todas las edificaciones
    @Query("DELETE FROM edificacion")
    void deleteAllEdificaciones();

    @Query("SELECT * FROM edificacion WHERE EdId = :edId LIMIT 1")
    Edificacion getEdificacionById(int edId);

}
