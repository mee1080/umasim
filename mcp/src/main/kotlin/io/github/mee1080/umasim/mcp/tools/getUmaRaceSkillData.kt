package io.github.mee1080.umasim.mcp.tools

import io.github.mee1080.umasim.mcp.errorResult
import io.github.mee1080.umasim.mcp.textResult
import io.github.mee1080.umasim.race.data2.findSkills
import io.modelcontextprotocol.kotlin.sdk.Tool
import io.modelcontextprotocol.kotlin.sdk.server.Server
import kotlinx.serialization.json.*

fun Server.getUmaRaceSkillData() {
    addTool(
        name = "get_uma_race_skill_data",
        description = "ウマ娘がレースで発動するスキルの情報を取得します",
        inputSchema = Tool.Input(
            properties = buildJsonObject {
                putJsonObject("skill_name") {
                    put("type", "string")
                    put("description", "スキルの名称")
                }
            },
            required = listOf("skill_name")
        ),
    ) { request ->
        try {
            val skillName = request.arguments["skill_name"]?.jsonPrimitive?.content
            if (skillName == null) {
                return@addTool errorResult("スキルの名称が必要です")
            }
            val skillData = findSkills(skillName)
            if (skillData == null) {
                return@addTool textResult("スキルが見つかりませんでした")
            }
            val results = skillData.map { skill ->
                buildJsonObject {
                    put("name", skill.name)
                    val description = buildJsonArray {
                        skill.info.subList(1, skill.info.size).forEach { add(it) }
                    }
                    put("description", description)
                }
            }
            textResult(results.toString())
        } catch (e: Exception) {
            e.printStackTrace()
            errorResult("エラーが発生しました")
        }
    }
}