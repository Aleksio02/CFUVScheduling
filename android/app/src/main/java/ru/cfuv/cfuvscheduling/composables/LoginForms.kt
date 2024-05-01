package ru.cfuv.cfuvscheduling.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
    viewModel: MainViewModel,
    showSkipButton: Boolean = false,
    onNavigateToRegister: () -> Unit,
    onBackToAccountPage: () -> Unit
) {
    val userData = viewModel.userData.collectAsState()
    if (userData.value != null) {
        onBackToAccountPage()
    }

    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text(
            text = stringResource(id = R.string.loginTitle),
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
fun RegisterScreen(
    viewModel: MainViewModel,
    onBackToAccountPage: () -> Unit
) {
    val userData = viewModel.userData.collectAsState()
    if (userData.value != null) {
        onBackToAccountPage()
    }

    Column {
        Text(
            text = stringResource(id = R.string.registerTitle),
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {
            var login by rememberSaveable { mutableStateOf("") }
            var pass by rememberSaveable { mutableStateOf("") }
            var pass2 by rememberSaveable { mutableStateOf("") }

            val loginRegex = Regex("[A-Za-z0-9-_.@]{8,}")
            val loginLettersRegex = Regex("[A-Za-z]+")
            val loginAllowed = loginRegex.matchEntire(login) != null && loginLettersRegex.containsMatchIn(login)
            OutlinedTextField(
                value = login,
                onValueChange = { login = it },
                label = { Text(text = stringResource(id = R.string.loginUsernameField)) },
                shape = RoundedCornerShape(24.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth()
            )
            AnimatedVisibility(visible = login != "" && !loginAllowed) {
                Text(
                    text = stringResource(id = R.string.registerLoginRequirements),
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(8.dp)
                )
            }

            val passwordRegex = Regex(".{8,}")
            val passwordMandatoryRegex = Regex("[A-Za-z0-9]+")
            val passwordAllowed = passwordRegex.matchEntire(pass) != null && passwordMandatoryRegex.containsMatchIn(pass)
            OutlinedTextField(
                value = pass,
                onValueChange = { pass = it },
                label = { Text(text = stringResource(id = R.string.loginPasswordField)) },
                shape = RoundedCornerShape(24.dp),
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth()
            )
            AnimatedVisibility(visible = pass != "" && !passwordAllowed) {
                Text(
                    text = stringResource(id = R.string.registerPasswordRequirements),
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(8.dp)
                )
            }
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

            var showErrorText by remember { mutableStateOf(false) }
            Button(
                onClick = {
                    if (!loginAllowed || !passwordAllowed || pass != pass2) {
                        showErrorText = true
                    } else {
                        viewModel.registerUser(login, pass)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(id = R.string.registerButtonLabel))
            }
            AnimatedVisibility(visible = showErrorText) {
                Text(
                    text = stringResource(id = R.string.registerChecksNotPassed),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(8.dp)
                )
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
        RegisterScreen(viewModel = viewModel(factory = MainViewModelFactory(LocalContext.current.dataStore)), {})
    }
}
