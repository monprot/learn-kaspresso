package com.example.kaspresso_learning

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

/**
 * Урок 7: Параметризированные тесты.
 *
 * Задание:
 * 1. Определите data class BrewingParams (teaTypeIndex, teaItemId, teaName, weight, vesselTag?, volumeMl?)
 * 2. Заполните companion object с 3 наборами данных (см. README)
 * 3. Напишите один тест, который:
 *    - Логинится
 *    - Нажимает FAB
 *    - Выбирает чай (тип → сорт по params)
 *    - Вводит вес
 *    - (если указано) Выбирает посуду
 *    - (если указано) Вводит объём
 *    - Нажимает «Сохранить»
 *    - Проверяет запись в ленте
 */

// TODO: Определите data class BrewingParams

@RunWith(Parameterized::class)
class ParameterizedBrewingTest(
    // TODO: private val params: BrewingParams
) : TestCase() {

    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "{0}")
        fun data() = listOf(
            TODO("Набор 1: Зелёный → Сенча, 5 г, Гайвань, 150 мл"),
            TODO("Набор 2: Чёрный → Ассам, 10 г, Чайник, без объёма"),
            TODO("Набор 3: Улун → Да Хун Пао, 8 г, без посуды, 200 мл")
        )
    }

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun createBrewingEntry() = run {
        step("Логинимся") {
            TODO("Вызвать login()")
        }

        step("Нажимаем FAB") {
            TODO("Нажать на FEED_FAB")
        }

        step("Выбираем чай") {
            TODO("BREWING_SELECT_TEA → tea_type_{params.teaTypeIndex} → tea_item_{params.teaItemId}")
        }

        step("Вводим вес") {
            TODO("performTextInput(params.weight) на BREWING_WEIGHT")
        }

        step("Выбираем посуду (если указана)") {
            TODO("params.vesselTag?.let { ... }")
        }

        step("Вводим объём (если указан)") {
            TODO("params.volumeMl?.let { ... }")
        }

        step("Сохраняем") {
            TODO("Нажать BREWING_SAVE")
        }

        step("Проверяем запись в ленте") {
            TODO("Проверить feed_post_0 отображается")
        }
    }
}
