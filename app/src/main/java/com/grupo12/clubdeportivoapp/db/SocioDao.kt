package com.grupo12.clubdeportivoapp.db

import android.content.ContentValues
import android.content.Context
import com.grupo12.clubdeportivoapp.Socio

class SocioDao(context: Context) {
    private val dbHelper = ClubDatabaseHelper(context)

    // CREATE
    fun insertar(socio: Socio): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("nombre", socio.nombre)
            put("apellido", socio.apellido)
            put("dni", socio.dni)
            put("telefono", socio.telefono)
            put("email", socio.email)
            put("fechaNacimiento", socio.fechaNacimiento)
            put("asociado", if (socio.asociado) 1 else 0)
            put("vencimiento", socio.vencimiento)
        }
        val id = db.insert("socios", null, values)
        db.close()
        return id
    }

    // READ
    fun obtenerTodos(): List<Socio> {
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM socios", null)
        val lista = mutableListOf<Socio>()
        while (cursor.moveToNext()) {
            val nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"))
            val apellido = cursor.getString(cursor.getColumnIndexOrThrow("apellido"))
            val dni = cursor.getString(cursor.getColumnIndexOrThrow("dni"))
            val telefono = cursor.getString(cursor.getColumnIndexOrThrow("telefono"))
            val email = cursor.getString(cursor.getColumnIndexOrThrow("email"))
            val fechaNacimiento = cursor.getString(cursor.getColumnIndexOrThrow("fechaNacimiento"))
            val asociado = cursor.getInt(cursor.getColumnIndexOrThrow("asociado")) == 1
            val vencimiento = cursor.getString(cursor.getColumnIndexOrThrow("vencimiento"))
            lista.add(Socio(nombre, apellido, dni, telefono, email, fechaNacimiento, asociado, vencimiento))
        }
        cursor.close()
        db.close()
        return lista
    }

    // UPDATE
    fun actualizar(socio: Socio): Int {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("nombre", socio.nombre)
            put("apellido", socio.apellido)
            put("telefono", socio.telefono)
            put("email", socio.email)
            put("fechaNacimiento", socio.fechaNacimiento)
            put("asociado", if (socio.asociado) 1 else 0)
            put("vencimiento", socio.vencimiento)
        }
        // El criterio suele ser el DNI, podés elegir otro campo único si lo preferís
        val rows = db.update("socios", values, "dni = ?", arrayOf(socio.dni))
        db.close()
        return rows
    }

    // READ: Obtener un socio por DNI
    fun obtenerPorDni(dni: String): Socio? {
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM socios WHERE dni = ?", arrayOf(dni))
        var socio: Socio? = null
        if (cursor.moveToFirst()) {
            val nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"))
            val apellido = cursor.getString(cursor.getColumnIndexOrThrow("apellido"))
            val telefono = cursor.getString(cursor.getColumnIndexOrThrow("telefono"))
            val email = cursor.getString(cursor.getColumnIndexOrThrow("email"))
            val fechaNacimiento = cursor.getString(cursor.getColumnIndexOrThrow("fechaNacimiento"))
            val asociado = cursor.getInt(cursor.getColumnIndexOrThrow("asociado")) == 1
            val vencimiento = cursor.getString(cursor.getColumnIndexOrThrow("vencimiento"))
            socio = Socio(nombre, apellido, dni, telefono, email, fechaNacimiento, asociado, vencimiento)
        }
        cursor.close()
        db.close()
        return socio
    }

    fun actualizarVencimiento(dni: String, nuevoVencimiento: String): Int {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("vencimiento", nuevoVencimiento)
        }
        return db.update(
            "socios",
            values,
            "dni = ?",
            arrayOf(dni)
        )
    }
    // DELETE
    fun eliminarPorDni(dni: String): Int {
        val db = dbHelper.writableDatabase
        val rows = db.delete("socios", "dni = ?", arrayOf(dni))
        db.close()
        return rows
    }
}