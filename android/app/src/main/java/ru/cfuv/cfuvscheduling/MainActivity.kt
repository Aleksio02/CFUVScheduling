package ru.cfuv.cfuvscheduling

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.List
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.cfuv.cfuvscheduling.composables.AccountScreen
import ru.cfuv.cfuvscheduling.composables.TimetableListScreen
import ru.cfuv.cfuvscheduling.composables.TimetableScreen
import ru.cfuv.cfuvscheduling.ui.theme.CFUVSchedulingTheme
import java.time.LocalDate

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CFUVSchedulingTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
                }
            }
        }
    }
}


const val START_DESTINATION = "timetable"
data class DestinationInfo(
    val id: String,
    val icon: ImageVector? = null,
    val iconResource: Int? = null,
    val label: Int,
    val composable: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModel: MainViewModel = viewModel()) {
    val navController = rememberNavController()
    val appBarTitle by viewModel.appBarTitle.collectAsState()

    val destinations = listOf(
        DestinationInfo(
            id = "timetable",
            icon = Icons.Rounded.List,
            label = R.string.timetableNavItem,
            composable = { TimetableScreen(date = LocalDate.now(), viewModel) }
        ),
        DestinationInfo(
            id = "timetableList",
            iconResource = R.drawable.table_view_24,
            label = R.string.timetableListNavItem,
            composable = { TimetableListScreen(viewModel) }
        ),
        DestinationInfo(
            id = "account",
            icon = Icons.Rounded.AccountCircle,
            label = R.string.accountNavItem,
            composable = { AccountScreen(viewModel) }
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = appBarTitle) })
        },
        bottomBar = {
            var selectedItem by rememberSaveable { mutableStateOf(START_DESTINATION) }
            NavigationBar {
                destinations.forEach { dest ->
                    NavigationBarItem(
                        selected = selectedItem == dest.id,
                        onClick = {
                            navController.navigate(dest.id)
                            selectedItem = dest.id
                        },
                        icon = {
                            dest.icon?.let { Icon(it, contentDescription = null) }
                            dest.iconResource?.let { Icon(painter = painterResource(id = it), contentDescription = null) }
                        },
                        label = { Text(text = stringResource(id = dest.label)) }
                    )
                }
            }
        }
    ) {
        Column(
            modifier = Modifier.padding(it)
        ) {
            NavHost(
                navController = navController,
                startDestination = START_DESTINATION,
            ) {
                destinations.forEach { dest ->
                    composable(dest.id, content = dest.composable)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    CFUVSchedulingTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            MainScreen()
        }
    }
}
