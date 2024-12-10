package com.example.loginsample.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.example.loginsample.Door;
import com.example.loginsample.R;
import com.example.loginsample.Room;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class EdificioView extends View {

    private Paint paintRoomOutline;
    private Paint paintDoor;
    private List<Room> rooms = new ArrayList<>();
    private List<Door> doors = new ArrayList<>();
    private List<float[]> allPoints = new ArrayList<>(); // Lista global de puntos

    public EdificioView(Context context) {
        super(context);
        init();
        loadCoordinates(context);
    }

    public EdificioView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        loadCoordinates(context);
    }

    public EdificioView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        loadCoordinates(context);
    }

    private void init() {
        paintRoomOutline = new Paint();
        paintRoomOutline.setColor(Color.BLUE);
        paintRoomOutline.setStyle(Paint.Style.STROKE);
        paintRoomOutline.setStrokeWidth(1);

        paintDoor = new Paint();
        paintDoor.setColor(Color.GREEN);
        paintDoor.setStrokeWidth(5);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int OFFSET_Y = 200; // Ajusta este valor según lo que necesites para el edificio

        // Pintura para los cuartos (contornos)
        Paint paintRoomOutline = new Paint();
        paintRoomOutline.setColor(Color.BLACK); // Establecer color para el contorno de los cuartos
        paintRoomOutline.setStyle(Paint.Style.STROKE);
        paintRoomOutline.setStrokeWidth(5);

        // Pintura para las puertas
        Paint paintDoor = new Paint();
        paintDoor.setColor(Color.RED);  // Color rojo para las puertas
        paintDoor.setStyle(Paint.Style.STROKE);
        paintDoor.setStrokeWidth(5);

        // Dibujar los contornos de los cuartos
        for (Room room : rooms) {
            Path roomPath = new Path();

            // Obtener los puntos de la lista y crear el contorno del cuarto
            List<float[]> points = room.getPoints();
            if (points != null && !points.isEmpty()) {
                // Empezar el Path desde el primer punto
                float[] firstPoint = points.get(0);
                roomPath.moveTo(firstPoint[0], firstPoint[1] + OFFSET_Y);

                // Conectar los puntos del contorno
                for (int i = 1; i < points.size(); i++) {
                    float[] point = points.get(i);
                    roomPath.lineTo(point[0], point[1] + OFFSET_Y);
                }

                // Cerrar el contorno del cuarto
                roomPath.close();
            }

            // Dibujar el contorno del cuarto
            canvas.drawPath(roomPath, paintRoomOutline);
        }


        Log.d("Door",""+doors.size());

        // Dibujar las puertas
        for (Door door : doors) {
            // Asegurarse de que las puertas tengan coordenadas correctas
            canvas.drawLine(door.getX1(), door.getY1() + OFFSET_Y, door.getX2(), door.getY2() + OFFSET_Y, paintDoor);
            Log.d("Door", "Puerta asdsad: (" + door.getX1() + ", " + door.getY1() + ") a (" + door.getX2() + ", " + door.getY2() + ")");

        }
    }

    private void loadCoordinates(Context context) {
        try {
            InputStream inputStream = context.getAssets().open("coordenadas_edificio.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;

            List<float[]> points = new ArrayList<>();  // Lista para almacenar todos los puntos
            Room currentRoom = null;  // Variable para el cuarto actual

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");

                if (line.startsWith("Punto")) {
                    // Guardamos el punto en la lista de puntos
                    float x = Float.parseFloat(parts[1]);
                    float y = Float.parseFloat(parts[2]);
                    points.add(new float[]{x, y});
                } else if (line.startsWith("Cuarto")) {
                    // Si encontramos "Cuarto", debemos crear un nuevo cuarto
                    // Si ya había un cuarto activo, lo agregamos a la lista
                    if (currentRoom != null) {
                        rooms.add(currentRoom);
                    }

                    // Creamos un nuevo cuarto vacío
                    currentRoom = new Room();

                    // Recorrer los índices de los puntos mencionados en la línea del cuarto
                    for (int i = 1; i < parts.length; i++) {
                        int pointIndex = Integer.parseInt(parts[i]);
                        if (pointIndex >= 0 && pointIndex < points.size()) {
                            float[] point = points.get(pointIndex);
                            currentRoom.addPoint(point[0], point[1]);
                        }
                    }
                } else if (line.startsWith("Puerta")) {
                    // Obtener las coordenadas de la puerta
                    if (parts.length == 5) {
                        try {
                            float x1 = Float.parseFloat(parts[1]);
                            float y1 = Float.parseFloat(parts[2]);
                            float x2 = Float.parseFloat(parts[3]);
                            float y2 = Float.parseFloat(parts[4]);

                            // Agregar la puerta a la lista
                            doors.add(new Door(x1, y1, x2, y2));

                            Log.d("Door", "Puerta leida: (" + x1 + ", " + y1 + ") a (" + x2 + ", " + y2 + ")");
                        } catch (NumberFormatException e) {
                            Log.e("Error", "Formato de puerta inválido", e);
                        }
                    }
                }

            }

            // Asegurándonos de agregar el último cuarto leído al final
            if (currentRoom != null) {
                rooms.add(currentRoom);  // Agregar el cuarto actual
            }

            reader.close();

            // Aquí podemos agregar las puertas a los cuartos si es necesario, como un paso posterior.
            // Si necesitamos vincular puertas a cuartos, se puede hacer ahora o más adelante.

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
