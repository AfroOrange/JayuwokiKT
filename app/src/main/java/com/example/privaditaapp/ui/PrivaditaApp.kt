package com.example.privaditaapp.ui

import android.app.Application
import com.google.firebase.FirebaseApp

class PrivaditaApp : Application() {

    override fun onCreate() {
        super.onCreate()

        FirebaseApp.initializeApp(this)
    }
}