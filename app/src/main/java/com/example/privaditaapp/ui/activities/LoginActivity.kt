package com.example.privaditaapp.ui.activities

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.privaditaapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase


class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    // UI elements
    private lateinit var loginButton: Button
    private lateinit var usernameField: EditText
    private lateinit var passwordField: EditText
    private lateinit var registerButton: Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)

        loginButton = findViewById(R.id.loginButton)
        usernameField = findViewById(R.id.usernameField)
        passwordField = findViewById(R.id.passwordField)
        registerButton = findViewById(R.id.registerButton)

        auth = Firebase.auth
        firestore = FirebaseFirestore.getInstance()

        val intent = Intent(this, MainActivity::class.java)

        loginButton.setOnClickListener {
            val email = usernameField.text.toString()
            val password = passwordField.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                signWithCredentials(email, password)
                Log.d(TAG, "!!!!Email: $email")
                Log.d(TAG, "!!!!Password: $password")

            } else {
                Toast.makeText(
                    baseContext,
                    "Email and password must not be empty.",
                    Toast.LENGTH_SHORT,
                ).show()
            }
        }

        registerButton.setOnClickListener {
            val email = usernameField.text.toString()
            val password = passwordField.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                createUser(email, password)
            } else {
                Toast.makeText(
                    baseContext,
                    "Email and password must not be empty.",
                    Toast.LENGTH_SHORT,
                ).show()
            }
        }
    }

    private fun createUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "createUserWithEmail:success")
                    val user = auth.currentUser
                    val userData = hashMapOf(
                        "email" to email,
                        "uid" to user!!.uid
                    )
                    firestore.collection("users").document(user.uid).set(userData)
                        .addOnSuccessListener {
                            Log.d(TAG, "DocumentSnapshot successfully written!")
                        }
                        .addOnFailureListener { e ->
                            Log.w(TAG, "Error writing document", e)
                        }
                } else {
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext,
                        "User creation failed: ${task.exception?.message}",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
    }

    private fun signWithCredentials(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    val user = auth.currentUser
                    // Access Firestore
                    val userData = hashMapOf(
                        "email" to email,
                        "uid" to user!!.uid
                    )
                    firestore.collection("users").document(user.uid).set(userData)
                        .addOnSuccessListener {
                            Log.d(TAG, "DocumentSnapshot successfully written!")
                        }
                        .addOnFailureListener { e ->
                            Log.w(TAG, "Error writing document", e)
                        }
                    
                    // if the login is successful, go to the main activity
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("email", email)
                    startActivity(intent)

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext,
                        "Authentication failed: ${task.exception?.message}",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            reload()
        }
    }

    private fun reload() {}
}