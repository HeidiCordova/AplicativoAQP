package com.example.loginsample.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.loginsample.R;

public class DetailFragment extends Fragment {
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
        Button btnViewMansion = view.findViewById(R.id.btn_view_mansion);
        Button btnViewComments = view.findViewById(R.id.btn_view_comments);

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

        // Añadir listener para redirigir al CommentsSectionFragment
        btnViewComments.setOnClickListener(v -> {
            CommentsSectionFragment commentsFragment = new CommentsSectionFragment();
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainerView, commentsFragment) // Verifica que este sea el ID correcto
                    .addToBackStack(null)
                    .commit();
        });

        return view;
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