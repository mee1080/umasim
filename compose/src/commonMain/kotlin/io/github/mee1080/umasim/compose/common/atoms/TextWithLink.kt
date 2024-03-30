package io.github.mee1080.umasim.compose.common.atoms

import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.*
import androidx.compose.ui.text.style.TextDecoration
import io.github.mee1080.umasim.compose.common.lib.buildPersistentList
import io.github.mee1080.umasim.compose.common.lib.jumpToUrl
import kotlinx.collections.immutable.ImmutableList

private val urlRegex = "https?://[\\w/:%#$&?()~.=+\\-]+".toRegex()

@Composable
fun TextWithLink(
    text: String,
    modifier: Modifier = Modifier,
    linkStyle: SpanStyle = SpanStyle(
        color = Color.Blue,
        textDecoration = TextDecoration.Underline,
    ),
) {
    val contents = buildPersistentList {
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

@OptIn(ExperimentalTextApi::class)
@Composable
fun TextWithLink(
    contents: ImmutableList<Pair<String, String?>>,
    modifier: Modifier = Modifier,
    linkStyle: SpanStyle = SpanStyle(
        color = Color.Blue,
        textDecoration = TextDecoration.Underline,
    ),
) {
    val content = buildAnnotatedString {
        contents.forEach { (text, url) ->
            if (url == null) {
                append(text)
            } else {
                withAnnotation(UrlAnnotation(url)) {
                    withStyle(linkStyle) {
                        append(text)
                    }
                }
            }
        }
    }
    ClickableText(
        text = content,
        style = LocalTextStyle.current,
        modifier = modifier,
    ) { offset ->
        content.getUrlAnnotations(offset, offset).firstOrNull()?.let { annotation ->
            jumpToUrl(annotation.item.url)
        }
    }
}