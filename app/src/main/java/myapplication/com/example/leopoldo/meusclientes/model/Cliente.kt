package myapplication.com.example.leopoldo.meusclientes.model

class Cliente {

    var id: Int? = 0
    var nome: String? = null
    var data: String? = null
    var isPendente: Int? = 0

    constructor()

    constructor(id: Int, nome: String, data: String, pendente: Int){
        this.id = id
        this.nome = nome
        this.data = data
        this.isPendente = pendente
    }

    // buscar referencia para criacao de uma nova tabela no DB
    companion object {
        val TABLE_NAME = "cliente_db"

        val COLUMN_ID = "id"
        val COLUMN_NOME = "nome"
        val COLUMN_DATA = "data"
        val COLUMN_PENDENTE = "pendente"

        val CREATE_TABLE = (
                "CREATE TABLE "
                + TABLE_NAME + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_NOME + " TEXT, "
                + COLUMN_DATA + " DATETIME DEFAULT CURRENT_TIMESTAMP, "
                + COLUMN_PENDENTE + " INTEGER"
                +")"
                )

    }
}