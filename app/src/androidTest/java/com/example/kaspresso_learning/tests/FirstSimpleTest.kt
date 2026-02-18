package com.example.kaspresso_learning.tests

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.kaspresso_learning.Tags
import com.example.kaspresso_learning.BaseTestCase
import org.junit.Test

class FirstSimpleTest : BaseTestCase() {

    @Test
    fun checkNameEditScreen() = run {
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

        step("Проверить, что открылся экран ввода имени") {

            step("Проверить заголовок «Как вас зовут?»") {
                composeTestRule
                    .onNodeWithTag(Tags.NAME_EDIT_TITLE)
                    .assertIsDisplayed()
                    .assertTextEquals("Как вас зовут?")
            }

            step("Проверить отображение поля ввода имени") {
                composeTestRule
                    .onNodeWithTag(Tags.NAME_EDIT_TEXT)
                    .assertIsDisplayed()
            }

            step("Проверить отображение кнопки 'Войти'") {
                composeTestRule
                    .onNodeWithText("Войти")
                    .assertIsDisplayed()
            }
        }
    }
}