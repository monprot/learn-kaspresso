package com.example.kaspresso_learning.tests

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import com.example.kaspresso_learning.Tags
import com.example.kaspresso_learning.BaseTestCase
import com.example.kaspresso_learning.steps.AvatarSelectSteps
import org.junit.Test

class NameEditScreenTest : BaseTestCase() {

    @Test
    fun checkNameEditScreen() = run {
        step("Нажать на второй аватар") {
            AvatarSelectSteps.selectAvatar(1)
        }

        step("Нажать 'Далее'") {
            AvatarSelectSteps.clickNextBtn()
        }

        step("Проверить, что открылся экран ввода имени") {

            step("Проверить заголовок «Как вас зовут?»") {
                composeTestRule
                    .onNodeWithTag(Tags.NAME_INPUT_TITLE)
                    .assertIsDisplayed()
                    .assertTextEquals("Как вас зовут?")
            }

            step("Проверить отображение поля ввода имени") {
                composeTestRule
                    .onNodeWithTag(Tags.NAME_INPUT_TEXT)
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