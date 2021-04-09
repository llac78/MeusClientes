package myapplication.com.example.leopoldo.meusclientes.model

class Cliente {

    var id: Int? = 0
    var nome: String? = null
    var data: String? = null
    var isPendente: Int? = 0
    var servicoPrestado: String? = null
    var observacoes: String? = null
    var valorServicoPrestado: Double? = null

    constructor()

    constructor(id: Int, nome: String, servicoPrestado: String, data: String, pendente: Int, obs: String, valorServico: Double){
        this.id = id
        this.nome = nome
        this.servicoPrestado = servicoPrestado
        this.data = data
        this.isPendente = pendente
        this.observacoes = obs
        this.valorServicoPrestado = valorServico
    }

    // buscar referencia para criacao de uma nova tabela no DB
    companion object {
        const val TABLE_NAME = "cliente_db"
        const val COLUMN_ID = "id"
        const val COLUMN_NOME = "nome"
        const val COLUMN_DATA = "data"
        const val COLUMN_PENDENTE = "pendente"
        const val COLUMN_SERVICO_PRESTADO = "servicoPrestado"
        const val COLUMN_VALOR_SERVICO_PRESTADO = "valorServicoPrestado"
        const val COLUMN_OBSERVACOES = "observacoes"

        const val CREATE_TABLE = (
                "CREATE TABLE "
                + TABLE_NAME + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_NOME + " TEXT, "
                + COLUMN_SERVICO_PRESTADO + " TEXT, "
                + COLUMN_DATA + " DATETIME DEFAULT CURRENT_TIMESTAMP, "
                + COLUMN_VALOR_SERVICO_PRESTADO + " REAL, "
                + COLUMN_PENDENTE + " INTEGER, "
                + COLUMN_OBSERVACOES + " TEXT"
                +")"
                )
    }
}