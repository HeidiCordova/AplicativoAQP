package com.example.loginsample.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loginsample.Comment;
import com.example.loginsample.CommentAdapter;
import com.example.loginsample.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class CommentsSectionFragment extends Fragment {

    private ArrayList<Comment> commentList;
    private CommentAdapter commentAdapter;
    private RecyclerView recyclerView;
    private RatingBar ratingBarInput;
    private EditText commentInput;
    private TextView averageRatingText;
    private RatingBar averageRatingBar;
    private File commentsFile;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comments_section, container, false);

        // Inicializar elementos de la interfaz de usuario
        recyclerView = view.findViewById(R.id.recyclerView_comments);
        ratingBarInput = view.findViewById(R.id.ratingBar_input);
        commentInput = view.findViewById(R.id.editText_comment);
        Button addCommentButton = view.findViewById(R.id.button_add_comment);
        averageRatingText = view.findViewById(R.id.textView_average_rating);
        averageRatingBar = view.findViewById(R.id.ratingBar_average);
        Button backButton = view.findViewById(R.id.button_back);

        // Inicializar RecyclerView
        commentList = new ArrayList<>();
        commentAdapter = new CommentAdapter(commentList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(commentAdapter);

        // Configurar archivo de comentarios
        commentsFile = new File(getContext().getFilesDir(), "comments.txt");
        loadCommentsFromFile();
        updateAverageRating();

        // Funcionalidad del botón para agregar comentarios
        addCommentButton.setOnClickListener(v -> addComment());

        // Funcionalidad del botón de retroceso
        backButton.setOnClickListener(v -> getParentFragmentManager().popBackStack());

        return view;
    }

    private void loadCommentsFromFile() {
        if (!commentsFile.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(commentsFile))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] partes = linea.split(";");
                if (partes.length == 3) {
                    String nombreUsuario = partes[0];
                    String textoComentario = partes[1];
                    float calificacion = Float.parseFloat(partes[2]);
                    commentList.add(new Comment(nombreUsuario, textoComentario, (int) calificacion));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Error al cargar comentarios", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveCommentToFile(Comment comment) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(commentsFile, true))) {
            writer.write(comment.getUsername() + ";" + comment.getCommentText() + ";" + comment.getRating() + "\n");
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Error al guardar comentario", Toast.LENGTH_SHORT).show();
        }
    }

    private void addComment() {
        String textoComentario = commentInput.getText().toString().trim();
        float calificacion = ratingBarInput.getRating();

        if (TextUtils.isEmpty(textoComentario) || calificacion == 0) {
            Toast.makeText(getContext(), "Por favor, ingresa un comentario y una calificación", Toast.LENGTH_SHORT).show();
            return;
        }

        Comment nuevoComentario = new Comment("Usuario", textoComentario, (int) calificacion);
        commentList.add(nuevoComentario);
        commentAdapter.notifyItemInserted(commentList.size() - 1);
        saveCommentToFile(nuevoComentario);
        updateAverageRating();

        // Limpiar entradas
        commentInput.setText("");
        ratingBarInput.setRating(0);

        Toast.makeText(getContext(), "Comentario agregado", Toast.LENGTH_SHORT).show();
    }

    private void updateAverageRating() {
        if (commentList.isEmpty()) {
            averageRatingText.setText("Aún no hay calificaciones");
            averageRatingBar.setRating(0);
            return;
        }

        float totalCalificaciones = 0;
        for (Comment comentario : commentList) {
            totalCalificaciones += comentario.getRating();
        }

        float promedio = totalCalificaciones / commentList.size();
        averageRatingText.setText(String.format("Calificación promedio: %.1f", promedio));
        averageRatingBar.setRating(promedio);
    }
}
