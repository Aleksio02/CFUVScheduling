package ru.cfuv.cfuvscheduling

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.cfuv.cfuvscheduling.composables.LoginScreen
import ru.cfuv.cfuvscheduling.composables.RegisterScreen
import ru.cfuv.cfuvscheduling.ui.theme.CFUVSchedulingTheme

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CFUVSchedulingTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "login") {
                        composable("login") { LoginScreen(onNavigateToRegister = { navController.navigate("register") }) }
                        composable("register") { RegisterScreen() }
                    }
                }
            }
        }
    }
}
