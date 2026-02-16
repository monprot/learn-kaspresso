package com.example.kaspresso_learning

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import org.junit.Rule
import org.junit.Test

/**
 * Урок 4: Добавление тега к элементу + полный логин.
 *
 * ⚠️ ПЕРЕД НАПИСАНИЕМ ТЕСТА:
 * 1. Добавьте в Tags.kt:  const val NAME_LOGIN_BUTTON = "name_input_login_button"
 * 2. Добавьте в NameInputScreen.kt тег кнопке «Войти»:
 *    .semantics { testTag = Tags.NAME_LOGIN_BUTTON }
 *
 * Задание:
 * 1. Выбрать аватар → нажать «Далее»
 * 2. Ввести «Тестер» через performTextInput
 * 3. Нажать «Войти» по новому тегу NAME_LOGIN_BUTTON
 * 4. Проверить: feed_screen_title содержит «Тестер»
 * 5. Проверить: feed_post_0 отображается
 */
class LoginFlowTest : TestCase() {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun fullLogin_fromAvatarToFeed() = run {
        step("Выбираем аватар") {
            TODO("Нажать на аватар с индексом 1")
        }

        step("Нажимаем «Далее»") {
            TODO("Нажать кнопку «Далее»")
        }

        step("Вводим имя «Тестер»") {
            TODO("Ввести текст в поле NAME_EDIT_TEXT через performTextInput(\"Тестер\")")
        }

        step("Нажимаем «Войти»") {
            TODO("Нажать кнопку «Войти» по тегу NAME_LOGIN_BUTTON (добавленному вами!)")
        }

        step("Проверяем приветствие в ленте") {
            TODO("Проверить, что FEED_TITLE содержит текст «Тестер»")
        }

        step("Проверяем наличие постов") {
            TODO("Проверить, что feed_post_0 отображается")
        }
    }
}
