package com.example.kaspresso_learning.screens

import com.example.kaspresso_learning.Tags
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode

object AvatarSelectScreen : ComposeScreen<AvatarSelectScreen>() {

    val nextButton: KNode = child {
        hasTestTag(Tags.AVATAR_NEXT_BUTTON)
    }

    val error: KNode = child {
        hasTestTag(Tags.AVATAR_ERROR)
    }

    fun avatar(index: Int): KNode = child {
        hasTestTag("${Tags.AVATAR_ICON}_$index")
    }
}