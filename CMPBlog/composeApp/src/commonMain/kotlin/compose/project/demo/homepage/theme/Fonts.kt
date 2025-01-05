package theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import cmpblog.composeapp.generated.resources.Res
import cmpblog.composeapp.generated.resources.noto_sans_sc_bold
import cmpblog.composeapp.generated.resources.noto_sans_sc_regular
import org.jetbrains.compose.resources.Font

@Composable
fun SansFont() = FontFamily(
    Font(Res.font.noto_sans_sc_regular),
    Font(Res.font.noto_sans_sc_bold, weight = FontWeight.Bold)
)
