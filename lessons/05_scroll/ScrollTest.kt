package com.example.kaspresso_learning

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import org.junit.Rule
import org.junit.Test

/**
 * Урок 6: Скролл и навигация в списках.
 *
 * Предусловие: залогиниться (можно вынести в private fun login()).
 *
 * Задание:
 * 1. Нажать FAB (Tags.FEED_FAB) → NewBrewingScreen
 * 2. Нажать «Выбрать чай» (Tags.BREWING_SELECT_TEA) → TeaSelectScreen
 * 3. Проверить 4 типа чая: tea_type_0…tea_type_3
 * 4. Нажать Пуэр (tea_type_3) → подсписок пуэров
 * 5. Скроллить к tea_item_20:
 *    composeTestRule.onNodeWithTag(Tags.TEA_SELECT_CONTAINER)
 *        .performScrollToNode(hasTestTag("${Tags.TEA_ITEM}_20"))
 * 6. Нажать tea_item_20
 * 7. Проверить Tags.BREWING_SELECTED_TEA_NAME содержит «Мэнхай»
 */
class ScrollTest : TestCase() {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    // TODO: Создайте private fun login() для переиспользования

    @Test
    fun scrollToTeaAndSelect() = run {
        step("Логинимся") {
            TODO("Вызвать login()")
        }

        step("Нажимаем FAB → Новая запись") {
            TODO("Нажать на FEED_FAB")
        }

        step("Нажимаем «Выбрать чай»") {
            TODO("Нажать на BREWING_SELECT_TEA")
        }

        step("Проверяем 4 типа чая") {
            TODO("В цикле проверить tea_type_0..tea_type_3")
        }

        step("Выбираем Пуэр") {
            TODO("Нажать на tea_type_3")
        }

        step("Скроллим к Мэнхай (id 20)") {
            TODO("performScrollToNode(hasTestTag(\"...\")) на TEA_SELECT_CONTAINER")
        }

        step("Нажимаем Мэнхай") {
            TODO("Нажать на tea_item_20")
        }

        step("Проверяем выбранный чай") {
            TODO("Проверить BREWING_SELECTED_TEA_NAME содержит «Мэнхай»")
        }
    }
}
