package com.example.tareafinalmoviles.ui.theme

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.tareafinalmoviles.data.RetrofitClient
import com.example.tareafinalmoviles.data.UsuarioApi
import kotlinx.coroutines.launch

@Composable
fun ConsultaScreen(navController: NavController) {
    // Estado para almacenar la lista de usuarios obtenida de la API
    val usuarios = remember { mutableStateOf<List<UsuarioApi>>(emptyList()) }

    // CoroutineScope para manejar la solicitud de red de forma asíncrona
    val coroutineScope = rememberCoroutineScope()

    // Estado para controlar si la carga de datos sigue en proceso
    var cargando by remember { mutableStateOf(true) }

    // LaunchedEffect se ejecuta cuando la pantalla se carga por primera vez
    LaunchedEffect(Unit) {
        coroutineScope.launch {
            try {
                // Llamada a la API para obtener la lista de usuarios
                usuarios.value = RetrofitClient.apiService.obtenerUsuarios()
            } catch (e: Exception) {
                e.printStackTrace() // Manejo de error en la consola
            } finally {
                cargando = false // Finaliza la carga, independientemente del resultado
            }
        }
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        // Botón para volver a la pantalla anterior
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            Button(onClick = { navController.popBackStack() }) {
                Text("Volver")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Título de la pantalla
        Text("Usuarios de la API", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(8.dp))

        if (cargando) {
            // Muestra un indicador de carga mientras los datos se obtienen de la API
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            // Lista de usuarios utilizando LazyColumn para optimizar el rendimiento
            LazyColumn {
                items(usuarios.value) { usuario ->
                    // Cada usuario se representa dentro de una Card
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            // Se muestran el nombre y el correo del usuario
                            Text(text = "Nombre: ${usuario.name}", style = MaterialTheme.typography.bodyLarge)
                            Text(text = "Correo: ${usuario.email}", style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }
        }
    }
}
