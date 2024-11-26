package com.example.loginsample.fragments;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loginsample.Comment;
import com.example.loginsample.CommentAdapter;
import com.example.loginsample.R;
import com.example.loginsample.data.AppDatabase;
import com.example.loginsample.data.dao.ComentarioDAO;
import com.example.loginsample.data.dao.EdificacionDAO;
import com.example.loginsample.data.dao.UsuarioDAO;
import com.example.loginsample.data.entity.Comentario;
import com.example.loginsample.data.entity.Edificacion;
import com.example.loginsample.data.entity.Usuario;
import com.example.loginsample.data.relations.ComentarioConUsuario;
import com.example.loginsample.utils.UsuarioLogueado;

import java.util.ArrayList;
import java.util.List;

public class CommentsSectionFragment extends Fragment {
    private RecyclerView commentsRecyclerView;
    private CommentAdapter commentAdapter;
    private List<Comment> commentList;
    private EditText commentInput;
    private RatingBar ratingBar;
    private RatingBar averageRatingBar;
    private TextView averageRateTextView;
    private ComentarioDAO comentarioDAO;
    private EdificacionDAO edificacionDAO;
    private UsuarioDAO usuarioDAO;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comments_section, container, false);

        AppDatabase db = AppDatabase.getInstance(getContext());
        usuarioDAO = db.usuarioDAO();
        comentarioDAO = db.comentarioDAO();
        edificacionDAO = db.edificacionDAO();

        // Inicializar componentes
        commentInput = view.findViewById(R.id.comment_input);
        ratingBar = view.findViewById(R.id.rating_bar);
        averageRatingBar = view.findViewById(R.id.average_rating_bar);
        averageRateTextView = view.findViewById(R.id.average_rate_text_view);
        Button submitCommentButton = view.findViewById(R.id.submit_comment_button);
        commentsRecyclerView = view.findViewById(R.id.comments_recycler_view);
        Button btnBackToDetail = view.findViewById(R.id.btn_back_to_detail);

        // Configuración del RecyclerView
        commentList = new ArrayList<>();
        commentAdapter = new CommentAdapter(commentList);
        commentsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        commentsRecyclerView.setAdapter(commentAdapter);

        // Obtener ID de la edificación desde los argumentos
        Bundle args = getArguments();
        int edificacionId = args != null ? args.getInt("EdificacionId", -1) : -1;
        // Registro de depuración
        Log.d("DEBUG", "EdificacionId recibido: " + edificacionId);
        if (edificacionId == -1) {
            Log.e("ERROR", "No se proporcionó un ID de edificación.");
            return view;
        }

        // Cargar comentarios desde la base de datos
        loadCommentsFromDatabase(edificacionId);

        // Calcular y mostrar el promedio de calificación
        calculateAndDisplayAverageRating(edificacionId);

        // Funcionalidad para agregar un nuevo comentario
        submitCommentButton.setOnClickListener(v -> {
            String commentText = commentInput.getText().toString();
            int rating = (int) ratingBar.getRating();

            if (!commentText.isEmpty() && rating > 0) {
                String username = UsuarioLogueado.getInstance().getNombreUsuario(); // Obtener el nombre del usuario logueado
                addCommentToDatabase(username, commentText, rating, edificacionId);

                // Limpiar campos de entrada
                commentInput.setText("");
                ratingBar.setRating(0);

                // Recalcular el promedio
                calculateAndDisplayAverageRating(edificacionId);
            } else {
                Toast.makeText(getContext(), "Por favor, ingresa un comentario y calificación válida.", Toast.LENGTH_SHORT).show();
            }
        });

        // Regresar al fragmento anterior
        btnBackToDetail.setOnClickListener(v -> getParentFragmentManager().popBackStack());

        return view;
    }
// Métodos relevantes actualizados del fragmento

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void addCommentToDatabase(String username, String commentText, int rating, int edificacionId) {
        new Thread(() -> {
            Comentario comentario = new Comentario();
            comentario.ComTex = commentText;
            comentario.ComCal = rating;
            comentario.UsId = UsuarioLogueado.getInstance().getIdUsuario(); // ID del usuario logueado
            comentario.EdId = edificacionId; // ID de la edificación
            comentario.ComFec = java.time.LocalDate.now().toString();

            try {
                // Validar claves foráneas
                Usuario usuario = usuarioDAO.getUsuarioById(comentario.UsId);
                Edificacion edificacion = edificacionDAO.getEdificacionById(edificacionId);

                if (usuario == null || edificacion == null) {
                    String errorMessage = (usuario == null)
                            ? "Usuario no válido."
                            : "Edificación no válida.";
                    getActivity().runOnUiThread(() ->
                            Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show()
                    );
                    return;
                }

                comentarioDAO.insertComentario(comentario);

                // Actualizar la UI
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        Comment newComment = new Comment(username, commentText, rating);
                        commentList.add(newComment);
                        commentAdapter.notifyItemInserted(commentList.size() - 1);
                    });
                }
            } catch (Exception e) {
                Log.e("ERROR", "Error al insertar comentario: " + e.getMessage(), e);
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() ->
                            Toast.makeText(getContext(), "Error al guardar el comentario.", Toast.LENGTH_SHORT).show()
                    );
                }
            }
        }).start();
    }

    private void loadCommentsFromDatabase(int edificacionId) {
        if (edificacionId == -1) {
            Log.e("ERROR", "ID de edificación inválido al cargar comentarios.");
            return;
        }

        new Thread(() -> {
            try {
                List<ComentarioConUsuario> comentariosConUsuarios = comentarioDAO.getComentariosConUsuarios(edificacionId);
                List<Comment> newComments = new ArrayList<>();

                for (ComentarioConUsuario comentarioConUsuario : comentariosConUsuarios) {
                    Comment comment = new Comment(
                            comentarioConUsuario.usuario.getNombre(),
                            comentarioConUsuario.comentario.getComTex(),
                            comentarioConUsuario.comentario.getComCal()
                    );
                    newComments.add(comment);
                }

                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        commentList.clear();
                        commentList.addAll(newComments);
                        commentAdapter.notifyDataSetChanged();
                    });
                }
            } catch (Exception e) {
                Log.e("ERROR", "Error al cargar comentarios: " + e.getMessage(), e);
            }
        }).start();
    }

    private void calculateAndDisplayAverageRating(int edificacionId) {
        if (edificacionId == -1) {
            Log.e("ERROR", "ID de edificación inválido al calcular el promedio de calificación.");
            return;
        }

        new Thread(() -> {
            try {
                List<Comentario> comentarios = comentarioDAO.getComentariosByEdificacion(edificacionId);
                int totalRating = 0;

                for (Comentario comentario : comentarios) {
                    totalRating += comentario.ComCal;
                }

                float averageRating = comentarios.isEmpty() ? 0f : (float) totalRating / comentarios.size();

                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        averageRatingBar.setRating(averageRating);
                        averageRateTextView.setText(String.format("%.1f", averageRating));
                    });
                }
            } catch (Exception e) {
                Log.e("ERROR", "Error al calcular promedio: " + e.getMessage(), e);
            }
        }).start();
    }
}