package com.grupo12.clubdeportivoapp

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.grupo12.clubdeportivoapp.databinding.ActivityAddSocioBinding
import com.grupo12.clubdeportivoapp.db.SocioDao
import com.grupo12.clubdeportivoapp.Socio
import java.text.SimpleDateFormat
import java.util.*

class AddSocioActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddSocioBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddSocioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener { finish() }

        // Date picker para fecha de nacimiento
        binding.etFechaNacimiento.setOnClickListener {
            val calendar = Calendar.getInstance()
            val picker = DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    // Formato yyyy-MM-dd
                    val fecha = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth)
                    binding.etFechaNacimiento.setText(fecha)
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            picker.show()
        }

        binding.btnRegistrar.setOnClickListener {
            val nombre = binding.etNombre.text.toString().trim()
            val apellido = binding.etApellido.text.toString().trim()
            val dni = binding.etDni.text.toString().trim()
            val telefono = binding.etTelefono.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val fechaNacimiento = binding.etFechaNacimiento.text.toString().trim()
            val asociado = binding.cbAsociado.isChecked

            // Validaciones simples
            if (nombre.isEmpty() || apellido.isEmpty() || dni.isEmpty() || fechaNacimiento.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos obligatorios", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Validar formato fecha nacimiento (yyyy-MM-dd)
            if (!fechaNacimiento.matches(Regex("\\d{4}-\\d{2}-\\d{2}"))) {
                Toast.makeText(this, "Formato de fecha inválido (usa YYYY-MM-DD)", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val socio = Socio(
                nombre = nombre,
                apellido = apellido,
                dni = dni,
                telefono = telefono,
                email = email,
                fechaNacimiento = fechaNacimiento,
                asociado = asociado,
                vencimiento = "" // Cuando se crea, sin vencimiento inicial
            )

            val socioDao = SocioDao(this)
            val existe = socioDao.obtenerPorDni(dni)
            if (existe != null) {
                Toast.makeText(this, "El socio con ese DNI ya existe", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val id = socioDao.insertar(socio)
            if (id != -1L) {
                Toast.makeText(this, "Socio registrado correctamente", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Error al registrar el socio", Toast.LENGTH_SHORT).show()
            }
        }
    }
}