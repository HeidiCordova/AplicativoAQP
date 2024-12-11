package com.example.loginsample;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private List<Comment> comentariosList;

    public CommentAdapter(List<Comment> comentariosList) {
        this.comentariosList = Collections.unmodifiableList(comentariosList);
    }

    @Override
    public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Infla el layout para cada item de comentario
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CommentViewHolder holder, int position) {
        // Asigna los datos del comentario a las vistas
        Comment comentario = comentariosList.get(position);
        holder.bind(comentario);
    }

    @Override
    public int getItemCount() {
        return comentariosList.size();
    }

    // ViewHolder para cada comentario
    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView comentarioTextView;
        TextView comentarioFechaTextView;

        public CommentViewHolder(View itemView) {
            super(itemView);
            comentarioTextView = itemView.findViewById(R.id.comment_text);
            comentarioFechaTextView = itemView.findViewById(R.id.comment_date);
        }

        public void bind(Comment comentario) {
            // Establece el texto del comentario
            comentarioTextView.setText(comentario.getText());
            // Establece la fecha del comentario (aquí está representada como un entero, ajusta según tus datos)
            comentarioFechaTextView.setText(String.valueOf(comentario.getRating()));
        }
    }
}

