package com.example.tareafinalmoviles.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface UsuarioDao {
    @Query("SELECT * FROM usuarios WHERE email = :email")
    fun obtenerUsuario(email: String): Flow<Usuario?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarUsuario(usuario: Usuario)

    @Update
    suspend fun actualizarUsuario(usuario: Usuario)

    @Query("UPDATE usuarios SET contadorAccesos = contadorAccesos + 1, ultimoAcceso = :nuevoAcceso WHERE email = :email")
    suspend fun incrementarAccesos(email: String, nuevoAcceso: Long)

    @Query("SELECT * FROM usuarios")
    suspend fun obtenerTodosUsuarios(): List<Usuario>

    @Query("SELECT * FROM usuarios LIMIT 1")
    suspend fun obtenerPrimerUsuario(): Usuario?

    @Query("SELECT * FROM usuarios WHERE email = :email LIMIT 1")
    suspend fun obtenerUsuarioPorEmail(email: String): Usuario?


}