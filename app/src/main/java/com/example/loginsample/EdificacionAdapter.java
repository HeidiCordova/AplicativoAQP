package com.example.loginsample;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.loginsample.data.entity.Edificacion;

import java.util.List;

public class EdificacionAdapter extends RecyclerView.Adapter<EdificacionAdapter.EdificacionViewHolder> {

    private List<Edificacion> edificacionList;
    private OnEdificacionClickListener onEdificacionClickListener;

    public EdificacionAdapter(List<Edificacion> edificacionList, OnEdificacionClickListener onEdificacionClickListener) {
        this.edificacionList = edificacionList;
        this.onEdificacionClickListener = onEdificacionClickListener;
    }

    @Override
    public EdificacionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_building, parent, false);
        return new EdificacionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EdificacionViewHolder holder, int position) {
        Edificacion edificacion = edificacionList.get(position);
        holder.titleTextView.setText(edificacion.getEdNom());
        holder.descriptionTextView.setText(edificacion.getEdDes());
        holder.imageView.setImageResource(edificacion.getEdIma());
    }

    @Override
    public int getItemCount() {
        return edificacionList.size();
    }

    public interface OnEdificacionClickListener {
        void onEdificacionClick(int position);
    }

    class EdificacionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView titleTextView;
        TextView descriptionTextView;
        ImageView imageView;

        public EdificacionViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.building_title);
            descriptionTextView = itemView.findViewById(R.id.building_description);
            imageView = itemView.findViewById(R.id.building_image);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onEdificacionClickListener.onEdificacionClick(getAdapterPosition());
        }
    }
}
