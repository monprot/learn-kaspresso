# Урок 1. Разбираем, что происходит в тесте

## Цель

Разобраться в содержании тестового класса, а так же втрёх ключевых понятиях Compose Testing API — **Finder**, **Assertion**, **Action** — и научиться читать тесты.

---
## Теория

### Структура теста в Kaspresso

> [!TIP]
> `TestCase()` — базовый класс Kaspresso. Благодаря ему доступны `run {}` и `step {}`.
> Шаги группируют тест логически и создают понятный лог.

Kaspresso добавляет к стандартным тестам **шаги** (`step`):

```kotlin
class MyTest : TestCase() {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun myTest() = run {
        step("Описание шага") {
            // действия и проверки
        }
        step("Ещё шаг") {
            // ...
        }
    }
}
```

`step {}` - Необязательный элемент, но позволяет сделать отчеты более читаемыми.

### `composeTestRule` — точка входа в Compose-тесты

```kotlin
@get:Rule
val composeTestRule = createAndroidComposeRule<MainActivity>()
```

| Часть | Что делает |
|:---|:---|
| `@get:Rule` | Аннотация JUnit. Говорит: «перед каждым тестом выполни подготовку, после — очистку». Правило запускает Activity и создаёт тестовое окружение Compose |
| `createAndroidComposeRule<MainActivity>()` | Создаёт правило, которое автоматически запускает `MainActivity` перед тестом. Без этого у теста не будет UI, с которым можно взаимодействовать |
| `composeTestRule` | Переменная, через которую вызываются **все** Finder'ы, Assertion'ы и Action'ы: `composeTestRule.onNodeWithTag(...)` |

---

## Compose Testing

Compose Testing API строится вокруг **семантического дерева** — специальной структуры, в которой каждый UI-элемент описан набором свойств (текст, тег, роль, состояние и т. д.). Тест **не** работает с пикселями — он обращается к узлам этого дерева.

Любой шаг теста состоит из трёх подшагов:

```
Найти элемент → Проверить его / Выполнить действие
     ▲                ▲                  ▲
   Finder          Assertion           Action
```

### 1. Finder (Поиск)

Finder — это метод, который находит один или несколько узлов семантического дерева по заданному условию.

```kotlin
// Найти ОДИН узел
composeTestRule.onNode(<SemanticsMatcher>)

// Найти НЕСКОЛЬКО узлов
composeTestRule.onAllNodes(<SemanticsMatcher>)
```

Для частых случаев есть удобные обёртки:

| Метод | Что ищет |
|:---|:---|
| `onNodeWithTag("tag")` | Элемент с указанным `testTag` |
| `onNodeWithText("Привет")` | Элемент с указанным текстом |
| `onNodeWithContentDescription("Назад")` | Элемент с указанным `contentDescription` |
| `onAllNodesWithTag("tag")` | **Все** элементы с указанным `testTag` |
| `onAllNodesWithText("Привет")` | **Все** элементы с указанным текстом |

> [!NOTE]
> `onNodeWith...` возвращает `SemanticsNodeInteraction` — объект, к которому можно применять assertion или action.
> `onAllNodesWith...` возвращает `SemanticsNodeInteractionCollection` — коллекцию таких объектов.

#### Пример

```kotlin
// Ищем кнопку по testTag
composeTestRule.onNodeWithTag("avatar_screen_next_button")

// То же самое через низкоуровневый API
composeTestRule.onNode(hasTestTag("avatar_screen_next_button"))

// Ищем элемент по тексту
composeTestRule.onNodeWithText("Далее")

// То же самое через низкоуровневый API
composeTestRule.onNode(hasText("Далее"))
```

---

### 2. Assertion (Проверка)

Assertion — это проверка состояния найденного элемента. Вызывается **цепочкой** после finder'а.

| Метод | Что проверяет |
|:---|:---|
| `.assertIsDisplayed()` | Элемент виден на экране |
| `.assertExists()` | Элемент есть в дереве (даже если за пределами экрана) |
| `.assertDoesNotExist()` | Элемента нет в дереве |
| `.assertTextEquals("текст")` | Текст целиком равен указанному |
| `.assertTextContains("часть")` | Текст содержит подстроку |
| `.assertIsEnabled()` | Элемент доступен для взаимодействия |
| `.assertIsSelected()` | Элемент выбран |
| `.assertHasClickAction()` | Элемент поддерживает клик |

Полный список можно найти в [шпаргалке по тестированию Compose](https://developer.android.com/develop/ui/compose/testing/testing-cheatsheet?hl=ru).

#### Пример

```kotlin
// Элемент отображается:
composeTestRule
    .onNodeWithTag("avatar_screen_container")
    .assertIsDisplayed()

// Можно комбинировать условия (и / или):
composeTestRule
    .onNode(matcher)
    .assert(hasText("Кнопка") or hasText("Button"))
```

#### Проверки коллекций

Когда finder возвращает несколько узлов, доступны специальные assertion'ы:

```kotlin
// Проверить количество
composeTestRule
    .onAllNodesWithContentDescription("Аватар")
    .assertCountEquals(4)

// Хотя бы один совпадает
composeTestRule
    .onAllNodesWithContentDescription("Аватар")
    .assertAny(hasTestTag("avatar_icon_0"))

// Все совпадают
composeTestRule
    .onAllNodesWithContentDescription("Аватар")
    .assertAll(hasClickAction())
```

---

### 3. Action (Действие)

Action — это имитация пользовательского действия над найденным элементом.

| Метод | Что делает |
|:---|:---|
| `.performClick()` | Нажатие (тап) |
| `.performTextInput("текст")` | Ввод текста |
| `.performTextClearance()` | Очистка текстового поля |
| `.performScrollTo()` | Прокрутка до элемента |
| `.performTouchInput { swipeLeft() }` | Жест (свайп, перетаскивание и т. д.) |

Полный список можно найти в [шпаргалке по тестированию Compose](https://developer.android.com/develop/ui/compose/testing/testing-cheatsheet?hl=ru).

#### Пример

```kotlin
// Нажимаем на кнопку
composeTestRule
    .onNodeWithTag("avatar_screen_next_button")
    .performClick()

// Вводим текст
composeTestRule
    .onNodeWithTag("name_input_screen_edit_text")
    .performTextInput("Иван")
```

---

### Собираем всё вместе: цепочка вызовов

Каждый тестовый сценарий — это **цепочка**: `Finder → Assertion / Action`.

```kotlin
composeTestRule                                 // правило (точка входа)
    .onNodeWithTag("avatar_screen_container")   // Finder: находим элемент
    .assertIsDisplayed()                        // Assertion: проверяем, что он видим
```

```kotlin
composeTestRule
    .onNodeWithTag("avatar_screen_next_button") // Finder
    .performClick()                             // Action
```

Можно читать как обычные предложения:

- *«Найди элемент с тегом `avatar_screen_container` и убедись, что он отображается»*
- *«Найди элемент с тегом `avatar_screen_next_button` и нажми на него»*

---


## Разбор теста `AvatarSelectScreenTest`

Откройте файл `app/src/androidTest/.../AvatarSelectScreenTest.kt` и прочитайте его, используя новые знания:

```kotlin
class AvatarSelectScreenTest : TestCase() { // TestCase() — базовый класс Kaspresso.

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>() // точка входа в Compose-тесты

    @Test
    fun avatarScreen_displaysFourAvatars() = run {
        // STEP 1 — Finder + Assertion
        step("Проверить, что экран выбора аватара отображается") {
            composeTestRule
                .onNodeWithTag(Tags.AVATAR_CONTAINER)   // Finder: ищем контейнер
                .assertIsDisplayed()                    // Assertion: проверяем видимость
        }

        // STEP 2 — Finder + Assertion (в цикле)
        step("Проверить, что отображаются 4 иконки аватаров") {
            for (i in 0..3) {
                composeTestRule
                    .onNodeWithTag("${Tags.AVATAR_ICON}_$i")  // Finder
                    .assertIsDisplayed()                      // Assertion
            }
        }

        // STEP 3 — Finder + Assertion
        step("Проверить, что кнопка «Далее» отображается") {
            composeTestRule
                .onNodeWithTag(Tags.AVATAR_NEXT_BUTTON)  // Finder
                .assertIsDisplayed()                     // Assertion    
        }
    }
}
```

Каждый `step` — это одна-две цепочки `Finder → Assertion / Action`. Тест читается как сценарий:

1. **Найди** контейнер экрана → **убедись**, что он видим
2. **Найди** каждый аватар по тегу → **убедись**, что он виден
3. **Найди** кнопку «Далее» → **убедись**, что она видна

---

> [!TIP]
> Перед выполнением Action и большинства Assertion, рекомендуется всегда выполнять `.assertIsDisplayed()`, чтобы убедиться, что элемент виден. Это позволит избежать ошибок и сделать тесты более надежными.

---

## Полезные ссылки

- [Compose Testing APIs — полное описание Finder / Assertion / Action](https://developer.android.com/develop/ui/compose/testing/apis?hl=ru)
- [Шпаргалка по тестированию ](https://developer.android.com/develop/ui/compose/testing/testing-cheatsheet?hl=ru)

---