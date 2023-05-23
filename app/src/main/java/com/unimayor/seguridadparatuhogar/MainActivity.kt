package com.unimayor.seguridadparatuhogar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : AppCompatActivity() {
    /**
     * Instancia de recursos XML para
     * utilizarlos en la clase MainActivity.kt
     * **/
    private lateinit var buttonInit: Button
    private lateinit var buttonRegister: Button
    private lateinit var username: EditText
    private lateinit var password: EditText

    /**
     * Metodo principal de la clase
     * **/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /**
         * Inicializacion de recursos
         * buscandolos por id dentro del XML
         * **/
        buttonInit = findViewById(R.id.login)
        buttonRegister = findViewById(R.id.register)
        username = findViewById(R.id.username)
        password = findViewById(R.id.password)

        /**
         * Llamado a la función notification()
         * **/
        notification()
        /**
         * Llamado a la función setup()
         * **/
        setup()

    }

    /**
     * función notification() se encarga de obtener el token
     * de indeitificacion de usuario unico para el envio de
     * notificaciones personalizadas desde firebase al movil
     * **/
    private fun notification() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val token = task.result
                println("Este es el token del dispositivo: $token")
            }
        }
    }

    /**
     * función setup() se registran en Firebase
     * los usuarios con username y password
     * mediante el llamdo de la funcion createUserWithEmailAndPassword
     * se valida el estado de la repuesta addOnCompleteListener
     * si el estado es it.isSuccessful = true entonces se accedera
     * al HomeActivity de lo contrario se mostrata un mensaje de
     * alerta, del mismo modo el incio de sesión mediante Firebase
     * llamando a la funcion signInWithEmailAndPassword el usuario
     * debe ingresar username y password previamente registrados
     * para entrar al HomeActivity, de lo conrtario se mostrará
     * un mensaje de error.
     * **/
    private fun setup() {
        title = "Autenticación"
        buttonRegister.setOnClickListener {
            if(username.text.isNotEmpty() && password.text.isNotEmpty()){

                FirebaseAuth.getInstance().createUserWithEmailAndPassword(username.text.toString(),
                    password.text.toString()).addOnCompleteListener {

                    if(it.isSuccessful){
                        showHome()
                    }else{
                        showAlert()
                    }
                }
            }
        }
        buttonInit.setOnClickListener {
            if(username.text.isNotEmpty() && password.text.isNotEmpty()){
                FirebaseAuth.getInstance().signInWithEmailAndPassword(username.text.toString(),
                    password.text.toString()).addOnCompleteListener {

                    if(it.isSuccessful){
                        showHome()
                    }else{
                        showAlert()
                    }
                }
            }
        }
    }

    /**
     * función showAlert() se crea un AlertDialog
     * para mostrar un mensaje de error
     * **/
    private fun showAlert(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Error al ingresar el usuario")
        builder.setPositiveButton("Aceptar", null)
        val dialog : AlertDialog = builder.create()
        dialog.show()
    }

    /**
     * función showHome() se crea un Intent
     * para la navegacion entre Actividades
     * **/
    private fun showHome(){
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }
}