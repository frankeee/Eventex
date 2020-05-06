package com.example.eventex;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Thread.sleep;


public class profilefragment extends Fragment {

    DatabaseHelper myDb;
    ImageView imagenBoton;
    TextView nombre,descricao;
    ImageButton editor;
    String uriImagen,titu,decricao,imagos,autor,precio,like,direccion,idoque,toti,coni,nome;
    Button creaEvent;
    Map<String, Object> getter;
    FirebaseFirestore db;
    String ido;
    DocumentReference docRef;
    List<String> titulos;
    List<String> decripciones;
    List<Bitmap> imagenes;
    List<String> autores;
    List<String> precios;
    List<String> likes;
    List<String> idsEventos;
    List<String> direcciones;
    List<String> urlImagenes;
    List<String> nombreAutor;
    List<String> imagenPerfilautor;
    int r;
    View rootView ;
    FirebaseStorage storage;
    String idParaImagen;
    File localFile;
    Cursor cursor2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.profile_layout, container, false);
        myDb = new DatabaseHelper(getActivity());
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        imagenBoton = rootView.findViewById(R.id.imagePet);
        nombre = rootView.findViewById(R.id.piragua);
        descricao = rootView.findViewById(R.id.descricri);
        editor = rootView.findViewById(R.id.imageEditor);
        creaEvent = rootView.findViewById(R.id.btnCreaEvent);
        nombreAutor = new ArrayList<String>();
        imagenPerfilautor = new ArrayList<>();
        titulos  = new ArrayList<String>();
        decripciones = new ArrayList<String>();
        imagenes = new ArrayList<>();
        autores = new ArrayList<>();
        precios = new ArrayList<>();
        likes = new ArrayList<>();
        idsEventos = new ArrayList<>();
        direcciones = new ArrayList<>();
        urlImagenes = new ArrayList<>();
        r =1;
        if(existe()) {
            Cursor cursor = getDatardis();
            nome = "";
            String descripcion = "noque";
            uriImagen = "";
            if (cursor != null && cursor.moveToFirst()) {
                nome = cursor.getString(cursor.getColumnIndex("nombre"));
                descripcion = cursor.getString(cursor.getColumnIndex("descripcion"));
                uriImagen = cursor.getString(cursor.getColumnIndex("imagen"));
                idParaImagen = cursor.getString(cursor.getColumnIndex("ID"));
                cursor.close();
            }
            descricao.setText(descripcion);
            nombre.setText(nome);

            try {
                if (uriImagen != "") {
                    StorageReference gsReference = storage.getReferenceFromUrl(toti);
                    try {
                        localFile = File.createTempFile("images", "jpg");
                        gsReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                // Local temp file has been created
                                Bitmap myBitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                                imagenBoton.setImageBitmap(myBitmap);
                            }
                        });
                    } catch (IOException r) {
                        r.printStackTrace();
                    }
                }
            } catch (Exception q) {

            }


        }

        cursor2 = myDb.getTodoMisEventos();
        if(cursor2.moveToFirst()&&cursor2!=null) {
            creadorListas(cursor2);
        }

        editor.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(),SettingsActivity.class);
                        String nombrado = nombre.getText().toString();
                        String describido = descricao.getText().toString();
                        String uriadoIma = uriImagen;
                        intent.putExtra("EXTRA_MESSAGE",nombrado);
                        intent.putExtra("EXTRA_MESSAGE2",describido);
                        intent.putExtra("EXTRA_MESSAGE3",uriadoIma);
                        startActivity(intent);
                    }
                }
        );
        creaEvent.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(),CreateEvent.class);
                        startActivity(intent);
                    }
                }
        );
        return rootView;
    }

    public boolean existe() {
        final boolean isInserted = myDb.existeUsuario();
        return isInserted;
    }

    public Cursor getDatardis(){
        Cursor cursor = myDb.getTodoDatos();
        return cursor;
    }

    public void eventImageGet(String urls){

        StorageReference gsReference = storage.getReferenceFromUrl(urls);
        try {
            localFile = File.createTempFile("images", "jpg");
            gsReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    // Local temp file has been created
                    Bitmap myBitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    imagenes.add(myBitmap);

                    if(cursor2.moveToNext()) {
                        creadorListas(cursor2);
                    }
                    else{
                        cursor2.close();
                        RecyclerView recyclerView = rootView.findViewById(R.id.ProfileRecycler);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                        homefragment.MyAdapter mAdapter = new homefragment.MyAdapter(titulos.subList(0, imagenes.size()), decripciones.subList(0, imagenes.size()), imagenes, direcciones.subList(0, imagenes.size()), likes.subList(0, imagenes.size()), autores.subList(0, imagenes.size()), precios.subList(0, imagenes.size()), idsEventos.subList(0, imagenes.size()),nombreAutor,imagenPerfilautor);
                        recyclerView.setAdapter(mAdapter);
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void creadorListas(Cursor cursor2){

        if(cursor2 != null) {
            ido =cursor2.getString(cursor2.getColumnIndex("ID"));
            docRef = db.collection("eventos").document(ido);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            getter = document.getData();
                            if(getter!=null) {
                                titu = getter.get("nombre").toString();
                                titulos.add(titu);
                                nombreAutor.add(nome);
                                imagenPerfilautor.add(uriImagen);
                                decricao = getter.get("descripcion").toString();
                                decripciones.add(decricao);
                                autor = getter.get("autor").toString();
                                autores.add(autor);
                                precio = getter.get("valor").toString();
                                precios.add(precio);
                                like = getter.get("likes").toString();
                                likes.add(like);
                                direccion = getter.get("direccion").toString();
                                direcciones.add(direccion);
                                idsEventos.add(ido);
                                coni = getter.get("imagen").toString();
                                urlImagenes.add(coni);
                                eventImageGet(coni);
                                }
                        }
                        }
                    }
                });
        }
    }

}
