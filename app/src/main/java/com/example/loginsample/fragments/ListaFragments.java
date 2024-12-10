package com.example.loginsample.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import androidx.room.Room;
import com.example.loginsample.R;
import com.example.loginsample.data.entity.Edificacion;
import com.example.loginsample.data.AppDatabase;
import com.example.loginsample.EdificacionAdapter;

import java.util.ArrayList;
import java.util.List;
public class ListaFragments extends Fragment {
    private RecyclerView recyclerView;
    private EdificacionAdapter edificacionAdapter;
    private List<Edificacion> edificacionList;
    private List<Edificacion> filteredEdificacionList;
    private EditText searchBar;
    private AppDatabase database;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lista, container, false);

        // Inicializamos la base de datos de Room
        database = Room.databaseBuilder(getContext(), AppDatabase.class, "edificacion_database")
                .allowMainThreadQueries() // Permite consultas en el hilo principal, optimízalo con AsyncTask o LiveData más adelante
                .build();

        // Inicializamos el RecyclerView y el adaptador
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        // Inicialización de listas
        edificacionList = new ArrayList<>();
        filteredEdificacionList = new ArrayList<>();

        // Configuración del adaptador
        edificacionAdapter = new EdificacionAdapter(filteredEdificacionList, new EdificacionAdapter.OnEdificacionClickListener() {
            @Override
            public void onEdificacionClick(int position) {
                Log.d("EdificacionesFragmentINNER", "Edificación seleccionada en la posición: " + position);
                Edificacion selectedEdificacion = filteredEdificacionList.get(position);

                DetailFragment detailFragment = DetailFragment.newInstance(
                        selectedEdificacion.getEdNom(),
                        selectedEdificacion.getEdDes(),
                        selectedEdificacion.getEdIma(),
                        selectedEdificacion.getEdId() // Pasa el ID del edificio
                );


                // Navegar al fragmento de detalle
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragmentContainerView, detailFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        recyclerView.setAdapter(edificacionAdapter);

        // Cargar los datos de la base de datos
        loadEdificacionesFromDatabase();

        // Configuración del campo de búsqueda
        searchBar = view.findViewById(R.id.search_bar);
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No implementado
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // No implementado
            }
        });

        return view;
    }

    // Método para cargar las edificaciones desde la base de datos
    private void loadEdificacionesFromDatabase() {
        List<Edificacion> storedEdificaciones = database.edificacionDAO().getAllEdificaciones();
        edificacionList.addAll(storedEdificaciones);
        Log.d("DetailFragment", ""+edificacionList.get(0).getEdIma());





        filteredEdificacionList.addAll(storedEdificaciones);
        // Asegúrate de notificar al adaptador que los datos han cambiado
        if (edificacionAdapter != null) {
            edificacionAdapter.notifyDataSetChanged();
        }
    }

    // Método de filtro
    private void filter(String text) {
        filteredEdificacionList.clear();
        if (text.isEmpty()) {
            filteredEdificacionList.addAll(edificacionList);
        } else {
            for (Edificacion edificacion : edificacionList) {
                if (edificacion.getEdNom().toLowerCase().contains(text.toLowerCase())) {
                    filteredEdificacionList.add(edificacion);
                }
            }
        }
        // Notificar al adaptador que los datos han cambiado
        if (edificacionAdapter != null) {
            edificacionAdapter.notifyDataSetChanged();
        }
    }
}
