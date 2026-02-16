package com.example.kaspresso_learning

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import org.junit.Rule
import org.junit.Test

/**
 * Урок 8: Навигация и проверка данных.
 *
 * Тест A — Профиль:
 * 1. Залогиниться как «Профиль»
 * 2. Перейти в профиль → проверить имя, счётчик (0), пустое состояние
 * 3. Создать запись → вернуться в профиль
 * 4. Проверить счётчик (1) и запись
 *
 * Тест B — Выход:
 * 1. Залогиниться → профиль → «Выйти»
 * 2. Проверить AvatarScreen
 * 3. Проверить back не возвращает в ленту
 * 4. Залогиниться заново с другим именем
 */
class NavigationTest : TestCase() {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    // TODO: private fun login(name: String)

    @Test
    fun profileScreen_showsDataCorrectly() = run {
        step("Логинимся") {
            TODO("Залогиниться как «Профиль»")
        }

        step("Переходим в профиль") {
            TODO("Нажать на вкладку «Профиль» через onNodeWithText")
        }

        step("Проверяем имя") {
            TODO("PROFILE_NAME содержит «Профиль»")
        }

        step("Проверяем счётчик (0 записей)") {
            TODO("PROFILE_COUNT содержит «0»")
        }

        step("Проверяем пустое состояние") {
            TODO("PROFILE_EMPTY отображается")
        }

        step("Создаём запись") {
            TODO("Вернуться в ленту → FAB → выбрать чай → ввести вес → сохранить")
        }

        step("Возвращаемся в профиль") {
            TODO("Вкладка «Профиль»")
        }

        step("Проверяем счётчик (1 запись)") {
            TODO("PROFILE_COUNT содержит «1»")
        }

        step("Проверяем запись в истории") {
            TODO("profile_history_item_0 отображается")
        }
    }

    @Test
    fun logout_andReLogin() = run {
        step("Логинимся") {
            TODO("Залогиниться как «Первый»")
        }

        step("Переходим в профиль и выходим") {
            TODO("Вкладка «Профиль» → PROFILE_LOGOUT")
        }

        step("Проверяем экран аватара") {
            TODO("AVATAR_CONTAINER отображается")
        }

        step("Проверяем back") {
            TODO("Системный back → AVATAR_CONTAINER всё ещё отображается")
        }

        step("Логинимся заново") {
            TODO("Залогиниться как «Второй»")
        }

        step("Проверяем новое имя") {
            TODO("FEED_TITLE содержит «Второй»")
        }
    }
}
