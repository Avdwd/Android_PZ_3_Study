package com.example.pz_3_shynkarenko

enum class Colors (val colorName: String, val rgb: Int) {
    RED("Червоний", 0xFFFF0000.toInt()),
    GREEN("Зелений", 0xFF00FF00.toInt()),
    BLUE("Синій", 0xFF0000FF.toInt()),
    YELLOW("Жовтий", 0xFFFFFF00.toInt()),
    CYAN("Блакитний", 0xFF00FFFF.toInt()),
    MAGENTA("Пурпуровий", 0xFFFF00FF.toInt()),
    ORANGE("Помаранчевий", 0xFFFFA500.toInt()),
    PURPLE("Фіолетовий", 0xFF800080.toInt()),
    BROWN("Коричневий", 0xFFA52A2A.toInt()),
    BLACK("Чорний", 0xFF000000.toInt()),
    WHITE("Білий", 0xFFFFFFFF.toInt()),
    GRAY("Сірий", 0xFF888888.toInt());

    companion object {
        fun getRandomColor(): Colors {
            return values().random()
        }
    }
}