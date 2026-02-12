package com.example.kaspresso_learning

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import org.junit.Rule
import org.junit.Test

class AvatarSelectScreenTest : TestCase() {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun checkAvatarScreen() = run {
        step("Проверяем, что экран выбора аватара отображается") {
            composeTestRule
                .onNodeWithTag(Tags.AVATAR_CONTAINER)
                .assertIsDisplayed()
        }

        step("Проверяем, что отображаются 4 иконки аватаров") {
            for (i in 0..3) {
                composeTestRule
                    .onNodeWithTag("${Tags.AVATAR_ICON}_$i")
                    .assertIsDisplayed()
            }
        }

        step("Проверяем, что кнопка «Далее» отображается") {
            composeTestRule
                .onNodeWithTag(Tags.AVATAR_NEXT_BUTTON)
                .assertIsDisplayed()
        }
    }
}
