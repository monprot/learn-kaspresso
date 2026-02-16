package com.example.kaspresso_learning

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import org.junit.Rule
import org.junit.Test

/**
 * Урок 9: Негативные тесты и валидация.
 *
 * Три сценария:
 *
 * Сценарий 1 — AvatarSelectScreen:
 *   «Далее» без выбора → ошибка → выбрать → «Далее» → NameInputScreen
 *
 * Сценарий 2 — NameInputScreen:
 *   «Войти» с пустым → ошибка → ввести имя → «Войти» → FeedScreen
 *
 * Сценарий 3 — NewBrewingScreen:
 *   «Сохранить» без данных → ошибка → выбрать чай без веса → ошибка → ввести вес → сохранить → OK
 */
class ValidationTest : TestCase() {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    // TODO: private fun login(name: String = "Тестер")

    @Test
    fun avatarScreen_showsErrorWithoutSelection() = run {
        step("Нажимаем «Далее» без выбора") {
            TODO("Нажать AVATAR_NEXT_BUTTON")
        }

        step("Проверяем ошибку") {
            TODO("AVATAR_ERROR отображается")
        }

        step("Выбираем аватар и нажимаем «Далее»") {
            TODO("avatar_icon_0 → performClick(), AVATAR_NEXT_BUTTON → performClick()")
        }

        step("Проверяем переход") {
            TODO("NAME_CONTAINER отображается")
        }
    }

    @Test
    fun nameScreen_showsErrorForEmptyName() = run {
        step("Переходим на NameInputScreen") {
            TODO("Выбрать аватар → «Далее»")
        }

        step("Нажимаем «Войти» с пустым полем") {
            TODO("NAME_LOGIN_BUTTON → performClick()")
        }

        step("Проверяем ошибку") {
            TODO("onNodeWithText(\"Имя не может быть пустым\") → assertIsDisplayed()")
        }

        step("Вводим имя и входим") {
            TODO("performTextInput → performClick")
        }

        step("Проверяем переход в ленту") {
            TODO("FEED_CONTAINER отображается")
        }
    }

    @Test
    fun brewingScreen_requiresTeaAndWeight() = run {
        step("Логинимся и открываем новую запись") {
            TODO("login() → FEED_FAB.performClick()")
        }

        step("Сохраняем без данных") {
            TODO("BREWING_SAVE → performClick()")
        }

        step("Проверяем ошибку") {
            TODO("onNodeWithText(\"Выберите чай и укажите вес\") → assertIsDisplayed()")
        }

        step("Выбираем чай без веса → сохраняем") {
            TODO("Выбрать чай → BREWING_SAVE")
        }

        step("Ошибка всё ещё отображается") {
            TODO("Проверить текст ошибки")
        }

        step("Вводим вес и сохраняем") {
            TODO("BREWING_WEIGHT → performTextInput → BREWING_SAVE → performClick")
        }

        step("Проверяем возврат в ленту") {
            TODO("FEED_CONTAINER отображается")
        }
    }
}
