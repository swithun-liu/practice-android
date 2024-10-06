package com.swithun.edgetoedge

import android.app.UiModeManager
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.annotation.ColorInt
import androidx.annotation.DoNotInline
import androidx.annotation.RequiresApi
import androidx.annotation.VisibleForTesting
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat

// The light scrim color used in the platform API 29+
// https://cs.android.com/android/platform/superproject/+/master:frameworks/base/core/java/com/android/internal/policy/DecorView.java;drc=6ef0f022c333385dba2c294e35b8de544455bf19;l=142
@VisibleForTesting
internal val DefaultLightScrim = Color.argb(0xe6, 0xFF, 0xFF, 0xFF)

// The dark scrim color used in the platform.
// https://cs.android.com/android/platform/superproject/+/master:frameworks/base/core/res/res/color/system_bar_background_semi_transparent.xml
// https://cs.android.com/android/platform/superproject/+/master:frameworks/base/core/res/remote_color_resources_res/values/colors.xml;l=67
@VisibleForTesting
internal val DefaultDarkScrim = Color.argb(0x80, 0x1b, 0x1b, 0x1b)

private var Impl: EdgeToEdgeImpl? = null

/**
 * Enables the edge-to-edge display for this [ComponentActivity].
 *
 * To set it up with the default style, call this method in your Activity's onCreate method:
 * ```
 *     override fun onCreate(savedInstanceState: Bundle?) {
 *         enableEdgeToEdge()
 *         super.onCreate(savedInstanceState)
 *         ...
 *     }
 * ```
 *
 * The default style configures the system bars with a transparent background when contrast can be
 * enforced by the system (API 29 or above). On older platforms (which only have 3-button/2-button
 * navigation modes), an equivalent scrim is applied to ensure contrast with the system bars.
 *
 * See [SystemBarStyle] for more customization options.
 *
 * @param statusBarStyle The [SystemBarStyle] for the status bar.
 * @param navigationBarStyle The [SystemBarStyle] for the navigation bar.
 */
@JvmName("enable")
@JvmOverloads
fun ComponentActivity.swithunEnableEdgeToEdge(
    statusBarStyle: SystemBarStyle = SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT),
    navigationBarStyle: SystemBarStyle = SystemBarStyle.auto(DefaultLightScrim, DefaultDarkScrim)
) {
    val view = window.decorView
    val statusBarIsDark = statusBarStyle.detectDarkMode(view.resources)
    val navigationBarIsDark = navigationBarStyle.detectDarkMode(view.resources)
    val impl = Impl ?: if (Build.VERSION.SDK_INT >= 30) {
        EdgeToEdgeApi30()
    } else if (Build.VERSION.SDK_INT >= 29) { // 10
        EdgeToEdgeApi29()
    } else if (Build.VERSION.SDK_INT >= 28) { // 9
        EdgeToEdgeApi28()
    } else if (Build.VERSION.SDK_INT >= 26) { // 8 27:8.1
        EdgeToEdgeApi26()
    } else if (Build.VERSION.SDK_INT >= 23) { // 6
        EdgeToEdgeApi23()
    } else if (Build.VERSION.SDK_INT >= 21) { // 5 22:5.1
        EdgeToEdgeApi21()
    } else { // < 5
        EdgeToEdgeBase()
    }.also { Impl = it }
    impl.setUp(
        statusBarStyle, navigationBarStyle, window, view, statusBarIsDark, navigationBarIsDark
    )
    impl.adjustLayoutInDisplayCutoutMode(window)
}

fun transparentNavigationBar(window: Window) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        window.isNavigationBarContrastEnforced = false
    }
//    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
//    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
//    var systemUiVisibility = window.decorView.systemUiVisibility
//    systemUiVisibility =
//        systemUiVisibility or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//    window.decorView.systemUiVisibility = systemUiVisibility
//    window.navigationBarColor = Color.TRANSPARENT

    //设置导航栏按钮或导航条颜色
//    setNavigationBarBtnColor(window, false)
}

fun setNavigationBarBtnColor(window: Window, light: Boolean) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        var systemUiVisibility = window.decorView.systemUiVisibility
        systemUiVisibility = if (light) { //白色按钮
            systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR.inv()
        } else { //黑色按钮
            systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
        }
        window.decorView.systemUiVisibility = systemUiVisibility
    }
}


/**
 * The style for the status bar or the navigation bar used in [enableEdgeToEdge].
 */
class SystemBarStyle private constructor(
    private val lightScrim: Int,
    internal val darkScrim: Int,
    internal val nightMode: Int,
    internal val detectDarkMode: (Resources) -> Boolean
) {

    companion object {

        /**
         * Creates a new instance of [SystemBarStyle]. This style detects the dark mode
         * automatically and applies the recommended style for each of the status bar and the
         * navigation bar. If this style doesn't work for your app, consider using either [dark] or
         * [light].
         * - On API level 29 and above, both the status bar and the navigation bar will be
         *   transparent. However, the navigation bar with 3 or 2 buttons will have a translucent
         *   scrim. This scrim color is provided by the platform and *cannot be customized*.
         * - On API level 28 and below, the status bar will be transparent, and the navigation bar
         *   will have one of the specified scrim colors depending on the dark mode.
         * @param lightScrim The scrim color to be used for the background when the app is in light
         * mode. Note that this is used only on API level 28 and below.
         * @param darkScrim The scrim color to be used for the background when the app is in dark
         * mode. This is also used on devices where the system icon color is always light. Note that
         * this is used only on API level 28 and below.
         * @param detectDarkMode Optional. Detects whether UI currently uses dark mode or not. The
         * default implementation can detect any of the standard dark mode features from the
         * platform, appcompat, and Jetpack Compose.
         */
        @JvmStatic
        @JvmOverloads
        fun auto(
            @ColorInt lightScrim: Int,
            @ColorInt darkScrim: Int,
            detectDarkMode: (Resources) -> Boolean = { resources ->
                (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) ==
                        Configuration.UI_MODE_NIGHT_YES
            }
        ): SystemBarStyle {
            return SystemBarStyle(
                lightScrim = lightScrim,
                darkScrim = darkScrim,
                nightMode = UiModeManager.MODE_NIGHT_AUTO,
                detectDarkMode = detectDarkMode
            )
        }

        /**
         * Creates a new instance of [SystemBarStyle]. This style consistently applies the specified
         * scrim color regardless of the system navigation mode.
         *
         * @param scrim The scrim color to be used for the background. It is expected to be dark
         * for the contrast against the light system icons.
         */
        @JvmStatic
        fun dark(@ColorInt scrim: Int): SystemBarStyle {
            return SystemBarStyle(
                lightScrim = scrim,
                darkScrim = scrim,
                nightMode = UiModeManager.MODE_NIGHT_YES,
                detectDarkMode = { _ -> true }
            )
        }

        /**
         * Creates a new instance of [SystemBarStyle]. This style consistently applies the specified
         * scrim color regardless of the system navigation mode.
         *
         * @param scrim The scrim color to be used for the background. It is expected to be light
         * for the contrast against the dark system icons.
         * @param darkScrim The scrim color to be used for the background on devices where the
         * system icon color is always light. It is expected to be dark.
         */
        @JvmStatic
        fun light(@ColorInt scrim: Int, @ColorInt darkScrim: Int): SystemBarStyle {
            return SystemBarStyle(
                lightScrim = scrim,
                darkScrim = darkScrim,
                nightMode = UiModeManager.MODE_NIGHT_NO,
                detectDarkMode = { _ -> false }
            )
        }
    }

    internal fun getScrim(isDark: Boolean) = if (isDark) darkScrim else lightScrim

    internal fun getScrimWithEnforcedContrast(isDark: Boolean): Int {
        return when {
            nightMode == UiModeManager.MODE_NIGHT_AUTO -> Color.TRANSPARENT
            isDark -> darkScrim
            else -> lightScrim
        }
    }
}

private interface EdgeToEdgeImpl {

    fun setUp(
        statusBarStyle: SystemBarStyle,
        navigationBarStyle: SystemBarStyle,
        window: Window,
        view: View,
        statusBarIsDark: Boolean,
        navigationBarIsDark: Boolean
    )

    fun adjustLayoutInDisplayCutoutMode(window: Window)
}

private open class EdgeToEdgeBase : EdgeToEdgeImpl {

    override fun setUp(
        statusBarStyle: SystemBarStyle,
        navigationBarStyle: SystemBarStyle,
        window: Window,
        view: View,
        statusBarIsDark: Boolean,
        navigationBarIsDark: Boolean
    ) {
        // No edge-to-edge before SDK 21.
    }

    override fun adjustLayoutInDisplayCutoutMode(window: Window) {
        // No display cutout before SDK 28.
    }
}

@RequiresApi(21)
private class EdgeToEdgeApi21 : EdgeToEdgeBase() {

    @Suppress("DEPRECATION")
    @DoNotInline
    override fun setUp(
        statusBarStyle: SystemBarStyle,
        navigationBarStyle: SystemBarStyle,
        window: Window,
        view: View,
        statusBarIsDark: Boolean,
        navigationBarIsDark: Boolean
    ) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
    }
}

@RequiresApi(23)
private class EdgeToEdgeApi23 : EdgeToEdgeBase() {

    @DoNotInline
    override fun setUp(
        statusBarStyle: SystemBarStyle,
        navigationBarStyle: SystemBarStyle,
        window: Window,
        view: View,
        statusBarIsDark: Boolean,
        navigationBarIsDark: Boolean
    ) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.statusBarColor = statusBarStyle.getScrim(statusBarIsDark)
        window.navigationBarColor = navigationBarStyle.darkScrim
        WindowInsetsControllerCompat(window, view).isAppearanceLightStatusBars = !statusBarIsDark
    }
}

@RequiresApi(26)
private open class EdgeToEdgeApi26 : EdgeToEdgeBase() {

    @DoNotInline
    override fun setUp(
        statusBarStyle: SystemBarStyle,
        navigationBarStyle: SystemBarStyle,
        window: Window,
        view: View,
        statusBarIsDark: Boolean,
        navigationBarIsDark: Boolean
    ) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.statusBarColor = statusBarStyle.getScrim(statusBarIsDark)
        window.navigationBarColor = navigationBarStyle.getScrim(navigationBarIsDark)
        WindowInsetsControllerCompat(window, view).run {
            isAppearanceLightStatusBars = !statusBarIsDark
            isAppearanceLightNavigationBars = !navigationBarIsDark
        }
    }
}

@RequiresApi(28)
private open class EdgeToEdgeApi28 : EdgeToEdgeApi26() {

    @DoNotInline
    override fun adjustLayoutInDisplayCutoutMode(window: Window) {
        window.attributes.layoutInDisplayCutoutMode =
            WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
    }
}

@RequiresApi(29)
private open class EdgeToEdgeApi29 : EdgeToEdgeApi28() {

    @DoNotInline
    override fun setUp(
        statusBarStyle: SystemBarStyle,
        navigationBarStyle: SystemBarStyle,
        window: Window,
        view: View,
        statusBarIsDark: Boolean,
        navigationBarIsDark: Boolean
    ) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.statusBarColor = statusBarStyle.getScrimWithEnforcedContrast(statusBarIsDark)
        window.navigationBarColor =
            navigationBarStyle.getScrimWithEnforcedContrast(navigationBarIsDark)
        window.isStatusBarContrastEnforced = false
        window.isNavigationBarContrastEnforced = true
        WindowInsetsControllerCompat(window, view).run {
            isAppearanceLightStatusBars = !statusBarIsDark
            isAppearanceLightNavigationBars = !navigationBarIsDark
        }
    }
}

@RequiresApi(30)
private class EdgeToEdgeApi30 : EdgeToEdgeApi29() {

    @DoNotInline
    override fun adjustLayoutInDisplayCutoutMode(window: Window) {
        window.attributes.layoutInDisplayCutoutMode =
            WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_ALWAYS
    }
}
