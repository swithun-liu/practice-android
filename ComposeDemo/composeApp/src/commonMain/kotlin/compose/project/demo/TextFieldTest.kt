package compose.project.demo

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation


@Composable
fun TextFieldTest() {
    CreditCardTextField()
}


@Composable
fun CreditCardTextField() {
    var cardNum by remember { mutableStateOf("") }
    TextField(
        value = cardNum,
        onValueChange = {input ->
            if (input.length <= 16 && input.none { !it.isDigit() }) {
                cardNum = input
            }
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        visualTransformation = CreditCardVisualTransformation(),
    )
}

class CreditCardVisualTransformation() : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        return TransformedText(
            text = buildAnnotatedString {
                val digits = text.filter { it.isDigit() }
                val trimmed = if (digits.length >= 16) digits.substring(0..15) else digits
                var out = ""
                // 遍历输入
                for (i in trimmed.indices) {
                    out += trimmed[i]
                    if (i % 4 == 3 && i != 15) out += "-"
                }
                append(out)
            },
            offsetMapping = object : OffsetMapping {
                override fun originalToTransformed(offset: Int): Int {
                    if (offset <= 3) return offset
                    if (offset <= 7) return offset + 1
                    if (offset <= 11) return offset + 2
                    if (offset <= 16) return offset + 3
                    return 19
                }

                override fun transformedToOriginal(offset: Int): Int {
                    if (offset <= 4) return offset
                    if (offset <= 9) return offset - 1
                    if (offset <= 14) return offset - 2
                    if (offset <= 19) return offset - 3
                    return 16
                }
            },
        )
    }
}
