package com.example.eventex;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


public class CreateEvent extends AppCompatActivity {
    Cursor curseado;
    Button aceptador;
    ImageButton imagenBota;
    EditText titulo,descripcion,direccion,valorentrada,autorr,catu;
    DatabaseHelper myDb;
    String path,juan;
    Uri pachon;
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        myDb = new DatabaseHelper(getApplicationContext());
        aceptador = findViewById(R.id.aceptEvent);
        titulo = findViewById(R.id.titulo);
        direccion = findViewById(R.id.direccion);
        descripcion = findViewById(R.id.descri);
        imagenBota = findViewById(R.id.imagen1);
        valorentrada = findViewById(R.id.valorentrada);
        catu = findViewById(R.id.categoria);
        autorr = findViewById(R.id.autor);
        path = "";
        db = FirebaseFirestore.getInstance();

        aceptador.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addEvento(db);
                    }
                }
        );
        imagenBota.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        performFileSearch();
                    }
                }
        );

    }

    public void performFileSearch() {

        Intent myFileIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);//new Intent(Intent.ACTION_OPEN_DOCUMENT);

        myFileIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION | Intent.FLAG_GRANT_PREFIX_URI_PERMISSION);
        //myFileIntent.setType("*/*");

        //onActivityResult(READ_REQUEST_CODE, Activity.RESULT_OK ,intent);
        startActivityForResult(myFileIntent,1);

    }


    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        final int takeFlags = data.getFlags()
                & (Intent.FLAG_GRANT_READ_URI_PERMISSION
                | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        switch (requestCode) {
            case 0:
                if (resultCode == RESULT_OK && data != null) {
                    Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                    imagenBota.setImageBitmap(selectedImage);
                }

                break;
            case 1:
                if (resultCode == RESULT_OK && data != null) {

                    Uri selectedImage = data.getData();
                    //getContentResolver().takePersistableUriPermission(selectedImage, takeFlags);
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    if (selectedImage != null) {
                        Cursor cursor = getContentResolver().query(selectedImage,
                                filePathColumn, null, null, null);
                        if (cursor != null) {
                            cursor.moveToFirst();

                            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                            String picturePath = cursor.getString(columnIndex);
                            imagenBota.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                            cursor.close();
                            path = picturePath;
                        }
                    }

                }
                break;
        }



        pachon = data.getData();

    }

    public void addEvento(FirebaseFirestore db) {
        // [START add_ada_lovelace]
        // Create a new user with a first and last name
        Map<String, Object> user = new HashMap<>();
        String nombre =titulo.getText().toString();
        String decri = descripcion.getText().toString();
        String dire = direccion.getText().toString();
        String valor = valorentrada.getText().toString();
        String autor = "fallo";
        String cati = catu.getText().toString();
        curseado = myDb.getTodoDatos();
        if(curseado!=null && curseado.moveToFirst()) {
            autor = curseado.getString(curseado.getColumnIndex("ID"));
            curseado.close();
        }
        user.put("nombre",nombre);
        user.put("descripcion",decri);
        user.put("direccion", dire);
        user.put("autor", autor);
        user.put("likes",0);
        user.put("valor",valor);
        user.put("categoria",cati);
        // Add a new document with a generated ID
        final String autor1 = autor;
        final FirebaseFirestore db1 = db;
        db.collection("eventos")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        juan =documentReference.getId();
                        myDb.insertEvento(juan);
                        addEventoalAutor(db1,autor1,juan);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

    }
    public void addEventoalAutor(FirebaseFirestore db,String autor,String id_evento) {
        DocumentReference updater = db.collection("usuarios").document(autor);

        updater.update("eventos", FieldValue.arrayUnion(id_evento));

    }
}
