package com.conkers.tareaapi3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.conkers.tareaapi3.Login.LoginInicio
import com.conkers.tareaapi3.MuestaApi.MuestaPantalla

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
                if (username == "user" && password == "password") {
                    loggedIn = true
                }
            })
        }
    }
}
