package com.example.kaspresso_learning.data

object TeaRepository {

    val allTeas: List<Tea> = listOf(
        // Зелёный
        Tea(1, "Сенча", TeaType.GREEN),
        Tea(2, "Лунцзин", TeaType.GREEN),
        Tea(3, "Матча", TeaType.GREEN),
        Tea(4, "Билочунь", TeaType.GREEN),
        Tea(5, "Жасминовый", TeaType.GREEN),
        Tea(6, "Хуан Шань Мао Фэн", TeaType.GREEN),
        Tea(7, "Тай Пин Хоу Куй", TeaType.GREEN),
        Tea(8, "Чжу Е Цин", TeaType.GREEN),
        Tea(9, "Синьян Маоцзянь", TeaType.GREEN),
        Tea(10, "Лю Ань Гуа Пянь", TeaType.GREEN),

        // Белый
        Tea(11, "Бай Хао Инь Чжэнь", TeaType.WHITE),
        Tea(12, "Бай Му Дань", TeaType.WHITE),
        Tea(13, "Шоу Мэй", TeaType.WHITE),
        Tea(14, "Гун Мэй", TeaType.WHITE),

        // Жёлтый
        Tea(15, "Цзюнь Шань Инь Чжэнь", TeaType.YELLOW),
        Tea(16, "Мэн Дин Хуан Я", TeaType.YELLOW),
        Tea(17, "Хо Шань Хуан Я", TeaType.YELLOW),

        // Улун
        Tea(18, "Те Гуаньинь", TeaType.OOLONG),
        Tea(19, "Да Хун Пао", TeaType.OOLONG),
        Tea(20, "Молочный Улун (Цзинь Сюань)", TeaType.OOLONG),
        Tea(21, "Фэн Хуан Дань Цун", TeaType.OOLONG),
        Tea(22, "Дун Дин", TeaType.OOLONG),
        Tea(23, "Шуй Сянь", TeaType.OOLONG),
        Tea(24, "Жоу Гуй", TeaType.OOLONG),
        Tea(25, "Хуан Цзинь Гуй", TeaType.OOLONG),
        Tea(26, "Али Шань", TeaType.OOLONG),
        Tea(27, "Дун Фан Мэй Жэнь", TeaType.OOLONG),
        Tea(28, "Женьшень Улун", TeaType.OOLONG),
        Tea(29, "Сы Цзи Чунь", TeaType.OOLONG),
        Tea(30, "Те Ло Хань (Железный Архат)", TeaType.OOLONG),
        Tea(31, "Бай Цзи Гуань (Белый Гребень)", TeaType.OOLONG),
        Tea(32, "Шуй Цзинь Гуй (Золотая Черепаха)", TeaType.OOLONG),
        Tea(33, "Ци Лань (Чудесная Орхидея)", TeaType.OOLONG),
        Tea(34, "Бай Жуй Сян (Белая Дафна)", TeaType.OOLONG),
        Tea(35, "Мао Се (Волосистый Краб)", TeaType.OOLONG),
        Tea(36, "Бэнь Шань", TeaType.OOLONG),
        Tea(37, "Я Ши Сян (Утиный Помет)", TeaType.OOLONG),
        Tea(38, "Ми Лань Сян (Медовая Орхидея)", TeaType.OOLONG),
        Tea(39, "Чжи Лань Сян (Ирис)", TeaType.OOLONG),
        Tea(40, "Гуй Хуа Сян (Османтус)", TeaType.OOLONG),
        Tea(41, "Юй Лань Сян (Магнолия)", TeaType.OOLONG),
        Tea(42, "Цзян Хуа Сян (Имбирь)", TeaType.OOLONG),
        Tea(43, "Хуан Чжи Сян (Гардения)", TeaType.OOLONG),
        Tea(44, "Син Жэнь Сян (Миндаль)", TeaType.OOLONG),
        Tea(45, "Мо Ли Сян (Жасмин)", TeaType.OOLONG),
        Tea(46, "Шань Линь Си (Ручей Елового Леса)", TeaType.OOLONG),
        Tea(47, "Ли Шань (Грушевая Гора)", TeaType.OOLONG),
        Tea(48, "Да Юй Лин (Перевал Даю)", TeaType.OOLONG),
        Tea(49, "Цуй Юй (Драгоценная Яшма)", TeaType.OOLONG),
        Tea(50, "Габа Али Шань", TeaType.OOLONG),

        // Красный (Чёрный)
        Tea(51, "Эрл Грей", TeaType.BLACK),
        Tea(52, "Ассам", TeaType.BLACK),
        Tea(53, "Дарджилинг", TeaType.BLACK),
        Tea(54, "Лапсанг Сушонг", TeaType.BLACK),
        Tea(55, "Кимун", TeaType.BLACK),
        Tea(56, "Дянь Хун", TeaType.BLACK),
        Tea(57, "Цзинь Цзюнь Мэй", TeaType.BLACK),
        Tea(58, "Исин Хун Ча", TeaType.BLACK),
        Tea(59, "Чжэн Шань Сяо Чжун", TeaType.BLACK),

        // Пуэр
        Tea(60, "Шу Пуэр", TeaType.PUERH),
        Tea(61, "Шен Пуэр", TeaType.PUERH),
        Tea(62, "Лао Бань Чжан", TeaType.PUERH),
        Tea(63, "Бин Дао", TeaType.PUERH),
        Tea(64, "Мэнхай", TeaType.PUERH),
        Tea(65, "Сяо То Ча", TeaType.PUERH),
        Tea(66, "И У", TeaType.PUERH),
        Tea(67, "Булан Шань", TeaType.PUERH),
        Tea(68, "Гунтин", TeaType.PUERH),
    )

    private val _entries = mutableListOf<BrewingEntry>()
    val entries: List<BrewingEntry> get() = _entries.toList()

    private var nextId = 1

    init {
        // Фейковые записи для ленты
        addFakeEntries()
    }

    fun addEntry(
        authorName: String,
        authorAvatarIndex: Int,
        tea: Tea,
        weightGrams: Int,
        volumeMl: Int? = null,
        vessel: Vessel? = null,
        infusions: Int? = null
    ): BrewingEntry {
        val entry = BrewingEntry(
            id = nextId++,
            authorName = authorName,
            authorAvatarIndex = authorAvatarIndex,
            tea = tea,
            weightGrams = weightGrams,
            volumeMl = volumeMl,
            vessel = vessel,
            infusions = infusions,
            timestamp = System.currentTimeMillis()
        )
        _entries.add(0, entry) // Новые сверху
        return entry
    }

    fun getUserEntries(userName: String): List<BrewingEntry> {
        return _entries.filter { it.authorName == userName }
    }

    private fun addFakeEntries() {
        val fakeUsers = listOf(
            "Алексей" to 0,
            "Мария" to 1,
            "Дмитрий" to 2,
            "Елена" to 3,
            "Николай" to 0,
            "Анна" to 1,
            "Иван" to 2,
        )

        val fakeData = listOf(
            Triple(allTeas[0], 5, Vessel.GAIWAN),     // Сенча (0)
            Triple(allTeas[51], 7, Vessel.TEAPOT),     // Ассам (51)
            Triple(allTeas[18], 8, Vessel.GAIWAN),     // Да Хун Пао (18)
            Triple(allTeas[60], 10, Vessel.GAIWAN),    // Шу Пуэр (60)
            Triple(allTeas[2], 3, Vessel.TEAPOT),      // Матча (2)
            Triple(allTeas[17], 6, Vessel.GAIWAN),     // Те Гуаньинь (17)
            Triple(allTeas[52], 5, Vessel.TEAPOT),     // Дарджилинг (52)
        )

        fakeData.forEachIndexed { index, (tea, weight, vessel) ->
            val (name, avatar) = fakeUsers[index]
            _entries.add(
                BrewingEntry(
                    id = nextId++,
                    authorName = name,
                    authorAvatarIndex = avatar,
                    tea = tea,
                    weightGrams = weight,
                    volumeMl = 150 + (index * 50),
                    vessel = vessel,
                    timestamp = System.currentTimeMillis() - (index * 3600_000L)
                )
            )
        }
    }
}
