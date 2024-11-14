package com.example.loginsample.fragments;

import java.util.ArrayList;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.example.loginsample.Building;
import com.example.loginsample.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MapaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapaFragment extends Fragment implements OnMapReadyCallback  {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private GoogleMap mMap;
    private SearchView searchView;

    private List<Building> buildingList; // Declara la lista de edificios


    public MapaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MapaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MapaFragment newInstance(String param1, String param2) {
        MapaFragment fragment = new MapaFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inicializa la lista de edificios
        buildingList = new ArrayList<>();
        buildingList.add(new Building("Catedral", "Santuario principal de la ciudad ocupando el lado norte de la Plaza de Armas", R.drawable.catedral));
        buildingList.add(new Building("Mansión del Fundador", "Histórica casona colonial de Arequipa, conocida por su arquitectura de sillar y rica herencia cultural y artística.", R.drawable.ingreso));
        buildingList.add(new Building("Monasterio de Santa Catalina", "Una pequeña ciudadela que ocupa un área de 20 mil metros cuadrados", R.drawable.monasterio));
        buildingList.add(new Building("Molino de Sabandía", "Construcción colonial donde se molían trigo y maíz", R.drawable.molino));
        buildingList.add(new Building("Mirador de Yanahuara", "Ofrece una vista panorámica de Arequipa y sus volcanes, rodeado de arcos de sillar con inscripciones poéticas.", R.drawable.yanahuara));
        buildingList.add(new Building("Plaza de Armas", "Centro histórico de la ciudad rodeado de impresionantes edificios coloniales y la catedral.", R.drawable.plaza_armas));
        buildingList.add(new Building("Museo Santuarios Andinos", "Hogar de la momia Juanita, una momia Inca congelada encontrada en el volcán Ampato.", R.drawable.santuarios_andinos));
        buildingList.add(new Building("Iglesia de la Compañía", "Templo jesuita del siglo XVII, con una impresionante fachada barroca tallada en sillar.", R.drawable.compania));
        buildingList.add(new Building("Cañón del Colca", "Uno de los cañones más profundos del mundo, famoso por sus vistas y observación de cóndores.", R.drawable.colca));
        buildingList.add(new Building("Baños Termales de Yura", "Aguas termales naturales conocidas por sus propiedades terapéuticas y relajantes.", R.drawable.banos_yura));
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

        // Configura el adaptador personalizado de la ventana de información
        mMap.setInfoWindowAdapter(new ventanaInformacion(getLayoutInflater()));

        // Itera sobre la lista de lugares y agrega un marcador para cada uno
        for (Building building : buildingList) {
            try {
                Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
                List<Address> addresses = geocoder.getFromLocationName(building.getTitle() + " Arequipa", 1);

                if (addresses != null && !addresses.isEmpty()) {
                    Address address = addresses.get(0);
                    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());

                    // Agrega un marcador en el mapa
                    mMap.addMarker(new MarkerOptions()
                            .position(latLng)
                            .title(building.getTitle())
                            .snippet(building.getDescription()));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Centra la cámara en una ubicación general de Arequipa
        LatLng arequipaCenter = new LatLng(-16.4090474, -71.537451);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(arequipaCenter, 13));
    }


    private void buscarUbicacion(String location) {
        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocationName(location, 1);
            if (addresses != null && !addresses.isEmpty()) {
                android.location.Address address = addresses.get(0);
                LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                mMap.clear(); // Limpia el mapa de marcadores anteriores
                mMap.addMarker(new MarkerOptions().position(latLng).title(location));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15)); // Ajusta el zoom según sea necesario
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public class ventanaInformacion implements GoogleMap.InfoWindowAdapter {

        private final View mWindow;

        public ventanaInformacion(LayoutInflater inflater) {
            // Infla el layout personalizado de la ventana de información
            mWindow = inflater.inflate(R.layout.custom_info_window, null);
        }

        private void renderWindowText(Marker marker, View view) {
            TextView title = view.findViewById(R.id.title);
            TextView description = view.findViewById(R.id.description);

            title.setText(marker.getTitle());
            description.setText(marker.getSnippet());
        }

        @Override
        public View getInfoWindow(Marker marker) {
            renderWindowText(marker, mWindow);
            return mWindow;
        }

        @Override
        public View getInfoContents(Marker marker) {
            return null;
        }
    }



}