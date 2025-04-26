package com.habiba.habitopia.CharactersData

import androidx.lifecycle.ViewModel

class homeViewModel : ViewModel() {
    var characterName: String? = null

    fun setAvater(character: String, eye: String, mouth: String):String {
        var newUrl = character
            .replace(Regex("mouthType=[^&]*"), "")
            .replace(Regex("eyeType=[^&]*"), "")

        if (newUrl.contains("?")) {
            newUrl += "&mouthType=$mouth&eyeType=$eye"
        } else {
            newUrl += "?mouthType=$mouth&eyeType=$eye"
        }

       return newUrl
    }

}
