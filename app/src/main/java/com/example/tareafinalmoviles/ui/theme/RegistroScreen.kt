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
    var nombre by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    val db = remember { AppDatabase.getInstance(context).usuarioDao() }

    val fondoColor = Color(0xFFADD8E6) // Azul claro

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(fondoColor)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Pantalla de Registro",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            shadowElevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Campo de nombre
                TextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Campo de email
                TextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Correo Electrónico") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Botón de Registro
                Button(
                    onClick = {
                        coroutineScope.launch {
                            val usuarioExistente = db.obtenerUsuario(email).first()
                            if (usuarioExistente != null) {
                                if (usuarioExistente.contadorAccesos > 0) {
                                    Toast.makeText(context, "El usuario ya ha sido registrado", Toast.LENGTH_SHORT).show()
                                } else {
                                    val usuarioActualizado = usuarioExistente.copy(
                                        contadorAccesos = 1,
                                        ultimoAcceso = System.currentTimeMillis()
                                    )
                                    db.actualizarUsuario(usuarioActualizado)
                                    Toast.makeText(context, "Usuario actualizado correctamente", Toast.LENGTH_SHORT).show()
                                    navController.navigate("${Pantalla.Principal.ruta}/$email")
                                }
                            } else if (nombre.isNotBlank() && email.isNotBlank()) {
                                val nuevoUsuario = Usuario(email, nombre, 1, System.currentTimeMillis())
                                db.insertarUsuario(nuevoUsuario)
                                Toast.makeText(context, "Usuario registrado correctamente", Toast.LENGTH_SHORT).show()
                                navController.navigate("${Pantalla.Principal.ruta}/$email")
                            } else {
                                Toast.makeText(context, "Por favor, llena todos los campos", Toast.LENGTH_SHORT).show()
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Registrar")
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Botón de Iniciar Sesión
                Button(
                    onClick = {
                        coroutineScope.launch {
                            val usuario = db.obtenerUsuario(email).first()
                            if (usuario == null) {
                                Toast.makeText(context, "Primero debes registrarte", Toast.LENGTH_SHORT).show()
                            } else {
                                val usuarioActualizado = usuario.copy(
                                    contadorAccesos = usuario.contadorAccesos + 1,
                                    ultimoAcceso = System.currentTimeMillis()
                                )
                                db.actualizarUsuario(usuarioActualizado)
                                Toast.makeText(context, "Bienvenido ${usuario.nombre}", Toast.LENGTH_SHORT).show()
                                navController.navigate("${Pantalla.Principal.ruta}/$email")
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Iniciar Sesión")
                }
            }
        }
    }
}
