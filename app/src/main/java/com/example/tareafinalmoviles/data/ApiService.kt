package com.example.tareafinalmoviles.data

import retrofit2.http.GET

data class UsuarioApi(
    val id: Int,
    val name: String,
    val email: String
)

interface ApiService {
    @GET("users")
    suspend fun obtenerUsuarios(): List<UsuarioApi>

}