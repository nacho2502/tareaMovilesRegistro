package com.example.tareafinalmoviles.ui.theme

sealed class Pantalla(val ruta: String) {
    object Registro : Pantalla("registro")
    object Principal : Pantalla("principal")
    object Consulta : Pantalla("consulta")
}