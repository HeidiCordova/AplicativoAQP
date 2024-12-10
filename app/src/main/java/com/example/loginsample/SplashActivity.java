package com.example.loginsample;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.loginsample.data.AppDatabase;
import com.example.loginsample.data.entity.Edificacion;

import java.util.ArrayList;
import java.util.List;

public class SplashActivity extends AppCompatActivity {

    private AppDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Inicializamos la base de datos
        database = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "edificacion_database")
                .allowMainThreadQueries()  // Para pruebas, pero se recomienda usar AsyncTask o LiveData
                .build();

        clearDatabase();

        // Encontrar el botón en el layout
        Button startButton = findViewById(R.id.startButton);

        // Listener para el botón "Empezar"
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Insertar un dato de prueba antes de ir a LoginActivity
                insertarDatos();

                // Iniciar LoginActivity cuando se hace clic en el botón
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();  // Finalizar la SplashActivity para que no regrese al presionar atrás
            }
        });
    }
    // Método para limpiar la base de datos
    private void clearDatabase() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                database.edificacionDAO().deleteAllEdificaciones();  // Elimina todas las edificaciones de la base de datos
            }
        }).start();
    }


    private void insertarDatos() {
        // Crear varios objetos Edificacion con datos de latitud y longitud
        List<Edificacion> edificaciones = new ArrayList<>();

        edificaciones.add(new Edificacion("Catedral",
                "Santuario principal de la ciudad ocupando el lado norte de la Plaza de Armas",
                R.drawable.catedral,
                -16.3986, -71.5350));  // Latitud y longitud

        edificaciones.add(new Edificacion("Mansión del Fundador",
                "Histórica casona colonial de Arequipa, conocida por su arquitectura de sillar y rica herencia cultural y artística.",
                R.drawable.ingreso,
                -16.3980, -71.5370));

        edificaciones.add(new Edificacion("Monasterio de Santa Catalina",
                "Una pequeña ciudadela que ocupa un área de 20 mil metros cuadrados",
                R.drawable.monasterio,
                -16.3984, -71.5380));

        edificaciones.add(new Edificacion("Molino de Sabandía",
                "Construcción colonial donde se molían trigo y maíz",
                R.drawable.molino,
                -16.3120, -71.6720));

        edificaciones.add(new Edificacion("Mirador de Yanahuara",
                "Ofrece una vista panorámica de Arequipa y sus volcanes, rodeado de arcos de sillar con inscripciones poéticas.",
                R.drawable.yanahuara,
                -16.3985, -71.5355));

        edificaciones.add(new Edificacion("Plaza de Armas",
                "Centro histórico de la ciudad rodeado de impresionantes edificios coloniales y la catedral.",
                R.drawable.plaza_armas,
                -16.3986, -71.5350));

        edificaciones.add(new Edificacion("Museo Santuarios Andinos",
                "Hogar de la momia Juanita, una momia Inca congelada encontrada en el volcán Ampato.",
                R.drawable.santuarios_andinos,
                -16.4094, -71.5322));

        edificaciones.add(new Edificacion("Iglesia de la Compañía",
                "Templo jesuita del siglo XVII, con una impresionante fachada barroca tallada en sillar.",
                R.drawable.compania,
                -16.3982, -71.5376));

        edificaciones.add(new Edificacion("Cañón del Colca",
                "Uno de los cañones más profundos del mundo, famoso por sus vistas y observación de cóndores.",
                R.drawable.colca,
                -15.6134, -71.8586));

        edificaciones.add(new Edificacion("Baños Termales de Yura",
                "Aguas termales naturales conocidas por sus propiedades terapéuticas y relajantes.",
                R.drawable.banos_yura,
                -16.2735, -71.5327));

        // Insertar en la base de datos en un hilo en segundo plano
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (Edificacion edificacion : edificaciones) {
                    database.edificacionDAO().insertEdificacion(edificacion);
                }
            }
        }).start();
    }

}
