package ru.cfuv.cfuvscheduling.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.cfuv.cfuvscheduling.MainViewModel
import ru.cfuv.cfuvscheduling.R
import ru.cfuv.cfuvscheduling.api.TTClassModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun TimetableScreen(date: LocalDate, viewModel: MainViewModel = viewModel()) {
    val currentGroupName by viewModel.currentGroupName.collectAsState()
    LaunchedEffect(currentGroupName) {
        viewModel.setAppBarTitle(currentGroupName)
    }
    val classes by viewModel.currentClasses.collectAsState()

    LazyColumn(
        Modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp)
    ) {
        item {
            Text(
                text = stringResource(
                    id = R.string.timetableTitle,
                    date.format(DateTimeFormatter.ofPattern("EEEE"))
                ),
                fontSize = 28.sp,
                modifier = Modifier.padding(bottom = 12.dp)
            )
        }
        items(classes) {
            ClassCard(it)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClassCard(data: TTClassModel) {
    var cardExpanded by rememberSaveable { mutableStateOf(false) }
    OutlinedCard(
        onClick = { cardExpanded = !cardExpanded },
        Modifier
            .padding(bottom = 8.dp)
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                val isPractical = data.classType.name == "practical"
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(
                            if (isPractical)
                                MaterialTheme.colorScheme.tertiaryContainer else
                                MaterialTheme.colorScheme.primaryContainer
                        )
                ) {
                    Text(
                        text = stringResource(id = if (isPractical) R.string.classTypePractical else R.string.classTypeLecture),
                        modifier = Modifier.padding(vertical = 8.dp, horizontal = 12.dp)
                    )
                }
                Text(
                    text = data.subjectName,
                    fontSize = 16.sp,
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
            AnimatedVisibility(visible = cardExpanded) {
                Text(
                    text = data.teacher.username,
                    modifier = Modifier.padding(top = 8.dp)
                )
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
        TimetableScreen(LocalDate.now())
    }
}
