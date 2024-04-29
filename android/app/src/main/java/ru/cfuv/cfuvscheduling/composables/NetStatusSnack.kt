package ru.cfuv.cfuvscheduling.composables

import android.content.Context
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import ru.cfuv.cfuvscheduling.R
import ru.cfuv.cfuvscheduling.api.NetErrors
import ru.cfuv.cfuvscheduling.api.NetStatus

@Composable
fun NetStatusSnack(
    context: Context,
    netStatus: NetStatus,
    snackbarHostState: SnackbarHostState
) {
    LaunchedEffect(snackbarHostState) {
        snackbarHostState.showSnackbar(
            message = when(netStatus.error) {
                NetErrors.UNAUTHORIZED -> context.resources.getString(R.string.loginInvalid)
                NetErrors.SERVERSIDE -> context.resources.getString(R.string.netErrorServerside)
                NetErrors.NO_INTERNET -> context.resources.getString(R.string.netErrorNoInternet)
                NetErrors.TIMEOUT -> context.resources.getString(R.string.netErrorTimeout)
                NetErrors.UNKNOWN -> context.resources.getString(R.string.netErrorGeneric)
                else -> "WTF"
            },
            duration = SnackbarDuration.Long
        )
    }
}
