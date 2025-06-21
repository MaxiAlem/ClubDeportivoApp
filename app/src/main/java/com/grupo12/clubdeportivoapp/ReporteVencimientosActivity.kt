package com.grupo12.clubdeportivoapp

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.grupo12.clubdeportivoapp.databinding.ActivityReporteVencimientosBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ReporteVencimientosActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReporteVencimientosBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReporteVencimientosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnDescargarReporte.setOnClickListener {
            showReporteGeneradoDialog()
        }

        // Obtiene dinámicamente los socios con vencimiento vencido o que vence hoy
        val usuariosVencidos = obtenerSociosVencidos()
        binding.rvUsuariosVencidos.layoutManager = LinearLayoutManager(this)
        binding.rvUsuariosVencidos.adapter = VencimientosAdapter(usuariosVencidos)
    }

    private fun obtenerSociosVencidos(): List<String> {
        val socioDao = com.grupo12.clubdeportivoapp.db.SocioDao(this)
        val todos = socioDao.obtenerTodos()
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val sdfMostrar = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val hoy = sdf.format(Date())

        return todos.filter { socio ->
            socio.vencimiento <= hoy
        }.map { socio ->
            val fechaVenc = try {
                sdfMostrar.format(sdf.parse(socio.vencimiento)!!)
            } catch (e: Exception) {
                socio.vencimiento
            }
            "${socio.nombre} ${socio.apellido} - Vence: $fechaVenc"
        }
    }

    private fun showReporteGeneradoDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_reporte_generado, null)
        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        val currentDate = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date())
        dialogView.findViewById<TextView>(R.id.tv_nombre_archivo).text = "reporte_vencimientos-$currentDate.pdf"

        dialogView.findViewById<Button>(R.id.btn_cerrar).setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }
}

class VencimientosAdapter(private val usuarios: List<String>) :
    androidx.recyclerview.widget.RecyclerView.Adapter<VencimientosAdapter.ViewHolder>() {

    class ViewHolder(itemView: android.view.View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        val textView: android.widget.TextView = itemView.findViewById(android.R.id.text1)
    }

    override fun onCreateViewHolder(parent: android.view.ViewGroup, viewType: Int): ViewHolder {
        val view = android.view.LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_1, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView.text = usuarios[position]
    }

    override fun getItemCount(): Int = usuarios.size
}