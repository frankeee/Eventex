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
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

public class searchfragment extends Fragment {
    DatabaseHelper myDb;
    Button buscador;
    EditText nombre;
    View rootView;
    Cursor cursor;
    RecyclerView recyclerView;
    String titu,decricao,imagos,autor,precio,like,direccion,idoque;
    List<String> titulos,decripciones,imagenes,autores,precios,likes,direcciones,idea;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.search_layout, container, false);
        myDb = new DatabaseHelper(getActivity());
        titulos = new ArrayList<String>();
        decripciones = new ArrayList<String>();
        imagenes = new ArrayList<>();
        autores = new ArrayList<>();
        precios = new ArrayList<>();
        likes = new ArrayList<>();
        idea = new ArrayList<>();
        direcciones = new ArrayList<>();
        buscador = rootView.findViewById(R.id.btnbusca);
        nombre = rootView.findViewById(R.id.nombreEdit);
        recyclerView = rootView.findViewById(R.id.searchRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        buscador.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String jona = nombre.getText().toString();
                        cursor = myDb.getEventoporCategoria(jona);
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
                                    idea.add(idoque);
                                    titulos.add(titu);
                                    decripciones.add(decricao);
                                    imagenes.add(imagos);
                                    autores.add(autor);
                                    precios.add(precio);
                                    likes.add(like);
                                    direcciones.add(direccion);
                                }
                            }
                            cursor.close();

                        }
                        homefragment.MyAdapter mAdapter = new homefragment.MyAdapter(titulos,decripciones,imagenes,direcciones,likes,autores,precios,idea);
                        recyclerView.setAdapter(mAdapter);
                        recyclerView.setItemAnimator(new DefaultItemAnimator());

                    }
                }

        );

        return rootView;

    }
}