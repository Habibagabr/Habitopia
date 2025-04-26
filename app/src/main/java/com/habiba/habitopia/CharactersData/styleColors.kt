package com.habiba.habitopia.CharactersData

object styleColors {

    val hairColorsList = listOf(
        "Auburn" to "#A55728",
        "Black" to "#2C1B18",
        "Blonde" to "#B58143",
        "BlondeGolden" to "#D6B370",
        "Brown" to "#724133",
        "BrownDark" to "#4A312C",
        "PastelPink" to "#F59797",
        "Platinum" to "#ECDCBF",
        "Red" to "#C93305",
        "SilverGray" to "#E8E1E1"
    )

    val clothesColorsList = listOf(
        "Black" to "#262E33",
        "Blue01" to "#65C9FF",
        "Blue02" to "#5199E4",
        "Blue03" to "#25557C",
        "Gray01" to "#E6E6E6",
        "Gray02" to "#929598",
        "Heather" to "#3C4F5C",
        "PastelBlue" to "#B1E2FF",
        "PastelGreen" to "#A7FFC4",
        "PastelOrange" to "#FFDEB5",
        "PastelRed" to "#FFAFB9",
        "PastelYellow" to "#FFFFB1",
        "Pink" to "#FF488E",
        "Red" to "#FF5C5C",
        "White" to "#FFFFFF"
    )

    fun getColorsForCategory(category: String): List<Pair<String, String>>? {
        return when (category.trim().lowercase()) {
            "hair style" -> hairColorsList
            "clothes" -> clothesColorsList
            else -> null
        }
    }
}
