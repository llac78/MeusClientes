package myapplication.com.example.leopoldo.meusclientes.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import myapplication.com.example.leopoldo.meusclientes.model.Cliente

private val DB_NAME = "cliente_db"

class ClienteDBHelper(context: Context) : SQLiteOpenHelper
    (context, DB_NAME, null, 1){

    val listagem: List<Cliente>
        get() {
            val listaClientes = ArrayList<Cliente>()
            val query = "SELECT * FROM " + Cliente.TABLE_NAME +
                    " ORDER BY " + Cliente.COLUMN_DATA + " DESC "

            val db = this.writableDatabase
            var cursor = db.rawQuery(query, null)

            if (cursor.moveToFirst()){
                do {
                    val item = Cliente()
                    item.id = cursor.getInt(cursor.getColumnIndex(Cliente.COLUMN_ID))
                    item.nome = cursor.getString(cursor.getColumnIndex(Cliente.COLUMN_NOME))
                    item.data = cursor.getString(cursor.getColumnIndex(Cliente.COLUMN_DATA))
                    item.isPendente = cursor.getInt(cursor.getColumnIndex(Cliente.COLUMN_PENDENTE))

                    listaClientes.add(item)

                } while (cursor.moveToNext())
            }
            db.close()

            return listaClientes
        }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(Cliente.CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + Cliente.TABLE_NAME)

        onCreate(db)
    }

    fun inserirCliente(item: String, pendente: Int): Long {
        val db = this.writableDatabase

        val values = ContentValues()
        values.put(Cliente.COLUMN_NOME, item)
        values.put(Cliente.COLUMN_PENDENTE, pendente)

        val item = db.insert(  Cliente.TABLE_NAME, null, values)
        db.close()

        return item
    }

    fun getCliente(item: Long): Cliente {
        val db = this.readableDatabase

        val cursor = db.query(Cliente.TABLE_NAME,
                     arrayOf(Cliente.COLUMN_ID, Cliente.COLUMN_NOME, Cliente.COLUMN_DATA, Cliente.COLUMN_PENDENTE),
            Cliente.COLUMN_ID + "=?", arrayOf(item.toString()), null, null, null, null)

        cursor?.moveToFirst()

        val listaItem = Cliente(
            cursor!!.getInt(cursor.getColumnIndex(Cliente.COLUMN_ID)),
            cursor.getString(cursor.getColumnIndex(Cliente.COLUMN_NOME)),
            cursor.getString(cursor.getColumnIndex(Cliente.COLUMN_DATA)),
            cursor!!.getInt(cursor.getColumnIndex(Cliente.COLUMN_PENDENTE))
        )
        cursor.close()

        return listaItem
    }

    fun deleteItem(cliente: Cliente){
        val db = this.writableDatabase
        db.delete(Cliente.TABLE_NAME, Cliente.COLUMN_ID + " = ?", arrayOf(cliente.id.toString()) )

        db.close()
    }

    fun deleteAll(){
        val db = this.writableDatabase
        db.delete(Cliente.TABLE_NAME, Cliente.COLUMN_ID + " > ? ", arrayOf("0"))

        db.close()
    }

    fun atualizarItem(cliente: Cliente): Int {
        val db = this.writableDatabase

        val values = ContentValues()
        values.put(Cliente.COLUMN_NOME, cliente.nome)
        values.put(Cliente.COLUMN_PENDENTE, cliente.isPendente)

        return db.update(Cliente.TABLE_NAME, values, Cliente.COLUMN_ID + " = ? ",
            arrayOf(cliente.id.toString()))
    }
}