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
    snackbarHostState: SnackbarHostState,
    messageHandler: ((NetErrors) -> String?)? = null
) {
    if (netStatus.error != null) {
        LaunchedEffect(snackbarHostState) {
            snackbarHostState.showSnackbar(
                message = messageHandler?.invoke(netStatus.error) ?: when (netStatus.error) {
                    NetErrors.BAD_REQUEST -> context.resources.getString(R.string.netErrorGeneric)
                    NetErrors.UNAUTHORIZED -> context.resources.getString(R.string.loginInvalid)
                    NetErrors.SERVERSIDE -> context.resources.getString(R.string.netErrorServerside)
                    NetErrors.NO_INTERNET -> context.resources.getString(R.string.netErrorNoInternet)
                    NetErrors.TIMEOUT -> context.resources.getString(R.string.netErrorTimeout)
                    NetErrors.UNKNOWN -> context.resources.getString(R.string.netErrorGeneric)
                },
                duration = SnackbarDuration.Long
            )
        }
    }
}
