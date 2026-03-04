package player.utils

import android.app.Activity
import android.content.ContextWrapper
import android.content.pm.ActivityInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

@Composable
internal actual fun VideoPlayerOrientation(orientation: ScreenOrientation) {
    val context = LocalContext.current

    LaunchedEffect(orientation) {
        var currentContext = context
        while (currentContext is ContextWrapper) {
            if (currentContext is Activity) {
                val window = currentContext.window
                val controller = WindowCompat.getInsetsController(window, window.decorView)

                if (orientation == ScreenOrientation.LANDSCAPE) {
                    // 1. Rotate to Landscape
                    currentContext.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

                    // 2. Hide System Bars (Full Screen)
                    controller.hide(WindowInsetsCompat.Type.systemBars())
                    controller.systemBarsBehavior =
                        WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                } else {
                    // 1. Rotate to Portrait
                    currentContext.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED

                    // 2. Show System Bars again
                    controller.show(WindowInsetsCompat.Type.systemBars())
                }
                break
            }
            currentContext = currentContext.baseContext
        }
    }
}

