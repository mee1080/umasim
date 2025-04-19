package io.github.mee1080.utility


fun normalizedLevenshteinDistance(s1: String, s2: String): Double {
    return levenshteinDistance(s1, s2).toDouble() / maxOf(s1.length, s2.length)
}

fun levenshteinDistance(s1: String, s2: String): Int {
    val len1 = s1.length
    val len2 = s2.length

    val dp = Array(len1 + 1) { IntArray(len2 + 1) }
    for (i in 0..len1) {
        dp[i][0] = i
    }
    for (j in 0..len2) {
        dp[0][j] = j
    }

    for (i in 1..len1) {
        for (j in 1..len2) {
            val cost = if (s1[i - 1] == s2[j - 1]) 0 else 1
            dp[i][j] = minOf(
                dp[i - 1][j] + 1,       // 削除
                dp[i][j - 1] + 1,       // 挿入
                dp[i - 1][j - 1] + cost // 置換
            )
        }
    }

    return dp[len1][len2]
}
