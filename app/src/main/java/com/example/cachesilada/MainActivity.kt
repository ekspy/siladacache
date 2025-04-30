package com.example.cachesilada

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private lateinit var editUsuario: EditText
    private lateinit var editSenha: EditText
    private lateinit var editItem: EditText
    private lateinit var btnLogin: Button
    private lateinit var btnSalvar: Button
    private lateinit var btnSegurar: Button
    private lateinit var btnLogoff: Button
    private lateinit var messageText: TextView
    private lateinit var itemInputContainer: View
    private val nomeArquivoCache = "login_cache.txt"
    private var isLoggedIn = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editUsuario = findViewById(R.id.edtUsuario)
        editSenha = findViewById(R.id.edtSenha)
        editItem = findViewById(R.id.edtItem)
        btnLogin = findViewById(R.id.btnLogin)
        btnSalvar = findViewById(R.id.btnSalvar)
        btnSegurar = findViewById(R.id.btnSegurar)
        btnLogoff = findViewById(R.id.btnLogoff)
        messageText = findViewById(R.id.messageText)
        itemInputContainer = findViewById(R.id.itemInputContainer)

        checkLoginStatus()

        btnSalvar.setOnClickListener {
            val usuario = editUsuario.text.toString()
            val senha = editSenha.text.toString()
            if (usuario.isNotEmpty() && senha.isNotEmpty()) {
                salvarUsuarioSenha(usuario, senha)
                Toast.makeText(this, "Credenciais salvas", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Preencha usuário e senha", Toast.LENGTH_SHORT).show()
            }
        }

        btnLogin.setOnClickListener {
            val usuario = editUsuario.text.toString()
            val senha = editSenha.text.toString()
            if (verificarLogin(usuario, senha)) {
                isLoggedIn = true
                updateUI()
                Toast.makeText(this, "Login feito com sucesso", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Usuário ou senha incorretos", Toast.LENGTH_SHORT).show()
            }
        }

        btnSegurar.setOnClickListener {
            val item = editItem.text.toString()
            if (item.isNotEmpty()) {
                itemInputContainer.visibility = View.GONE
                messageText.text = "Infelizmente não podemos segurar isso sem verba"
                messageText.visibility = View.VISIBLE
                Handler(Looper.getMainLooper()).postDelayed({
                    messageText.visibility = View.GONE
                    itemInputContainer.visibility = View.VISIBLE
                    editItem.text.clear()
                }, 5000)
            } else {
                Toast.makeText(this, "Digite o item a segurar", Toast.LENGTH_SHORT).show()
            }
        }

        btnLogoff.setOnClickListener {
            isLoggedIn = false
            deleteCacheFile()
            updateUI()
            Toast.makeText(this, "Logoff realizado", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkLoginStatus() {
        val cacheFile = File(cacheDir, nomeArquivoCache)
        if (cacheFile.exists()) {
            isLoggedIn = true
        }
        updateUI()
    }

    private fun updateUI() {
        if (isLoggedIn) {
            editUsuario.visibility = View.GONE
            editSenha.visibility = View.GONE
            btnLogin.visibility = View.GONE
            btnSalvar.visibility = View.GONE
            itemInputContainer.visibility = View.VISIBLE
            btnLogoff.visibility = View.VISIBLE
        } else {
            editUsuario.visibility = View.VISIBLE
            editSenha.visibility = View.VISIBLE
            btnLogin.visibility = View.VISIBLE
            btnSalvar.visibility = View.VISIBLE
            itemInputContainer.visibility = View.GONE
            btnLogoff.visibility = View.GONE
            messageText.visibility = View.GONE
        }
    }

    private fun salvarUsuarioSenha(usuario: String, senha: String) {
        try {
            val arquivo = File(cacheDir, nomeArquivoCache)
            FileWriter(arquivo).use { writer ->
                writer.write("$usuario;$senha")
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun verificarLogin(usuarioDigitado: String, senhaDigitada: String): Boolean {
        return try {
            val arquivo = File(cacheDir, nomeArquivoCache)
            if (!arquivo.exists()) {
                false
            } else {
                FileReader(arquivo).use { reader ->
                    BufferedReader(reader).use { bufferedReader ->
                        val linha = bufferedReader.readLine()
                        val dados = linha.split(";")
                        val usuarioSalvo = dados[0]
                        val senhaSalva = dados[1]
                        (usuarioDigitado == usuarioSalvo && senhaDigitada == senhaSalva)
                    }
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }

    private fun deleteCacheFile() {
        val arquivo = File(cacheDir, nomeArquivoCache)
        if (arquivo.exists()) {
            arquivo.delete()
        }
    }
}