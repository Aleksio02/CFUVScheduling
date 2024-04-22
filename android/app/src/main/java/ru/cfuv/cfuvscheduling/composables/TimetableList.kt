package ru.cfuv.cfuvscheduling.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.cfuv.cfuvscheduling.MainViewModel
import ru.cfuv.cfuvscheduling.R

@Composable
fun TimetableListScreen(viewModel: MainViewModel = viewModel()) {
    val selectedID by viewModel.currentGroupIdx.collectAsState()
    val groups by viewModel.groupList.collectAsState()

    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.setAppBarTitle(context.getString(R.string.timetableListBarTitle))
    }

    LazyColumn {
        item {
            OutlinedCard(
                modifier = Modifier.padding(12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(16.dp)
                ) {
                    Icon(
                        Icons.Outlined.Info,
                        contentDescription = null
                    )
                    Text(
                        text = stringResource(id = R.string.timetableListDescription),
                        modifier = Modifier.padding(start = 12.dp)
                    )
                }
            }
        }
        items(groups.size) { idx ->
            ListItem(
                headlineContent = { Text(text = groups[idx]) },
                leadingContent = {
                    RadioButton(
                        selected = selectedID == idx,
                        onClick = { viewModel.setCurrentGroup(idx) }
                    )
                },
                modifier = Modifier.clickable { viewModel.setCurrentGroup(idx) }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TimetableListScreenPreview() {
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        TimetableListScreen()
    }
}
