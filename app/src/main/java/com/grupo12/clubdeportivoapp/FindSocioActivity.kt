package com.grupo12.clubdeportivoapp

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.grupo12.clubdeportivoapp.databinding.ActivityFindSocioBinding
import com.grupo12.clubdeportivoapp.db.SocioDao

class FindSocio : AppCompatActivity() {
    private lateinit var binding: ActivityFindSocioBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFindSocioBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val modo = intent.getStringExtra("modo") ?: "editar" // "editar" por defecto


        binding.btnBack.setOnClickListener { finish() }

        binding.btnBuscar.setOnClickListener {
            val query = binding.etBusqueda.text.toString().trim().lowercase()
            val socioDao = SocioDao(this)
            val socios = socioDao.obtenerTodos()

            val resultados = if (query.isEmpty()) {
                emptyList()
            } else {
                socios.filter {
                    it.dni.lowercase().contains(query) ||
                            it.nombre.lowercase().contains(query) ||
                            it.apellido.lowercase().contains(query)
                }
            }

            val adapter = ArrayAdapter(
                this,
                android.R.layout.simple_list_item_1,
                resultados.map { "${it.nombre} ${it.apellido} - DNI: ${it.dni}" }
            )
            binding.lvResultados.adapter = adapter

            binding.lvResultados.setOnItemClickListener { _, _, position, _ ->
                val socioSeleccionado = resultados[position]
                if (modo == "perfil") {
                    val intent = Intent(this, PerfilSocioActivity::class.java)
                    intent.putExtra("dni", socioSeleccionado.dni)
                    startActivity(intent)
                } else {
                    val intent = Intent(this, AddSocioActivity::class.java)
                    intent.putExtra("dni", socioSeleccionado.dni)
                    startActivity(intent)
                }
            }
        }
    }
}