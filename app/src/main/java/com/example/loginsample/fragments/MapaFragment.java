package com.example.loginsample.fragments;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.example.loginsample.R;
import com.example.loginsample.data.AppDatabase;
import com.example.loginsample.data.entity.Edificacion;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapaFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private SearchView searchView;
    private AppDatabase database;

    public MapaFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Inicializamos la base de datos de Room
        database = Room.databaseBuilder(getContext(), AppDatabase.class, "edificacion_database")
                .allowMainThreadQueries() // Permite consultas en el hilo principal, optimízalo con AsyncTask o LiveData más adelante
                .build();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mapa, container, false);

        // Inicializa el SupportMapFragment y notifica cuando el mapa está listo.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        // Inicializa el SearchView
        searchView = view.findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Aquí puedes manejar la búsqueda
                buscarUbicacion(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Cargar las edificaciones desde la base de datos
        loadEdificacionesFromDatabase();

        // Centra la cámara en una ubicación general de Arequipa
        LatLng arequipaCenter = new LatLng(-16.4090474, -71.537451);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(arequipaCenter, 13));
    }

    private void loadEdificacionesFromDatabase() {
        List<Edificacion> edificacionList = database.edificacionDAO().getAllEdificaciones();

        for (Edificacion edificacion : edificacionList) {
            try {
                Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
                List<Address> addresses = geocoder.getFromLocationName(edificacion.getEdNom() + " Arequipa", 1);

                if (addresses != null && !addresses.isEmpty()) {
                    Address address = addresses.get(0);
                    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());

                    // Agrega un marcador en el mapa
                    mMap.addMarker(new MarkerOptions()
                            .position(latLng)
                            .title(edificacion.getEdNom())
                            .snippet(edificacion.getEdDes()));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void buscarUbicacion(String location) {
        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocationName(location, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                mMap.clear(); // Limpia el mapa de marcadores anteriores
                mMap.addMarker(new MarkerOptions().position(latLng).title(location));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15)); // Ajusta el zoom según sea necesario
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
