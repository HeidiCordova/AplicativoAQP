package com.example.loginsample.data;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.loginsample.data.dao.ComentarioDAO;
import com.example.loginsample.data.dao.EdificacionDAO;
import com.example.loginsample.data.dao.UsuarioDAO;
import com.example.loginsample.data.entity.Comentario;
import com.example.loginsample.data.entity.Edificacion;
import com.example.loginsample.data.entity.Usuario;



@Database(entities = {Edificacion.class, Comentario.class, Usuario.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;

    public abstract EdificacionDAO edificacionDAO();
    public abstract UsuarioDAO usuarioDAO();
    public abstract ComentarioDAO comentarioDAO();  // Aquí agregamos el DAO para Comentario

    public static synchronized AppDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "edificacion_database")
                    .build();
        }
        return INSTANCE;
    }
}
