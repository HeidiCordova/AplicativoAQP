package com.example.loginsample.fragments;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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


        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        buildingList = new ArrayList<>();
        buildingList.add(new Building("Catedral", "Santuario principal de la ciudad ocupando el lado norte de la Plaza de Armas", R.drawable.catedral));
        buildingList.add(new Building("Mansión del Fundador", "La Mansión del Fundador es una histórica casona colonial de Arequipa, conocida por su arquitectura de sillar y su rica herencia cultural y artística.", R.drawable.ingreso));
        buildingList.add(new Building("Monasterio de Santa Catalina", "Una pequeña ciudadela que ocupa un área de 20 mil metros cuadrados", R.drawable.monasterio));
        buildingList.add(new Building("Molino de Sabandía", "Una construcción colonial donde se molían trigo y maíz", R.drawable.molino));


        buildingAdapter = new BuildingAdapter(buildingList ,  new BuildingAdapter.OnBuildingClickListener() {
            @Override
            public void onBuildingClick(int position) {
                Log.d("EdificacionesFragmentINNER", "Edificación seleccionada en la posición: " + position);
                Log.v("EdificacionesFragmentINNER", "SE LLAMO AL METODO DE ABAJO: " + position);

                Building selectedBuilding = buildingList.get(position);

                int buildingId = position;
                Log.d("EdificacionesFragmentINNER", "SE COGIO EL ID  DE LA EDIFICACION " + position);

                DetailFragment detailFragment = DetailFragment.newInstance(buildingId);
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();

                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragmentContainerView, detailFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        recyclerView.setAdapter(buildingAdapter);

        return view;
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
