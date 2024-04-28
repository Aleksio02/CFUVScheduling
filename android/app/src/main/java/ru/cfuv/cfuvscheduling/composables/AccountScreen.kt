package ru.cfuv.cfuvscheduling.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.cfuv.cfuvscheduling.MainViewModel
import ru.cfuv.cfuvscheduling.api.DummyAccountInfoModel
import ru.cfuv.cfuvscheduling.R

val ACCOUNT = DummyAccountInfoModel(
    "SanyaPilot",
    "Студент"
)

@Composable
fun AccountScreen(
    viewModel: MainViewModel = viewModel(),
    onNavigateToLogin: () -> Unit
) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.setAppBarTitle(context.getString(R.string.accountBarTitle))
    }

    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = ACCOUNT.name,
                    fontSize = 32.sp
                )
                Text(
                    text = stringResource(id = R.string.accountRole, ACCOUNT.role),
                    fontSize = 20.sp
                )
            }
            FilledTonalButton(
                onClick = { onNavigateToLogin() },
                modifier = Modifier.padding(end = 16.dp),
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 24.dp,
                    top = 8.dp,
                    bottom = 8.dp
                )
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.rounded_login_24),
                    contentDescription = null,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(text = stringResource(id = R.string.login))
            }
        }
        var notificationsEnabled by rememberSaveable { mutableStateOf(false) }
        ListItem(
            headlineContent = { Text(text = stringResource(id = R.string.settingsNotificationsTitle)) },
            supportingContent = { Text(text = stringResource(id = R.string.settingsNotificationsDescription)) },
            leadingContent = { Icon(Icons.Rounded.Notifications, contentDescription = null) },
            trailingContent = { Switch(checked = notificationsEnabled, onCheckedChange = { notificationsEnabled = !notificationsEnabled }) },
            modifier = Modifier
                .padding(top = 8.dp)
                .clickable { notificationsEnabled = !notificationsEnabled } // TODO: notifications logics
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AccountScreenPreview() {
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        AccountScreen(onNavigateToLogin = {})
    }
}
