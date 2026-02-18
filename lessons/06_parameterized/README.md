# Урок 6. Параметризированные тесты

## Цель

Научиться запускать **один и тот же тест с разными данными** — параметризация. Вместо дублирования теста для каждого набора данных, пишем один тест и подставляем параметры.

---

## Теория

### Зачем параметризация?

Допустим, нужно проверить создание записи с разными чаями, весами, посудой. Можно написать 3 отдельных теста, а можно — **один параметризированный**:

```kotlin
// ❌ Дублирование
@Test fun createBrewing_sencha() = ...
@Test fun createBrewing_assam() = ...
@Test fun createBrewing_daHongPao() = ...

// ✅ Параметризация
data class BrewingParams(...)
val testData = listOf(params1, params2, params3)
```

### Подход: Data-Driven Test

В Kaspresso/JUnit4 простейший способ — определить набор данных и вызвать тест для каждого:

```kotlin
data class BrewingParams(
    val teaTypeIndex: Int,      // ordinal типа чая (0–3)
    val teaItemId: Int,         // id сорта
    val teaName: String,        // ожидаемое имя для проверки
    val weight: String,         // вес (граммы)
    val vesselTag: String?,     // тег посуды (null = не выбирать)
    val volumeMl: String?       // объём (null = не заполнять)
)
```

### JUnit4 Parameterized Runner

Классический способ параметризации в JUnit4:

```kotlin
@RunWith(Parameterized::class)
class ParameterizedBrewingTest(private val params: BrewingParams) : TestCase() {

    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "{0}")
        fun data() = listOf(
            BrewingParams(
                teaTypeIndex = 0, teaItemId = 1, teaName = "Сенча",
                weight = "5", vesselTag = Tags.BREWING_VESSEL_GAIWAN, volumeMl = "150"
            ),
            BrewingParams(
                teaTypeIndex = 1, teaItemId = 7, teaName = "Ассам",
                weight = "10", vesselTag = Tags.BREWING_VESSEL_TEAPOT, volumeMl = null
            ),
            BrewingParams(
                teaTypeIndex = 2, teaItemId = 12, teaName = "Да Хун Пао",
                weight = "8", vesselTag = null, volumeMl = "200"
            )
        )
    }
}
```

### Работа с необязательными полями

Некоторые поля не обязательны. В тесте нужно проверять, нужно ли заполнять поле:

```kotlin
// Посуда (если указана)
params.vesselTag?.let { tag ->
    composeTestRule.onNodeWithTag(tag).performClick()
}

// Объём (если указан)
params.volumeMl?.let { volume ->
    composeTestRule.onNodeWithTag(Tags.BREWING_VOLUME).performTextInput(volume)
}
```

---

## Тестовые данные

| Параметр | Набор 1 | Набор 2 | Набор 3 |
|:---|:---|:---|:---|
| Тип чая | Зелёный (0) | Чёрный (1) | Улун (2) |
| Сорт | Сенча (id 1) | Ассам (id 7) | Да Хун Пао (id 12) |
| Вес (г) | 5 | 10 | 8 |
| Посуда | Гайвань | Чайник | _(не выбирать)_ |
| Объём (мл) | 150 | _(не заполнять)_ | 200 |

---

## Задание

Создайте `ParameterizedBrewingTest.kt`:

1. Определите `data class BrewingParams` с полями из таблицы
2. Используйте `@RunWith(Parameterized::class)` и `companion object`
3. Напишите один тест, который для каждого набора:
   - Логинится
   - Нажимает FAB
   - Выбирает чай (тип → сорт)
   - Вводит вес
   - _(если указано)_ Выбирает посуду
   - _(если указано)_ Вводит объём
   - Нажимает «Сохранить»
   - Проверяет, что вернулись в ленту и видна запись

---

## Подсказки

<details>
<summary>Подсказка 1: Структура Parameterized-теста</summary>

```kotlin
@RunWith(Parameterized::class)
class ParameterizedBrewingTest(
    private val params: BrewingParams
) : TestCase() {

    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "{0}")
        fun data() = listOf(/* наборы данных */)
    }

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun createBrewingEntry() = run { ... }
}
```

</details>

<details>
<summary>Подсказка 2: Выбор чая (тип → сорт)</summary>

```kotlin
step("Выбираем чай") {
    composeTestRule.onNodeWithTag(Tags.BREWING_SELECT_TEA).performClick()
    composeTestRule.onNodeWithTag("${Tags.TEA_TYPE}_${params.teaTypeIndex}").performClick()
    composeTestRule.onNodeWithTag("${Tags.TEA_ITEM}_${params.teaItemId}").performClick()
}
```

Если сорт может быть за пределами экрана — добавьте `performScrollToNode`.

</details>

---

## Эталонное решение

<details>
<summary>Полный код ParameterizedBrewingTest.kt</summary>

```kotlin
package com.example.kaspresso_learning

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

data class BrewingParams(
    val teaTypeIndex: Int,
    val teaItemId: Int,
    val teaName: String,
    val weight: String,
    val vesselTag: String?,
    val volumeMl: String?
) {
    override fun toString() = "$teaName (weight=$weight)"
}

@RunWith(Parameterized::class)
class ParameterizedBrewingTest(
    private val params: BrewingParams
) : TestCase() {

    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "{0}")
        fun data() = listOf(
            BrewingParams(
                teaTypeIndex = 0, teaItemId = 1, teaName = "Сенча",
                weight = "5", vesselTag = Tags.BREWING_VESSEL_GAIWAN, volumeMl = "150"
            ),
            BrewingParams(
                teaTypeIndex = 1, teaItemId = 7, teaName = "Ассам",
                weight = "10", vesselTag = Tags.BREWING_VESSEL_TEAPOT, volumeMl = null
            ),
            BrewingParams(
                teaTypeIndex = 2, teaItemId = 12, teaName = "Да Хун Пао",
                weight = "8", vesselTag = null, volumeMl = "200"
            )
        )
    }

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private fun login() {
        composeTestRule.onNodeWithTag("${Tags.AVATAR_ICON}_0").performClick()
        composeTestRule.onNodeWithTag(Tags.AVATAR_NEXT_BUTTON).performClick()
        composeTestRule.onNodeWithTag(Tags.NAME_INPUT_TEXT).performTextInput("Тестер")
        composeTestRule.onNodeWithTag(Tags.NAME_INPUT_LOGIN_BUTTON).performClick()
    }

    @Test
    fun createBrewingEntry() = run {
        step("Логинимся") {
            login()
        }

        step("Нажимаем FAB") {
            composeTestRule
                .onNodeWithTag(Tags.FEED_FAB)
                .performClick()
        }

        step("Выбираем чай: ${params.teaName}") {
            composeTestRule.onNodeWithTag(Tags.BREWING_SELECT_TEA).performClick()
            composeTestRule
                .onNodeWithTag("${Tags.TEA_TYPE}_${params.teaTypeIndex}")
                .performClick()
            composeTestRule
                .onNodeWithTag("${Tags.TEA_ITEM}_${params.teaItemId}")
                .performClick()
        }

        step("Вводим вес: ${params.weight} г") {
            composeTestRule
                .onNodeWithTag(Tags.BREWING_WEIGHT)
                .performTextInput(params.weight)
        }

        step("Выбираем посуду (если указана)") {
            params.vesselTag?.let { tag ->
                composeTestRule.onNodeWithTag(tag).performClick()
            }
        }

        step("Вводим объём (если указан)") {
            params.volumeMl?.let { volume ->
                composeTestRule
                    .onNodeWithTag(Tags.BREWING_VOLUME)
                    .performTextInput(volume)
            }
        }

        step("Сохраняем") {
            composeTestRule
                .onNodeWithTag(Tags.BREWING_SAVE)
                .performClick()
        }

        step("Проверяем запись в ленте") {
            composeTestRule
                .onNodeWithTag("${Tags.FEED_POST}_0")
                .assertIsDisplayed()
        }
    }
}
```

</details>
