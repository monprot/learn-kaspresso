package com.example.kaspresso_learning.data

object TeaRepository {

    val allTeas: List<Tea> = listOf(
        // Зелёный
        Tea(1, "Сенча", TeaType.GREEN),
        Tea(2, "Лунцзин", TeaType.GREEN),
        Tea(3, "Матча", TeaType.GREEN),
        Tea(4, "Билочунь", TeaType.GREEN),
        Tea(5, "Жасминовый", TeaType.GREEN),
        // Чёрный
        Tea(6, "Эрл Грей", TeaType.BLACK),
        Tea(7, "Ассам", TeaType.BLACK),
        Tea(8, "Дарджилинг", TeaType.BLACK),
        Tea(9, "Лапсанг Сушонг", TeaType.BLACK),
        Tea(10, "Кимун", TeaType.BLACK),
        // Улун
        Tea(11, "Те Гуаньинь", TeaType.OOLONG),
        Tea(12, "Да Хун Пао", TeaType.OOLONG),
        Tea(13, "Молочный Улун", TeaType.OOLONG),
        Tea(14, "Фэн Хуан Дань Цун", TeaType.OOLONG),
        Tea(15, "Дун Дин", TeaType.OOLONG),
        // Пуэр
        Tea(16, "Шу Пуэр", TeaType.PUERH),
        Tea(17, "Шен Пуэр", TeaType.PUERH),
        Tea(18, "Лао Бань Чжан", TeaType.PUERH),
        Tea(19, "Бин Дао", TeaType.PUERH),
        Tea(20, "Мэнхай", TeaType.PUERH),
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
            Triple(allTeas[0], 5, Vessel.GAIWAN),     // Сенча
            Triple(allTeas[6], 7, Vessel.TEAPOT),      // Ассам
            Triple(allTeas[11], 8, Vessel.GAIWAN),     // Да Хун Пао
            Triple(allTeas[15], 10, Vessel.GAIWAN),    // Шу Пуэр
            Triple(allTeas[2], 3, Vessel.TEAPOT),      // Матча
            Triple(allTeas[10], 6, Vessel.GAIWAN),     // Те Гуаньинь
            Triple(allTeas[7], 5, Vessel.TEAPOT),      // Дарджилинг
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
