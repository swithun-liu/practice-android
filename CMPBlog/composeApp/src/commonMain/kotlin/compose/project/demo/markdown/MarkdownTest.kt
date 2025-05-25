package compose.project.demo.markdown

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp

val markdownContent = """  
	# Sample  
	* Markdown  
	* [Link](https://example.com)  
	![Image](https://example.com/img.png)  
	<a href="https://www.google.com/">Google</a>  
"""

//Minimal example
@Composable
fun MinimalExampleContent() {
}

@Composable
fun LaTeXView(latex: String) {
}

//Complex example
//@Composable
//fun ComplexExampleContent() {
//    MarkdownText(
//        modifier = Modifier.padding(8.dp),
//        markdown = markdown,
//        maxLines = 3,
////        fontResource = R.font.montserrat_medium,
//        style = TextStyle(
//            color = Color.Blue,
//            fontSize = 12.sp,
//            lineHeight = 10.sp,
//            textAlign = TextAlign.Justify,
//        ),
//
//        )
//}