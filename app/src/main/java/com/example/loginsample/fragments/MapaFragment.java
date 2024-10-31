package com.example.loginsample.fragments;

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
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
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

        // Agregar un marcador y mover la cámara
        LatLng location = new LatLng(-16.40446, -71.52454); // Cambia las coordenadas a las que desees
        mMap.addMarker(new MarkerOptions().position(location).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
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

}