package com.example.privaditaapp.ui.firebase

import android.util.Log
import com.example.privaditaapp.ui.models.Players
import com.google.firebase.firestore.FirebaseFirestore

class FirebaseManager {

    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    fun fetchPlayers(onSuccess: (List<Players>) -> Unit, onFailure: (Exception) -> Unit) {
        firestore.collection("Jayuwoki").document("Privadita").collection("Players").get()
            .addOnSuccessListener { result ->
                val players = result.map { document ->
                    document.toObject(Players::class.java)
                }
                onSuccess(players)
            }
            .addOnFailureListener { exception ->
                Log.w("FirebaseManager", "Error fetching players", exception)
                onFailure(exception)
            }
    }

    fun addPlayer(player: Players, playerName: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        firestore.collection("Jayuwoki").document("Privadita").collection("Players")
            .document(playerName)
            .set(player)
            .addOnSuccessListener {
                Log.d("FirebaseManager", "Player added successfully")
                onSuccess()
            }
            .addOnFailureListener { exception ->
                Log.w("FirebaseManager", "Error adding player", exception)
                onFailure(exception)
            }
    }

    fun deletePlayer(playerName: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        firestore.collection("Jayuwoki").document("Privadita").collection("Players")
            .document(playerName)
            .delete()
            .addOnSuccessListener {
                Log.d("FirebaseManager", "Player deleted successfully")
                onSuccess()
            }
            .addOnFailureListener { exception ->
                Log.w("FirebaseManager", "Error deleting player", exception)
                onFailure(exception)
            }
    }
}