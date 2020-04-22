package com.example.eventex;

import android.content.Intent;
import android.database.Cursor;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class perfilAjeno extends AppCompatActivity {
    TextView juan;
    DatabaseHelper myDb;
    Cursor cursor;
    String decricao,imagos,autor,precio,like,direccion,idepart,titu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_ajeno);
        Intent intent = getIntent();
        String NOME = intent.getStringExtra("EXTRA_MESSAGE");
        juan = findViewById(R.id.piragua);
        myDb = new DatabaseHelper(getApplicationContext());
        juan.setText(NOME);
        RecyclerView recyclerView = findViewById(R.id.AjenoProfileRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        List<String> titulos = new ArrayList<String>();
        List<String> decripciones = new ArrayList<String>();
        List<String> imagenes = new ArrayList<>();
        List<String> autores = new ArrayList<>();
        List<String> precios = new ArrayList<>();
        List<String> likes = new ArrayList<>();
        List<String> direcciones = new ArrayList<>();
        List<String> ids = new ArrayList<>();
        cursor = myDb.getEventosAmis(NOME);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                {
                    idepart = cursor.getString(cursor.getColumnIndex("ID"));
                    titu = cursor.getString(cursor.getColumnIndex("nombre"));
                    decricao = cursor.getString(cursor.getColumnIndex("descripcion"));
                    imagos = cursor.getString(cursor.getColumnIndex("imagen"));
                    autor = cursor.getString(cursor.getColumnIndex("autor"));
                    precio = cursor.getString(cursor.getColumnIndex("valorentrada"));
                    like = cursor.getString(cursor.getColumnIndex("likes"));
                    direccion = cursor.getString(cursor.getColumnIndex("direccion"));
                    titulos.add(titu);
                    decripciones.add(decricao);
                    imagenes.add(imagos);
                    autores.add(autor);
                    precios.add(precio);
                    likes.add(like);
                    direcciones.add(direccion);
                    ids.add(idepart);
                }
            }
            cursor.close();
        }
        homefragment.MyAdapter mAdapter = new homefragment.MyAdapter(titulos,decripciones,imagenes,direcciones,likes,autores,precios,ids);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

    }
}
