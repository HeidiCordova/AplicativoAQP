package com.example.loginsample.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.loginsample.R;
import java.util.Arrays;
import java.util.List;

public class ListaFragments extends Fragment {

    private RecyclerView recyclerView;
    private SimpleAdapter adapter;

    public ListaFragments() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_lista, container, false);

        // Inicializar RecyclerView
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Lista de sitios turísticos de Arequipa
        List<String> touristSites = Arrays.asList(
                "Monasterio de Santa Catalina",
                "Catedral de Arequipa",
                "Plaza de Armas",
                "Mundo Alpaca",
                "Mirador de Yanahuara"
        );

        // Configurar el adaptador con la lista
        adapter = new SimpleAdapter(touristSites);
        recyclerView.setAdapter(adapter);

        return view;
    }

    // Adaptador simple
    private class SimpleAdapter extends RecyclerView.Adapter<SimpleAdapter.ViewHolder> {

        private List<String> data;

        public SimpleAdapter(List<String> data) {
            this.data = data;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.textView.setText(data.get(position));
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView textView;

            public ViewHolder(View itemView) {
                super(itemView);
                textView = itemView.findViewById(android.R.id.text1);
            }
        }
    }
}
