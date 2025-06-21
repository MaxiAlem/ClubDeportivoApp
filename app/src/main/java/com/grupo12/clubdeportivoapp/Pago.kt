package com.grupo12.clubdeportivoapp

data class Pago(
    val id: Int = 0,                    // autoincremental, lo pone la base
    val fecha: String,                   // fecha y hora del pago (ej: "2025-06-21")
    val monto: Double,
    val tipo: String,
    val concepto: String,                // "Pago Mensual", "Pago Diario", "Alta Socio", etc
    val dniSocio: String? = null         // dni del socio (nullable para no socio)??
)