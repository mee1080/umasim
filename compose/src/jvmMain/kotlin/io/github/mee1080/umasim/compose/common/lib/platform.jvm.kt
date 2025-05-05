package io.github.mee1080.umasim.compose.common.lib

import io.github.mee1080.umasim.BuildKonfig
import io.github.mee1080.umasim.compose.generated.resources.LINESeedJP_A_TTF_Rg
import io.github.mee1080.umasim.compose.generated.resources.Res
import io.github.mee1080.utility.fetchFromUrl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.awt.Desktop
import java.net.URI

actual fun jumpToUrl(url: String) {
    if (Desktop.isDesktopSupported()) {
        val desktop = Desktop.getDesktop()
        if (desktop?.isSupported(Desktop.Action.BROWSE) == true) {
            desktop.browse(URI(url))
        }
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
actual val mainDispatcher = Dispatchers.Default.limitedParallelism(1)

actual val asyncDispatcher = Dispatchers.Default

actual val defaultFontResource = Res.font.LINESeedJP_A_TTF_Rg

actual val defaultThreadCount = 4

actual val progressReportInterval = 100

actual val progressReportDelay = 1L

private val jsonDecoder = Json { ignoreUnknownKeys = true }

actual fun CoroutineScope.launchCheckUpdate(onUpdate: (newVersion: String) -> Unit) {
    if (BuildKonfig.APP_VERSION.isEmpty()) return
    launch(asyncDispatcher) {
        runCatching {
            val json = fetchFromUrl("https://api.github.com/repos/mee1080/umasim/releases/latest")
            val data = jsonDecoder.decodeFromString<GitHubReleaseData>(json)
            println("${BuildKonfig.APP_VERSION} -> ${data.tag_name}")
            if (BuildKonfig.APP_VERSION != data.tag_name) {
                onUpdate(data.tag_name)
            }
        }.onFailure {
            it.printStackTrace()
        }
    }
}

@Serializable
@Suppress("PropertyName")
private data class GitHubReleaseData(
    val tag_name: String,
)
