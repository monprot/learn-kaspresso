# Урок 4. Page Object (ComposeScreen)

## Цель

Познакомиться с паттерном **Page Object** и применить его через `ComposeScreen` из Kaspresso. Выделить экраны в отдельные объекты, чтобы тесты стали читаемыми, а изменения в UI не ломали все тесты сразу.

---

## Зачем нужен Page Object

### Проблема: дублирование и хрупкость

Посмотрите на тесты, которые мы написали в предыдущих уроках:

```kotlin
// Тест 1: проверка ошибки
composeTestRule.onNodeWithTag(Tags.AVATAR_NEXT_BUTTON).assertIsDisplayed().performClick()
composeTestRule.onNodeWithTag(Tags.AVATAR_ERROR).assertIsDisplayed()

// Тест 2: переход на экран ввода имени
composeTestRule.onNodeWithTag("${Tags.AVATAR_ICON}_2").assertIsDisplayed().performClick()
composeTestRule.onNodeWithTag(Tags.AVATAR_NEXT_BUTTON).assertIsDisplayed().performClick()

// Тест 3: полный логин
composeTestRule.onNodeWithTag("${Tags.AVATAR_ICON}_1").assertIsDisplayed().performClick()
composeTestRule.onNodeWithTag(Tags.AVATAR_NEXT_BUTTON).assertIsDisplayed().performClick()
composeTestRule.onNodeWithTag(Tags.NAME_EDIT_TEXT).assertIsDisplayed().performTextInput("Тестер")
```

Обратите внимание:
- Код `onNodeWithTag(Tags.AVATAR_NEXT_BUTTON)` **дублируется** во всех трёх тестах
- Если тег кнопки «Далее» изменится, придётся исправлять **каждый** тест
- Чем больше тестов — тем больше мест для исправлений и тем выше вероятность ошибки

### Решение: Page Object

**Page Object** — это паттерн проектирования тестов, при котором каждый экран (или крупная область UI) описывается как **отдельный объект**. Этот объект содержит:

- **Элементы** экрана (кнопки, поля, тексты) — описанные через матчеры
- Иногда — **действия** на экране (например, «заполнить форму и отправить»)

Тесты работают не с «сырыми» тегами и локаторами, а с понятными свойствами объекта-экрана.

> [!IMPORTANT]
> **Ключевой принцип Page Object:** тег или локатор элемента указывается **ровно в одном месте** — внутри Page Object. Все тесты обращаются к элементу через его свойство. Если локатор изменится — достаточно поправить одну строку.

---

## Принципы паттерна

### Откуда пришёл Page Object

Паттерн Page Object был впервые описан в контексте веб-тестирования (Selenium WebDriver) Мартином Фаулером. Идея оказалась настолько универсальной, что её адаптировали для всех платформ — включая мобильную автоматизацию с Kaspresso.

### Основные правила

1. **Один экран = один Page Object.** Каждый экран приложения описывается отдельным классом.

2. **Инкапсуляция локаторов.** Теги, матчеры и другие локаторы **скрыты** внутри Page Object. Тест не знает, как именно находится элемент — он просто обращается к свойству.

3. **Методы возвращают элементы, а не результаты.** Page Object предоставляет доступ к элементам, а проверки (assertions) остаются в тесте.

4. **Не содержит assertions.** Page Object описывает **что есть** на экране, а не **что должно быть**. Проверки — ответственность теста.

### Что это даёт

| Без Page Object | С Page Object |
|---|---|
| Тег `"avatar_screen_next_button"` в 15 тестах | Тег в **одном** месте — `AvatarSelectScreen.nextButton` |
| Изменение тега → правки в 15 файлах | Изменение тега → правка в **одном** файле |
| Тест: `onNodeWithTag("avatar_screen_next_button")` — что это? | Тест: `nextButton { assertIsDisplayed() }` — сразу понятно |
| Новый тестировщик: «что за строка?» | Новый тестировщик: «а, кнопка на экране аватара» |

> [!TIP]
> **Правило большого пальца:** если тег встречается в 2+ тестах — выносите в Page Object. Чем раньше вы начнёте — тем меньше рефакторинга будет потом.

---

## ComposeScreen и KNode в Kaspresso

### Библиотека Kakao Compose

Kaspresso использует библиотеку [Kakao Compose](https://github.com/KakaoCup/Compose) для описания Page Object-ов. Её ключевые компоненты:

- **`ComposeScreen`** — базовый класс для Page Object (аналог «экрана»)
- **`KNode`** — описание элемента внутри экрана (кнопка, поле, текст и т.д.)
- **`onComposeScreen`** — DSL-функция для работы с экраном внутри теста

### Создание ComposeScreen

Каждый экран описывается как класс, наследующий `ComposeScreen`:

```kotlin
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode
import androidx.compose.ui.semantics.SemanticsNodeInteractionsProvider

class AvatarSelectScreen(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<AvatarSelectScreen>(
        semanticsProvider = semanticsProvider
    ) {

    val nextButton: KNode = child {
        hasTestTag(Tags.AVATAR_NEXT_BUTTON)
    }

    val error: KNode = child {
        hasTestTag(Tags.AVATAR_ERROR)
    }
}
```

Разберём по частям:

- `semanticsProvider` — передаётся автоматически, когда вы используете `onComposeScreen`
- `ComposeScreen<AvatarSelectScreen>` — базовый класс с типовым параметром (самореференция)
- `child { ... }` — объявление элемента как **дочернего узла** экрана
- `hasTestTag(...)` — матчер для поиска узла по `testTag`

### KNode — описание элемента

`KNode` — это «виртуальный элемент» на экране. Он не ищет узел при создании, а хранит **набор матчеров** и выполняет поиск только при обращении (assertion или action).

Элемент можно описать несколькими способами:

```kotlin
// Способ 1: child (элемент — дочерний узел экрана)
val nextButton: KNode = child {
    hasTestTag(Tags.AVATAR_NEXT_BUTTON)
}

// Способ 2: KNode с явным указанием родителя
val nextButton = KNode(this) {
    hasTestTag(Tags.AVATAR_NEXT_BUTTON)
}
```

Можно комбинировать матчеры:

```kotlin
val loginButton: KNode = child {
    hasTestTag(Tags.NAME_LOGIN_BUTTON)
    hasText("Войти")
}
```

### viewBuilderAction — привязка экрана к контейнеру

Если экран имеет свой контейнер с тегом, можно указать его через `viewBuilderAction`. Тогда все `child`-элементы будут искаться **только внутри** этого контейнера:

```kotlin
class AvatarSelectScreen(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<AvatarSelectScreen>(
        semanticsProvider = semanticsProvider,
        viewBuilderAction = { hasTestTag(Tags.AVATAR_CONTAINER) }
    ) {
    // nextButton ищется только внутри avatar_screen_container
    val nextButton: KNode = child {
        hasTestTag(Tags.AVATAR_NEXT_BUTTON)
    }
}
```

> [!TIP]
> `viewBuilderAction` — необязательный параметр. Если экран — это абстракция без конкретного контейнера, можно его не указывать.

### Использование в тесте: onComposeScreen

В тесте для работы с экраном используется DSL-функция `onComposeScreen`:

```kotlin
@Test
fun loginTest() = run {
    step("Выбрать аватар и нажать Далее") {
        onComposeScreen<AvatarSelectScreen>(composeTestRule) {
            nextButton {
                assertIsDisplayed()
                performClick()
            }
        }
    }
}
```

Внутри блока `onComposeScreen` вы обращаетесь к элементам экрана напрямую — `nextButton`, `error` и т.д. У каждого элемента можно вызывать assertions и actions в фигурных скобках.

### До и после

**До** (без Page Object):

```kotlin
step("Нажать Далее") {
    composeTestRule
        .onNodeWithTag(Tags.AVATAR_NEXT_BUTTON)
        .assertIsDisplayed()
        .performClick()
}
```

**После** (с ComposeScreen):

```kotlin
step("Нажать Далее") {
    onComposeScreen<AvatarSelectScreen>(composeTestRule) {
        nextButton {
            assertIsDisplayed()
            performClick()
        }
    }
}
```

---

## Почему assertIsDisplayed() перед действием

Из документации Kaspresso:

> Элемент может существовать в семантическом дереве, но **физически не быть видимым** на экране. В этом случае `performClick()` сработает «в пустоту» — тест упадёт позже с непонятной ошибкой.
> 
> Вызов `assertIsDisplayed()` перед действием запускает **Kaspresso Interceptors**, включая `AutoScrollSemanticsBehaviorInterceptor`, который автоматически прокрутит экран до элемента.

> [!IMPORTANT]
> **Всегда вызывайте `assertIsDisplayed()` перед `performClick()` и другими действиями.** Это не только проверка, но и триггер для автопрокрутки и других интерцепторов Kaspresso.

---

## Kaspresso Builder

Для полной поддержки Compose-интерцепторов используйте `Kaspresso.Builder.withComposeSupport()`:

```kotlin
class PageObjectTest : TestCase(
    kaspressoBuilder = Kaspresso.Builder.withComposeSupport()
) {
    // ...
}
```

Это включает все стандартные интерцепторы:
- **FlakySafeSemanticsBehaviorInterceptor** — автоматически повторяет неудачные действия
- **AutoScrollSemanticsBehaviorInterceptor** — прокручивает к невидимым элементам
- **FailureLoggingSemanticsBehaviorInterceptor** — понятные сообщения об ошибках
- **SystemDialogSafetySemanticsBehaviorInterceptor** — закрывает системные диалоги

---

## Структура проекта

```
app/src/androidTest/java/com/example/kaspresso_learning/
├── screen/                          ← Page Object-ы (ComposeScreen)
│   ├── AvatarSelectScreen.kt
│   ├── NameInputScreen.kt
│   └── FeedScreen.kt
├── AvatarSelectScreenTest.kt        ← тесты урока 2
├── LoginFlowTest.kt                 ← тест урока 3
└── PageObjectTest.kt                ← тест этого урока
```

> [!TIP]
> Хорошая практика — держать Page Object-ы в отдельной папке `screen/`, чтобы они не смешивались с тестами.

---

## Задание: Рефакторинг тестов с ComposeScreen

### Подготовка — создайте ComposeScreen-ы

Создайте папку `screen/` в директории тестов и добавьте 3 файла:

1. **`AvatarSelectScreen.kt`**
   - Элементы: `nextButton`, `error`
   - Для аватаров подумайте, как передать индекс

2. **`NameInputScreen.kt`**
   - Элементы: `nameField`, `loginButton`

3. **`FeedScreen.kt`**
   - Элементы: `title`, `fab`
   - Для постов подумайте, как передать индекс

### Тест — перепишите логин с ComposeScreen

Откройте `PageObjectTest.kt` и перепишите тест, используя `onComposeScreen`:

**Шаги:**
1. Через `AvatarSelectScreen` — выбрать аватар и нажать «Далее»
2. Через `NameInputScreen` — ввести имя и нажать «Войти»
3. Через `FeedScreen` — проверить заголовок и наличие постов

**Ожидаемый результат:**
- Тест проходит, в коде **нет** прямых вызовов `onNodeWithTag` — все элементы описаны в ComposeScreen-ах

---

## Подсказки

<details>
<summary>Подсказка 1: Структура ComposeScreen</summary>

```kotlin
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode
import androidx.compose.ui.semantics.SemanticsNodeInteractionsProvider

class AvatarSelectScreen(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<AvatarSelectScreen>(
        semanticsProvider = semanticsProvider
    ) {

    val nextButton: KNode = child {
        hasTestTag(Tags.AVATAR_NEXT_BUTTON)
    }

    val error: KNode = child {
        hasTestTag(Tags.AVATAR_ERROR)
    }
}
```

</details>

<details>
<summary>Подсказка 2: Элемент с параметром (аватар по индексу)</summary>

Для элементов, зависящих от параметра, используйте **функцию** вместо свойства:

```kotlin
fun avatar(index: Int): KNode = child {
    hasTestTag("${Tags.AVATAR_ICON}_$index")
}
```

В тесте:

```kotlin
onComposeScreen<AvatarSelectScreen>(composeTestRule) {
    avatar(2) {
        assertIsDisplayed()
        performClick()
    }
}
```

</details>

<details>
<summary>Подсказка 3: onComposeScreen в тесте</summary>

```kotlin
step("Выбрать аватар и перейти далее") {
    onComposeScreen<AvatarSelectScreen>(composeTestRule) {
        avatar(2) {
            assertIsDisplayed()
            performClick()
        }
        nextButton {
            assertIsDisplayed()
            performClick()
        }
    }
}
```

Несколько элементов одного экрана можно проверять в одном блоке `onComposeScreen`.

</details>

<details>
<summary>Подсказка 4: Ввод текста через KNode</summary>

```kotlin
onComposeScreen<NameInputScreen>(composeTestRule) {
    nameField {
        assertIsDisplayed()
        performTextInput("Kaspresso User")
    }
}
```

</details>

---

## Эталонное решение

<details>
<summary>AvatarSelectScreen.kt</summary>

```kotlin
package com.example.kaspresso_learning.screen

import androidx.compose.ui.semantics.SemanticsNodeInteractionsProvider
import com.example.kaspresso_learning.Tags
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode

class AvatarSelectScreen(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<AvatarSelectScreen>(
        semanticsProvider = semanticsProvider
    ) {

    val nextButton: KNode = child {
        hasTestTag(Tags.AVATAR_NEXT_BUTTON)
    }

    val error: KNode = child {
        hasTestTag(Tags.AVATAR_ERROR)
    }

    fun avatar(index: Int): KNode = child {
        hasTestTag("${Tags.AVATAR_ICON}_$index")
    }
}
```

</details>

<details>
<summary>NameInputScreen.kt</summary>

```kotlin
package com.example.kaspresso_learning.screen

import androidx.compose.ui.semantics.SemanticsNodeInteractionsProvider
import com.example.kaspresso_learning.Tags
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode

class NameInputScreen(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<NameInputScreen>(
        semanticsProvider = semanticsProvider
    ) {

    val nameField: KNode = child {
        hasTestTag(Tags.NAME_EDIT_TEXT)
    }

    val loginButton: KNode = child {
        hasTestTag(Tags.NAME_LOGIN_BUTTON)
    }
}
```

</details>

<details>
<summary>FeedScreen.kt</summary>

```kotlin
package com.example.kaspresso_learning.screen

import androidx.compose.ui.semantics.SemanticsNodeInteractionsProvider
import com.example.kaspresso_learning.Tags
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode

class FeedScreen(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<FeedScreen>(
        semanticsProvider = semanticsProvider
    ) {

    val title: KNode = child {
        hasTestTag(Tags.FEED_TITLE)
    }

    val fab: KNode = child {
        hasTestTag(Tags.FEED_FAB)
    }

    fun post(index: Int): KNode = child {
        hasTestTag("${Tags.FEED_POST}_$index")
    }
}
```

</details>

<details>
<summary>PageObjectTest.kt</summary>

```kotlin
package com.example.kaspresso_learning

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.example.kaspresso_learning.screen.AvatarSelectScreen
import com.example.kaspresso_learning.screen.FeedScreen
import com.example.kaspresso_learning.screen.NameInputScreen
import com.kaspersky.kaspresso.kaspresso.Kaspresso
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import org.junit.Rule
import org.junit.Test

class PageObjectTest : TestCase(
    kaspressoBuilder = Kaspresso.Builder.withComposeSupport()
) {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun loginWithPageObjects() = run {
        step("Выбрать аватар и перейти далее") {
            onComposeScreen<AvatarSelectScreen>(composeTestRule) {
                avatar(2) {
                    assertIsDisplayed()
                    performClick()
                }
                nextButton {
                    assertIsDisplayed()
                    performClick()
                }
            }
        }

        step("Ввести имя и войти") {
            onComposeScreen<NameInputScreen>(composeTestRule) {
                nameField {
                    assertIsDisplayed()
                    performTextInput("Kaspresso User")
                }
                loginButton {
                    assertIsDisplayed()
                    performClick()
                }
            }
        }

        step("Проверить ленту") {
            onComposeScreen<FeedScreen>(composeTestRule) {
                title {
                    assertIsDisplayed()
                    assertTextContains("Kaspresso User")
                }
                post(0) {
                    assertIsDisplayed()
                }
            }
        }
    }
}
```

</details>

---

## 📚 Полезные ссылки

- [Page Object — Martin Fowler](https://martinfowler.com/bliki/PageObject.html) — оригинальная статья о паттерне
- [Page Object Models — Selenium Documentation](https://www.selenium.dev/documentation/test_practices/encouraged/page_object_models/) — описание паттерна в контексте Selenium
- [Поддержка Compose в Kaspresso — документация](https://kasperskylab.github.io/Kaspresso/ru/Wiki/Jetpack_Compose/) — ComposeScreen, интерцепторы, предостережения
- [Kakao Compose — GitHub](https://github.com/KakaoCup/Compose) — библиотека ComposeScreen / KNode, примеры использования
- [Kaspresso Wiki — GitHub](https://kasperskylab.github.io/Kaspresso/ru/) — полная документация Kaspresso
- [Тестирование Compose — документация Android](https://developer.android.com/develop/ui/compose/testing?hl=ru)
