# learn-kaspresso

Практический курс по автотестированию Android-приложений с [Kaspresso](https://github.com/KasperskyLab/Kaspresso).

## О проекте

Курс построен вокруг тестового приложения **TeaLog** — дневника чайных заварок, написанного на **Jetpack Compose**. В приложении пользователь:

1. Выбирает аватар → вводит имя → попадает в ленту
2. В ленте видит записи всех пользователей
3. Может создать новую запись (выбрать чай, указать параметры)
4. Может перейти в профиль и увидеть свои записи

На примере этого приложения вы пройдёте путь от настройки окружения до написания полноценных UI-автотестов.

## Требования

- **Android Studio** (актуальная версия)
- Базовые знания **Kotlin**
- Базовые знания **Git**

## Как начать

1. Склонируйте репозиторий:
   ```bash
   git clone https://github.com/monprot/learn-kaspresso.git
   ```
2. Откройте проект в Android Studio
3. Создайте эмулятор (Device Manager → Create Device)
4. Запустите приложение (`app`) на эмуляторе — убедитесь, что оно работает
5. Переходите к **Уроку 0**

## Уроки

| #  | Урок | Описание |
|:---|:-----|:---------|
| 00 | [Настройка среды](lessons/00_setup/setup.md) | Установка Android Studio, клонирование проекта, запуск первого теста |
| 01 | [Архитектура теста](lessons/01_test_architecture/compose_testing.md) | Структура теста в Kaspresso, Finder / Assertion / Action |
| 02 | [Поиск элементов](lessons/02_finding_elements/README.md) | `testTag`, карта тегов, поиск и проверка элементов |
| 03 | [Первый тест](lessons/03_first_test/README.md) | Пишем полноценный тест сценария от логина до ленты |
| 04 | [Добавление тега](lessons/04_adding_tag/README.md) | Что делать, если у элемента нет `testTag` |
| 05 | [Page Object](lessons/05_page_object/README.md) | Паттерн Page Object для организации тестов |
| 06 | [Скролл](lessons/06_scroll/README.md) | Работа с прокруткой и длинными списками |
| 07 | [Параметризация](lessons/07_parameterized/README.md) | Параметризованные тесты |
| 08 | [Навигация](lessons/08_navigation/README.md) | Тестирование навигации между экранами |
| 09 | [Валидация](lessons/09_validation/README.md) | Валидация данных и обработка ошибок |

## Структура проекта

```
learn-kaspresso/
├── app/
│   └── src/
│       ├── main/              ← Код приложения (TeaLog)
│       ├── test/              ← Unit-тесты (JVM)
│       └── androidTest/       ← Инструментальные тесты (на эмуляторе)
│           └── java/com/example/kaspresso_learning/
│               └── AvatarSelectScreenTest.kt   ← Пример теста
└── lessons/                   ← Материалы курса
    ├── 00_setup/
    ├── 01_test_architecture/
    ├── 02_finding_elements/
    ├── ...
    └── 09_validation/
```

## Полезные ссылки

- [Kaspresso (GitHub)](https://github.com/KasperskyLab/Kaspresso)
- [Compose Testing APIs](https://developer.android.com/develop/ui/compose/testing)
- [Compose Testing Cheatsheet](https://developer.android.com/develop/ui/compose/testing/testing-cheatsheet)
