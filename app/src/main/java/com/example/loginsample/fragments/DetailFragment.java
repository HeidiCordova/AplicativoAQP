package com.example.loginsample.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.loginsample.Comment;
import com.example.loginsample.CommentAdapter;
import com.example.loginsample.R;

import java.util.ArrayList;
import java.util.List;

public class DetailFragment extends Fragment {
    private static final String ARG_BUILDING_ID = "building_id";
    private int buildingId; // Identificador para el edificio seleccionado
    private RecyclerView commentsRecyclerView;
    private CommentAdapter commentAdapter;
    private List<Comment> commentList;
    private Button btnView360;
    private String title;
    private String description;
    private int imageResId;
    private Button btnViewMansion;
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

        return view;
    }

    private void loadBuildingData(int buildingId, ImageView imageView, TextView titleTextView, TextView descriptionTextView) {
        // Ejemplo de datos estáticos
        if (buildingId == 0) {
            titleTextView.setText("Catedral");
            descriptionTextView.setText("Santuario principal de la ciudad ocupando el lado norte de la Plaza de Armas.");
            imageView.setImageResource(R.drawable.catedral);
        } else if (buildingId == 1) {
            titleTextView.setText("Mansión del Fundador");
            descriptionTextView.setText("La Mansión del Fundador es una histórica casona colonial de Arequipa, conocida por su arquitectura de sillar y su rica herencia cultural y artística.");
            imageView.setImageResource(R.drawable.ingreso);
        } else if (buildingId == 2) {
            titleTextView.setText("Monasterio de Santa Catalina");
            descriptionTextView.setText("Este complejo turístico fue fundado en 1579.");
            imageView.setImageResource(R.drawable.monasterio);
        }
        else if (buildingId == 3) {
            titleTextView.setText("Molino de Sabandia");
            descriptionTextView.setText("Una construcción colonial donde se molían trigo y maíz.");
            imageView.setImageResource(R.drawable.molino);
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