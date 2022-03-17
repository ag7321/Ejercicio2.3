package com.example.ejercicio23;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.example.ejercicio23.Modelo.Photograh;
import com.example.ejercicio23.configuraciones.SQLiteConexion;
import com.example.ejercicio23.configuraciones.Transacciones;

import java.util.ArrayList;

public class viewPhotograh extends AppCompatActivity {
    SQLiteConexion conexion;

    RecyclerView.Adapter adapter;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<Photograh> photograhsList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_photograh);

        conexion = new SQLiteConexion(this, Transacciones.NameDatabase,null,1);

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        photograhsList = new ArrayList<>();


        getPhotograhs();


        adapter = new Adapter(photograhsList);
        recyclerView.setAdapter(adapter);
    }

    private void getPhotograhs() {
        SQLiteDatabase db = conexion.getReadableDatabase();
        Photograh photograhs = null;
        Cursor cursor = db.rawQuery("SELECT * FROM "+ Transacciones.tablaPhotograh,null);
        while (cursor.moveToNext()){
            photograhs = new Photograh();
            photograhs.setId(cursor.getInt(0));
            photograhs.setDescripcion(cursor.getString(1));
            photograhs.setImagen(cursor.getString(2));

            photograhsList.add(photograhs);
        }

    }
}