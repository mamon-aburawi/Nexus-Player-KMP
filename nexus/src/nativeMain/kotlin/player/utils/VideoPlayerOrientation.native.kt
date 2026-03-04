package player.utils

import androidx.compose.runtime.Composable
import platform.UIKit.UIApplication
import platform.UIKit.UIInterfaceOrientationMaskLandscapeRight
import platform.UIKit.UIInterfaceOrientationMaskPortrait
import platform.UIKit.UIStatusBarAnimation
import platform.UIKit.UIWindowScene
import platform.UIKit.UIWindowSceneGeometryPreferencesIOS
import platform.UIKit.setStatusBarHidden

@Composable
internal actual fun VideoPlayerOrientation(orientation: ScreenOrientation) {
    val windowScene = UIApplication.sharedApplication.connectedScenes.firstOrNull() as? UIWindowScene
    val uiApp = UIApplication.sharedApplication
    val geometryUpdate = when (orientation) {
        ScreenOrientation.LANDSCAPE -> {
            UIWindowSceneGeometryPreferencesIOS(UIInterfaceOrientationMaskLandscapeRight)
        }
        ScreenOrientation.PORTRAIT -> {
            UIWindowSceneGeometryPreferencesIOS(UIInterfaceOrientationMaskPortrait)
        }
    }
    if (orientation == ScreenOrientation.LANDSCAPE){
        uiApp.setStatusBarHidden(true, withAnimation = UIStatusBarAnimation.UIStatusBarAnimationFade)
    }else {
        uiApp.setStatusBarHidden(false, withAnimation = UIStatusBarAnimation.UIStatusBarAnimationFade)
    }
    windowScene?.requestGeometryUpdateWithPreferences(geometryUpdate) { error ->
        println("Ios target: Orientation change failed: ${error?.localizedDescription}")
    }
}