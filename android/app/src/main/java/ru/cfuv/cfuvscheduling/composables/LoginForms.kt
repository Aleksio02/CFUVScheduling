package ru.cfuv.cfuvscheduling.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.cfuv.cfuvscheduling.MainViewModel
import ru.cfuv.cfuvscheduling.MainViewModelFactory
import ru.cfuv.cfuvscheduling.R
import ru.cfuv.cfuvscheduling.dataStore

@Composable
fun LoginScreen(
    viewModel: MainViewModel = viewModel(),
    showSkipButton: Boolean = false,
    onNavigateToRegister: () -> Unit,
    onBackToAccountPage: () -> Unit
) {
    val userData = viewModel.userData.collectAsState()
    if (userData.value != null) {
        onBackToAccountPage()
    }

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
                onClick = { viewModel.loginUser(login, pass) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Text(text = stringResource(id = R.string.loginButtonLabel))
            }
            if (showSkipButton) {
                OutlinedButton(
                    onClick = { onBackToAccountPage() },
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(text = stringResource(id = R.string.loginButtonNoAccountLabel))
                }
            }
            OutlinedButton(
                onClick = onNavigateToRegister,
                modifier = Modifier
                    .fillMaxWidth()
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
        LoginScreen(viewModel = viewModel(factory = MainViewModelFactory(LocalContext.current.dataStore)), showSkipButton = true, onNavigateToRegister = {}, onBackToAccountPage = {})
    }
}

@Composable
fun RegisterScreen(onBackToAccountPage: () -> Unit) {
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
        RegisterScreen({})
    }
}
