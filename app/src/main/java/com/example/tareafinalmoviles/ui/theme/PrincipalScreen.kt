package com.example.tareafinalmoviles.ui.theme

import android.Manifest
import android.content.Context
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.content.pm.PackageManager
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.Card
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import android.app.NotificationChannel
import android.app.NotificationManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import com.example.tareafinalmoviles.data.AppDatabase

@Composable
fun PrincipalScreen(navController: NavController, context: Context, emailUsuario: String) {
    val context = LocalContext.current
    val handler = remember { Handler(Looper.getMainLooper()) }
    var mostrarNotificaciones by remember { mutableStateOf(true) }
    val coroutineScope = rememberCoroutineScope()
    val usuarioDao = remember { AppDatabase.getInstance(context).usuarioDao() }

    var numeroAccesos by remember { mutableStateOf(0) }
    var ultimaFechaAcceso by remember { mutableStateOf("Nunca") }

    // Solo obtenemos y mostramos los datos del usuario, sin incrementar el contador.
    LaunchedEffect(emailUsuario) {
        coroutineScope.launch {
            val usuario = usuarioDao.obtenerUsuarioPorEmail(emailUsuario)
            usuario?.let {
                numeroAccesos = it.contadorAccesos
                ultimaFechaAcceso = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                    .format(Date(it.ultimoAcceso))
            }
        }
    }

    // Crear canal de notificación solo en Android 8+.
    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "canal_notificaciones",
                "Notificaciones",
                NotificationManager.IMPORTANCE_HIGH
            )
            val manager = context.getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(channel)
        }
        // Enviar notificación de bienvenida.
        mostrarNotificacion(context, "Bienvenido a la aplicación")
    }

    // Diseño principal de la pantalla.
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE3F2FD)) // Fondo azul muy claro.
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Título de bienvenida.
        Text(
            text = "Bienvenido 👋",
            style = MaterialTheme.typography.headlineMedium,
            color = Color(0xFF1565C0)
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Tarjeta que muestra la información del usuario.
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Número de accesos:",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.Gray,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "$numeroAccesos",
                    style = MaterialTheme.typography.displaySmall,
                    color = Color(0xFF1E88E5),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Último acceso:",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.Gray,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = ultimaFechaAcceso,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.DarkGray,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
        }
        Spacer(modifier = Modifier.height(24.dp))

        // Botón para consultar la API.
        Button(
            onClick = {
                navController.navigate(Pantalla.Consulta.ruta)
                mostrarNotificaciones = false // Detiene el envío periódico de notificaciones.
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E88E5))
        ) {
            Text(
                text = "🔍 Consultar API",
                style = MaterialTheme.typography.titleLarge,
                color = Color.White
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Botón para cerrar la aplicación.
        OutlinedButton(
            onClick = { System.exit(0) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFD32F2F))
        ) {
            Text(
                text = "🚪 Cerrar Aplicación",
                style = MaterialTheme.typography.titleLarge
            )
        }
    }

    // Notificaciones periódicas.
    DisposableEffect(mostrarNotificaciones) {
        val runnable = object : Runnable {
            override fun run() {
                mostrarNotificacion(context, "¡No olvides consultar la API!")
                if (mostrarNotificaciones) {
                    handler.postDelayed(this, 500)
                }
            }
        }
        if (mostrarNotificaciones) {
            handler.postDelayed(runnable, 500)
        }
        onDispose {
            handler.removeCallbacks(runnable)
        }
    }
}

// Función para mostrar una notificación.
fun mostrarNotificacion(context: Context, mensaje: String) {
    if (ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.POST_NOTIFICATIONS
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        return
    }
    val builder = NotificationCompat.Builder(context, "canal_notificaciones")
        .setSmallIcon(android.R.drawable.ic_dialog_info)
        .setContentTitle("Recordatorio")
        .setContentText(mensaje)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
    with(NotificationManagerCompat.from(context)) {
        notify(1, builder.build())
    }
}