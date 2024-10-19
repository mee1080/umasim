package io.github.mee1080.umasim.compose.common.atoms

import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.*
import androidx.compose.ui.text.style.TextDecoration
import io.github.mee1080.umasim.compose.common.lib.jumpToUrl

private val urlRegex = "https?://[\\w/:%#$&?()~.=+\\-]+".toRegex()

val defaultTextLinkStyle = TextLinkStyles(
    style = SpanStyle(
        color = Color.Blue,
        textDecoration = TextDecoration.Underline,
    ),
    hoveredStyle = SpanStyle(
        color = Color.Red,
    ),
    focusedStyle = SpanStyle(
        color = Color.Red,
    ),
)

@Composable
fun TextWithLink(
    text: String,
    modifier: Modifier = Modifier,
    linkStyle: TextLinkStyles = defaultTextLinkStyle,
) {
    val contents = buildList {
        var index = 0
        var result = urlRegex.find(text)
        while (result != null) {
            val start = result.range.first
            if (start > index) {
                add(text.substring(index, start) to null)
            }
            val url = text.substring(result.range)
            add(url to url)
            index = result.range.last + 1
            result = result.next()
        }
        if (index < text.length) {
            add(text.substring(index) to null)
        }
    }
    TextWithLink(contents, modifier, linkStyle)
}

@Composable
fun TextWithLink(
    contents: List<Pair<String, String?>>,
    modifier: Modifier = Modifier,
    linkStyle: TextLinkStyles = defaultTextLinkStyle,
) {
    val content = buildAnnotatedString {
        contents.forEach { (text, url) ->
            if (url == null) {
                append(text)
            } else {
                val annotation = LinkAnnotation.Url(url, linkStyle) {
                    jumpToUrl(url)
                }
                withLink(annotation) {
                    append(text)
                }
            }
        }
    }
    Text(
        text = content,
        style = LocalTextStyle.current,
        modifier = modifier
    )
}