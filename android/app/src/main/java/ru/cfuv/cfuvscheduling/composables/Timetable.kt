package ru.cfuv.cfuvscheduling.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.cfuv.cfuvscheduling.ClassTypes
import ru.cfuv.cfuvscheduling.MainViewModel
import ru.cfuv.cfuvscheduling.MainViewModelFactory
import ru.cfuv.cfuvscheduling.R
import ru.cfuv.cfuvscheduling.UserRoles
import ru.cfuv.cfuvscheduling.api.TTClassDuration
import ru.cfuv.cfuvscheduling.api.TTClassModel
import ru.cfuv.cfuvscheduling.api.UserModel
import ru.cfuv.cfuvscheduling.dataStore
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun TimetableScreen(
    viewModel: MainViewModel = viewModel(),
    onCreateClass: () -> Unit,
) {
    val currentGroup by viewModel.currentGroup.collectAsState()
    LaunchedEffect(currentGroup) {
        viewModel.setAppBarTitle(currentGroup.name)
    }
    val currentDate by viewModel.currentDate.collectAsState()
    val classes by viewModel.currentClasses.collectAsState()
    val userData by viewModel.userData.collectAsState()
    val allowedToAdd by viewModel.userCanCreateClasses.collectAsState()

    Scaffold(
        floatingActionButton = {
            if (allowedToAdd) {
                FloatingActionButton(onClick = onCreateClass) {
                    Icon(
                        painter = painterResource(id = R.drawable.rounded_add_24),
                        contentDescription = null
                    )
                }
            }
        },
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) {
        LazyColumn(
            Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                        .padding(bottom = 12.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp)
                    ) {
                        Text(
                            text = stringResource(id = R.string.timetableTitle),
                            fontSize = 28.sp,
                        )
                        Text(
                            text = currentDate.format(DateTimeFormatter.ofPattern("E, d MMM")),
                            fontSize = 24.sp
                        )
                    }
                    FilledTonalIconButton(
                        onClick = { viewModel.changeDate(-1) },
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Icon(imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowLeft, contentDescription = null)
                    }
                    FilledTonalIconButton(onClick = { viewModel.changeDate(1) }) {
                        Icon(imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight, contentDescription = null)
                    }
                }
            }
            items(classes) {
                ClassCard(
                    data = it,
                    userData = userData,
                    onChangeComment = { comment ->
                        viewModel.updateClassComment(it.id, comment)
                    },
                    onDelete = {
                        viewModel.deleteClass(it.id)
                    }
                )
            }
            // Empty column to ensure that last item under the FAB is accessible
            if (allowedToAdd) {
                item {
                    Column(
                        modifier = Modifier.height(80.dp)
                    ) {}
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClassCard(data: TTClassModel, userData: UserModel?, onChangeComment: (String) -> Unit, onDelete: () -> Unit) {
    var cardExpanded by rememberSaveable { mutableStateOf(false) }
    var commentDialogOpened by rememberSaveable { mutableStateOf(false) }

    // Comment dialog
    if (commentDialogOpened) {
        CommentDialog(
            classData = data,
            onDismissRequest = { commentDialogOpened = false },
            onConfirmation = {
                onChangeComment(it)
                commentDialogOpened = false
            }
        )
    }

    val classType = try {
        ClassTypes.valueOf(data.classType.name.uppercase())
    } catch (e: IllegalArgumentException) {
        ClassTypes.UNKNOWN
    }

    OutlinedCard(
        onClick = { cardExpanded = !cardExpanded },
        Modifier
            .padding(horizontal = 12.dp)
            .padding(bottom = 8.dp)
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(
                            when (classType) {
                                ClassTypes.LECTURE -> MaterialTheme.colorScheme.primaryContainer
                                ClassTypes.PRACTICAL -> MaterialTheme.colorScheme.tertiaryContainer
                                ClassTypes.CONSULTATION -> MaterialTheme.colorScheme.secondaryContainer
                                ClassTypes.UNKNOWN -> MaterialTheme.colorScheme.surfaceContainer
                            }
                        )
                ) {
                    Text(
                        text = stringResource(
                            id = when (classType) {
                                ClassTypes.LECTURE -> R.string.classTypeLecture
                                ClassTypes.PRACTICAL -> R.string.classTypePractical
                                ClassTypes.CONSULTATION -> R.string.classTypeConsultation
                                ClassTypes.UNKNOWN -> R.string.classTypeUnknown
                            }
                        ),
                        modifier = Modifier.padding(vertical = 8.dp, horizontal = 12.dp)
                    )
                }
                Text(
                    text = data.subjectName,
                    fontSize = 16.sp,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                        .weight(1f),
                )
                Text(
                    text = data.classroom,
                    fontSize = 16.sp,
                )
                Column(
                    horizontalAlignment = Alignment.End,
                    modifier = Modifier
                        .padding(start = 12.dp)
                ) {
                    Text(
                        text = data.duration.startTime.format(DateTimeFormatter.ofPattern("HH:mm")),
                        modifier = Modifier.padding(bottom = 2.dp)
                    )
                    Text(text = data.duration.endTime.format(DateTimeFormatter.ofPattern("HH:mm")))
                }
            }
            if (data.comment.isNotEmpty()) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Icon(painter = painterResource(id = R.drawable.rounded_sticky_note_2_24), contentDescription = null)
                    Text(text = data.comment, modifier = Modifier.padding(start = 8.dp))
                }
            }
            AnimatedVisibility(visible = cardExpanded) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                ) {
                    val teacherName =
                        if (data.teacher.firstName != null && data.teacher.lastName != null && data.teacher.secondName != null) {
                            "${data.teacher.lastName} " +
                            "${data.teacher.firstName[0].uppercase()}. " +
                            "${data.teacher.secondName[0].uppercase()}."
                        } else {
                            data.teacher.username
                        }
                    Text(
                        text = teacherName,
                        modifier = Modifier.weight(1f)
                    )
                    // Show comment button
                    val classBelongsToTeacher = userData?.role == UserRoles.TEACHER.name && data.teacher.username == userData.username
                    if (classBelongsToTeacher) {
                        FilledTonalButton(
                            onClick = { commentDialogOpened = true },
                            contentPadding = PaddingValues(
                                start = 16.dp,
                                end = 24.dp,
                                top = 8.dp,
                                bottom = 8.dp
                            ),
                            modifier = Modifier.padding(end = 4.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.rounded_edit_note_24),
                                contentDescription = null,
                                modifier = Modifier.padding(end = 8.dp)
                            )
                            Text(text = stringResource(id = R.string.addCommentButton))
                        }
                    }
                    // Deletion button
                    if (userData?.role == UserRoles.ADMIN.name ||
                        (classBelongsToTeacher && classType == ClassTypes.CONSULTATION)) {
                        FilledTonalIconButton(
                            onClick = {
                                if (userData?.role == UserRoles.ADMIN.name) {
                                    onDelete()
                                } else {
                                    // No logics (((
                                }
                            },
                            colors = IconButtonDefaults
                                .filledTonalIconButtonColors(
                                    containerColor = MaterialTheme.colorScheme.errorContainer
                                )
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.rounded_delete_24),
                                contentDescription = stringResource(id = R.string.classRemoveButton)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TimetableScreenPreview() {
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        TimetableScreen(viewModel = viewModel(factory = MainViewModelFactory(LocalContext.current.dataStore)), onCreateClass = {})
    }
}

@Composable
fun CommentDialog(
    classData: TTClassModel,
    onDismissRequest: () -> Unit,
    onConfirmation: (String) -> Unit
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(28.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.addCommentDialogTitle),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                var tempComment by rememberSaveable { mutableStateOf(classData.comment) }
                OutlinedTextField(
                    value = tempComment,
                    onValueChange = { tempComment = it },
                    label = { Text(text = stringResource(id = R.string.addCommentDialogFieldLabel)) },
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ){
                    TextButton(onClick = onDismissRequest) {
                        Text(text = stringResource(id = R.string.cancel))
                    }
                    TextButton(onClick = { onConfirmation(tempComment) }) {
                        Text(text = stringResource(id = android.R.string.ok))
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun ClassCardPreview() {
    ClassCard(
        data = TTClassModel(
            id = 0,
            subjectName = "Testing test",
            classroom = "404A",
            duration = TTClassDuration(
                number = 1,
                startTime = LocalTime.now(),
                endTime = LocalTime.now()
            ),
            comment = "",
            classType = TTClassModel.N("practical"),
            teacher = UserModel(
                username = "SanyaPilot",
                role = "ADMIN"
            )
        ),
        userData = UserModel(
            username = "SanyaPilot",
            role = "ADMIN"
        ),
        onChangeComment = {},
        onDelete = {}
    )
}

@Preview
@Composable
fun CommentDialogPreview() {
    CommentDialog(
        classData = TTClassModel(
            id = 0,
            subjectName = "Операционные системы",
            classroom = "8А",
            duration = TTClassDuration(
                number = 1,
                startTime = LocalTime.now(),
                endTime = LocalTime.now(),
            ),
            classType = TTClassModel.N(name = "practical"),
            teacher = UserModel(
                username = "zloykin",
                firstName = "Евгений",
                secondName = "Сергеевич",
                lastName = "Зойкин",
                role = "TEACHER"
            ),
            comment = ""
        ),
        onDismissRequest = {},
        onConfirmation = {}
    )
}
