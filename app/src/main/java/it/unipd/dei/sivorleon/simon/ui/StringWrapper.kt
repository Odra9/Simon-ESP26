package it.unipd.dei.sivorleon.simon.ui

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import it.unipd.dei.sivorleon.simon.ui.theme.Red

/**
 * Utility class to format output strings
 */
class StringWrapper {
    companion object {
        /**
         * Separate with commas every character in a string
         *
         * @return Comma separated string
         */
        fun commaSeparate(input: String) : String {
            var output = ""
            for (char in input) {
                output += "$char, "
            }

            // remove the last ", " added
            return output.slice(IntRange(0, output.length-3))
        }

        /**
         * Returns an annotated string with black text before the error position and red after
         * This will also call commaSeparate() on the input
         *
         * @param[input] Input string
         * @param[errorIndex] Index of first red character
         *
         * @return Annotated, comma separated string
         */
        fun annotateError(input: String, errorIndex : Int) : AnnotatedString {
            val blackText = input.slice(IntRange(0, (errorIndex - 1)))
            val redText = input.slice(IntRange(errorIndex, input.length - 1))

            val annotatedText = buildAnnotatedString {
                if (blackText.isNotEmpty()) {
                    append(commaSeparate(blackText))

                    if (redText.isNotEmpty()) {
                        append(", ")
                    }
                }
                if (redText.isNotEmpty()) {
                    withStyle(style = SpanStyle(color = Red)) {
                        append(commaSeparate(redText))
                    }
                }
            }

            return annotatedText
        }
    }
}