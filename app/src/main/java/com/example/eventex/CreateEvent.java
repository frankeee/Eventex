package com.example.eventex;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


public class CreateEvent extends AppCompatActivity {
    Cursor curseado;
    Button aceptador;
    ImageButton imagenBota;
    EditText titulo,descripcion,direccion,valorentrada,catu;
    DatabaseHelper myDb;
    String path,juan;
    Uri pachon;
    FirebaseFirestore db;
    FirebaseStorage storage ;
    DocumentReference docRef;
    Uri downloadUri;
    int s;
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
        path = "";
        pachon = Uri.parse(path);
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        aceptador.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(pachon.toString()==""){
                            dialogCreator();
                        }
                        else {
                            addEvento(db);
                            Intent intento = new Intent(getApplicationContext(), MainActivity.class);
                            intento.putExtra("EXTRA", "1");
                            startActivity(intento);
                        }
                    }
                }
        );
        imagenBota.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED) {
                            // Permission is not granted
                            ActivityCompat.requestPermissions(CreateEvent.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},s );
                        }
                        else {
                            performFileSearch();
                        }
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
        try{
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



        pachon = data.getData();}catch(Exception e){}

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
        String nombreAutor ="";
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
        user.put("imagen","");
        // Add a new document with a generated ID
        final String autor1 = autor;
        final FirebaseFirestore db1 = db;
        final FirebaseFirestore db2 = db;
        db.collection("eventos")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        juan =documentReference.getId();
                        myDb.insertEvento(juan);
                        addEventoalAutor(db1,autor1,juan);
                        StorageReference storeRef = storage.getReference();
                        final StorageReference imageRef = storeRef.child("images/"+juan+"/profile.jpg");
                        UploadTask uploader = imageRef.putFile(pachon);
                        Task<Uri> urlTask = uploader.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                            @Override
                            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                if (!task.isSuccessful()) {
                                    throw task.getException();
                                }

                                // Continue with the task to get the download URL
                                return imageRef.getDownloadUrl();
                            }
                        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if (task.isSuccessful()) {
                                    downloadUri = task.getResult();

                                    updateImagenEvento(db2,juan);
                                } else {
                                    // Handle failures
                                    // ...
                                }
                            }
                        });
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
    public void updateImagenEvento(FirebaseFirestore db,String id){
        DocumentReference updater = db.collection("eventos").document(id);
        updater.update("imagen",downloadUri.toString());
    }

    public Dialog dialogCreator() {
        // Use the Builder class for convenient dialog construction

        //final EditText editor = new EditText(this);

        AlertDialog.Builder constructor = new AlertDialog.Builder(this);
        constructor.setMessage("Elija una imagen")
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // FIRE ZE MISSILES!

                    }
                });
                //.setView(editor);
        // Create the AlertDialog object and return it
        return constructor.show();
    }
}
