# Aplicación de Registro de Usuarios 

## Descripción  
Esta aplicación permite a los usuarios registrarse e iniciar sesión, almacenando su información en una base de datos local con **Room**. Muestra también el número de accesos y la ultima fecha de acceso del usuario.

## Características principales  
- **Registro e inicio de sesión** con persistencia de datos.  
- **Gestión de accesos**: Muestra varios datos del acceso de los usuarios.
- **Notificaciones**: Se muestran notificaciones al usuario al iniciar la aplicación.  
- **API**: Uso de una API de usuarios.

---

##  Requisitos para ejecutar el proyecto  
1. **Android Studio** instalado.  
2. **Emulador o dispositivo Android** con API 26+.  
3. **Conexión a Internet** (si se usa en conjunto con una API externa).  
4. **Permisos** de notificación para Android 13+ (Tiramisu).  

---

##  Estructura de clases
```
├── data
│   ├── Usurio.kt
│   ├── UsuarioDAO.kt
│   ├── AppDatabase.kt
│   ├── ApiService.kt
│   └── RetroFitCliente.kt
│
├── ui
│   ├── AppNavigation.kt
│   ├── ConsultaScreen.kt
│   ├── PrincipalScreen.kt
│   └── RegistroScreen.kt     
└── MainActivity.kt
```


---

##  Pasos para ejecutar el proyecto  
1. Clonar el repositorio:  
   ```sh
   git clone https://github.com/nacho2502/tareaMovilesRegistro.git

---

##  Análisis de decisiones tomadas durante el desarrollo
La primera decisión de diseño importante fue hacer solo una pantalla para el diseño y el inicio de sesión. En la pantalla principal hay dos botones, uno para el inicio de sesión y otro para el resgistro, el botón de inicio de sesión no funcionara si el usuario ya esta registrado y el botón de inicio de sesión no funcionara si el usuario no está registrado. Después de iniciar sesión o registrarnos podemos encontrar una pantalla con varias funcionalidades, para empezar una notificación que sonara todo el rato avisandonos que no olvidemos consultar la API. Después segun el usuario que hayamos elegido aparecera su número de accesos y su última fecha de acceso. Finamente encontramos ubicado en la parte de abajo dos botones, uno para consutar la API y el otro para salir de la aplicación. Si pulsamos el botón de la API nos llevara a la pantalla de la API con todos los usuarios y un botón de volver hacia atrás.

He minimizado las pantallas a 3 para hacer la navegación más rápida y precisa, además he añadido decoración en las tres pantallas para hacer la interfaz más amigable.

---

##  Organización del proyecto con buenas prácticas
- **Separación de responsabilidades**
- **Uso de patrones modernos**
- **Organización en carpetas**
- **Gestión de dependencias**
- **Versionamiento y control del código**

---