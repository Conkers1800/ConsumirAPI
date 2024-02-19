package com.conkers.tareaapi3

/**
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TareaApi3Theme()
        }
    }
}

@Composable
fun TareaApi3Theme() {
    var loggedIn by remember { mutableStateOf(false) }

    MaterialTheme {
        if (loggedIn) {
            MuestaPantalla()
        } else {
            LoginInicio(onLoginClicked = { username, password ->
                // Aquí iría la lógica de autenticación
                // Por ahora, simplemente simularemos la autenticación con un valor fijo
               // if (username == "user" && password == "password") {
                    loggedIn = true
                //}
            })
        }
    }
}
**/
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.conkers.tareaapi3.Login.Login
import com.conkers.tareaapi3.MuestaApi.MuestraPantalla
import com.conkers.tareaapi3.response.incioDesesion

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            NavHost(navController, startDestination = "login") {
                composable("login") {
                    MyApp(navController)
                }
                composable(
                    route = "success/{cookie}",
                    arguments = listOf(navArgument("cookie") { type = NavType.StringType })
                ) { backStackEntry ->
                    val cookie = backStackEntry.arguments?.getString("cookie") ?: ""
                    MuestraPantalla(cookie)
                }
            }
        }
    }
}

@Composable
fun MyApp(navController: NavController) {
    val context = LocalContext.current
    var loggedIn by remember { mutableStateOf(false) }
    var successMessage by remember { mutableStateOf("") }
    var errorText by remember { mutableStateOf("") }
    var username by remember { mutableStateOf(TextFieldValue("")) }
    var password by remember { mutableStateOf(TextFieldValue("")) }
    var cookie by remember { mutableStateOf("") }

    if (loggedIn) {
        MensajeDeiniciosesion(navController = navController, cookie = cookie)
    } else {
        Login(
            context = context,
            username = username,
            password = password,
            onUsernameChange = { username = it },
            onPasswordChange = { password = it },
            onLoginClicked = {
                incioDesesion(context, username.text, password.text, navController) { success, message, receivedCookie ->
                    if (success) {
                        loggedIn = true
                        successMessage = message ?: "Inicio de sesion exitoso"
                        cookie = receivedCookie ?: ""
                    } else {
                        errorText = message ?: "Error desconocido"
                        // Mostrar un mensaje de error al usuario
                        Toast.makeText(context, errorText, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        )
    }

    if (errorText.isNotEmpty()) {
        Toast.makeText(context, errorText, Toast.LENGTH_SHORT).show()
    }
}


@Composable
fun MensajeDeiniciosesion(navController: NavController, cookie: String) {
    Surface(color = MaterialTheme.colorScheme.background) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Inicio de sesion exitoso", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { navController.navigate("success/$cookie") },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("Continuar")
            }
        }
    }
}