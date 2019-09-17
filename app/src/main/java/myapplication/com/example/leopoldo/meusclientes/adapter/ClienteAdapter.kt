package myapplication.com.example.leopoldo.meusclientes.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import myapplication.com.example.leopoldo.meusclientes.R
import myapplication.com.example.leopoldo.meusclientes.model.Cliente
import java.text.ParseException
import java.text.SimpleDateFormat

class ClienteAdapter(private val context: Context, private val listaItens: List<Cliente>) :
    RecyclerView.Adapter<ClienteAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item_layout, parent, false)

        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return listaItens.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val listaItem = listaItens[position]

        holder.nome.setText(listaItem.nome)
        holder.data.text = formataData(listaItem.data!!)

        if(listaItem.isPendente == 1){
            holder.pendente.setTextColor(Color.RED)
            holder.pendente.setText(R.string.pendenteItem)
        } else {
            holder.pendente.setTextColor(Color.GREEN)
            holder.pendente.setText(R.string.naoPendente)
        }
    }

    private fun formataData(data: String): String {
        try {
            val formatoInicial = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val recebeData = formatoInicial.parse(data)
            val formatoFinal = SimpleDateFormat("d MMM, yyyy")

            return formatoFinal.format(recebeData)
        } catch (e: ParseException){
            return ""
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var nome: TextView = view.findViewById(R.id.textViewNome)
        var data: TextView = view.findViewById(R.id.textViewDataAtual)
        var pendente: TextView = view.findViewById(R.id.textViewPendente)
    }
}
