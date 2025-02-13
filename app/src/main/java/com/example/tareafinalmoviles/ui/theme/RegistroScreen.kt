package com.example.tareafinalmoviles.ui.theme

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.background
import androidx.compose.material.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.tareafinalmoviles.data.AppDatabase
import com.example.tareafinalmoviles.data.Usuario
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@Composable
fun RegistroScreen(navController: NavController, context: Context) {
    // Estado para almacenar los valores de los campos de texto
    var nombre by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    // Coroutines para realizar operaciones asíncronas con la base de datos
    val coroutineScope = rememberCoroutineScope()

    // Instancia de la base de datos de usuarios
    val db = remember { AppDatabase.getInstance(context).usuarioDao() }

    // Definimos el color de fondo de la pantalla
    val fondoColor = Color(0xFFADD8E6) // Azul claro

    Column(
        modifier = Modifier
            .fillMaxSize() // Ocupa todo el tamaño disponible
            .background(fondoColor) // Aplica el color de fondo
            .padding(16.dp), // Añade un margen alrededor
        verticalArrangement = Arrangement.Center, // Centra los elementos verticalmente
        horizontalAlignment = Alignment.CenterHorizontally // Centra los elementos horizontalmente
    ) {
        // Título de la pantalla
        Text(
            text = "Pantalla de Registro",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 32.dp) // Añade espacio debajo
        )

        // Surface que contiene los campos de entrada y botones
        Surface(
            modifier = Modifier.fillMaxWidth(), // Ocupa todo el ancho disponible
            shape = RoundedCornerShape(16.dp), // Bordes redondeados
            shadowElevation = 8.dp // Sombra para darle profundidad
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp) // Añade un margen interno
                    .fillMaxWidth(), // Ocupa todo el ancho
                horizontalAlignment = Alignment.CenterHorizontally // Centra los elementos horizontalmente
            ) {
                // Campo para ingresar el nombre
                TextField(
                    value = nombre, // Valor actual del campo
                    onValueChange = { nombre = it }, // Actualiza el valor al escribir
                    label = { Text("Nombre") }, // Etiqueta del campo
                    modifier = Modifier.fillMaxWidth() // Ocupa todo el ancho disponible
                )

                Spacer(modifier = Modifier.height(8.dp)) // Espacio entre campos

                // Campo para ingresar el correo electrónico
                TextField(
                    value = email, // Valor actual del campo
                    onValueChange = { email = it }, // Actualiza el valor al escribir
                    label = { Text("Correo Electrónico") }, // Etiqueta del campo
                    modifier = Modifier.fillMaxWidth() // Ocupa todo el ancho disponible
                )

                Spacer(modifier = Modifier.height(16.dp)) // Espacio entre campos

                // Botón para registrar un nuevo usuario
                Button(
                    onClick = {
                        // Ejecuta una operación asíncrona para registrar o actualizar el usuario
                        coroutineScope.launch {
                            val usuarioExistente = db.obtenerUsuario(email).first()
                            if (usuarioExistente != null) {
                                // Si el usuario ya está registrado, pero no ha iniciado sesión antes
                                if (usuarioExistente.contadorAccesos > 0) {
                                    Toast.makeText(context, "El usuario ya ha sido registrado", Toast.LENGTH_SHORT).show()
                                } else {
                                    // Si el usuario existe y necesita ser actualizado
                                    val usuarioActualizado = usuarioExistente.copy(
                                        contadorAccesos = 1,
                                        ultimoAcceso = System.currentTimeMillis()
                                    )
                                    db.actualizarUsuario(usuarioActualizado)
                                    Toast.makeText(context, "Usuario actualizado correctamente", Toast.LENGTH_SHORT).show()
                                    navController.navigate("${Pantalla.Principal.ruta}/$email") // Navega a la pantalla principal
                                }
                            } else if (nombre.isNotBlank() && email.isNotBlank()) {
                                // Si el usuario no existe, y los campos no están vacíos, lo registramos
                                val nuevoUsuario = Usuario(email, nombre, 1, System.currentTimeMillis())
                                db.insertarUsuario(nuevoUsuario)
                                Toast.makeText(context, "Usuario registrado correctamente", Toast.LENGTH_SHORT).show()
                                navController.navigate("${Pantalla.Principal.ruta}/$email") // Navega a la pantalla principal
                            } else {
                                // Si alguno de los campos está vacío, mostramos un mensaje de error
                                Toast.makeText(context, "Por favor, llena todos los campos", Toast.LENGTH_SHORT).show()
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth() // Ocupa todo el ancho disponible
                ) {
                    Text("Registrar") // Texto del botón
                }

                Spacer(modifier = Modifier.height(8.dp)) // Espacio entre botones

                // Botón para iniciar sesión
                Button(
                    onClick = {
                        // Ejecuta una operación asíncrona para iniciar sesión
                        coroutineScope.launch {
                            val usuario = db.obtenerUsuario(email).first()
                            if (usuario == null) {
                                // Si el usuario no existe en la base de datos, muestra un mensaje de error
                                Toast.makeText(context, "Primero debes registrarte", Toast.LENGTH_SHORT).show()
                            } else {
                                // Si el usuario existe, actualiza el contador de accesos e informa al usuario
                                val usuarioActualizado = usuario.copy(
                                    contadorAccesos = usuario.contadorAccesos + 1,
                                    ultimoAcceso = System.currentTimeMillis()
                                )
                                db.actualizarUsuario(usuarioActualizado)
                                Toast.makeText(context, "Bienvenido ${usuario.nombre}", Toast.LENGTH_SHORT).show()
                                navController.navigate("${Pantalla.Principal.ruta}/$email") // Navega a la pantalla principal
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth() // Ocupa todo el ancho disponible
                ) {
                    Text("Iniciar Sesión") // Texto del botón
                }
            }
        }
    }
}
