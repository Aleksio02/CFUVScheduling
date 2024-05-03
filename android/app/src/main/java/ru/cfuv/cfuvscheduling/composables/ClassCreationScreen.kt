package ru.cfuv.cfuvscheduling.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.cfuv.cfuvscheduling.MainViewModel
import ru.cfuv.cfuvscheduling.MainViewModelFactory
import ru.cfuv.cfuvscheduling.R
import ru.cfuv.cfuvscheduling.api.NetErrors
import ru.cfuv.cfuvscheduling.dataStore
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClassCreationScreen(viewModel: MainViewModel, onNavigateUp: () -> Unit) {
    // Variables
    val className = rememberSaveable { mutableStateOf("") }
    val classRoom = rememberSaveable { mutableStateOf("") }
    var selectedDateMillis: Long? by rememberSaveable { mutableStateOf(null) }
    var number by rememberSaveable { mutableStateOf("") }
    val comment = rememberSaveable { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.classCreationTitle)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = null)
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text(text = stringResource(id = R.string.save)) },
                icon = { Icon(painter = painterResource(id = R.drawable.rounded_save_24), contentDescription = null) },
                onClick = {
                    if (className.value.isEmpty() || classRoom.value.isEmpty() || number.isEmpty() || selectedDateMillis == null) {
                        showError = true
                        return@ExtendedFloatingActionButton
                    }
                    viewModel.createConsultation(
                        name = className.value,
                        room = classRoom.value,
                        date = LocalDate.ofEpochDay(selectedDateMillis!! / 24 / 60 / 60 / 1000),
                        position = number.toInt(),
                        comment = comment.value
                    ) {
                        onNavigateUp()
                    }
                },
            )
        }
    ) { paddings ->
        val netStatus = viewModel.netStatus.collectAsState()
        if (!netStatus.value.ok) {
            val creationErrorString = stringResource(id = R.string.classCreationError)
            NetStatusSnack(
                context = context,
                netStatus = netStatus.value,
                snackbarHostState = snackbarHostState,
                messageHandler = {
                    when(it) {
                        NetErrors.BAD_REQUEST -> creationErrorString
                        else -> null
                    }
                }
            )
        }
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(paddings)
                .padding(horizontal = 24.dp)
        ) {
            // Name
            ClassCreationField(
                text = className,
                label = stringResource(id = R.string.classCreationNameFieldLabel),
                isError = showError && className.value.isEmpty()
            )

            // Room
            ClassCreationField(
                text = classRoom,
                label = stringResource(id = R.string.classCreationRoomFieldLabel),
                isError = showError && classRoom.value.isEmpty()
            )

            // Date && Position
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                var dateDialogShown by remember { mutableStateOf(false) }
                if (dateDialogShown) {
                    val datePickerState = rememberDatePickerState()
                    val confirmEnabled = remember {
                        derivedStateOf { datePickerState.selectedDateMillis != null }
                    }
                    DatePickerDialog(
                        onDismissRequest = { dateDialogShown = false },
                        confirmButton = {
                            TextButton(
                                onClick = {
                                    selectedDateMillis = datePickerState.selectedDateMillis
                                    dateDialogShown = false
                                },
                                enabled = confirmEnabled.value
                            ) {
                                Text(text = stringResource(id = android.R.string.ok))
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = { dateDialogShown = false }) {
                                Text(text = stringResource(id = R.string.cancel))
                            }
                        }
                    ) {
                        DatePicker(state = datePickerState)
                    }
                }
                OutlinedTextField(
                    value = selectedDateMillis?.let {
                        LocalDate.ofEpochDay(it / 24 / 60 / 60 / 1000)
                            .format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT))  // Fuck Java
                    } ?: "",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text(text = stringResource(id = R.string.classCreationDateFieldLabel)) },
                    trailingIcon = {
                        IconButton(onClick = { dateDialogShown = true }) {
                            Icon(
                                imageVector = Icons.Rounded.DateRange,
                                contentDescription = null
                            )
                        }
                    },
                    isError = showError && selectedDateMillis == null,
                    modifier = Modifier
                        .weight(1f)
                        .clickable { dateDialogShown = true }
                )
                val decimalRegex = Regex("\\d+")
                OutlinedTextField(
                    value = number,
                    onValueChange = { input ->
                        val numbers = decimalRegex.findAll(input)
                        number = numbers.map { it.value }.joinToString("").take(2)
                    },
                    label = { Text(text = stringResource(id = R.string.classCreationPositionFieldLabel)) },
                    isError = showError && number.isEmpty(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.weight(1f),
                )
            }
            // Comment
            ClassCreationField(
                text = comment,
                label = stringResource(id = R.string.classCreationCommentFieldLabel),
                supportingText = { Text(text = stringResource(id = R.string.inputOptional)) }
            )
        }
    }
}

@Composable
fun ClassCreationField(
    text: MutableState<String>,
    label: String,
    supportingText: @Composable (() -> Unit)? = null,
    isError: Boolean = false
) {
    OutlinedTextField(
        value = text.value,
        onValueChange = { text.value = it },
        label = { Text(text = label) },
        supportingText = supportingText,
        singleLine = true,
        isError = isError,
        modifier = Modifier.fillMaxWidth()
    )
}

@Preview
@Composable
fun ClassCreationScreenPreview() {
    ClassCreationScreen(viewModel(factory = MainViewModelFactory(LocalContext.current.dataStore)), {})
}
