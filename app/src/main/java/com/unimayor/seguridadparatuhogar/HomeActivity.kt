package com.unimayor.seguridadparatuhogar


import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.unimayor.seguridadparatuhogar.adapter.MyAdapter
import kotlin.collections.ArrayList

class HomeActivity : AppCompatActivity() {

    private lateinit var logout : Button
    private lateinit var recycler : RecyclerView
    private lateinit var myFirebaseMessagingService: MyFirebaseMessagingService

    // Obtén una referencia a la instancia de la base de datos
    val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    val myRef: DatabaseReference = database.getReference("/test/json/movimiento")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        myFirebaseMessagingService = MyFirebaseMessagingService()

        logout = findViewById(R.id.logout)
        recycler = findViewById(R.id.recycler_view)
        val context = this

        logout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            onBackPressed()
        }

        getFirebaseData(context)

    }

    private fun getFirebaseData(context: Context){
        myRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    recycler.layoutManager = LinearLayoutManager(context)

                    val value = dataSnapshot.value

                    value.let {
                        val adapter = MyAdapter(it as ArrayList<String>)
                        recycler.adapter = adapter

                        val title = getString(R.string.title)
                        val body = getString(R.string.body)

                        myFirebaseMessagingService.showNotification(context, title, body)
                    }

                } else {
                    Log.e("Firebase", "Error no existe el nodo que estas buscando!")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Error en la consulta: ${error.message}")
            }
        })
    }
}