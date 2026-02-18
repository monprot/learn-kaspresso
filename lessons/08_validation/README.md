# Урок 8. Негативные тесты и валидация

## Цель

Научиться писать **негативные тесты** — проверки, что приложение корректно обрабатывает ошибки пользователя: пустые поля, недозаполненные формы, невалидные состояния.

---

## Теория

### Позитивные vs Негативные тесты

| Тип | Что проверяет | Пример |
|:---|:---|:---|
| Позитивный | Правильный сценарий | Заполнить все поля → сохранить → OK |
| Негативный | Ошибки и edge cases | Нажать «Далее» без выбора → ошибка |

### Почему негативные тесты важны?

- Пользователи **будут** нажимать кнопки без заполнения полей
- Приложение не должно крашиться — оно должно показывать понятную ошибку
- Регрессия чаще всего ломает именно обработку ошибок

### Проверка текста ошибки

```kotlin
// Проверить, что текст ошибки отображается
composeTestRule
    .onNodeWithTag(Tags.AVATAR_ERROR)
    .assertIsDisplayed()

// Проверить содержимое текста
composeTestRule
    .onNodeWithText("Имя не может быть пустым")
    .assertIsDisplayed()
```

### assertDoesNotExist

Когда нужно проверить, что элемент **не появился**:

```kotlin
// Ошибка НЕ должна быть видна
composeTestRule
    .onNodeWithTag(Tags.AVATAR_ERROR)
    .assertDoesNotExist()
```

---

## Задание

Напишите `ValidationTest.kt` с тремя сценариями:

### Сценарий 1: AvatarSelectScreen

1. Нажать «Далее» без выбора аватара
2. Проверить: ошибка `avatar_screen_error_text` отображается
3. Выбрать аватар → нажать «Далее»
4. Проверить: переход на `NameInputScreen` (ошибка исчезла)

### Сценарий 2: NameInputScreen

1. *Предусловие*: выбрать аватар → перейти на `NameInputScreen`
2. Нажать «Войти» с пустым полем
3. Проверить: текст «Имя не может быть пустым» отображается
4. Ввести имя → нажать «Войти»
5. Проверить: переход в ленту

### Сценарий 3: NewBrewingScreen

1. *Предусловие*: залогиниться → нажать FAB
2. Нажать «Сохранить» без заполнения полей
3. Проверить: текст «Выберите чай и укажите вес» отображается
4. Выбрать чай, **не** вводить вес → «Сохранить»
5. Проверить: ошибка всё ещё отображается
6. Ввести вес → «Сохранить»
7. Проверить: запись создана, вернулись в ленту

---

## Подсказки

<details>
<summary>Подсказка 1: Проверка текста ошибки на NameInputScreen</summary>

У ошибки нет `testTag`, поэтому ищите по тексту:

```kotlin
composeTestRule
    .onNodeWithText("Имя не может быть пустым")
    .assertIsDisplayed()
```

</details>

<details>
<summary>Подсказка 2: Негативный тест NewBrewingScreen</summary>

Сначала нажать «Сохранить» без данных:

```kotlin
composeTestRule.onNodeWithTag(Tags.BREWING_SAVE).performClick()
composeTestRule.onNodeWithText("Выберите чай и укажите вес").assertIsDisplayed()
```

Затем выбрать чай, но не вводить вес — снова нажать сохранить:

```kotlin
// Выбрать чай
composeTestRule.onNodeWithTag(Tags.BREWING_SELECT_TEA).performClick()
composeTestRule.onNodeWithTag("${Tags.TEA_TYPE}_0").performClick()
composeTestRule.onNodeWithTag("${Tags.TEA_ITEM}_1").performClick()

// Попробовать сохранить без веса
composeTestRule.onNodeWithTag(Tags.BREWING_SAVE).performClick()
composeTestRule.onNodeWithText("Выберите чай и укажите вес").assertIsDisplayed()
```

</details>

---

## Эталонное решение

<details>
<summary>Полный код ValidationTest.kt</summary>

```kotlin
package com.example.kaspresso_learning

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import org.junit.Rule
import org.junit.Test

class ValidationTest : TestCase() {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private fun login(name: String = "Тестер") {
        composeTestRule.onNodeWithTag("${Tags.AVATAR_ICON}_0").performClick()
        composeTestRule.onNodeWithTag(Tags.AVATAR_NEXT_BUTTON).performClick()
        composeTestRule.onNodeWithTag(Tags.NAME_INPUT_TEXT).performTextInput(name)
        composeTestRule.onNodeWithTag(Tags.NAME_INPUT_LOGIN_BUTTON).performClick()
    }

    @Test
    fun avatarScreen_showsErrorWithoutSelection() = run {
        step("Нажимаем «Далее» без выбора") {
            composeTestRule
                .onNodeWithTag(Tags.AVATAR_NEXT_BUTTON)
                .performClick()
        }

        step("Проверяем ошибку") {
            composeTestRule
                .onNodeWithTag(Tags.AVATAR_ERROR)
                .assertIsDisplayed()
        }

        step("Выбираем аватар и нажимаем «Далее»") {
            composeTestRule
                .onNodeWithTag("${Tags.AVATAR_ICON}_0")
                .performClick()
            composeTestRule
                .onNodeWithTag(Tags.AVATAR_NEXT_BUTTON)
                .performClick()
        }

        step("Проверяем переход на NameInputScreen") {
            composeTestRule
                .onNodeWithTag(Tags.NAME_CONTAINER)
                .assertIsDisplayed()
        }
    }

    @Test
    fun nameScreen_showsErrorForEmptyName() = run {
        step("Выбираем аватар и переходим далее") {
            composeTestRule.onNodeWithTag("${Tags.AVATAR_ICON}_0").performClick()
            composeTestRule.onNodeWithTag(Tags.AVATAR_NEXT_BUTTON).performClick()
        }

        step("Нажимаем «Войти» с пустым полем") {
            composeTestRule
                .onNodeWithTag(Tags.NAME_INPUT_LOGIN_BUTTON)
                .performClick()
        }

        step("Проверяем ошибку") {
            composeTestRule
                .onNodeWithText("Имя не может быть пустым")
                .assertIsDisplayed()
        }

        step("Вводим имя и входим") {
            composeTestRule
                .onNodeWithTag(Tags.NAME_INPUT_TEXT)
                .performTextInput("Тестер")
            composeTestRule
                .onNodeWithTag(Tags.NAME_INPUT_LOGIN_BUTTON)
                .performClick()
        }

        step("Проверяем переход в ленту") {
            composeTestRule
                .onNodeWithTag(Tags.FEED_CONTAINER)
                .assertIsDisplayed()
        }
    }

    @Test
    fun brewingScreen_requiresTeaAndWeight() = run {
        step("Логинимся") { login() }

        step("Открываем новую запись") {
            composeTestRule.onNodeWithTag(Tags.FEED_FAB).performClick()
        }

        step("Нажимаем «Сохранить» без данных") {
            composeTestRule.onNodeWithTag(Tags.BREWING_SAVE).performClick()
        }

        step("Проверяем ошибку") {
            composeTestRule
                .onNodeWithText("Выберите чай и укажите вес")
                .assertIsDisplayed()
        }

        step("Выбираем чай без веса и сохраняем") {
            composeTestRule.onNodeWithTag(Tags.BREWING_SELECT_TEA).performClick()
            composeTestRule.onNodeWithTag("${Tags.TEA_TYPE}_0").performClick()
            composeTestRule.onNodeWithTag("${Tags.TEA_ITEM}_1").performClick()
            composeTestRule.onNodeWithTag(Tags.BREWING_SAVE).performClick()
        }

        step("Ошибка всё ещё отображается") {
            composeTestRule
                .onNodeWithText("Выберите чай и укажите вес")
                .assertIsDisplayed()
        }

        step("Вводим вес и сохраняем") {
            composeTestRule
                .onNodeWithTag(Tags.BREWING_WEIGHT)
                .performTextInput("5")
            composeTestRule
                .onNodeWithTag(Tags.BREWING_SAVE)
                .performClick()
        }

        step("Проверяем возврат в ленту") {
            composeTestRule
                .onNodeWithTag(Tags.FEED_CONTAINER)
                .assertIsDisplayed()
        }
    }
}
```

</details>
