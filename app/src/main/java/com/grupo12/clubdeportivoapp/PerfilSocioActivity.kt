package com.grupo12.clubdeportivoapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.grupo12.clubdeportivoapp.databinding.ActivityPerfilSocioBinding
import com.grupo12.clubdeportivoapp.db.SocioDao

class PerfilSocioActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPerfilSocioBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPerfilSocioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dni = intent.getStringExtra("dni")
        if (dni.isNullOrEmpty()) {
            Toast.makeText(this, "No se encontró el socio", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val socioDao = SocioDao(this)
        val socio = socioDao.obtenerPorDni(dni)
        if (socio == null) {
            Toast.makeText(this, "No se encontró el socio", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        with(binding) {
            // Botón Atrás
            btnBack.setOnClickListener { finish() }

            // Botón Editar
            btnEditar.setOnClickListener {
                val intent = Intent(this@PerfilSocioActivity, AddSocioActivity::class.java)
                intent.putExtra("dni", socio.dni)
                startActivity(intent)
            }

            // Botón Ver Clases - Navega a MantenimientoActivity
            btnVerClases.setOnClickListener {
                startActivity(Intent(this@PerfilSocioActivity, MantenimientoActivity::class.java))
            }

            // Botón Guardar
            btnGuardar.setOnClickListener { showDatosGuardadosDialog() }

            // Botón Registrar Pago - Navega a RegistrarPagoActivity
            btnRegistrarPagoDni.setOnClickListener {
                startActivity(
                    Intent(this@PerfilSocioActivity, RegistrarPagoActivity::class.java).apply {
                        putExtra("dni", socio.dni)
                    }
                )
            }

            // Mostrar datos
            tvNombre.text = "${socio.nombre} ${socio.apellido}"
            tvDni.text = "DNI: ${socio.dni}"
            tvTelefono.text = "Teléfono: ${socio.telefono}"
            tvEmail.text = "Email: ${socio.email}"
            tvFechaNacimiento.text = "Fecha Nacimiento: ${socio.fechaNacimiento}"
            tvEstado.text = "Estado: " + if (socio.asociado) "Asociado" else "No Asociado"
            tvVencimiento.text = "Vencimiento: ${socio.vencimiento}"
        }
    }

    private fun showDatosGuardadosDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_guardar_ok, null)
        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        dialogView.findViewById<android.widget.Button>(R.id.btn_cerrar).setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun mostrarMensaje(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
    }
}