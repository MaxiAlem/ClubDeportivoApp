package com.grupo12.clubdeportivoapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PagoAdapter(private val pagos: List<Pago>) : RecyclerView.Adapter<PagoAdapter.PagoViewHolder>() {

    inner class PagoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNombreSocio: TextView = itemView.findViewById(R.id.tv_nombre_socio)
        val tvDetallePago: TextView = itemView.findViewById(R.id.tv_detalle_pago)
        val tvFecha: TextView = itemView.findViewById(R.id.tv_fecha)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_pago, parent, false)
        return PagoViewHolder(view)
    }

    override fun onBindViewHolder(holder: PagoViewHolder, position: Int) {
        val pago = pagos[position]
        holder.tvNombreSocio.text = "DNI: ${pago.dniSocio ?: "-"}"
        holder.tvDetallePago.text = "Monto: $${pago.monto}  |  Tipo: ${pago.tipo}  |  Concepto: ${pago.concepto}"
        holder.tvFecha.text = pago.fecha
    }

    override fun getItemCount(): Int = pagos.size
}