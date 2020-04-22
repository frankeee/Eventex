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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class profilefragment extends Fragment {

    DatabaseHelper myDb;
    ImageView imagenBoton;
    TextView nombre,descricao;
    ImageButton editor;
    String uriImagen,titu,decricao,imagos,autor,precio,like,direccion,idoque;
    Button creaEvent;
    Map<String, Object> getter;
    FirebaseFirestore db;
    String ido;
    DocumentReference docRef;
    List<String> titulos;
    List<String> decripciones;
    List<String> imagenes;
    List<String> autores;
    List<String> precios;
    List<String> likes;
    List<String> idsEventos;
    List<String> direcciones;
    View rootView ;
    FirebaseStorage storage;
    String idParaImagen;
    File localFile;
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
        titulos  = new ArrayList<String>();
        decripciones = new ArrayList<String>();
        imagenes = new ArrayList<>();
        autores = new ArrayList<>();
        precios = new ArrayList<>();
        likes = new ArrayList<>();
        idsEventos = new ArrayList<>();
        direcciones = new ArrayList<>();
        if(existe()) {
            Cursor cursor = getDatardis();
            String nome = " peru";
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
            /*
            if (uriImagen != "") {
                imagenBoton.setImageURI(Uri.parse(uriImagen));
            }
            */
            docRef = db.collection("usuarios").document(idParaImagen);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            getter = document.getData();
                            if (getter != null) {
                                titu = getter.get("imagen").toString();
                                if (titu != "") {
                                    StorageReference gsReference = storage.getReferenceFromUrl(titu);
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

                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }


                                }

                            }
                        }
                    }
                }
            });




        }

        Cursor cursor2 = myDb.getTodoMisEventos();
        /*
        if(cursor2 != null) {
            while(cursor2.moveToNext()){
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
                                    imagenes.add("");
                                    idsEventos.add(ido);
                                    RecyclerView recyclerView = rootView.findViewById(R.id.ProfileRecycler);
                                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                                    homefragment.MyAdapter mAdapter = new homefragment.MyAdapter(titulos,decripciones,imagenes,direcciones,likes,autores,precios,idsEventos);
                                    recyclerView.setAdapter(mAdapter);
                                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                                }

                            }
                        }
                    }
                });
                //getter = getFirebaseEvent(db,ido);


            }
            cursor2.close();
            }
        */
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
/*
    public Map<String, Object> getFirebaseEvent(FirebaseFirestore db,String id){
        Toast.makeText(getActivity(), "llamo a la funcion", Toast.LENGTH_LONG).show();
        DocumentReference docRef = db.collection("eventos").document(id);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Toast.makeText(getActivity(), "encontro el archivo", Toast.LENGTH_LONG).show();
                        getter=document.getData();
                        titu = getter.get("nombre").toString();
                        titulos.add(titu);
                        decricao = getter.get("descripcion").toString();
                        decripciones.add(decricao);
                        autor = getter.get("autor").toString();
                        autores.add(autor);
                        precio =getter.get("valor").toString();
                        precios.add(precio);
                        like =getter.get("likes").toString();
                        likes.add(like);
                        direccion =getter.get("direccion").toString();
                        direcciones.add(direccion);
                        imagenes.add("");
                        idsEventos.add(id);
                    } else {

                    }
                } else {

                }
            }

        });



        return getter;
    }*/
}
