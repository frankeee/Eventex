package com.example.eventex;

import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;


public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String  DATABASE_NAME = "MiBase";
    public static final String TABLE_NAME = "datos";
    public static final String TABLE_NAME2 = "eventos";
    public static final String TABLE_NAME3 = "seguidores";
    public static final String TABLE_NAME4 = "seguidos";
    public static final String TABLE_NAME5 = "guardados";
    public static final String COL_0 = "ID";
    public static final String COL_1 = "nombre";
    public static final String COL_2 = "descripcion";
    public static final String COL_3 =  "imagen";
    public static final String COL_4 = "direccion";
    public static final String COL_5 = "guardados";
    public static final String COL_6 = "seguidores";
    public static final String COL_14 = "seguidos";
    public static final String COL_7 = "ID";
    public static final String COL_15 = "nombre";
    public static final String COL_8 = "descripcion";
    public static final String COL_9 =  "imagen";
    public static final String COL_10 = "direccion";
    public static final String COL_11 = "likes";
    public static final String COL_12 = "categoria";
    public static final String COL_13 = "fecha";
    public static  final String COL_16 = "valorentrada";
    public static final String COL_17 = "autor";
    public DatabaseHelper(Context context){
        super(context,DATABASE_NAME,null , 4);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME +" ("+COL_0+" TEXT PRIMARY KEY , "+COL_1+"  TEXT , "+COL_2+" TEXT , "+COL_3+" TEXT , "+COL_4+" TEXT)");
        db.execSQL("CREATE TABLE " + TABLE_NAME2 +" ("+COL_7+" TEXT PRIMARY KEY)"); //, "+COL_15+" TEXT , "+COL_10+" TEXT , "+COL_9+" TEXT , "+COL_8+" TEXT , "+COL_11+" INTEGER , "+COL_12+" TEXT , "+COL_13+" TEXT , "+COL_16+" INTEGER , "+COL_17+" TEXT
        //db.execSQL("CREATE TABLE " + TABLE_NAME3 +" ("+COL_6+" INTEGER PRIMARY KEY )");
        db.execSQL("CREATE TABLE " + TABLE_NAME4 +" ("+COL_14+" TEXT PRIMARY KEY)");
        db.execSQL("CREATE TABLE " + TABLE_NAME5 +" ("+COL_5+" TEXT PRIMARY KEY)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME2);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME3);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME4);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME5);
        onCreate(db);
    }

    public Cursor getTodoDatos(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME,null);
        return res;
    }

    public Cursor getTodoSeguidos(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME4,null);
        return res;
    }

    public Cursor getEventosAmis(String peron){
        SQLiteDatabase db = this.getWritableDatabase();
        String juan = peron;
        String query = "SELECT * from "+TABLE_NAME2+ " where autor = "+juan;
        Cursor cursor = db.rawQuery(query,null);
        return cursor;
    }

    public Cursor getEventoporCategoria(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        String juan = id;
        String query = "SELECT * from "+TABLE_NAME2+ " where categoria = "+juan;
        Cursor cursor = db.rawQuery(query,null);
        return cursor;
    }



    public Cursor getEventosGuardados(String peron){
        SQLiteDatabase db = this.getWritableDatabase();
        String juan = peron;
        String query = "SELECT * from "+TABLE_NAME2+ " where ID = "+juan;
        Cursor cursor = db.rawQuery(query,null);
        return cursor;
    }

    public Cursor getTodoMisEventos(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME2,null);
        return res;
    }

    public boolean updateDatos(String id,String nombre,String descripcion,String direccion,String imagenUri) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_0,id);
        contentValues.put(COL_1,nombre);
        contentValues.put(COL_2,descripcion);
        contentValues.put(COL_4,direccion);
        if(imagenUri!=""){
            contentValues.put(COL_3,imagenUri);
        }
        String juan = id;

        db.update(TABLE_NAME, contentValues, "ID = ?",new String[] {juan });
        return true;
    }

    public boolean updateImagen(String id,String imagenUri) {
        if(imagenUri!="") {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(COL_0, id);
            contentValues.put(COL_3, imagenUri);
            String juan = id;

            db.update(TABLE_NAME, contentValues, "ID = ?", new String[]{juan});
        }
        return true;
    }

    public boolean insertEvento(String id) {//String nombre,String descripcion,String direccion,String categoria,String fecha,String imagen,String valorentrada,String usuario
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_7,id);
        //contentValues.put(COL_15,nombre);
        //contentValues.put(COL_8,descripcion);
        //contentValues.put(COL_10,direccion);
        //contentValues.put(COL_11,"0");
        //contentValues.put(COL_12,categoria);
        //contentValues.put(COL_13,fecha);
        //contentValues.put(COL_9,imagen);
        //contentValues.put(COL_16,valorentrada);
        //contentValues.put(COL_17,usuario);
        long result = db.insert(TABLE_NAME2,null ,contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }

    public boolean insertDatos(String id,String nombre,String descripcion,String direccion){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_0,id);
        contentValues.put(COL_1,nombre);
        contentValues.put(COL_2,descripcion);
        contentValues.put(COL_3,"");
        contentValues.put(COL_4,direccion);
        long result = db.insert(TABLE_NAME,null ,contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }



    public boolean existeUsuario(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * from "+TABLE_NAME;
        Cursor cursor = db.rawQuery(query,null);
        if(cursor.getCount() <= 0){
            return false;
        }
        return true;
    }

    public boolean insertSeguidos(String seguido){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_14,seguido);
        long result = db.insert(TABLE_NAME4,null ,contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }

    public boolean insertGuardados(String guardado){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_5,guardado);
        long result = db.insert(TABLE_NAME5,null ,contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }
    public Cursor getTodoGuardados(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME5,null);
        return res;
    }



}
