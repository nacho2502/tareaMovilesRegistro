package com.example.tareafinalmoviles

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.Composable
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.tareafinalmoviles.ui.theme.ConsultaScreen
import com.example.tareafinalmoviles.ui.theme.Pantalla
import com.example.tareafinalmoviles.ui.theme.PrincipalScreen
import com.example.tareafinalmoviles.ui.theme.RegistroScreen

class MainActivity : ComponentActivity() {

    // Registrar un lanzador para solicitar permisos de notificación
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            // Si el permiso es concedido, mostrar la notificación
            if (isGranted) {
                mostrarNotificacion()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Crear el canal de notificación si es necesario
        createNotificationChannel()

        // Verificar y solicitar permisos de notificación en Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Solicitar permiso de notificación explícitamente para Android 13+
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            // Si la versión es anterior a Android 13, mostrar la notificación directamente
            mostrarNotificacion()
        }

        // Configurar la interfaz de usuario con composables
        setContent {
            val navController = rememberNavController() // Controlador de navegación
            val context = LocalContext.current // Contexto actual

            // Navegación entre pantallas con la estructura de rutas
            NavHost(
                navController = navController,
                startDestination = Pantalla.Registro.ruta // Pantalla inicial de Registro
            ) {
                // Ruta para la pantalla de registro
                composable(Pantalla.Registro.ruta) {
                    RegistroScreen(navController = navController, context = context)
                }
                // Ruta para la pantalla principal, que recibe un argumento (emailUsuario)
                composable(
                    route = "${Pantalla.Principal.ruta}/{emailUsuario}",
                    arguments = listOf(navArgument("emailUsuario") { type = NavType.StringType })
                ) { backStackEntry ->
                    val emailUsuario = backStackEntry.arguments?.getString("emailUsuario") ?: ""
                    PrincipalScreen(navController = navController, context = context, emailUsuario = emailUsuario)
                }
                // Ruta para la pantalla de consulta
                composable(Pantalla.Consulta.ruta) {
                    ConsultaScreen(navController = navController)
                }
            }
        }
    }

    // Crear el canal de notificación (requerido para Android 8+)
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "mi_canal_id" // ID único para el canal
            val channelName = "Canal de Notificaciones" // Nombre del canal
            val importance = NotificationManager.IMPORTANCE_DEFAULT // Importancia de la notificación
            val channel = NotificationChannel(channelId, channelName, importance) // Crear el canal

            // Obtener el servicio de notificación y crear el canal
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    // Función para mostrar una notificación
    private fun mostrarNotificacion() {
        val channelId = "mi_canal_id" // El ID del canal de notificación
        val notificationId = 1 // ID único para la notificación

        // Crear el builder de la notificación
        val builder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground) // Ícono de la notificación (asegúrate de que este ícono exista)
            .setContentTitle("¡Hola!") // Título de la notificación
            .setContentText("Bienvenido a la aplicación") // Texto de la notificación
            .setPriority(NotificationCompat.PRIORITY_DEFAULT) // Establecer la prioridad

        // Verificar si el permiso de notificaciones está concedido antes de mostrar la notificación
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Mostrar la notificación con el ID proporcionado
            NotificationManagerCompat.from(this).notify(notificationId, builder.build())
        }
    }
}
