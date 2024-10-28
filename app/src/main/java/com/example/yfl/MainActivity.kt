package com.example.yfl

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var usernameInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var loginButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        usernameInput = findViewById(R.id.usernameInput)
        passwordInput = findViewById(R.id.passwordInput)
        loginButton = findViewById(R.id.loginButton)

        setupInputListeners()

        loginButton.setOnClickListener {
            handleLogin()
        }
    }

    private fun setupInputListeners() {
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                loginButton.isEnabled = isInputValid()
                checkCapsLock(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        }

        usernameInput.addTextChangedListener(textWatcher)
        passwordInput.addTextChangedListener(textWatcher)
    }

    private fun isInputValid(): Boolean {
        val username = usernameInput.text.toString().trim()
        val password = passwordInput.text.toString().trim()
        return username.isNotEmpty() && password.isNotEmpty()
    }

    private fun handleLogin() {
        val username = usernameInput.text.toString().trim()
        val password = passwordInput.text.toString().trim()

        val processedPassword = password.replace("\\s".toRegex(), "")

        if (username == "admin" && processedPassword == "123") {
            Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
        } else {
            showErrorDialog("Invalid username or password.")
        }
    }

    private fun showErrorDialog(message: String) {
        AlertDialog.Builder(this)
            .setTitle("Login Error")
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    private fun checkCapsLock(input: String) {
        val isCapsLockOn = input.isNotEmpty() && input.all { it.isUpperCase() }
        if (isCapsLockOn) {
            Toast.makeText(this, "Caps Lock is ON", Toast.LENGTH_SHORT).show()
        }
    }
}
