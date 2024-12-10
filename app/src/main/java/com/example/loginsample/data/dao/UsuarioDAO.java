package com.example.loginsample.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.loginsample.data.entity.Usuario;

import java.util.List;

@Dao
public interface UsuarioDAO {
    @Insert
    long insertUsuario(Usuario usuario);

    @Query("SELECT * FROM usuarios WHERE UsId = :id")
    Usuario getUsuarioById(int id);

    @Query("SELECT * FROM usuarios")
    List<Usuario> getAllUsuarios();
}
