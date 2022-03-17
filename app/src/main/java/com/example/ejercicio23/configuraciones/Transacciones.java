package com.example.ejercicio23.configuraciones;

public class Transacciones {
    //Nombre de la base de datos
    public static final String NameDatabase = "Photograh";

    //Creacion de las tablas de la base de datos

    public static final String tablaPhotograh = "photograh";

    /*
    *
    * Campos especificos de la tabla Personas
    * */

    public static final String id = "id";
    public static final String descripcion = "descripcion";
    public static final String imagen = "imagen";


    public static final String CreateTablePhotograh = "CREATE TABLE "+tablaPhotograh + "(id INTEGER PRIMARY KEY AUTOINCREMENT,"+
            "descripcion TEXT, imagen BLOB)";

    public static final String DropTablePhotograh = "DROP TABLE IF EXISTS "+ tablaPhotograh;
}
