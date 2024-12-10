package com.example.loginsample;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loginsample.R;
import com.example.loginsample.data.entity.Comentario;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private List<Comentario> comentariosList;

    public CommentAdapter(List<Comentario> comentariosList) {
        this.comentariosList = comentariosList;
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
        Comentario comentario = comentariosList.get(position);
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

        public void bind(Comentario comentario) {
            // Establece el texto del comentario
            comentarioTextView.setText(comentario.ComTex);
            // Establece la fecha del comentario
            comentarioFechaTextView.setText(comentario.ComFec);
        }
    }
}
