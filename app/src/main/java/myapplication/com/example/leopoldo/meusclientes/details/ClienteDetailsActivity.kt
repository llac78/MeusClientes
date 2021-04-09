package myapplication.com.example.leopoldo.meusclientes.details

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import myapplication.com.example.leopoldo.meusclientes.R
import myapplication.com.example.leopoldo.meusclientes.utils.MascaraMonetaria

class ClienteDetailsActivity : AppCompatActivity() {

    private lateinit var txtNome: TextView
    private lateinit var txtPendente: TextView
    private lateinit var txtServicoPrestado: TextView
    private lateinit var txtValorServicoPrestado: TextView
    private lateinit var txtObservacoes: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cliente_details)

        // Inicialização da Toolbar
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        toolbar.setTitleTextColor(Color.WHITE)
        toolbar.title = "Detalhes"
        setSupportActionBar(toolbar)

        val actionbar = supportActionBar
        actionbar?.setDisplayHomeAsUpEnabled(true)

        txtNome = findViewById(R.id.txtNome)
        txtPendente = findViewById(R.id.txtPendente)
        txtServicoPrestado = findViewById(R.id.txtServicoPrestado)
        txtValorServicoPrestado = findViewById(R.id.txtValorServico)
        txtObservacoes = findViewById(R.id.txtObservacoes)

        var intent = intent

        var nome = intent.getStringExtra("nome")
        txtNome.text = nome

        var servicoPrestado = intent.getStringExtra("servicoPrestado")
        txtServicoPrestado.text = servicoPrestado

        var valorServicoPrestado = intent.getDoubleExtra("valorServicoPrestado", 0.0)
        var valor = MascaraMonetaria.formatarValor(valorServicoPrestado).replace("$", "$ ")
        txtValorServicoPrestado.text = valor

        var observacoes = intent.getStringExtra("observacoes")
        txtObservacoes.text = observacoes

        var pendente = intent.getIntExtra("pendente", 0)
        if(pendente == 0) {
            txtPendente.text = getString(R.string.naoPendente)
            txtPendente.setTextColor(Color.rgb(24, 138, 29))
            txtValorServicoPrestado.setTextColor(Color.rgb(24, 138, 29))
        } else {
            txtPendente.text = getString(R.string.pendenteItem)
            txtPendente.setTextColor(Color.RED)
            txtValorServicoPrestado.setTextColor(Color.RED)
        }
    }
}