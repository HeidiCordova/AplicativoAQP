package com.example.loginsample.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.loginsample.Comment;
import com.example.loginsample.CommentAdapter;
import com.example.loginsample.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class DetailFragment extends Fragment {
    private RecyclerView commentsRecyclerView;
    private CommentAdapter commentAdapter;
    private List<Comment> commentList;
    private String title;
    private String description;
    private int imageResId;

    public static DetailFragment newInstance(String title, String description, int imageResId) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("description", description);
        args.putInt("imageResId", imageResId);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        TextView titleTextView = view.findViewById(R.id.title_text_view);
        TextView descriptionTextView = view.findViewById(R.id.description_text_view);
        ImageView imageView = view.findViewById(R.id.image_view);
        EditText commentInput = view.findViewById(R.id.comment_input);
        RatingBar ratingBar = view.findViewById(R.id.rating_bar);
        RatingBar averageRatingBar = view.findViewById(R.id.average_rating_bar);
        TextView averageRateTextView = view.findViewById(R.id.average_rate);
        Button submitCommentButton = view.findViewById(R.id.submit_comment_button);
        commentsRecyclerView = view.findViewById(R.id.comments_recycler_view);
        Button btnViewMansion = view.findViewById(R.id.btn_view_mansion);

        titleTextView.setText(title);
        descriptionTextView.setText(description);
        imageView.setImageResource(imageResId);
        // Añadir listener para redirigir al MansionFragment
        btnViewMansion.setOnClickListener(v -> {
            MansionFragment mansionFragment = new MansionFragment();
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainerView, mansionFragment)
                    .addToBackStack(null)
                    .commit();
        });
        // Configuración del RecyclerView para comentarios
        commentList = new ArrayList<>();
        commentAdapter = new CommentAdapter(commentList);
        commentsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        commentsRecyclerView.setAdapter(commentAdapter);

        // Cargar los comentarios desde un archivo .txt
        loadCommentsFromFile();

        // Calcular el promedio redondeado y actualizar el RatingBar y TextView
        float averageRating = calculateAndRoundAverageRating(commentList);
        averageRatingBar.setRating(averageRating);  // Actualizar el RatingBar
        averageRateTextView.setText(String.format("%.1f", averageRating));  // Actualizar el texto

        // Añadir funcionalidad para agregar un nuevo comentario
        submitCommentButton.setOnClickListener(v -> {
            String commentText = commentInput.getText().toString();
            int rating = (int) ratingBar.getRating(); // Cambiar a 'ratingBar' en lugar de 'averageRatingBar'

            // Asegurarse de que el comentario no esté vacío y tenga una calificación válida
            if (!commentText.isEmpty() && rating > 0) {
                String username = "admin"; // Aquí deberías obtener el nombre del usuario logueado

                // Verificar si el usuario ya ha comentado
                if (!hasUserCommented(username)) {
                    Comment newComment = new Comment(username, commentText, rating);
                    commentList.add(newComment);
                    commentAdapter.notifyItemInserted(commentList.size() - 1);

                    // Guardar el comentario en el archivo
                    saveCommentToFile(username, commentText, rating);

                    // Recalcular el promedio después de agregar el comentario
                    float newAverageRating = calculateAndRoundAverageRating(commentList);
                    averageRatingBar.setRating(newAverageRating);  // Actualizar 'averageRatingBar' con el nuevo promedio
                    averageRateTextView.setText(String.format("%.1f", newAverageRating));

                    // Limpiar los campos de entrada
                    commentInput.setText("");
                    ratingBar.setRating(0); // Restablecer 'ratingBar' para el próximo comentario
                } else {
                    Toast.makeText(getContext(), "Ya has comentado este lugar.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    private void loadCommentsFromFile() {
        try {
            // Acceder al archivo en 'assets' usando getAssets()
            InputStream inputStream = getContext().getAssets().open("comments.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                // Ahora usamos ";" como delimitador
                String[] parts = line.split(";");
                if (parts.length == 3) {
                    String username = parts[0].trim();  // Nombre de usuario
                    String text = parts[1].trim();      // Texto del comentario
                    int rating = Integer.parseInt(parts[2].trim());  // Calificación

                    // Crear el comentario y agregarlo a la lista
                    Comment comment = new Comment(username, text, rating);
                    commentList.add(comment);
                }
            }
            commentAdapter.notifyDataSetChanged();  // Actualiza el RecyclerView
            reader.close();  // Cierra el BufferedReader
        } catch (IOException e) {
            Log.e("DetailFragment", "Error al leer el archivo de comentarios", e);
            Toast.makeText(getContext(), "Error al cargar comentarios", Toast.LENGTH_SHORT).show();
        }
    }

    private float calculateAndRoundAverageRating(List<Comment> comments) {
        if (comments.isEmpty()) {
            return 0f;  // Si no hay comentarios, el promedio es 0
        }

        int totalRating = 0;
        for (Comment comment : comments) {
            totalRating += comment.getRating();
        }

        float average = (float) totalRating / comments.size();  // Calcula el promedio
        return Math.round(average * 2) / 2.0f;  // Redondear a 0.5 más cercano
    }

    // Método para verificar si el usuario ya ha comentado
    private boolean hasUserCommented(String username) {
        for (Comment comment : commentList) {
            if (comment.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    // Método para guardar el comentario en el archivo
    private void saveCommentToFile(String username, String commentText, int rating) {
        try {
            // Obtener acceso al archivo comments.txt
            File file = new File(getContext().getFilesDir(), "comments.txt");
            FileWriter fileWriter = new FileWriter(file, true);  // 'true' para agregar al final
            BufferedWriter writer = new BufferedWriter(fileWriter);

            // Escribir el comentario en el archivo, separado por ";"
            writer.write(username + ";" + commentText + ";" + rating);
            writer.newLine();

            writer.close();
        } catch (IOException e) {
            Log.e("DetailFragment", "Error al guardar el comentario", e);
            Toast.makeText(getContext(), "Error al guardar el comentario", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            title = getArguments().getString("title");
            description = getArguments().getString("description");
            imageResId = getArguments().getInt("imageResId");
        }
    }
}