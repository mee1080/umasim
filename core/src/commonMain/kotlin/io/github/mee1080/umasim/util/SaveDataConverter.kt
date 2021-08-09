package io.github.mee1080.umasim.util

import io.github.mee1080.umasim.data.Chara
import io.github.mee1080.umasim.data.Store
import io.github.mee1080.umasim.data.SupportCard

object SaveDataConverter {

    private const val CHARA_DATA_VERSION = 1

    private const val SUPPORT_DATA_VERSION = 1

    private const val SUPPORT_INFO_VERSION = 1

    fun charaToString(chara: Chara?): String {
        return chara?.let { "$CHARA_DATA_VERSION,${it.id},${it.rarity},${it.rank}" } ?: ""
    }

    fun stringToChara(data: String?): Chara? {
        if (data == null) return null
        val info = data.split(",")
        return if (info[0].toIntOrNull() == CHARA_DATA_VERSION) {
            try {
                Store.getChara(info[1].toInt(), info[2].toInt(), info[3].toInt())
            } catch (_: Exception) {
                null
            }
        } else {
            null
        }
    }

    data class SupportInfo(
        val id: Int,
        val talent: Int,
        val join: Boolean = false,
        val friend: Boolean = false,
    )

    fun supportListToString(list: List<SupportInfo?>): String {
        return "$SUPPORT_DATA_VERSION," + list.filterNotNull().joinToString(",") {
            "$SUPPORT_INFO_VERSION:${it.id}:${it.talent}:${if (it.join) 1 else 0}:${if (it.friend) 1 else 0}"
        }
    }

    fun supportCardListToString(list: List<SupportCard?>): String {
        return supportListToString(list.filterNotNull().map { SupportInfo(it.id, it.talent) })
    }

    fun stringToSupportList(data: String?): List<SupportInfo> {
        if (data == null) return emptyList()
        val infoList = data.split(",")
        if (infoList[0].toIntOrNull() == SUPPORT_DATA_VERSION) {
            return (1 until infoList.size).mapNotNull {
                val info = infoList[it].split(":")
                if (info[0].toIntOrNull() == SUPPORT_INFO_VERSION) {
                    try {
                        SupportInfo(
                            info[1].toInt(),
                            info[2].toInt(),
                            info[3] == "1",
                            info[4] == "1",
                        )
                    } catch (_: Exception) {
                        null
                    }
                } else {
                    null
                }
            }
        }
        return emptyList()
    }

    fun stringToSupportCardList(data: String?): List<SupportCard> {
        return stringToSupportList(data).mapNotNull { Store.getSupportOrNull(it.id, it.talent) }
    }
}