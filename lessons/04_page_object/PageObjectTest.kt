package com.example.kaspresso_learning

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.kaspersky.kaspresso.kaspresso.Kaspresso
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import org.junit.Rule
import org.junit.Test

/**
 * Урок 4: Page Object (ComposeScreen).
 *
 * ⚠️ ПЕРЕД НАПИСАНИЕМ ТЕСТА:
 * Создайте папку screen/ в androidTest и добавьте 3 ComposeScreen-а:
 * - AvatarSelectScreen — nextButton, error, fun avatar(index)
 * - NameInputScreen — nameField, loginButton
 * - FeedScreen — title, fab, fun post(index)
 *
 * Задание:
 * Перепишите тест логина, используя onComposeScreen<...> вместо прямых onNodeWithTag.
 */
class PageObjectTest : TestCase(
    kaspressoBuilder = Kaspresso.Builder.withComposeSupport()
) {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun loginWithPageObjects() = run {
        step("Выбрать аватар и перейти далее") {
            // TODO: onComposeScreen<AvatarSelectScreen>(composeTestRule) {
            //     avatar(2) { assertIsDisplayed(); performClick() }
            //     nextButton { assertIsDisplayed(); performClick() }
            // }
            TODO()
        }

        step("Ввести имя и войти") {
            // TODO: onComposeScreen<NameInputScreen>(composeTestRule) {
            //     nameField { assertIsDisplayed(); performTextInput("Kaspresso User") }
            //     loginButton { assertIsDisplayed(); performClick() }
            // }
            TODO()
        }

        step("Проверить ленту") {
            // TODO: onComposeScreen<FeedScreen>(composeTestRule) {
            //     title { assertIsDisplayed(); assertTextContains("Kaspresso User") }
            //     post(0) { assertIsDisplayed() }
            // }
            TODO()
        }
    }
}
