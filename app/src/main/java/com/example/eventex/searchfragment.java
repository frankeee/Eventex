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

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class searchfragment extends Fragment {
    DatabaseHelper myDb;
    Button buscador;
    EditText nombre,zona,cate;
    View rootView;
    DocumentReference docRef;
    RecyclerView recyclerView;
    String titu,decricao,imagos,autor,precio,like,direccion,idoque,coni,nombreAuti,imagoperfil;
    List<String> titulos,decripciones,autores,precios,likes,direcciones,idea;
    FirebaseFirestore db;
    FirebaseStorage storage;
    List<Bitmap> imagenes;
    RadioButton evento;
    RadioButton cuenta;
    File localFile;
    List<String> nombreAutor;
    List<String> imagenPerfilautor;
    List<Map<String, Object>> supageta;
    Map<String, Object> getter;
    int n;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.search_layout, container, false);
        myDb = new DatabaseHelper(getActivity());
        n =0;
        titulos = new ArrayList<String>();
        decripciones = new ArrayList<String>();
        imagenes = new ArrayList<>();
        autores = new ArrayList<>();
        precios = new ArrayList<>();
        likes = new ArrayList<>();
        idea = new ArrayList<>();
        supageta = new ArrayList<>();
        nombreAutor = new ArrayList<>();
        imagenPerfilautor = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        direcciones = new ArrayList<>();
        buscador = rootView.findViewById(R.id.btnbusca);
        nombre = rootView.findViewById(R.id.nombreEdit);
        zona = rootView.findViewById(R.id.zonaEdit);
        cate = rootView.findViewById(R.id.cateEdit);
        evento = rootView.findViewById((R.id.radioEvento));
        cuenta = rootView.findViewById((R.id.radioCuenta));
        recyclerView = rootView.findViewById(R.id.searchRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        buscador.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        fireBaseGetter();
                    }
                }

        );

        return rootView;

    }


    void fireBaseGetter(){
        String jhon = "";
        if (cuenta.isSelected()) {
            jhon = cuenta.getText().toString();
        } else {
            jhon = evento.getText().toString();
        }
        db.collection(jhon)
                .whereEqualTo("nombre", nombre.getText().toString())
                .whereEqualTo("categoria", cate.getText().toString())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                idea.add(document.getId());
                                supageta.add(document.getData());
                            }
                            creadorListas(supageta);
                        } else {
                            //Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
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
                    n++;
                    if(n<supageta.size()){
                        creadorListas(supageta);
                    }
                    else{
                        RecyclerView recyclerView = rootView.findViewById(R.id.searchRecycler);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                        homefragment.MyAdapter mAdapter = new homefragment.MyAdapter(titulos.subList(0, imagenes.size()), decripciones.subList(0, imagenes.size()), imagenes, direcciones.subList(0, imagenes.size()), likes.subList(0, imagenes.size()), autores.subList(0, imagenes.size()), precios.subList(0, imagenes.size()), idea.subList(0, imagenes.size()),nombreAutor.subList(0, imagenes.size()),imagenPerfilautor.subList(0, imagenes.size()));
                        recyclerView.setAdapter(mAdapter);
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void creadorListas(List<Map<String, Object>> supagata){
        Map<String,Object> geto = supagata.get(n);
        titu = geto.get("nombre").toString();
        titulos.add(titu);
        decricao = geto.get("descripcion").toString();
        decripciones.add(decricao);
        autor = geto.get("autor").toString();
        autores.add(autor);
        precio = geto.get("valor").toString();
        precios.add(precio);
        like = geto.get("likes").toString();
        likes.add(like);
        direccion = geto.get("direccion").toString();
        direcciones.add(direccion);
        coni = geto.get("imagen").toString();
        usuarioPoreventoget(autor,coni);

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
                            nombreAuti = getter.get("nombre").toString();
                            nombreAutor.add(nombreAuti);
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