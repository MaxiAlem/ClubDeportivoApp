package com.grupo12.clubdeportivoapp.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class ClubDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        // Tabla socios
        db.execSQL(
            """
        CREATE TABLE socios (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            nombre TEXT NOT NULL,
            apellido TEXT NOT NULL,
            dni TEXT NOT NULL,
            telefono TEXT NOT NULL,
            email TEXT NOT NULL,
            fechaNacimiento TEXT NOT NULL,
            asociado INTEGER NOT NULL,
            vencimiento TEXT NOT NULL
        )
        """.trimIndent()
        )
        // Tabla usuarios
        db.execSQL(
            "CREATE TABLE usuarios (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "username TEXT NOT NULL UNIQUE, " +
                    "password TEXT NOT NULL)"
        )
        // Tabla pagos
        db.execSQL(
            """
        CREATE TABLE pagos (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            fecha TEXT NOT NULL,
            monto REAL NOT NULL,
            tipo TEXT NOT NULL,
            concepto TEXT NOT NULL,
            dni_socio TEXT,
            FOREIGN KEY (dni_socio) REFERENCES socios(dni)
        )
        """.trimIndent()
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS pagos")
        db.execSQL("DROP TABLE IF EXISTS socios")
        db.execSQL("DROP TABLE IF EXISTS usuarios")
        onCreate(db)
    }

    companion object {
        private const val DATABASE_NAME = "club_deportivo.db"
        private const val DATABASE_VERSION = 2 // LA VERSION SIEMPRE SE SUBE
    }
}