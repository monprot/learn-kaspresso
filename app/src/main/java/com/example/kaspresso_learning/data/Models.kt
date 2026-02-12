package com.example.kaspresso_learning.data

enum class TeaType(val displayName: String) {
    GREEN("Зелёный"),
    BLACK("Чёрный"),
    OOLONG("Улун"),
    PUERH("Пуэр")
}

enum class Vessel(val displayName: String) {
    GAIWAN("Гайвань"),
    TEAPOT("Чайник")
}

data class Tea(
    val id: Int,
    val name: String,
    val type: TeaType
)

data class BrewingEntry(
    val id: Int,
    val authorName: String,
    val authorAvatarIndex: Int,
    val tea: Tea,
    val weightGrams: Int,
    val volumeMl: Int? = null,
    val vessel: Vessel? = null,
    val infusions: Int? = null,
    val timestamp: Long
)
