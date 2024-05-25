package `fun`.vari.tigrazul

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.res.loadSvgPainter
import androidx.compose.ui.res.useResource
import androidx.compose.ui.unit.Density
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import `fun`.vari.tigrazul.ui.HomeTabBar
import `fun`.vari.tigrazul.ui.theme.TigrazulTheme


@Composable
@Preview
fun App() {
    TigrazulTheme{
        HomeTabBar()
    }
}

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Tigrazul",
        icon =  BitmapPainter(useResource("icon/icon.png",::loadImageBitmap))
    ) {
        App()
    }
}


