package com.example.tareafinalmoviles.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuarios")
data class Usuario(
    @PrimaryKey val email: String,
    val nombre: String,
    val contadorAccesos: Int,
    val ultimoAcceso: Long
)