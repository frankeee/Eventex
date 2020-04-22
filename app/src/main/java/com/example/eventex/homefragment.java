package com.example.eventex;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;




public class homefragment extends Fragment {

    Map<String, Object> getter,gettor;
    DatabaseHelper myDb;
    Cursor cursor;
    String titu;
    String decricao, imagos, autor, precio, like, direccion, idepart;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.home_layout, container, false);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        imagos = "";
        myDb = new DatabaseHelper(getActivity());
        Cursor cursor2 = myDb.getTodoSeguidos();
        List<String> amiwis = new ArrayList<String>();
        if (cursor2 != null) {
            while (cursor2.moveToNext()) {
                String amiwo = cursor2.getString(cursor2.getColumnIndex("seguidos"));
                amiwis.add(amiwo);
            }
            cursor2.close();
        }


        List<String> titulos = new ArrayList<String>();
        List<String> decripciones = new ArrayList<String>();
        List<String> imagenes = new ArrayList<>();
        List<String> autores = new ArrayList<>();
        List<String> precios = new ArrayList<>();
        List<String> likes = new ArrayList<>();
        List<String> direcciones = new ArrayList<>();
        List<String> ids = new ArrayList<>();
        int i = 0;
        while (i < amiwis.size()) {
            DocumentReference docRef = db.collection("usuarios").document(amiwis.get(i));

            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                   @Override
                                                   public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                       if (task.isSuccessful()) {
                                                           DocumentSnapshot document = task.getResult();
                                                           if (document.exists()) {
                                                               getter = document.getData();
                                                           }
                                                       }
                                                   }
                                               });
            List<String> idsEventos = (List<String>) getter.get("eventos");
            int h = 0;
                while (h<idsEventos.size()) {
                        DocumentReference doqui = db.collection("eventos").document(idsEventos.get(h));
                        doqui.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        gettor = document.getData();
                                    }
                                }
                            }
                        });
                        idepart = idsEventos.get(h);
                        titu = gettor.get("nombre").toString();
                        decricao = gettor.get("descripcion").toString();;
                        //imagos = gettor.get("imagen").toString();
                        autor = gettor.get("autor").toString();
                        precio = gettor.get("valor").toString();
                        like = gettor.get("likes").toString();
                        direccion = gettor.get("direccion").toString();
                        titulos.add(titu);
                        decripciones.add(decricao);
                        imagenes.add(imagos);
                        autores.add(autor);
                        precios.add(precio);
                        likes.add(like);
                        direcciones.add(direccion);
                        ids.add(idepart);
                        h++;
                        RecyclerView recyclerView = rootView.findViewById(R.id.myRecycler);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                        MyAdapter mAdapter = new MyAdapter(titulos, decripciones, imagenes, direcciones, likes, autores, precios, ids);
                        recyclerView.setAdapter(mAdapter);
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                }

            i++;
        }
        if (cursor != null) {
            cursor.close();
        }

        return rootView;
    }





    static class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
        private List mDataset;
        private List mDataset2;
        private List mDataset3;
        private List mDataset4;
        private List mDataset5;
        private List mDataset6;
        private List mDataset7;
        private List mDataset8;
        DatabaseHelper myDebo;
        Context context;
        TextView idero;
        Button usu;

        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder
        public class MyViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case
            public TextView titu, detri, money, dire, likes, idas;
            public ImageView imagencix;
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
                idas = v.findViewById(R.id.id);
            }
        }

        // Provide a suitable constructor (depends on the kind of dataset)
        public MyAdapter(List titulo, List descri, List imagenex, List dire, List likes, List autor, List plata, List idos) {
            mDataset = titulo;
            mDataset2 = descri;
            mDataset3 = imagenex;
            mDataset4 = dire;
            mDataset5 = likes;
            mDataset6 = autor;
            mDataset7 = plata;
            mDataset8 = idos;
        }

        // Create new views (invoked by the layout manager)
        @Override
        public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
            // create a new view
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);

            MyViewHolder vh = new MyViewHolder(v);
            ImageButton likero = v.findViewById(R.id.imageButton);
            idero = v.findViewById(R.id.id);
            usu = v.findViewById(R.id.nombre);
            myDebo = new DatabaseHelper(parent.getContext());
            likero.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String jhon = idero.getText().toString();
                    int juan = Integer.parseInt(jhon);
                    myDebo.insertGuardados(juan);
                }
            });
            final Intent intent = new Intent(parent.getContext(), perfilAjeno.class);
            context = parent.getContext();
            usu.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            String nombrado = usu.getText().toString();
                            intent.putExtra("EXTRA_MESSAGE", nombrado);
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
            String bowie = mDataset.get(position).toString();
            String bobardo = mDataset2.get(position).toString();
            String rey = mDataset3.get(position).toString();
            String dire = mDataset4.get(position).toString();
            String like = mDataset5.get(position).toString();
            String autorsete = mDataset6.get(position).toString();
            String valorcete = mDataset7.get(position).toString();
            String iduos = mDataset8.get(position).toString();
            holder.idas.setText(iduos);
            holder.titu.setText(bowie);
            holder.detri.setText(bobardo);
            if (rey != "") {

                holder.imagencix.setImageBitmap(BitmapFactory.decodeFile(rey));
            }
            holder.dire.setText(dire);
            holder.likes.setText(like);
            holder.autorcix.setText(autorsete);
            holder.money.setText(valorcete);
        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return mDataset.size();
        }

    }
}
