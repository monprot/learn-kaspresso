# Урок 7. Навигация и проверка данных

## Цель

Научиться тестировать **BottomNavigation**, переходы между вкладками, проверку данных на разных экранах и выход из аккаунта.

---

## Теория

### BottomNavigation

Приложение TeaLog имеет нижнюю панель навигации с двумя вкладками:
- **Лента** (`FeedScreen`)
- **Профиль** (`ProfileScreen`)

В тестах переключение вкладок — это нажатие на текст в NavigationBar:

```kotlin
// Переключиться на «Профиль»
composeTestRule.onNodeWithText("Профиль").performClick()

// Переключиться обратно на «Ленту»
composeTestRule.onNodeWithText("Лента").performClick()
```

### Проверка данных на ProfileScreen

После логина на `ProfileScreen` отображаются:
- Имя пользователя (`profile_name`)
- Количество записей (`profile_count`)
- Список записей или пустое состояние (`profile_empty_text`)

```kotlin
composeTestRule
    .onNodeWithTag(Tags.PROFILE_NAME)
    .assertTextEquals("Тестер")

composeTestRule
    .onNodeWithTag(Tags.PROFILE_COUNT)
    .assertTextContains("0")
```

### Нажатие системной кнопки «Назад»

```kotlin
// Эмулируем системный back:
composeTestRule.activityRule.scenario.onActivity {
    it.onBackPressedDispatcher.onBackPressed()
}
```

> [!IMPORTANT]
> После выхода (logout) нажатие «назад» **не должно** возвращать в ленту — стек навигации очищается.

---

## Задание

### Тест A: Профиль и навигация

1. Залогиниться как «Профиль»
2. Нажать вкладку «Профиль» в BottomNav
3. Проверить имя: `profile_name` = «Профиль»
4. Проверить счётчик: `profile_count` содержит «0»
5. Проверить пустое состояние: `profile_empty_text` отображается
6. Вернуться в ленту → создать запись → вернуться в профиль
7. Проверить: `profile_count` содержит «1»
8. Проверить: `profile_history_item_0` отображается

### Тест B: Выход и повторный вход

1. Залогиниться
2. Перейти в профиль
3. Нажать «Выйти» (`profile_logout`)
4. Проверить, что открылся `AvatarSelectScreen` (`avatar_screen_container`)
5. Проверить, что кнопка «Назад» не возвращает в ленту (экран выбора аватара остаётся)
6. Залогиниться с новым именем
7. Проверить, что в ленте отображается новое имя

---

## Подсказки

<details>
<summary>Подсказка 1: Переключение вкладок</summary>

```kotlin
step("Перейти в профиль") {
    composeTestRule.onNodeWithText("Профиль").performClick()
}
```

</details>

<details>
<summary>Подсказка 2: Создание записи и возврат</summary>

```kotlin
step("Создать запись") {
    composeTestRule.onNodeWithText("Лента").performClick()
    composeTestRule.onNodeWithTag(Tags.FEED_FAB).performClick()
    composeTestRule.onNodeWithTag(Tags.BREWING_SELECT_TEA).performClick()
    composeTestRule.onNodeWithTag("${Tags.TEA_TYPE}_0").performClick()
    composeTestRule.onNodeWithTag("${Tags.TEA_ITEM}_1").performClick()
    composeTestRule.onNodeWithTag(Tags.BREWING_WEIGHT).performTextInput("5")
    composeTestRule.onNodeWithTag(Tags.BREWING_SAVE).performClick()
}
step("Перейти в профиль") {
    composeTestRule.onNodeWithText("Профиль").performClick()
}
```

</details>

<details>
<summary>Подсказка 3: Проверка back после logout</summary>

```kotlin
step("Проверяем, что back не возвращает в ленту") {
    composeTestRule.activityRule.scenario.onActivity {
        it.onBackPressedDispatcher.onBackPressed()
    }
    // Экран выбора аватара должен остаться
    composeTestRule
        .onNodeWithTag(Tags.AVATAR_CONTAINER)
        .assertIsDisplayed()
}
```

</details>

---

## Эталонное решение

<details>
<summary>NavigationTest.kt — Тест A: Профиль и навигация</summary>

```kotlin
package com.example.kaspresso_learning

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import org.junit.Rule
import org.junit.Test

class NavigationTest : TestCase() {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private fun login(name: String = "Профиль") {
        composeTestRule.onNodeWithTag("${Tags.AVATAR_ICON}_0").performClick()
        composeTestRule.onNodeWithTag(Tags.AVATAR_NEXT_BUTTON).performClick()
        composeTestRule.onNodeWithTag(Tags.NAME_INPUT_TEXT).performTextInput(name)
        composeTestRule.onNodeWithTag(Tags.NAME_INPUT_LOGIN_BUTTON).performClick()
    }

    @Test
    fun profileScreen_showsDataCorrectly() = run {
        step("Логинимся") { login("Профиль") }

        step("Переходим в профиль") {
            composeTestRule.onNodeWithText("Профиль").performClick()
        }

        step("Проверяем имя") {
            composeTestRule
                .onNodeWithTag(Tags.PROFILE_NAME)
                .assertTextEquals("Профиль")
        }

        step("Проверяем счётчик (0 записей)") {
            composeTestRule
                .onNodeWithTag(Tags.PROFILE_COUNT)
                .assertTextContains("0")
        }

        step("Проверяем пустое состояние") {
            composeTestRule
                .onNodeWithTag(Tags.PROFILE_EMPTY)
                .assertIsDisplayed()
        }

        step("Возвращаемся в ленту и создаём запись") {
            composeTestRule.onNodeWithText("Лента").performClick()
            composeTestRule.onNodeWithTag(Tags.FEED_FAB).performClick()
            composeTestRule.onNodeWithTag(Tags.BREWING_SELECT_TEA).performClick()
            composeTestRule.onNodeWithTag("${Tags.TEA_TYPE}_0").performClick()
            composeTestRule.onNodeWithTag("${Tags.TEA_ITEM}_1").performClick()
            composeTestRule.onNodeWithTag(Tags.BREWING_WEIGHT).performTextInput("5")
            composeTestRule.onNodeWithTag(Tags.BREWING_SAVE).performClick()
        }

        step("Возвращаемся в профиль") {
            composeTestRule.onNodeWithText("Профиль").performClick()
        }

        step("Проверяем счётчик (1 запись)") {
            composeTestRule
                .onNodeWithTag(Tags.PROFILE_COUNT)
                .assertTextContains("1")
        }

        step("Проверяем запись в истории") {
            composeTestRule
                .onNodeWithTag("${Tags.PROFILE_HISTORY_ITEM}_0")
                .assertIsDisplayed()
        }
    }
}
```

</details>

<details>
<summary>LogoutTest.kt — Тест B: Выход и повторный вход</summary>

```kotlin
package com.example.kaspresso_learning

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import org.junit.Rule
import org.junit.Test

class LogoutTest : TestCase() {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private fun login(name: String) {
        composeTestRule.onNodeWithTag("${Tags.AVATAR_ICON}_0").performClick()
        composeTestRule.onNodeWithTag(Tags.AVATAR_NEXT_BUTTON).performClick()
        composeTestRule.onNodeWithTag(Tags.NAME_INPUT_TEXT).performTextInput(name)
        composeTestRule.onNodeWithTag(Tags.NAME_INPUT_LOGIN_BUTTON).performClick()
    }

    @Test
    fun logout_andReLogin() = run {
        step("Логинимся") { login("Первый") }

        step("Переходим в профиль") {
            composeTestRule.onNodeWithText("Профиль").performClick()
        }

        step("Нажимаем «Выйти»") {
            composeTestRule
                .onNodeWithTag(Tags.PROFILE_LOGOUT)
                .performClick()
        }

        step("Проверяем, что открылся экран аватара") {
            composeTestRule
                .onNodeWithTag(Tags.AVATAR_CONTAINER)
                .assertIsDisplayed()
        }

        step("Проверяем, что back не возвращает в ленту") {
            composeTestRule.activityRule.scenario.onActivity {
                it.onBackPressedDispatcher.onBackPressed()
            }
            composeTestRule
                .onNodeWithTag(Tags.AVATAR_CONTAINER)
                .assertIsDisplayed()
        }

        step("Логинимся с новым именем") {
            login("Второй")
        }

        step("Проверяем новое имя в ленте") {
            composeTestRule
                .onNodeWithTag(Tags.FEED_TITLE)
                .assertTextContains("Второй")
        }
    }
}
```

</details>
