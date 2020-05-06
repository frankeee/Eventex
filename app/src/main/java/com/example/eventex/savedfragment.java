package com.example.eventex;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import java.util.List;
import java.util.Map;

public class savedfragment extends Fragment {
    DatabaseHelper myDb;
    Cursor cursor2;
    String titu,decricao,autor,precio,like,direccion,idoque,coni,nombreauti,imagoperfil;
    List<String> titulos,decripciones,autores,precios,likes,direcciones,idea;
    File localFile;
    List<String> nombreAutor;
    List<String> imagenPerfilautor;
    Map<String, Object> getter;
    FirebaseStorage storage;
    FirebaseFirestore db;
    List<Bitmap> imagenes;
    View rootView;
    DocumentReference docRef;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.saved_layout, container, false);
        myDb = new DatabaseHelper(getActivity());
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        titulos = new ArrayList<String>();
        decripciones = new ArrayList<String>();
        imagenes = new ArrayList<Bitmap>();
        autores = new ArrayList<>();
        precios = new ArrayList<>();
        likes = new ArrayList<>();
        direcciones = new ArrayList<>();
        idea = new ArrayList<>();
        imagenPerfilautor = new ArrayList<>();
        nombreAutor = new ArrayList<>();
        cursor2 = myDb.getTodoGuardados();

        if(cursor2!=null&&cursor2.moveToFirst()){
            creadorListas(cursor2);
            }

        return rootView;
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
                    RecyclerView recyclerView = rootView.findViewById(R.id.reciclaSaved);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    homefragment.MyAdapter mAdapter = new homefragment.MyAdapter(titulos.subList(0, imagenes.size()), decripciones.subList(0, imagenes.size()), imagenes, direcciones.subList(0, imagenes.size()), likes.subList(0, imagenes.size()), autores.subList(0, imagenes.size()), precios.subList(0, imagenes.size()), idea.subList(0, imagenes.size()),nombreAutor,imagenPerfilautor);
                    recyclerView.setAdapter(mAdapter);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    if(cursor2.moveToNext()) {
                        creadorListas(cursor2);
                    }
                    else{
                        cursor2.close();
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void creadorListas(Cursor cursor2){

        if(cursor2 != null) {
            idoque =cursor2.getString(cursor2.getColumnIndex("guardados"));
            docRef = db.collection("eventos").document(idoque);
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
                                idea.add(idoque);
                                coni = getter.get("imagen").toString();
                                usuarioPoreventoget(autor,coni);
                            }
                        }
                    }
                }
            });
        }
    }
    void usuarioPoreventoget(String id,String imgurl){
        final String pe = imgurl;
        docRef = db.collection("usuarios").document(id);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        getter = document.getData();
                        if(getter!=null) {
                            nombreauti = getter.get("nombre").toString();
                            nombreAutor.add(nombreauti);
                            imagoperfil = getter.get("imagen").toString();
                            imagenPerfilautor.add(imagoperfil);
                            eventImageGet(pe);
                        }
                    }
                }
            }
        });
    }
}
