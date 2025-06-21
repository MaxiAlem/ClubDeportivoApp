package com.grupo12.clubdeportivoapp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.grupo12.clubdeportivoapp.databinding.ActivityAddSocioBinding
import com.grupo12.clubdeportivoapp.db.SocioDao

class AddSocioActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddSocioBinding
    private var modoEdicion = false
    private var socioExistente: Socio? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddSocioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dniIntent = intent.getStringExtra("dni")
        val dao = SocioDao(this)

        if (dniIntent != null) {
            // Modo edición
            modoEdicion = true
            socioExistente = dao.obtenerPorDni(dniIntent)
            if (socioExistente != null) {
                llenarCampos(socioExistente!!)
                binding.etDni.isEnabled = false // No permitir editar DNI
                binding.btnRegistrar.text = "Guardar cambios"
            } else {
                Toast.makeText(this, "Socio no encontrado", Toast.LENGTH_SHORT).show()
                finish()
                return
            }
        } else {
            // Modo alta
            binding.etDni.isEnabled = true
            binding.btnRegistrar.text = "Registrar socio"
        }

        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnRegistrar.setOnClickListener {
            if (validateForm()) {
                if (modoEdicion && socioExistente != null) {
                    // Editar socio existente
                    val socioEditado = socioExistente!!.copy(
                        nombre = binding.etNombre.text.toString(),
                        apellido = binding.etApellido.text.toString(),
                        telefono = binding.etTelefono.text.toString(),
                        email = binding.etEmail.text.toString(),
                        fechaNacimiento = binding.etFechaNacimiento.text.toString(),
                        asociado = binding.cbAsociado.isChecked
                        // vencimiento: lo que corresponda, si querés mantenerlo igual podés dejarlo así
                    )
                    dao.actualizar(socioEditado)
                    Toast.makeText(this, "Socio actualizado", Toast.LENGTH_SHORT).show()
                } else {
                    // Registrar nuevo socio
                    val socio = Socio(
                        nombre = binding.etNombre.text.toString(),
                        apellido = binding.etApellido.text.toString(),
                        dni = binding.etDni.text.toString(),
                        telefono = binding.etTelefono.text.toString(),
                        email = binding.etEmail.text.toString(),
                        fechaNacimiento = binding.etFechaNacimiento.text.toString(),
                        asociado = binding.cbAsociado.isChecked,
                        vencimiento = ""
                    )
                    dao.insertar(socio)
                    Toast.makeText(this, "Socio registrado", Toast.LENGTH_SHORT).show()
                }
                finish()
            } else {
                Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun llenarCampos(socio: Socio) {
        binding.etNombre.setText(socio.nombre)
        binding.etApellido.setText(socio.apellido)
        binding.etDni.setText(socio.dni)
        binding.etTelefono.setText(socio.telefono)
        binding.etEmail.setText(socio.email)
        binding.etFechaNacimiento.setText(socio.fechaNacimiento)
        binding.cbAsociado.isChecked = socio.asociado
    }

    private fun validateForm(): Boolean {
        return binding.etNombre.text.toString().isNotEmpty() &&
                binding.etApellido.text.toString().isNotEmpty() &&
                binding.etDni.text.toString().isNotEmpty() &&
                binding.etTelefono.text.toString().isNotEmpty()
    }
}