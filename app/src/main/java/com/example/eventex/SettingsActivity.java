package com.example.eventex;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SettingsActivity extends AppCompatActivity {
    EditText nombre, descripcion,direccion;
    Button acepto;
    ImageButton imagonBoton;
    DatabaseHelper myDb;
    String path,juan,bebe,coca;
    Uri pachon;
    FirebaseFirestore db;
    public static final int PICK_IMAGE = 1;
    FirebaseStorage storage;
    Uri downloadUri;
    int s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        myDb = new DatabaseHelper(getApplicationContext());
        nombre = findViewById(R.id.nombrer);
        descripcion = findViewById(R.id.descripcionr);
        acepto = findViewById(R.id.btnacept);
        imagonBoton = findViewById(R.id.settingImage);
        direccion = findViewById(R.id.direccion);
        path = "";
        pachon = Uri.parse(path);
        Intent intent = getIntent();
        String NOME = intent.getStringExtra("EXTRA_MESSAGE");
        String DERI = intent.getStringExtra("EXTRA_MESSAGE2");
        String uriIma = intent.getStringExtra("EXTRA_MESSAGE3");
        nombre.setText(NOME);
        descripcion.setText(DERI);
        /*
        if (uriIma != "") {
            imagonBoton.setImageURI(Uri.parse(uriIma));
        }
        */

        acepto.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (!myDb.existeUsuario()) {
                            addUsuario(db);
                        } else {
                            Cursor cursor = myDb.getTodoDatos();
                            if (cursor != null && cursor.moveToFirst()) {
                                bebe = cursor.getString(cursor.getColumnIndex("ID"));
                                cursor.close();
                                updateUsuario(db, bebe);
                            }
                        }
                        if (pachon != null) {

                        StorageReference storeRef = storage.getReference();
                        final StorageReference imageRef = storeRef.child("images/" + bebe + "/profile.jpg");
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
                                    Cursor cursorsix = myDb.getTodoDatos();
                                    if (cursorsix.moveToFirst()) {
                                        coca = cursorsix.getString(cursorsix.getColumnIndex("ID"));
                                        updateImagen(db, coca);
                                    }
                                } else {
                                    // Handle failures
                                    // ...
                                }
                            }
                        });
                    }
                       Intent intento = new Intent(getApplicationContext(),MainActivity.class);
                       intento.putExtra("EXTRA","1");
                       startActivity(intento);
                    }
                }
        );
        imagonBoton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED) {
                            // Permission is not granted
                            ActivityCompat.requestPermissions(SettingsActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},s );
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


        //onActivityResult(READ_REQUEST_CODE, Activity.RESULT_OK ,intent);
        startActivityForResult(myFileIntent, 1);

    }


    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        try {
            final int takeFlags = data.getFlags()
                    & (Intent.FLAG_GRANT_READ_URI_PERMISSION
                    | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        imagonBoton.setImageBitmap(selectedImage);
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
                                imagonBoton.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                                cursor.close();
                            }
                        }

                    }
                    break;
            }


            path = data.getData().getPath();
            pachon = data.getData();
        }catch(Exception e){}

    }

    public void addUsuario(FirebaseFirestore db) {
        // [START add_ada_lovelace]
        // Create a new user with a first and last name
        Map<String, Object> user = new HashMap<>();

        user.put("nombre",nombre.getText().toString() );
        user.put("descripcion", descripcion.getText().toString());
        user.put("direccion", direccion.getText().toString());
        user.put("guardados", Arrays.asList());
        user.put("seguidos", Arrays.asList());
        user.put("eventos",Arrays.asList());
        user.put("imagen","");
        // Add a new document with a generated ID
        db.collection("usuarios")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        juan =documentReference.getId();
                        myDb.insertDatos(juan,nombre.getText().toString(),descripcion.getText().toString(),direccion.getText().toString());
                        myDb.updateImagen(juan,pachon.toString());

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

    }

    public void updateUsuario(FirebaseFirestore db,String id){
        DocumentReference updater = db.collection("usuarios").document(id);

        if(nombre.getText().toString()!="") {
            updater.update("nombre", nombre.getText().toString());
        }
        if(descripcion.getText().toString()!="") {
            updater.update("descripcion", descripcion.getText().toString());
        }
        if(direccion.getText().toString()!="") {
            updater.update("direccion", direccion.getText().toString());
        }
        //updater.update("imagen",downloadUri.toString());
        myDb.updateDatos(id,nombre.getText().toString(),descripcion.getText().toString(),direccion.getText().toString(),pachon.toString());
                /*
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });*/


    }
    public void updateImagen(FirebaseFirestore db,String id){
        DocumentReference updater = db.collection("usuarios").document(id);
        updater.update("imagen",downloadUri.toString());


    }

}