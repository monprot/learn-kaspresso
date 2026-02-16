# Вынести

### Семантическое дерево

Compose строит **семантическое дерево** — иерархию узлов, каждый из которых описывает UI-элемент. Тесты работают именно с этим деревом, а не с визуальным представлением.

#### Объединённое и необъединённое дерево

По умолчанию тесты работают с **объединённым (merged)** деревом. Некоторые контейнеры (например, `Button`) объединяют семантику дочерних элементов:

```kotlin
// Код:
MyButton {
    Text("Hello")
    Text("World")
}

// Объединённое дерево:
// Node: Role = 'Button', Text = '[Hello, World]'

// Необъединённое дерево:
// Node: Role = 'Button'
//   ├─ Node: Text = '[Hello]'
//   └─ Node: Text = '[World]'
```

Чтобы искать в необъединённом дереве, передайте `useUnmergedTree = true`:

```kotlin
composeTestRule
    .onNodeWithText("World", useUnmergedTree = true)
    .assertIsDisplayed()
```

#### Отладка: printToLog

Чтобы вывести дерево в логи и понять, что «видит» тест:

```kotlin
composeTestRule.onRoot().printToLog("MY_TAG")
// или необъединённое:
composeTestRule.onRoot(useUnmergedTree = true).printToLog("MY_TAG")
```

> [!TIP]
> Если тест «не находит» элемент — используйте `printToLog()`, чтобы увидеть дерево глазами теста. Часто проблема в объединении узлов.

Задание для третьего урока
**Бонус:** выведите семантическое дерево через `printToLog()` в тесте и сравните вывод с тем, что показывает Layout Inspector.


### Объединённое и необъединённое дерево

Семантическое дерево существует в **двух вариантах**:

- **Объединённое (merged)** — дочерние узлы сливаются с родителем. Например, `Button` с `Icon` и `Text` внутри отображается как **один узел**. **Тест-фреймворк использует именно его по умолчанию.**
- **Необъединённое (unmerged)** — каждый узел сохраняется отдельно.

```kotlin
// Пример: Button объединяет семантику дочерних узлов
Button(onClick = { }) {
    Icon(Icons.Default.Favorite, contentDescription = null)
    Text("Нравится")
}
// В объединённом дереве это ОДИН узел с текстом "Нравится" и действием onClick
```

Это значит, что вы можете найти кнопку **по её тексту**, даже если текст — это дочерний `Text`-composable:

```kotlin
composeTestRule.onNodeWithText("Нравится").performClick() // ✅ Работает!
```

> [!TIP]
> В Layout Inspector можно переключаться между объединённым и необъединённым деревом через **View Options** → **Highlight Semantics Layers** на панели Component Tree.

### Вывод дерева в лог (для отладки)
Чтобы увидеть семантическое дерево в тесте, используйте `printToLog()`:

```kotlin
// Вывести объединённое дерево (используется по умолчанию)
composeTestRule.onRoot().printToLog("SEMANTICS")

// Вывести необъединённое дерево (все узлы по отдельности)
composeTestRule.onRoot(useUnmergedTree = true).printToLog("SEMANTICS_UNMERGED")
```

В Logcat вы увидите что-то вроде:

```
Printing with useUnmergedTree = 'false'
Node #1 at (l=0, t=0, r=1080, b=2280)
 |-Node #2 at (l=0, t=0, r=1080, b=2280)
   TestTag = 'avatar_screen_container'
    |-Node #5 at (l=168, t=480, r=444, b=756)
      TestTag = 'avatar_icon_0'
      Role = 'Image'
      ContentDescription = 'Аватар 1'
    |-Node #9 at (l=336, t=1800, r=744, b=1920)
      TestTag = 'avatar_screen_next_button'
      Text = 'Далее'
      Role = 'Button'
```

> [!TIP]
> Если тест «не находит» элемент — используйте `printToLog()`, чтобы увидеть дерево глазами теста. Часто проблема в объединении узлов.




---


### Иерархические матчеры (Selectors)

Продвинутый способ поиска — навигация по дереву:

| Матчер | Что делает |
|:---|:---|
| `hasParent(matcher)` | Найти узел, чей родитель совпадает |
| `hasAnyAncestor(matcher)` | Найти узел, у которого есть предок, совпадающий с matcher |
| `hasAnySibling(matcher)` | Найти узел, у которого есть «сосед», совпадающий с matcher |
| `hasAnyDescendant(matcher)` | Найти узел, у которого есть потомок, совпадающий с matcher |

```kotlin
// Найти элемент, чей родитель содержит текст «Button»:
composeTestRule
    .onNode(hasParent(hasText("Button")))
    .assertIsDisplayed()
```

Также можно использовать **селекторы** для навигации по результатам:

```kotlin
composeTestRule
    .onNode(hasTestTag("Players"))
    .onChildren()                    // все дочерние узлы
    .filter(hasClickAction())        // только кликабельные
    .assertCountEquals(4)
    .onFirst()                       // первый из них
    .assert(hasText("John"))
```

## Часть 3. Объединённое и необъединённое дерево

### Два варианта семантического дерева

Семантическое дерево существует в **двух вариантах**:

- **Объединённое (merged)** — дочерние узлы сливаются с родителем. Например, `Button` с `Icon` и `Text` внутри отображается как **один узел**. **Тест-фреймворк использует его по умолчанию.**
- **Необъединённое (unmerged)** — каждый узел сохраняется отдельно.

```kotlin
// Пример: Button объединяет семантику дочерних узлов
Button(onClick = { }) {
    Icon(Icons.Default.Favorite, contentDescription = null)
    Text("Нравится")
}

// Объединённое дерево (merged):
// Node: Role = 'Button', Text = '[Нравится]', OnClick = '...'

// Необъединённое дерево (unmerged):
// Node: Role = 'Button'
//   ├─ Node: ContentDescription = null  (Icon)
//   └─ Node: Text = '[Нравится]'        (Text)
```

Благодаря объединению вы можете найти кнопку **по её тексту**, даже если текст — это дочерний `Text`-composable:

```kotlin
composeTestRule.onNodeWithText("Нравится").performClick() // ✅ Работает!
```

### Когда нужно необъединённое дерево

Иногда объединение мешает — например, когда в кнопке два `Text` и нужно проверить конкретный. В таких случаях используйте `useUnmergedTree = true`:

```kotlin
composeTestRule
    .onNodeWithText("Нравится", useUnmergedTree = true)
    .assertIsDisplayed()
```

> [!TIP]
> В Layout Inspector можно переключаться между объединённым и необъединённым деревом через **View Options** → **Highlight Semantics Layers** на панели Component Tree.

---

## Часть 4. Отладка: printToLog

Когда тест «не находит» элемент, самый быстрый способ разобраться — вывести семантическое дерево в Logcat:

```kotlin
// Вывести объединённое дерево (по умолчанию)
composeTestRule.onRoot().printToLog("TREE")

// Вывести необъединённое дерево
composeTestRule.onRoot(useUnmergedTree = true).printToLog("TREE_UNMERGED")
```

В Logcat (фильтр по тегу `TREE`) вы увидите:

```
Printing with useUnmergedTree = 'false'
Node #1 at (l=0, t=0, r=1080, b=2280)
 |-Node #2 at (l=0, t=0, r=1080, b=2280)
   TestTag = 'avatar_screen_container'
    |-Node #5 at (l=168, t=480, r=444, b=756)
      TestTag = 'avatar_icon_0'
      Role = 'Image'
      ContentDescription = 'Аватар 1'
    |-Node #9 at (l=336, t=1800, r=744, b=1920)
      TestTag = 'avatar_screen_next_button'
      Text = 'Далее'
      Role = 'Button'
```

> [!TIP]
> Если тест не находит элемент — используйте `printToLog()`, чтобы увидеть дерево глазами теста. Часто проблема в объединении узлов или в отсутствии `testTag`.

---

## Часть 5. Иерархические матчеры и селекторы

Для сложных случаев можно искать элементы **по их положению в дереве**:

| Матчер | Что делает |
|:---|:---|
| `hasParent(matcher)` | Узел, чей родитель совпадает |
| `hasAnyAncestor(matcher)` | Узел, у которого есть подходящий предок |
| `hasAnySibling(matcher)` | Узел, у которого есть подходящий «сосед» |
| `hasAnyDescendant(matcher)` | Узел, у которого есть подходящий потомок |

```kotlin
// Найти текст внутри конкретного контейнера:
composeTestRule
    .onNode(
        hasText("Ошибка") and hasAnyAncestor(hasTestTag("avatar_screen_container"))
    )
    .assertIsDisplayed()
```

Также можно использовать **селекторы** для навигации по результатам:

```kotlin
composeTestRule
    .onNode(hasTestTag("Players"))
    .onChildren()                    // все дочерние узлы
    .filter(hasClickAction())        // только кликабельные
    .assertCountEquals(4)
    .onFirst()                       // первый из них
    .assert(hasText("John"))
```

> [!NOTE]
> Иерархические матчеры — продвинутая тема. На этом этапе достаточно знать, что они существуют. Мы вернёмся к ним, когда столкнёмся с неоднозначным поиском элементов.

---

**⭐ Бонус:** добавьте в начало теста шаг с `printToLog()` и сравните вывод в Logcat с тем, что показывает Layout Inspector.