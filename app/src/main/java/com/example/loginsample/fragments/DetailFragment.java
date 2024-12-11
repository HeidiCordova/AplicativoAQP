package com.example.loginsample.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import androidx.room.Room;

import com.example.loginsample.Comment;
import com.example.loginsample.CommentAdapter;
import com.example.loginsample.R;
import com.example.loginsample.data.mapper.ComentarioToCommentMapper;
import com.example.loginsample.data.AppDatabase;
import com.example.loginsample.data.entity.Comentario;
import com.example.loginsample.data.entity.Edificacion;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class DetailFragment extends Fragment {
    private EditText commentEditText;  // Asegúrate de declarar la variable EditText
    private static final String ARG_BUILDING_ID = "building_id";
    private int buildingId;  // Identificador para el edificio seleccionado
    private RecyclerView commentsRecyclerView;
    private List<Comentario> commentList; // Lista para almacenar los comentarios
    private Button submitCommentButton;
    private EditText commentInput;

    // ID del usuario (debes obtenerlo de alguna parte, por ejemplo de la sesión del usuario)
    private int userId = 1;  // Cambia esto por el ID real del usuario logueado

    public static DetailFragment newInstance(String title, String description, int imageResId, int buildingId) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_BUILDING_ID, buildingId);  // Se pasa el ID del edificio
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            buildingId = getArguments().getInt(ARG_BUILDING_ID);  // Recuperamos el ID del edificio
        }

        Log.d("DetailFragment", "Building ID recibido: " + buildingId);

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        // Recupera el ID del edificio desde los argumentos
        int buildingId = getArguments().getInt(ARG_BUILDING_ID);
        Log.d("DetailFragment", "Building ID recibido: " + buildingId);

        // Crear un Executor para manejar la consulta en segundo plano
        Executor executor = Executors.newSingleThreadExecutor();

        // Realiza la consulta de la base de datos en un hilo en segundo plano
        executor.execute(new Runnable() {
            @Override
            public void run() {
                // Consulta la base de datos para obtener los detalles del edificio
                final Edificacion selectedEdificacion = AppDatabase.getDatabase(getContext()).edificacionDAO().getEdificacionById(buildingId);

                // Aseguramos que estamos en el hilo principal para actualizar la UI
                if (getActivity() != null && isAdded()) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (selectedEdificacion != null) {
                                Log.d("DetailFragment", "Edificio encontrado: " + selectedEdificacion.getEdNom());

                                // Actualiza la UI con los datos del edificio
                                TextView titleTextView = view.findViewById(R.id.title_text_view);
                                TextView descriptionTextView = view.findViewById(R.id.description_text_view);
                                ImageView buildingImageView = view.findViewById(R.id.image_view);

                                titleTextView.setText(selectedEdificacion.getEdNom());
                                descriptionTextView.setText(selectedEdificacion.getEdDes());

                                // Carga la imagen del edificio
                                int imageResourceId = selectedEdificacion.getEdIma();
                                Log.d("DetailFragment", "Imagen ID: " + imageResourceId);
                                if (imageResourceId != 0) { // Verifica si el ID de la imagen es válido
                                    buildingImageView.setImageResource(imageResourceId);
                                } else {
                                    buildingImageView.setImageResource(R.drawable.colca);  // Imagen por defecto
                                }
                            } else {
                                Log.d("DetailFragment", "Edificio no encontrado");
                                Toast.makeText(getContext(), "Edificio no encontrado", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Log.d("DetailFragment", "Fragment no está adjunto a la actividad");
                }
            }
        });

        return view;
    }





    private void submitComment() {
        // Obtenemos el texto del comentario
        String commentText = commentEditText.getText().toString();

        // Verificamos que el texto no esté vacío
        if (!commentText.isEmpty()) {
            // Usamos Executor para realizar la verificación en segundo plano
            Executor executor = Executors.newSingleThreadExecutor();
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    // Verificamos si el Edificio y Usuario existen
                    boolean buildingExists = buildingIdExists(buildingId);
                    boolean userExists = userIdExists(userId);

                    // Solo procedemos si ambos existen
                    if (buildingExists && userExists) {
                        // Creamos el nuevo comentario
                        Comentario newComment = new Comentario();
                        newComment.setEdId(buildingId);  // Asegúrate de que 'buildingId' esté correctamente configurado
                        newComment.setUsId(userId);      // Asegúrate de que 'userId' esté correctamente configurado
                        newComment.setComTex(commentText);

                        // Inserta el comentario en segundo plano
                        AppDatabase.getDatabase(getContext()).comentarioDAO().insertComentario(newComment);

                        // Después de la inserción, actualizamos la UI en el hilo principal
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // Limpiamos el campo de texto del comentario
                                commentEditText.setText("");

                                // Volvemos a cargar los comentarios
                                loadComments();
                            }
                        });
                    } else {
                        // Mostrar un mensaje de error si las claves foráneas no existen
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getContext(), "El edificio o el usuario no existen", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });
        } else {
            // Mostrar un mensaje de error si el comentario está vacío
            Toast.makeText(getContext(), "Por favor ingrese un comentario", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean buildingIdExists(int buildingId) {
        // Verifica si el Edificio existe en la base de datos (ahora en segundo plano)
        return AppDatabase.getDatabase(getContext()).edificacionDAO().getEdificacionById(buildingId) != null;
    }

    private boolean userIdExists(int userId) {
        // Verifica si el Usuario existe en la base de datos (ahora en segundo plano)
        return AppDatabase.getDatabase(getContext()).usuarioDAO().getUsuarioById(userId) != null;
    }




    private void loadComments() {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            // Obtener comentarios desde la base de datos
            List<Comentario> listaComentario = AppDatabase.getDatabase(getContext())
                    .comentarioDAO()
                    .getComentariosByEdificacion(buildingId);

            // Convertir Comentario a Comment
            List<Comment> comentarios = ComentarioToCommentMapper.convertirComentarios(listaComentario);

            // Actualizar la UI en el hilo principal
            getActivity().runOnUiThread(() -> {
                CommentAdapter adapter = new CommentAdapter(comentarios);
                commentsRecyclerView.setAdapter(adapter);
            });
        });
    }
}
