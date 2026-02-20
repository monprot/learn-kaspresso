# Урок 6. Скроллы и работа с LazyList

## Цель

Научиться работать с **прокручиваемыми списками** в Compose-тестах: понять разницу между `LazyColumn` и `verticalScroll`, освоить ключевые методы скроллинга (`performScrollToIndex`, `performScrollToNode`, `performScrollToKey`), научиться тестировать элементы, которые **не видны** на экране без прокрутки.

---

## Часть 1. Почему скроллинг — особый случай в тестировании

### Проблема невидимых элементов

В предыдущих уроках мы работали с элементами, которые **сразу видны** на экране. Но в реальных приложениях контент часто не помещается на одном экране — его нужно прокрутить.

В нашем приложении лента содержит **7 карточек** чаёв. На экране эмулятора видны только **3–4** из них. Остальные находятся **за пределами видимой области**.

Попробуем обратиться к последней карточке без скроллинга:

```kotlin
// ❌ Элемент feed_post_6 НЕ видим — тест упадёт!
composeTestRule
    .onNodeWithTag("${Tags.FEED_POST}_6")
    .assertIsDisplayed()
```

**Ошибка:**
```
Assert failed: The component with TestTag = 'feed_post_6' is not displayed!
```

Элемент **существует** в семантическом дереве (Compose знает о нём), но **физически не отрисован** на экране, потому что LazyColumn создаёт и рисует только те элементы, которые попадают в видимую область.

> [!IMPORTANT]
> **LazyColumn** и **LazyRow** создают элементы **лениво**: узлы, которые не видны пользователю, могут **даже не существовать** в семантическом дереве. Это принципиальное отличие от обычного `Column` с `verticalScroll`, где все элементы создаются сразу, просто скрыты за краем экрана.

### Два вида прокручиваемых контейнеров

В Compose существует два подхода к scrollable-контенту:

| | `Column` + `verticalScroll` | `LazyColumn` / `LazyRow` |
|:---|:---|:---|
| **Создание элементов** | Все сразу | Только видимые + буфер |
| **Семантическое дерево** | Все узлы присутствуют | Только отрисованные узлы |
| **Когда использовать** | Небольшие списки (<20 элементов) | Большие динамические списки |
| **Пример в проекте** | `NewBrewingScreen` (форма) | `FeedScreen` (лента), `TeaSelectScreen` (каталог) |
| **Семантическое действие** | `ScrollBy` | `ScrollToIndex`, `ScrollToKey`, `ScrollBy` |

В нашем приложении:
- **`FeedScreen`** → `LazyColumn` с 7 карточками заваривания
- **`TeaSelectScreen`** → `LazyColumn` со списком сортов чая (до 30+ элементов)
- **`ProfileScreen`** → `LazyColumn` с историей записей пользователя
- **`NewBrewingScreen`** → `Column` с `verticalScroll` (форма заваривания)

---

## Часть 2. Скроллинг в Compose-тестах

### performScrollToIndex — прокрутка к элементу по индексу

`performScrollToIndex(index)` прокручивает `LazyColumn`/`LazyRow` так, чтобы элемент с указанным **индексом** стал видимым.

```kotlin
// Прокручиваем LazyColumn ленты до 7-й карточки (индекс 6)
composeTestRule
    .onNodeWithTag(Tags.FEED_LIST)
    .performScrollToIndex(6)
```

> [!IMPORTANT]
> `performScrollToIndex` вызывается **на самом `LazyColumn`**, а не на родительском контейнере и не на элементе внутри списка. Если `testTag` стоит на обычном `Column` — вы получите ошибку: `the node is missing [ScrollToIndex]`.

**Важные детали:**
- Индексация начинается с **0**
- После вызова элемент с указанным индексом **гарантированно существует** в семантическом дереве и видим
- Можно далее обращаться к элементу по его тегу

```kotlin
// Полный пример: проскроллить к карточке и проверить автора
composeTestRule
    .onNodeWithTag(Tags.FEED_LIST)
    .performScrollToIndex(6)

composeTestRule
    .onNode(
        hasTestTag(Tags.FEED_POST_AUTHOR_NAME) and hasText("Иван")
    )
    .assertIsDisplayed()
```

### performScrollToNode — прокрутка к элементу по матчеру

`performScrollToNode(matcher)` прокручивает список до тех пор, пока не найдёт узел, соответствующий **семантическому матчеру**. Это более гибкий способ — не нужно знать индекс, достаточно описать **что** найти.

```kotlin
// Прокрутить до карточки с автором «Иван»
composeTestRule
    .onNodeWithTag(Tags.FEED_LIST)
    .performScrollToNode(
        hasTestTag(Tags.FEED_POST_AUTHOR_NAME) and hasText("Иван")
    )
```

Преимущества `performScrollToNode`:
- **Не нужно знать индекс** — полезно, когда порядок может меняться
- **Декларативный подход** — описываем *что* ищем, а не *где* оно находится
- Ищет узел **среди потомков** списка

```kotlin
// Можно искать по testTag конкретной карточки
composeTestRule
    .onNodeWithTag(Tags.FEED_LIST)
    .performScrollToNode(hasTestTag("${Tags.FEED_POST}_5"))
```

> [!TIP]
> `performScrollToNode` полезен, когда вы знаете **содержимое** элемента, но не его позицию. Например, в каталоге чаёв можно проскроллить к конкретному сорту по тексту.

### performScrollToKey — прокрутка по ключу элемента

`performScrollToKey(key)` прокручивает `LazyColumn` к элементу, у которого указан конкретный **ключ** (key). Ключ задаётся в коде приложения при описании элементов `LazyColumn`.

```kotlin
// В коде приложения (если ключи заданы):
LazyColumn {
    itemsIndexed(entries, key = { _, entry -> entry.id }) { index, entry ->
        BrewingCard(entry)
    }
}

// В тесте:
composeTestRule
    .onNodeWithTag(Tags.FEED_LIST)
    .performScrollToKey(entryId)
```

> [!NOTE]
> `performScrollToKey` требует, чтобы разработчик **явно указал** параметр `key` при объявлении элементов `LazyColumn`. Если ключи не заданы — метод не сработает. В нашем проекте ключи явно **не используются**, поэтому мы будем пользоваться `performScrollToIndex` и `performScrollToNode`.

### Сравнение способов скроллинга

| Метод | Когда использовать | Требования |
|:---|:---|:---|
| `performScrollToIndex(i)` | Знаем точную позицию элемента | Элемент от `LazyColumn` |
| `performScrollToNode(m)` | Знаем свойства элемента (тег, текст) | Элемент от `LazyColumn` |
| `performScrollToKey(k)` | Знаем уникальный ключ в данных | `key` задан в `items()` |
| `performScrollTo()` | Обычный `Column` + `verticalScroll` | Элемент от обычного Column |

---

## Часть 3. Скроллинг обычного Column (verticalScroll)

### performScrollTo — для не-ленивых контейнеров

Если контент находится внутри обычного `Column` с модификатором `verticalScroll` (как на экране `NewBrewingScreen`), то все элементы **уже существуют** в семантическом дереве. Но они могут быть скрыты за краем экрана.

Для прокрутки к таким элементам используется `performScrollTo()` — он вызывается **на самом элементе**, а не на контейнере:

```kotlin
// Прокрутить ДО кнопки «Сохранить» на экране NewBrewingScreen
composeTestRule
    .onNodeWithTag(Tags.BREWING_SAVE)
    .performScrollTo()
    .assertIsDisplayed()
    .performClick()
```

**Разница с методами LazyColumn:**

```kotlin
// LazyColumn — скроллим СПИСОК:
composeTestRule
    .onNodeWithTag(Tags.FEED_LIST)           // ← сам LazyColumn
    .performScrollToIndex(6)                 // ← скроллим список

// Column + verticalScroll — скроллим К ЭЛЕМЕНТУ:
composeTestRule
    .onNodeWithTag(Tags.BREWING_SAVE)        // ← сам элемент
    .performScrollTo()                       // ← скроллим к нему
```

> [!IMPORTANT]
> **`performScrollTo()`** работает только с элементами внутри `scrollable`-контейнеров (`Column` + `verticalScroll`, `ScrollView`). Для `LazyColumn` он **не работает** — используйте `performScrollToIndex` или `performScrollToNode`.

---

## Часть 4. Kaspresso и автоскроллинг

### Interceptors для автопрокрутки

Kaspresso имеет встроенный механизм **автопрокрутки** через Compose-интерцепторы. Когда вы используете `Kaspresso.Builder.withComposeSupport()` (как в нашем `BaseTestCase`), Kaspresso пытается автоматически проскроллить к элементу перед выполнением действия.

Это означает, что в некоторых случаях **скроллить вручную не обязательно**:

```kotlin
// Kaspresso может автоматически проскроллить к элементу:
AvatarSelectScreen.nextButton {
    assertIsDisplayed()  // ← интерцептор попытается найти и проскроллить
    performClick()
}
```

### Когда автоскролл НЕ работает

Автоскролл имеет **ограничения**:

1. **LazyColumn/LazyRow** — элементы, которые ещё **не созданы** (за пределами буфера), могут не быть найдены интерцептором. Автоскролл не знает, в какую сторону и насколько далеко скроллить.

2. **Большие списки** — если нужный элемент далеко от текущей позиции, автоскролл может не дотянуться за отведённое количество попыток.

3. **Вложенные скролл-контейнеры** — когда один `LazyColumn` внутри другого `scrollable`-контейнера.

> [!CAUTION]
> **Не полагайтесь на автоскролл для LazyColumn!** Явный `performScrollToIndex` или `performScrollToNode` — надёжнее и предсказуемее. Автоскролл — хороший помощник для простых случаев, но для тестирования списков лучше скроллить явно.

---

## Часть 5. Скроллинг в Kakao Compose (ComposeScreen)

### Скроллинг через KNode

В Kakao Compose (используется в наших `ComposeScreen`) можно выполнять скроллинг через `KNode`:

```kotlin
object FeedScreen : ComposeScreen<FeedScreen>() {

    val feedList: KNode = child {
        hasTestTag(Tags.FEED_LIST)
    }

    fun post(index: Int): KNode = child {
        hasTestTag("${Tags.FEED_POST}_$index")
    }
}
```

В тесте:

```kotlin
step("Проскроллить до последней карточки") {
    FeedScreen {
        feedList.performScrollToIndex(6)
        post(6).assertIsDisplayed()
    }
}
```

### Скроллинг до узла по матчеру

```kotlin
import androidx.compose.ui.test.hasTestTag as matchTag
import androidx.compose.ui.test.hasText as matchText

step("Проскроллить до карточки Ивана") {
    FeedScreen {
        feedList.performScrollToNode(
            matchTag(Tags.FEED_POST_AUTHOR_NAME) and matchText("Иван")
        )
    }
}
```

---

## Часть 6. Практические паттерны

### Паттерн 1: «Скролл + проверка»

Самый частый сценарий — проскроллить к элементу и сразу проверить его содержимое:

```kotlin
step("Проверить данные на последней карточке") {
    // 1. Скроллим контейнер
    FeedScreen.feedList.performScrollToIndex(6)

    // 2. Проверяем элемент
    FeedScreen.teaNameOnCard("Иван") {
        assertIsDisplayed()
        assertTextEquals("Дарджилинг")
    }
}
```

### Паттерн 2: «Проскроллить и нажать»

Элемент может быть не видим при первом отображении — нужно сначала проскроллить, потом нажать:

```kotlin
step("Выбрать чай из каталога") {
    // Проскроллить до сорта «Дарджилинг» в списке
    TeaSelectScreen {
        teaList.performScrollToNode(matchText("Дарджилинг"))
        tea("Дарджилинг") {
            assertIsDisplayed()
            performClick()
        }
    }
}
```

### Паттерн 3: «Проверка количества элементов»

Проверить, что в списке определённое количество элементов:

```kotlin
step("Проверить, что в ленте 7 карточек") {
    for (i in 0 until 7) {
        FeedScreen.feedList.performScrollToIndex(i)
        FeedScreen.post(i).assertIsDisplayed()
    }
}
```

### Паттерн 4: «Проверка отсутствия лишних элементов»

Убедиться, что после последнего элемента нет «призраков»:

```kotlin
step("Проверить, что 8-й карточки нет") {
    composeTestRule
        .onNodeWithTag("${Tags.FEED_POST}_7")
        .assertDoesNotExist()
}
```

> [!TIP]
> `assertDoesNotExist()` проверяет, что узел **отсутствует** в семантическом дереве. Это единственный assertion, который можно вызвать на несуществующем узле — остальные (`assertIsDisplayed`, `assertTextEquals`) упадут.

---

## Задание: Скроллинг в каталоге чаёв

Напишите тест `TeaCatalogScrollTest.kt`, который проверяет скроллинг в каталоге Улунов.

**Предусловие:**
- Залогиниться через `LoginSharedSteps.login()`

**Шаги:**
1. Нажать FAB (кнопка «+») для создания новой записи
2. Нажать «Выбрать чай»
3. Выбрать категорию «Улун»
4. Проверить, что **первый** сорт списка — **«Те Гуаньинь»** — отображается
5. Проскроллить список по индексу до элемента **«Мао Се (Волосистый Краб)»** (index 17) и проверить, что элемент отображается
6. Проскроллить с помощью `performScrollToNode` до последнего Улуна — **«Габа Али Шань»** — и проверить отображение
7. Проскроллить обратно к началу списка (index 0) и убедиться, что **«Те Гуаньинь»** снова видна

> [!TIP]
> В этом задании вы отработаете оба основных способа скроллинга: `performScrollToIndex` (по номеру) и `performScrollToNode` (по содержимому).

---
