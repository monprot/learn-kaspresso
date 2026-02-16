package com.example.kaspresso_learning.tests

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.example.kaspresso_learning.MainActivity
import com.example.kaspresso_learning.Tags
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import org.junit.Rule
import org.junit.Test

class LoginFlowTest : TestCase() {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun loginToFeed() = run {
        step("Нажать на второй аватар") {
            composeTestRule
                .onNodeWithTag("${Tags.AVATAR_ICON}_2")
                .assertIsDisplayed()
                .performClick()
        }

        step("Нажать 'Далее'") {
            composeTestRule
                .onNodeWithTag(Tags.AVATAR_NEXT_BUTTON)
                .assertIsDisplayed()
                .performClick()
        }

        step("Ввести имя «Тестер» в поле ввода") {
            composeTestRule
                .onNodeWithTag(Tags.NAME_EDIT_TEXT)
                .assertIsDisplayed()
                .performTextInput("Тестер")
        }

        step("Нажать на кнопку «Войти»") {
            composeTestRule
                .onNodeWithTag(Tags.NAME_EDIT_LOGIN_BUTTON)
                .assertIsDisplayed()
                .performClick()
        }

        step("Проверить отображение заголовок-приветствие на экране ленты") {
            composeTestRule
                .onNodeWithTag(Tags.FEED_TITLE)
                .assertIsDisplayed()
                .assertTextContains("Тестер", substring = true)
        }

        step("Проверить отображение поста") {
            composeTestRule
                .onNodeWithTag("${Tags.FEED_POST}_0")
                .assertIsDisplayed()
        }
    }
}