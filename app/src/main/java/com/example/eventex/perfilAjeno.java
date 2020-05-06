package com.example.eventex;

import android.content.Intent;
import android.database.Cursor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class perfilAjeno extends AppCompatActivity {
    TextView juan;
    DatabaseHelper myDb;
    String decricao,imagos,autor,precio,like,direccion,idepart,titu,nombreUsuarios,coni;
    List<String> titulos,decripciones,autores,precios,likes,direcciones,ids,idsUusarios,nombreAutor;
    List<Bitmap> imagenes;
    List<Bitmap> imagenesPerfilUsu;
    FirebaseFirestore db;
    FirebaseStorage storage;
    DocumentReference docRef;
    Map<String, Object> getter;
    List<String> idsEventosFbUsuarios;
    File localFile;
    Bitmap profileBitmap;
    ImageView profileImage;
    int h;
    Button agregar;
    String idUsu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_ajeno);
        agregar = findViewById(R.id.btnAdd);
        profileImage = findViewById(R.id.imagePet);
        Intent intent = getIntent();
        idUsu = intent.getStringExtra("EXTRA_MESSAGE");
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        juan = findViewById(R.id.piragua);
        myDb = new DatabaseHelper(getApplicationContext());
        titulos = new ArrayList<String>();
        decripciones = new ArrayList<String>();
        imagenes = new ArrayList<>();
        imagenesPerfilUsu = new ArrayList<>();
        autores = new ArrayList<>();
        precios = new ArrayList<>();
        likes = new ArrayList<>();
        direcciones = new ArrayList<>();
        nombreAutor = new ArrayList<>();
        ids = new ArrayList<>();
        idsUusarios = new ArrayList<>();
        h = 0;
        docRef = db.collection("usuarios").document(idUsu);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        getter = document.getData();
                        idsEventosFbUsuarios = (List<String>) getter.get("eventos");
                        nombreUsuarios = getter.get("nombre").toString();
                        juan.setText(nombreUsuarios);
                        String img = getter.get("imagen").toString();
                        StorageReference gsReference = storage.getReferenceFromUrl(img);
                        try {
                            localFile = File.createTempFile("images", "jpg");
                            gsReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                    // Local temp file has been created
                                    profileBitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                                    profileImage.setImageBitmap(profileBitmap);
                                    if (idsEventosFbUsuarios.size()>0) {
                                        creadorListas(idsEventosFbUsuarios.get(h));
                                    }
                                }
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

        agregar.setOnClickListener(

                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Cursor cursovich = myDb.getTodoDatos();
                        if(cursovich!=null && cursovich.moveToFirst()) {
                            String idPropia = cursovich.getString(cursovich.getColumnIndex("ID"));
                            addSeguidoalAutor(db, idPropia, idUsu);
                            cursovich.close();
                        }
                        myDb.insertSeguidos(idUsu);
                    }
                }
        );


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
                    RecyclerView recyclerView = findViewById(R.id.AjenoProfileRecycler);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    homefragment.MyAdapter mAdapter = new homefragment.MyAdapter(titulos.subList(0, imagenes.size()), decripciones.subList(0, imagenes.size()), imagenes, direcciones.subList(0, imagenes.size()), likes.subList(0, imagenes.size()), autores.subList(0, imagenes.size()), precios.subList(0, imagenes.size()), ids.subList(0, imagenes.size()),nombreAutor,imagenesPerfilUsu.subList(0, imagenes.size()));
                    recyclerView.setAdapter(mAdapter);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    h++;
                    if(h<idsEventosFbUsuarios.size()) {
                        creadorListas(idsEventosFbUsuarios.get(h));
                    }

                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void creadorListas(final String eventix){

            docRef = db.collection("eventos").document(eventix);
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
                                nombreAutor.add(nombreUsuarios);
                                imagenesPerfilUsu.add(profileBitmap);
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
                                ids.add(eventix);
                                coni = getter.get("imagen").toString();
                                eventImageGet(coni);
                            }
                        }
                    }
                }
            });
        }

    public static void addSeguidoalAutor(FirebaseFirestore db, String autor, String perfil) {
        DocumentReference updater = db.collection("usuarios").document(autor);

        updater.update("seguidos", FieldValue.arrayUnion(perfil));

    }
    }

