
package com.example.loginsample.utils;

import com.example.loginsample.AccountEntity;
import com.example.loginsample.data.entity.Usuario;

public class UsuarioLogueado {
    private static UsuarioLogueado instance;
    private AccountEntity usuario;

    // Constructor privado para Singleton
    private UsuarioLogueado() {}

    // Obtener la instancia única de UsuarioLogueado
    public static synchronized UsuarioLogueado getInstance() {
        if (instance == null) {
            instance = new UsuarioLogueado();
        }
        return instance;
    }

    // Establecer el usuario logueado
    public void setUsuario(AccountEntity usuario) {
        this.usuario = usuario;
    }

    // Obtener el nombre del usuario
    public String getNombreUsuario() {
        return usuario != null ? usuario.getNombre() : "Invitado";
    }

    // Obtener el ID del usuario
    public int getIdUsuario() {
        return usuario != null ? usuario.getId() : -1; // Devuelve -1 si no hay usuario logueado
    }

    // Método para limpiar el usuario (al cerrar sesión)
    public void clear() {
        usuario = null;
    }
}
