package myapplication.com.example.leopoldo.meusclientes

import android.content.Context
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class ItemLongPressListener(context: Context, recyclerView: RecyclerView, val listener: ClickListener?)
    : RecyclerView.OnItemTouchListener {

    // para identificar o click, se é um simples ou longo
    private val gestureDetector: GestureDetector
    init {
        gestureDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener(){
            override fun onSingleTapUp(e: MotionEvent?): Boolean {
                return true
            }

            override fun onLongPress(e: MotionEvent) {
                val child = recyclerView.findChildViewUnder(e.x, e.y) // buscar o item clicado pelas coordenadas x e y
                if (child != null && listener != null){
                    listener.onLongClick(child, recyclerView.getChildAdapterPosition(child))
                }
            }
        })
    }

    // para o click simples
    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
        val child = rv.findChildViewUnder(e.x, e.y)
        if(child != null && listener != null && gestureDetector.onTouchEvent(e)){
            listener.onClick(child, rv.getChildAdapterPosition(child))
        }

        return false
    }

    // métodos implementados não utilizados
    override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}
    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}

    interface ClickListener {
        fun onClick(view: View, position: Int)
        fun onLongClick(view: View?, position: Int)
    }
}