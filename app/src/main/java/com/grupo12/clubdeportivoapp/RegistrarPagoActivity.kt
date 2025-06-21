package com.grupo12.clubdeportivoapp

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.grupo12.clubdeportivoapp.databinding.ActivityRegistrarPagoBinding
import com.grupo12.clubdeportivoapp.db.PagoDao
import com.grupo12.clubdeportivoapp.db.SocioDao
import java.text.SimpleDateFormat
import java.util.*

class RegistrarPagoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistrarPagoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrarPagoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViews()
    }

    private fun setupViews() {
        val dni = intent.getStringExtra("dni")
        if (dni.isNullOrEmpty()) {
            Toast.makeText(this, "No se recibió el DNI del socio.", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val socioDao = SocioDao(this)
        val socio = socioDao.obtenerPorDni(dni)
        if (socio == null) {
            Toast.makeText(this, "No se encontró el socio.", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        binding.tvNombre.text = socio.nombre
        binding.tvDni.text = "DNI: ${socio.dni}"

        binding.spTipoPago.adapter = ArrayAdapter.createFromResource(
            this,
            R.array.tipos_pago,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        binding.toggleGroupConcepto.addOnButtonCheckedListener { group, checkedId, isChecked ->
            if (isChecked) {
                val label = when (checkedId) {
                    R.id.btnMensual -> "Concepto: Pago Mensual"
                    R.id.btnDiario -> "Concepto: Pago Diario"
                    else -> "Concepto: Pago Mensual"
                }
                binding.tvConceptoPago.text = label
            }
        }



        binding.btnBack.setOnClickListener { finish() }

        binding.btnRegistrarPago.setOnClickListener {
            val montoStr = binding.etMonto.text.toString()
            if (montoStr.isEmpty()) {
                Toast.makeText(this, "Ingrese un monto válido", Toast.LENGTH_SHORT).show()
            } else {
                val monto = montoStr.toDoubleOrNull()
                if (monto == null || monto <= 0) {
                    Toast.makeText(this, "Monto inválido", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                // OBTENER EL CONCEPTO EN ESTE MOMENTO, NO ANTES
                val concepto = when (binding.toggleGroupConcepto.checkedButtonId) {
                    R.id.btnMensual -> "Pago Mensual"
                    R.id.btnDiario -> "Pago Diario"
                    else -> "Pago Mensual"
                }
                registrarPago(monto, socio, concepto)
            }
        }
    }

    private fun registrarPago(monto: Double, socio: Socio, concepto: String) {
        val tipoPago = binding.spTipoPago.selectedItem.toString()
        val pagoDao = PagoDao(this)
        val socioDao = SocioDao(this)
        val fechaActual = obtenerFechaActual()

        // Guardar el pago en la base
        val pago = Pago(
            fecha = fechaActual,
            monto = monto,
            tipo = tipoPago,
            concepto = concepto,
            dniSocio = socio.dni
        )
        pagoDao.insertarPago(pago)

        // Actualizar vencimiento del socio según el tipo de concepto
        val nuevoVencimiento = calcularNuevaVigencia(socio.vencimiento, concepto)
        socioDao.actualizarVencimiento(socio.dni, nuevoVencimiento)

        // Si pagó un mes y no era asociado, actualizar estado
        if (concepto == "Pago Mensual" && !socio.asociado) {
            val socioActualizado = socio.copy(asociado = true)
            socioDao.actualizar(socioActualizado)
        }

        Toast.makeText(
            this,
            "Pago registrado: $$monto ($tipoPago) para ${socio.nombre}. Nuevo vencimiento: $nuevoVencimiento",
            Toast.LENGTH_LONG
        ).show()
        finish()
    }

    private fun obtenerFechaActual(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(Date())
    }

    private fun calcularNuevaVigencia(vencimientoActual: String, concepto: String): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return try {
            val fecha = sdf.parse(vencimientoActual)
            val calendar = Calendar.getInstance()
            calendar.time = fecha ?: Date()
            if (concepto == "Pago Mensual") {
                calendar.add(Calendar.MONTH, 1)
            } else {
                calendar.add(Calendar.DAY_OF_YEAR, 1)
            }
            sdf.format(calendar.time)
        } catch (e: Exception) {
            val calendar = Calendar.getInstance()
            if (concepto == "Pago Mensual") {
                calendar.add(Calendar.MONTH, 1)
            } else {
                calendar.add(Calendar.DAY_OF_YEAR, 1)
            }
            sdf.format(calendar.time)
        }
    }
}