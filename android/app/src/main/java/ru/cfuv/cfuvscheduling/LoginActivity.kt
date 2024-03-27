package ru.cfuv.cfuvscheduling

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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

@Composable
fun LoginScreen(onNavigateToRegister: () -> Unit) {
    Column(modifier = Modifier.padding(24.dp)) {
        Text(
            text = stringResource(id = R.string.loginTitle),
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = stringResource(id = R.string.loginDescription),
            fontSize = 20.sp,
            modifier = Modifier.padding(top = 8.dp)
        )
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            var login by rememberSaveable { mutableStateOf("") }
            var pass by rememberSaveable { mutableStateOf("") }
            OutlinedTextField(
                value = login,
                onValueChange = { login = it },
                label = { Text(text = stringResource(id = R.string.loginUsernameField)) },
                shape = RoundedCornerShape(24.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = pass,
                onValueChange = { pass = it },
                label = { Text(text = stringResource(id = R.string.loginPasswordField)) },
                shape = RoundedCornerShape(24.dp),
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier
                    .fillMaxWidth()
            )
            Button(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Text(text = stringResource(id = R.string.loginButtonLabel))
            }
            OutlinedButton(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(text = stringResource(id = R.string.loginButtonNoAccountLabel))
            }
            OutlinedButton(
                onClick = onNavigateToRegister,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Text(text = stringResource(id = R.string.registerButtonLabel))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    Surface(
        modifier = Modifier.fillMaxSize(),
    ) {
        LoginScreen({})
    }
}

@Composable
fun RegisterScreen() {
    Column(modifier = Modifier.padding(24.dp)) {
        Text(
            text = stringResource(id = R.string.registerTitle),
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = stringResource(id = R.string.loginDescription),
            fontSize = 20.sp,
            modifier = Modifier.padding(top = 8.dp)
        )
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            var login by rememberSaveable { mutableStateOf("") }
            var pass by rememberSaveable { mutableStateOf("") }
            var pass2 by rememberSaveable { mutableStateOf("") }

            OutlinedTextField(
                value = login,
                onValueChange = { login = it },
                label = { Text(text = stringResource(id = R.string.loginUsernameField)) },
                shape = RoundedCornerShape(24.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = pass,
                onValueChange = { pass = it },
                label = { Text(text = stringResource(id = R.string.loginPasswordField)) },
                shape = RoundedCornerShape(24.dp),
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier
                    .fillMaxWidth()
            )
            OutlinedTextField(
                value = pass2,
                onValueChange = { pass2 = it },
                label = { Text(text = stringResource(id = R.string.registerPasswordAgainField)) },
                supportingText = {
                    AnimatedVisibility(visible = pass != pass2) {
                        Text(text = stringResource(id = R.string.registerPasswordNoMatch))
                    }
                },
                isError = pass != pass2,
                shape = RoundedCornerShape(24.dp),
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier
                    .fillMaxWidth()
            )

            Button(
                onClick = { /*TODO*/ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(id = R.string.registerButtonLabel))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    Surface(
        modifier = Modifier.fillMaxSize(),
    ) {
        RegisterScreen()
    }
}
