package com.example.kaspresso_learning.screens

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import com.example.kaspresso_learning.Tags
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode

class AvatarSelectScreen(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<AvatarSelectScreen>(
        semanticsProvider = semanticsProvider
    ) {

    val nextButton: KNode = child {
        hasTestTag(Tags.AVATAR_NEXT_BUTTON)
    }
}