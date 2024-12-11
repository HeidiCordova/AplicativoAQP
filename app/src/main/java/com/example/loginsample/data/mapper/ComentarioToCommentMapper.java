package com.example.loginsample.data.mapper;

import com.example.loginsample.Comment;
import com.example.loginsample.data.entity.Comentario;
import java.util.ArrayList;
import java.util.List;

public class ComentarioToCommentMapper {

    public static List<Comment> convertirComentarios(List<Comentario> listaComentario) {
        List<Comment> comentariosConvertidos = new ArrayList<>();
        for (Comentario comentario : listaComentario) {
            // Convertimos cada Comentario a Comment
            comentariosConvertidos.add(new Comment(
                    "Usuario " + comentario.getUsId(), // Placeholder para el nombre del usuario
                    comentario.getComTex(),           // Texto del comentario
                    comentario.getComCal()            // Calificación del comentario
            ));
        }
        return comentariosConvertidos;
    }
}
