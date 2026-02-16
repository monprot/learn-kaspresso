package com.example.kaspresso_learning.tests

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.example.kaspresso_learning.MainActivity
import com.example.kaspresso_learning.Tags
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import org.junit.Rule
import org.junit.Test

class AvatarSelectScreenTest : TestCase() {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun checkAvatarScreen() = run {
        step("Проверить, что экран выбора аватара отображается") {
            composeTestRule
                .onNode(hasTestTag(Tags.AVATAR_CONTAINER))
                .assertIsDisplayed()
        }

        step("Проверить, что отображаются 4 иконки аватаров") {
            for (i in 0..3) {
                composeTestRule
                    .onNodeWithTag("${Tags.AVATAR_ICON}_$i")
                    .assertIsDisplayed()
            }
        }

        step("Проверить, что кнопка «Далее» отображается") {
            composeTestRule
                .onNodeWithTag(Tags.AVATAR_NEXT_BUTTON)
                .assertIsDisplayed()
        }
    }

    @Test
    fun checkAvatarError() = run {
        step("Нажать на кнопку 'Далее'") {
            composeTestRule
                .onNodeWithTag(Tags.AVATAR_NEXT_BUTTON)
                .assertIsDisplayed()
                .performClick()
        }

        step("Проверить отображение ошибки") {
            composeTestRule
                .onNodeWithTag(Tags.AVATAR_ERROR)
                .assertIsDisplayed()
                .assertTextEquals("Сначала выберите аватар!")
        }
    }
}
