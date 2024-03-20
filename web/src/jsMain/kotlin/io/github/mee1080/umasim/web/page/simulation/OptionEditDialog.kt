package io.github.mee1080.umasim.web.page.simulation

import androidx.compose.runtime.*
import io.github.mee1080.umasim.ai.UafActionSelector
import io.github.mee1080.umasim.web.components.atoms.MdDialog
import io.github.mee1080.umasim.web.components.atoms.MdInputType
import io.github.mee1080.umasim.web.components.atoms.MdOutlinedTextField
import io.github.mee1080.umasim.web.components.atoms.rows
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.width

private val json = Json {
    encodeDefaults = true
    ignoreUnknownKeys = false
    prettyPrint = true
}

@Composable
fun OptionEditDialog(open: Boolean, current: UafActionSelector.Option, onClose: (UafActionSelector.Option?) -> Unit) {
    OptionEditDialog(open, json.encodeToString(current)) { input ->
        val option = input?.let {
            runCatching { json.decodeFromString<UafActionSelector.Option>(it) }.getOrNull()
        }
        onClose(option)
    }
}

@Composable
private fun OptionEditDialog(open: Boolean, current: String, onClose: (String?) -> Unit) {
    var edit by remember { mutableStateOf(current) }
    MdDialog(
        open = open,
        onPrimaryButton = { onClose(edit) },
        onSecondaryButton = { onClose(null) },
        onClosed = { onClose(null) },
    ) {
        MdOutlinedTextField(
            type = MdInputType.TextArea,
            value = edit,
            attrs = {
                onInput { edit = it }
                rows(16)
                style {
                    width(360.px)
                }
            }
        )
    }
}