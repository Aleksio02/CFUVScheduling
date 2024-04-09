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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.cfuv.cfuvscheduling.R

val GROUPS = listOf("ПИ-231(1)", "ПИ-231(2)", "ПИ-232(1)", "ПИ-232(2)", "ПИ-233(1)", "ПИ-233(2)")

@Composable
fun TimetableListScreen() {
    var selectedID by rememberSaveable { mutableIntStateOf(0) }
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
        items(GROUPS.size) { idx ->
            ListItem(
                headlineContent = { Text(text = GROUPS[idx]) },
                leadingContent = { RadioButton(selected = selectedID == idx, onClick = { selectedID = idx }) },
                modifier = Modifier.clickable { selectedID = idx }
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
