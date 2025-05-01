package com.habiba.habitopia.ViewModel

import androidx.lifecycle.ViewModel

class CharacterViewModel : ViewModel() {

    var selectedHair: String? = null
    var selectedClothes: String? = null
    var selectedSkinTone: String? = null
    var selectedFacialHair: String? = null
    var selectedHairColor: String? = null
    var selectedClothColor: String? = null

    fun reset() {
        selectedHair = null
        selectedClothes = null
        selectedSkinTone = null
        selectedFacialHair = null
        selectedHairColor = null
        selectedClothColor = null
    }

    fun buildAvatarUrl(gender: String): String {
        val topType = when (gender.lowercase()) {
            "male" -> selectedHair ?: "ShortHairShortCurly"
            "female" -> selectedHair ?: "LongHairStraight"
            else -> selectedHair ?: "ShortHairShortCurly"
        }

        val clotheType = selectedClothes ?: "Hoodie"
        val skinColor = selectedSkinTone ?: "Light"
        val facialHairType = selectedFacialHair ?: "Blank"
        val clotheColor = selectedClothColor ?: "Blue03"
        val hairColor = selectedHairColor ?: "Brown"

        return  "https://avataaars.io/?avatarStyle=Transparent" +
                "&topType=$topType" +
                "&clotheType=$clotheType" +
                "&skinColor=$skinColor" +
                "&facialHairType=$facialHairType" +
                "&hairColor=$hairColor" +
                "&accessoriesType=Blank" +
                "&mouthType=Smile" +
                "&clotheColor=$clotheColor"
    }


}
