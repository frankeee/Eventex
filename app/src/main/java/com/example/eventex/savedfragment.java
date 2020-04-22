package com.example.eventex;

import android.database.Cursor;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class savedfragment extends Fragment {
    DatabaseHelper myDb;
    Cursor cursor,cursor2;
    String titu,decricao,imagos,autor,precio,like,direccion,idoque;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.saved_layout, container, false);
        myDb = new DatabaseHelper(getActivity());
        //myDb.insertGuardados(0);
        //myDb.insertGuardados(1);
        //myDb.insertGuardados(2);
        //myDb.insertGuardados(3);
        RecyclerView recyclerView = rootView.findViewById(R.id.reciclaSaved);
        cursor2 = myDb.getTodoGuardados();
        List<String> guardados = new ArrayList<String>();
        if(cursor2!=null){
            while(cursor2.moveToNext()){
                String guardado = cursor2.getString(cursor2.getColumnIndex("guardados"));
                guardados.add(guardado);
            }
            cursor2.close();
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        List<String> titulos = new ArrayList<String>();
        List<String> decripciones = new ArrayList<String>();
        List<String> imagenes = new ArrayList<>();
        List<String> autores = new ArrayList<>();
        List<String> precios = new ArrayList<>();
        List<String> likes = new ArrayList<>();
        List<String> direcciones = new ArrayList<>();
        List<String> idea = new ArrayList<>();
        int i = 0;
        while(i<guardados.size()) {
            cursor = myDb.getEventosGuardados(guardados.get(i));
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    {
                        titu = cursor.getString(cursor.getColumnIndex("nombre"));
                        decricao = cursor.getString(cursor.getColumnIndex("descripcion"));
                        imagos = cursor.getString(cursor.getColumnIndex("imagen"));
                        autor = cursor.getString(cursor.getColumnIndex("autor"));
                        precio = cursor.getString(cursor.getColumnIndex("valorentrada"));
                        like = cursor.getString(cursor.getColumnIndex("likes"));
                        direccion = cursor.getString(cursor.getColumnIndex("direccion"));
                        idoque = cursor.getString(cursor.getColumnIndex("ID"));
                        titulos.add(titu);
                        decripciones.add(decricao);
                        imagenes.add(imagos);
                        autores.add(autor);
                        precios.add(precio);
                        likes.add(like);
                        direcciones.add(direccion);
                        idea.add(idoque);
                    }
                }
            }
            i++;
        }
        if(cursor!=null) {
            cursor.close();
        }
        homefragment.MyAdapter mAdapter = new homefragment.MyAdapter(titulos,decripciones,imagenes,direcciones,likes,autores,precios,idea);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        return rootView;
    }
}
