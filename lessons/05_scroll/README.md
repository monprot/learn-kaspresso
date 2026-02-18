# Урок 5. Скролл и навигация в списках

## Цель

Научиться работать со **скроллом** в Compose-тестах: находить элементы за пределами экрана, прокручивать к ним и взаимодействовать.

---

## Теория

### Проблема

`LazyColumn` (и `LazyRow`) отрисовывает только видимые элементы. Если элемент не на экране:

```kotlin
// ❌ Упадёт, если элемент ещё не отрисован
composeTestRule.onNodeWithTag("tea_item_20").assertIsDisplayed()
```

### performScrollToNode

Чтобы прокрутить к нужному элементу внутри `LazyColumn`:

```kotlin
// Прокрутить скроллируемый контейнер до элемента с нужным тегом
composeTestRule
    .onNodeWithTag("tea_select_container")
    .performScrollToNode(hasTestTag("tea_item_20"))
```

После скролла элемент становится видимым и с ним можно взаимодействовать:

```kotlin
composeTestRule
    .onNodeWithTag("tea_item_20")
    .assertIsDisplayed()
    .performClick()
```

### hasTestTag

`hasTestTag(...)` — это **matcher** (условие поиска), а не прямой поиск элемента. Используется внутри `performScrollToNode`:

```kotlin
import androidx.compose.ui.test.hasTestTag

// Скроллить, пока не найдём элемент с тегом
composeTestRule
    .onNodeWithTag("контейнер")
    .performScrollToNode(hasTestTag("целевой_элемент"))
```

### Двухуровневый список

`TeaSelectScreen` содержит **два уровня**:

1. **Типы чая** — `tea_type_0`…`tea_type_3` (Зелёный, Чёрный, Улун, Пуэр)
2. **Сорта чая** — `tea_item_1`…`tea_item_20` (после выбора типа)

Чтобы добраться до конкретного сорта:
1. Нажать на тип чая
2. Прокрутить список сортов к нужному

---

## Карта тегов для этого урока

| Экран | Элемент | testTag |
|:---|:---|:---|
| Feed | FAB | `feed_screen_fab` |
| Brewing | Кнопка «Выбрать чай» | `new_brewing_select_tea` |
| Brewing | Название чая | `new_brewing_selected_tea_name` |
| Tea Select | Контейнер | `tea_select_container` |
| Tea Select | Тип чая (ordinal) | `tea_type_0`…`tea_type_3` |
| Tea Select | Сорт чая (id) | `tea_item_1`…`tea_item_20` |

---

## Задание

Напишите тест, который проходит путь: **Лента → Новая запись → Выбрать чай → Пуэр → Мэнхай**

### Предусловие
Залогиньтесь (выбрать аватар → ввести имя → «Войти»).

### Шаги теста

1. Нажать FAB (`feed_screen_fab`) → открывается `NewBrewingScreen`
2. Нажать «Выбрать чай» (`new_brewing_select_tea`) → открывается `TeaSelectScreen`
3. Проверить 4 типа чая: `tea_type_0`…`tea_type_3`
4. Нажать «Пуэр» (`tea_type_3`) → открывается подсписок пуэров
5. **Скроллить** к элементу `tea_item_20` (Мэнхай) через `performScrollToNode`
6. Нажать на «Мэнхай»
7. Проверить, что `new_brewing_selected_tea_name` содержит «Мэнхай»

---

## Подсказки

<details>
<summary>Подсказка 1: Функция-помощник для логина</summary>

Логин повторяется в каждом тесте. Вынесите в отдельную функцию:

```kotlin
private fun login(name: String = "Тестер") {
    composeTestRule.onNodeWithTag("${Tags.AVATAR_ICON}_0").performClick()
    composeTestRule.onNodeWithTag(Tags.AVATAR_NEXT_BUTTON).performClick()
    composeTestRule.onNodeWithTag(Tags.NAME_INPUT_TEXT).performTextInput(name)
    composeTestRule.onNodeWithTag(Tags.NAME_INPUT_LOGIN_BUTTON).performClick()
}
```

</details>

<details>
<summary>Подсказка 2: Скролл в TeaSelectScreen</summary>

У `TeaSelectScreen` нет тега-контейнера для списка сортов — он общий (`tea_select_container`). Но при выборе типа чая список сортов заменяет список типов, и контейнер тот же.

Для прокрутки к сорту чая используйте:

```kotlin
composeTestRule
    .onNodeWithTag(Tags.TEA_SELECT_CONTAINER)
    .performScrollToNode(hasTestTag("${Tags.TEA_ITEM}_20"))
```

</details>

---

## Эталонное решение

<details>
<summary>Полный код ScrollTest.kt</summary>

```kotlin
package com.example.kaspresso_learning

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.test.performTextInput
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import org.junit.Rule
import org.junit.Test

class ScrollTest : TestCase() {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private fun login(name: String = "Тестер") {
        composeTestRule.onNodeWithTag("${Tags.AVATAR_ICON}_0").performClick()
        composeTestRule.onNodeWithTag(Tags.AVATAR_NEXT_BUTTON).performClick()
        composeTestRule.onNodeWithTag(Tags.NAME_INPUT_TEXT).performTextInput(name)
        composeTestRule.onNodeWithTag(Tags.NAME_INPUT_LOGIN_BUTTON).performClick()
    }

    @Test
    fun scrollToTeaAndSelect() = run {
        step("Логинимся") {
            login()
        }

        step("Нажимаем FAB → Новая запись") {
            composeTestRule
                .onNodeWithTag(Tags.FEED_FAB)
                .performClick()
        }

        step("Нажимаем «Выбрать чай»") {
            composeTestRule
                .onNodeWithTag(Tags.BREWING_SELECT_TEA)
                .performClick()
        }

        step("Проверяем 4 типа чая") {
            for (i in 0..3) {
                composeTestRule
                    .onNodeWithTag("${Tags.TEA_TYPE}_$i")
                    .assertIsDisplayed()
            }
        }

        step("Выбираем Пуэр") {
            composeTestRule
                .onNodeWithTag("${Tags.TEA_TYPE}_3")
                .performClick()
        }

        step("Скроллим к Мэнхай (id 20)") {
            composeTestRule
                .onNodeWithTag(Tags.TEA_SELECT_CONTAINER)
                .performScrollToNode(hasTestTag("${Tags.TEA_ITEM}_20"))
        }

        step("Нажимаем Мэнхай") {
            composeTestRule
                .onNodeWithTag("${Tags.TEA_ITEM}_20")
                .performClick()
        }

        step("Проверяем выбранный чай") {
            composeTestRule
                .onNodeWithTag(Tags.BREWING_SELECTED_TEA_NAME)
                .assertTextContains("Мэнхай")
        }
    }
}
```

</details>
