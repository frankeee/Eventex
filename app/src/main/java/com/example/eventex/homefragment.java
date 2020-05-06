package com.example.eventex;

import android.content.Context;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;




public class homefragment extends Fragment {
    DocumentReference docRef;
    File localFile;
    Map<String, Object> getter,gettor;
    DatabaseHelper myDb;
    Cursor cursor;
    FirebaseFirestore db;
    FirebaseStorage storage;
    String decricao,imagos,autor,precio,like,direccion,idepart,titu,nombreUsuarios,imagenPerfilUsuario,coni;
    List<String> titulos,decripciones,autores,precios,likes,direcciones,idsEventos,nombreAutor;
    List<Bitmap> imagenes;
    List<String> imagenesPerfilUsu;
    List<String> tutiEventi;
    int intUsuario,intEvento ;
    View rootView;
    List<String> amiwis;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.home_layout, container, false);
        db = FirebaseFirestore.getInstance();
        storage =FirebaseStorage.getInstance();
        imagos = "";
        intUsuario = 0;
        intEvento = 0;
        tutiEventi = new ArrayList<>();
        titulos = new ArrayList<String>();
        decripciones = new ArrayList<String>();
        imagenes = new ArrayList<>();
        imagenesPerfilUsu = new ArrayList<>();
        autores = new ArrayList<>();
        precios = new ArrayList<>();
        likes = new ArrayList<>();
        direcciones = new ArrayList<>();
        nombreAutor = new ArrayList<>();
        idsEventos = new ArrayList<>();

        myDb = new DatabaseHelper(getActivity());
        Cursor cursor2 = myDb.getTodoSeguidos();
        amiwis = new ArrayList<String>();
        if (cursor2 != null) {
            while (cursor2.moveToNext()) {
                String amiwo = cursor2.getString(cursor2.getColumnIndex("seguidos"));
                amiwis.add(amiwo);
            }
            cursor2.close();
        }
        if(amiwis.size()>0) {
            agarradorUsuarios(amiwis.get(intUsuario));
        }










        if (cursor != null) {
            cursor.close();
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

                    intEvento++;
                    if(intEvento<tutiEventi.size()) {
                        creadorListas(tutiEventi.get(intEvento));
                    }
                    else{
                        intUsuario++;
                        if(intUsuario<amiwis.size()){
                            intEvento =0;
                            agarradorUsuarios(amiwis.get(intUsuario));
                        }
                        else{
                            RecyclerView recyclerView = rootView.findViewById(R.id.myRecycler);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                            homefragment.MyAdapter mAdapter = new homefragment.MyAdapter(titulos.subList(0, imagenes.size()), decripciones.subList(0, imagenes.size()), imagenes, direcciones.subList(0, imagenes.size()), likes.subList(0, imagenes.size()), autores.subList(0, imagenes.size()), precios.subList(0, imagenes.size()), idsEventos.subList(0, imagenes.size()),nombreAutor,imagenesPerfilUsu.subList(0, imagenes.size()));
                            recyclerView.setAdapter(mAdapter);
                            recyclerView.setItemAnimator(new DefaultItemAnimator());
                        }
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
                            imagenesPerfilUsu.add(imagenPerfilUsuario);
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
                            idsEventos.add(eventix);
                            coni = getter.get("imagen").toString();
                            eventImageGet(coni);
                        }
                    }
                }
            }
        });
    }

    void agarradorUsuarios(String usuario){
        docRef = db.collection("usuarios").document(usuario);

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        getter = document.getData();
                        imagenPerfilUsuario = getter.get("imagen").toString();
                        nombreUsuarios = getter.get("nombre").toString();
                        tutiEventi = (List<String>) getter.get("eventos");
                        creadorListas(tutiEventi.get(intEvento));
                    }
                }
            }
        });

    }


    static class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
        private List titulos;
        private List descripciones;
        private List<Bitmap> imagenes;
        private List direcciones;
        private List likes;
        private List autors;
        private List valor;
        private List idevento;
        private List nombreAutor;
        private List imagenPerfilAutor;
        DatabaseHelper myDebo;
        Context context;
        TextView idero;
        Button usu;
        TextView idUsu;

        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder
        public class MyViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case
            public TextView titu, detri, money, dire, likes, idas,idautor;
            public ImageView imagencix,profileImage;
            public Button autorcix;

            public MyViewHolder(View v) {
                super(v);
                titu = v.findViewById(R.id.titulo);
                detri = v.findViewById(R.id.descriItem);
                imagencix = v.findViewById(R.id.imageView2);
                likes = v.findViewById(R.id.likes);
                dire = v.findViewById(R.id.direccion);
                money = v.findViewById(R.id.valorentrada);
                autorcix = v.findViewById(R.id.nombre);
                idas = v.findViewById(R.id.idEvento);
                idautor = v.findViewById(R.id.idUsuario);
                profileImage = v.findViewById(R.id.imagenPerfil);
            }
        }

        // Provide a suitable constructor (depends on the kind of dataset)
        public MyAdapter(List titulo, List descri, List<Bitmap> imagenex, List dire, List like, List autor, List plata, List idos,List nombreautor,List imagenPerfil) {
            titulos = titulo;
            descripciones = descri;
            imagenes = imagenex;
            direcciones = dire;
            likes = like;
            autors = autor;
            valor = plata;
            idevento = idos;
            nombreAutor = nombreautor;
            imagenPerfilAutor = imagenPerfil;
        }

        // Create new views (invoked by the layout manager)
        @Override
        public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
            // create a new view
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);

            MyViewHolder vh = new MyViewHolder(v);
            ImageButton likero = v.findViewById(R.id.imageButton);
            idero = v.findViewById(R.id.idEvento);
            usu = v.findViewById(R.id.nombre);
            idUsu = v.findViewById(R.id.idUsuario);
            myDebo = new DatabaseHelper(parent.getContext());
            likero.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String jhon = idero.getText().toString();
                    myDebo.insertGuardados(jhon);
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    addGuardadoalAutor(db,idUsu.getText().toString(),idero.getText().toString());

                }
            });
            final Intent intent = new Intent(parent.getContext(), perfilAjeno.class);
            context = parent.getContext();
            usu.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            String idnombrado = idUsu.getText().toString();
                            intent.putExtra("EXTRA_MESSAGE", idnombrado);
                            context.startActivity(intent);
                        }
                    }
            );

            return vh;
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            // - get element from your dataset at this position
            // - replace the contents of the view with that element
            String bowie = titulos.get(position).toString();
            String bobardo = descripciones.get(position).toString();
            Bitmap rey = imagenes.get(position);
            String dire = direcciones.get(position).toString();
            String like = likes.get(position).toString();
            String autorsete = autors.get(position).toString();
            String valorcete = valor.get(position).toString();
            String iduos = idevento.get(position).toString();
            String uriImag = imagenPerfilAutor.get(position).toString();
            try {
                holder.profileImage.setImageURI(Uri.parse(uriImag));
            }catch(Exception e){

            }
            holder.idas.setText(iduos);
            holder.titu.setText(bowie);
            holder.detri.setText(bobardo);
            holder.imagencix.setImageBitmap(rey);
            holder.dire.setText(dire);
            holder.likes.setText(like);
            holder.autorcix.setText(nombreAutor.get(position).toString());
            holder.money.setText(valorcete);
            holder.idautor.setText(autorsete);
        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override

        public int getItemCount() {
            try{return idevento.size();
        }catch(Exception e){
                return 0;
            }

        }

    }
    public static void addGuardadoalAutor(FirebaseFirestore db, String autor, String id_evento) {
        DocumentReference updater = db.collection("usuarios").document(autor);

        updater.update("guardados", FieldValue.arrayUnion(id_evento));

    }
}
