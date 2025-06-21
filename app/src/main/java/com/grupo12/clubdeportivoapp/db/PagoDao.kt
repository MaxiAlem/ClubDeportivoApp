package com.grupo12.clubdeportivoapp.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.grupo12.clubdeportivoapp.Pago

class PagoDao(context: Context) {
    private val dbHelper = ClubDatabaseHelper(context)

    // Insertar un pago
    fun insertarPago(pago: Pago): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("fecha", pago.fecha)
            put("monto", pago.monto)
            put("tipo", pago.tipo)
            put("concepto", pago.concepto)
            put("dni_socio", pago.dniSocio)
        }
        return db.insert("pagos", null, values)
    }

    // Obtener todos los pagos
    fun obtenerTodosPagos(): List<Pago> {
        val db = dbHelper.readableDatabase
        val pagos = mutableListOf<Pago>()
        val cursor: Cursor = db.rawQuery("SELECT * FROM pagos ORDER BY fecha DESC", null)
        if (cursor.moveToFirst()) {
            do {
                pagos.add(cursorToPago(cursor))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return pagos
    }

    // Obtener pagos por DNI de socio
    fun obtenerPorDniSocio(dni: String): List<Pago> {
        val db = dbHelper.readableDatabase
        val pagos = mutableListOf<Pago>()
        val cursor: Cursor = db.rawQuery(
            "SELECT * FROM pagos WHERE dni_socio = ? ORDER BY fecha DESC",
            arrayOf(dni)
        )
        if (cursor.moveToFirst()) {
            do {
                pagos.add(cursorToPago(cursor))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return pagos
    }

    // Convertir un cursor a objeto Pago
    private fun cursorToPago(cursor: Cursor): Pago {
        return Pago(
            id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
            fecha = cursor.getString(cursor.getColumnIndexOrThrow("fecha")),
            monto = cursor.getDouble(cursor.getColumnIndexOrThrow("monto")),
            tipo = cursor.getString(cursor.getColumnIndexOrThrow("tipo")),
            concepto = cursor.getString(cursor.getColumnIndexOrThrow("concepto")),
            dniSocio = cursor.getString(cursor.getColumnIndexOrThrow("dni_socio"))
        )
    }
}