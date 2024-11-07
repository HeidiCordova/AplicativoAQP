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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loginsample.Building;
import com.example.loginsample.BuildingAdapter;
import com.example.loginsample.R;

import java.util.ArrayList;
import java.util.List;

public class ListaFragments extends Fragment {
    private String mParam1;
    private String mParam2;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private RecyclerView recyclerView;
    private BuildingAdapter buildingAdapter;
    private List<Building> buildingList;
    private List<Building> filteredBuildingList;
    private EditText searchBar;

    public static ListaFragments newInstance(String param1, String param2) {
        ListaFragments fragment = new ListaFragments();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_lista, container, false);

        // Configuración del RecyclerView
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Inicialización de listas
        buildingList = new ArrayList<>();
        filteredBuildingList = new ArrayList<>();

        // Agregar edificaciones
        buildingList.add(new Building("Catedral", "Santuario principal de la ciudad ocupando el lado norte de la Plaza de Armas", R.drawable.catedral));
        buildingList.add(new Building("Mansión del Fundador", "La Mansión del Fundador es una histórica casona colonial de Arequipa, conocida por su arquitectura de sillar y su rica herencia cultural y artística.", R.drawable.ingreso));
        buildingList.add(new Building("Monasterio de Santa Catalina", "Una pequeña ciudadela que ocupa un área de 20 mil metros cuadrados", R.drawable.monasterio));
        buildingList.add(new Building("Molino de Sabandía", "Una construcción colonial donde se molían trigo y maíz", R.drawable.molino));

        // Crear una copia de buildingList para mantener los datos originales
        filteredBuildingList.addAll(buildingList);

        // Configuración del adaptador
        buildingAdapter = new BuildingAdapter(filteredBuildingList, new BuildingAdapter.OnBuildingClickListener() {
            @Override
            public void onBuildingClick(int position) {
                Log.d("EdificacionesFragmentINNER", "Edificación seleccionada en la posición: " + position);

                Building selectedBuilding = filteredBuildingList.get(position);
                int buildingId = buildingList.indexOf(selectedBuilding);

                DetailFragment detailFragment = DetailFragment.newInstance(buildingId);
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();

                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragmentContainerView, detailFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        recyclerView.setAdapter(buildingAdapter);

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

    // Método de filtro
    private void filter(String text) {
        filteredBuildingList.clear();
        if (text.isEmpty()) {
            filteredBuildingList.addAll(buildingList);
        } else {
            for (Building building : buildingList) {
                if (building.getTitle().toLowerCase().contains(text.toLowerCase())) {
                    filteredBuildingList.add(building);
                }
            }
        }
        buildingAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
}
