package io.github.mee1080.umasim.web.page.simulation

import androidx.compose.runtime.*
import io.github.mee1080.umasim.simulation2.SerializableActionSelectorGenerator
import io.github.mee1080.umasim.web.components.atoms.MdDialog
import io.github.mee1080.umasim.web.components.atoms.MdInputType
import io.github.mee1080.umasim.web.components.atoms.MdOutlinedTextField
import io.github.mee1080.umasim.web.components.atoms.rows
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.width

val json = Json {
    encodeDefaults = true
    ignoreUnknownKeys = false
    prettyPrint = true
}

@Composable
fun OptionEditDialog(
    open: Boolean,
    current: SerializableActionSelectorGenerator,
    onClose: (SerializableActionSelectorGenerator?) -> Unit,
) {
    OptionEditDialog(open, current.serialize()) { input ->
        val option = input?.let {
            runCatching { current.deserialize(it) }.getOrNull()
        }
        onClose(option)
    }
}

@Composable
fun OptionEditDialog(open: Boolean, current: String, onClose: (String?) -> Unit) {
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