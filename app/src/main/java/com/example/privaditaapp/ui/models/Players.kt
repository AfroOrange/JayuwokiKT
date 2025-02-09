package com.example.privaditaapp.ui.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Players (
    val avatarURL: String? = null,
    val discriminator: String? = null,
    val elo: Int = 0,
    val idPlayer: Int = 0,
    val losses: Int = 0,
    val name: String = "",
    val role: String? = null,
    val wins: Int = 0
) : Parcelable
