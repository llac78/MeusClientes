
package myapplication.com.example.leopoldo.meusclientes

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_cliente_details.*
import myapplication.com.example.leopoldo.meusclientes.adapter.ClienteAdapter
import myapplication.com.example.leopoldo.meusclientes.database.ClienteDBHelper
import myapplication.com.example.leopoldo.meusclientes.details.ClienteDetailsActivity
import myapplication.com.example.leopoldo.meusclientes.model.Cliente
import myapplication.com.example.leopoldo.meusclientes.utils.MascaraMonetaria
import kotlin.properties.Delegates


class MainActivity : AppCompatActivity() {

    private var coordinator: CoordinatorLayout? = null
    private var recyclerView: RecyclerView? = null
    private var db: ClienteDBHelper? = null
    private var itemsList = ArrayList<Cliente>()
    private var mAdapter: ClienteAdapter? = null
    private var pendente: Int = 0
    private var textEmpty: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicialização da Toolbar
        val toolbar = findViewById<View>(R.id.toolbar) as androidx.appcompat.widget.Toolbar
        toolbar.setTitleTextColor(Color.WHITE)
        setSupportActionBar(toolbar)

        coordinator = findViewById(R.id.layout_main_coord)
        recyclerView = findViewById(R.id.recyclerMain)
        db = ClienteDBHelper(this)
        textEmpty =  findViewById(R.id.textViewEmpty)

        val fab = findViewById<View>(R.id.fab) as FloatingActionButton
        fab.setOnClickListener {
            showDialog(false, null, -1)
        }

        db?.listagem?.let { itemsList.addAll(it) }
        mAdapter = ClienteAdapter(this, itemsList)

        // Um texto é informado caso a lista esteja vazia
        if (mAdapter?.itemCount!! > 0) textEmpty!!.visibility = View.INVISIBLE

        // Verificação dos eventos ocorridos na lista
        mAdapter?.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver(){
            override fun onChanged() {
                super.onChanged()
                checkEmpty()
            }
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)
                checkEmpty()
            }
            override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
                super.onItemRangeRemoved(positionStart, itemCount)
                checkEmpty()
            }
            fun checkEmpty() {
                textEmpty?.visibility = if (mAdapter?.itemCount == 0) View.VISIBLE else View.INVISIBLE
                recyclerView?.visibility = if (mAdapter?.itemCount == 0) View.INVISIBLE else View.VISIBLE
            }
        })

        val mLayoutManager = LinearLayoutManager(applicationContext)
        recyclerView?.layoutManager = mLayoutManager
        recyclerView?.itemAnimator = DefaultItemAnimator()
        recyclerView?.adapter = mAdapter

        // Função de click
        recyclerView?.addOnItemTouchListener(
            ItemLongPressListener(
                this, recyclerView!!, object : ItemLongPressListener.ClickListener {
                    override fun onClick(view: View, position: Int) {
                        val cliente = itemsList[position]
                        val intent = Intent(this@MainActivity, ClienteDetailsActivity::class.java)
                        intent.putExtra("nome", cliente.nome)
                        intent.putExtra("pendente", cliente.isPendente)
                        intent.putExtra("servicoPrestado", cliente.servicoPrestado)
                        intent.putExtra("valorServicoPrestado", cliente.valorServicoPrestado)
                        intent.putExtra("observacoes", cliente.observacoes)
                        startActivity(intent)
                    }
                    override fun onLongClick(view: View?, position: Int) {
                        showActionsDialog(position)
                    }
                }
            )
        )
    }

    // Exibição do menu de opções de editar, excluir e excluir todos os itens da lista
    private fun showActionsDialog(position: Int) {
        val options = arrayOf<CharSequence>(
            getString(R.string.dialogEditTitulo),
            getString(R.string.excluir),
            getString(R.string.excluirTodos)
        )

        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.tituloOpcao))
        builder.setItems(options) { _, index ->
            when (index) {
                0 -> showDialog(true, itemsList[position], position)
                1 -> showDialogConfirmacaoDelete(itemsList[position], position, index)
                2 -> showDialogConfirmacaoDelete(itemsList[position], position, index)
                else -> Toast.makeText(
                    applicationContext,
                    getString(R.string.toastErro),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        builder.show()
    }

    // Exibição do dialog de cadastro de itens
    private fun showDialog(isUpdate: Boolean, cliente: Cliente?, position: Int) {
        val layoutInflater = LayoutInflater.from(applicationContext)
        val view = layoutInflater.inflate(R.layout.form_dialog, null)
        val userInput = AlertDialog.Builder(this@MainActivity)
        userInput.setView(view) // para ser exibida

        var titulo = view.findViewById<TextView>(R.id.textViewDialogTitle)
        var inputNome = view.findViewById<EditText>(R.id.editTextDialogNome)
        var inputServicoPrestado = view.findViewById<EditText>(R.id.editTextServicoPrestado)
        var inputValorServicoPrestado = view.findViewById<EditText>(R.id.editTextValorServicoPrestado)
        inputValorServicoPrestado.addTextChangedListener(MascaraMonetaria(inputValorServicoPrestado))
        var switchPendente = view.findViewById<Switch>(R.id.switchPendente)
        var inputObservacoes = view.findViewById<EditText>(R.id.edtObservacoes)

        titulo.text = if (!isUpdate) getString(R.string.dialogNovoTitulo) else getString(R.string.dialogEditTitulo)

        if (isUpdate && cliente != null) {
            inputNome.setText(cliente.nome)
            inputServicoPrestado.setText(cliente.servicoPrestado)
            var valor = cliente.valorServicoPrestado?.let { MascaraMonetaria.formatarValor(it) }
            inputValorServicoPrestado.setText(valor)
            inputObservacoes.setText(cliente.observacoes)
            if (cliente.isPendente == 1) switchPendente.toggle()
        }
        userInput
            .setCancelable(false)
            .setPositiveButton(if (isUpdate) getString(R.string.btn_atualizar) else getString(R.string.btn_salvar)) { dialogBox, id -> }
            .setNegativeButton(getString(R.string.btn_cancelar)) { dialogBox, id -> dialogBox.cancel() }

        val alertDialog = userInput.create()
        alertDialog.show()

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(View.OnClickListener {
            if (TextUtils.isEmpty(inputNome.text.toString())) {
                Toast.makeText(this@MainActivity, getString(R.string.toastNome), Toast.LENGTH_SHORT)
                    .show()
                return@OnClickListener
            } else {
                alertDialog.dismiss()
            }
            if (isUpdate && cliente != null) {
                pendente = if (switchPendente.isChecked) 1 else 0
                atualizarCliente(inputNome.text.toString(), pendente, position, inputServicoPrestado.text.toString(),
                    inputValorServicoPrestado.text.toString(), inputObservacoes.text.toString())
            } else {
                pendente = if (switchPendente.isChecked) 1 else 0
                createCliente(inputNome.text.toString(), pendente, inputServicoPrestado.text.toString(),
                    inputValorServicoPrestado.text.toString(), inputObservacoes.text.toString())
            }
        })
    }

    // Exibição do dialog de exclusão de itens
    private fun showDialogConfirmacaoDelete(cliente: Cliente?, position: Int, index: Int) {
        val layoutInflater = LayoutInflater.from(applicationContext)
        val view = layoutInflater.inflate(R.layout.confirmacao_dialog, null)
        val userInput = AlertDialog.Builder(this@MainActivity)
        userInput.setView(view) // para ser exibida

        var titulo = view.findViewById<TextView>(R.id.textViewDialogTitleConfirmation)
        var texto = view.findViewById<TextView>(R.id.textViewDialogConfirmation)
        titulo.text = getString(R.string.atencao)
        if (index == 1)  {
            texto.text = getString(R.string.confirmacao_excluir_item)
        } else {
            texto.text = getString(R.string.confirmacao_excluir_todos)
        }
        userInput
            .setCancelable(false)
            .setPositiveButton(getString(R.string.btn_excluir) ) { dialogBox, id -> }
            .setNegativeButton(getString(R.string.btn_cancelar)) { dialogBox, id -> dialogBox.cancel() }

        val alertDialog = userInput.create()
        alertDialog.show()
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(View.OnClickListener {
            if (index == 1)  deleteItem(position) else deleteAll()
            alertDialog.dismiss()
        })
    }

    // ********** CRUD METHODS **********

    private fun atualizarCliente(
        nome: String,
        pendente: Int,
        position: Int,
        servicoPrestado: String,
        valorServicoPrestado: String,
        obs: String
    ) {
        var valor by Delegates.notNull<Double>()
        if(valorServicoPrestado.contains(getString(R.string.label_moeda))){
            valor = valorServicoPrestado.replace(getString(R.string.label_moeda), " ")
                .replace(".","")
                .replace(",",".").trim().toDouble()
        }
        val item = itemsList[position]
        item.nome = nome
        item.servicoPrestado = servicoPrestado
        item.valorServicoPrestado = valor
        item.observacoes = obs
        item.isPendente = pendente
        db!!.atualizarItem(item)

        itemsList[position] = item
        mAdapter!!.notifyItemChanged(position)
        Toast.makeText(this, getString(R.string.toast_update), Toast.LENGTH_SHORT).show()
    }

    private fun createCliente(
        nome: String,
        pendente: Int,
        servicoPrestado: String,
        valorServicoPrestado: String,
        obs: String
    ) {
        var valor by Delegates.notNull<Double>()
        if(valorServicoPrestado.contains(getString(R.string.label_moeda))){
            valor = valorServicoPrestado.replace(getString(R.string.label_moeda), " ")
                                        .replace(".","")
                                        .replace(",",".").trim().toDouble()
            Log.d("LLL", valor.toString() + "----------------------")
        }
        val item = db?.inserirCliente(nome, servicoPrestado, pendente, valor, obs)
        val novoItem = item?.let { db?.getCliente(it) } // buscar item que acabou de ser adicionado no DB

        if (novoItem != null) {
            itemsList.add(0, novoItem)
            mAdapter?.notifyDataSetChanged()
        }
        Toast.makeText(this, getString(R.string.toast_create), Toast.LENGTH_SHORT).show()
    }

    private fun deleteItem(position: Int) {
        db?.deleteItem(itemsList[position])
        itemsList.removeAt(position)
        mAdapter?.notifyItemRemoved(position)
        Toast.makeText(this, getString(R.string.toast_delete), Toast.LENGTH_SHORT).show()
    }

    private fun deleteAll() {
        db?.deleteAll()
        itemsList.removeAll(itemsList)
        mAdapter?.notifyDataSetChanged()
        Toast.makeText(this, getString(R.string.toast_delete_all), Toast.LENGTH_SHORT).show()
    }
}


