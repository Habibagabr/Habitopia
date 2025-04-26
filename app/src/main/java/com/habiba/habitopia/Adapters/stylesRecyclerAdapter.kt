package com.habiba.habitopia.Adapters

import com.habiba.habitopia.CharacterViewModel
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.habiba.habitopia.R
import com.habiba.habitopia.utils.renderSvgToBitmapWithDynamicWebView

class stylesRecyclerAdapter(
    private val gender: String,
    private val stylesList: List<String>,
    private val currentCategory: String,
    private val character: ImageView,
    private val viewModel: CharacterViewModel
) : RecyclerView.Adapter<stylesRecyclerAdapter.StylesViewHolder>() {

    class StylesViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val styleImage: ImageView = view.findViewById(R.id.styleImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StylesViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.character_item, parent, false)
        return StylesViewHolder(view)
    }

    override fun onBindViewHolder(holder: StylesViewHolder, position: Int) {
        val styleValue = stylesList[position]
        val previewUrl = generatePreviewUrl(styleValue)

        renderSvgToBitmapWithDynamicWebView(
            context = holder.itemView.context,
            svgUrl = previewUrl,
            width = 770,
            height = 770
        ) { bitmap ->
            holder.styleImage.setImageBitmap(bitmap)
        }

        holder.styleImage.setOnClickListener {
            when (currentCategory.lowercase()) {
                "hair style" -> viewModel.selectedHair = styleValue
                "clothes" -> viewModel.selectedClothes = styleValue
                "skin tone" -> viewModel.selectedSkinTone = styleValue
                "facial hair" -> viewModel.selectedFacialHair = styleValue
            }

            val finalUrl = viewModel.buildAvatarUrl(gender)
            renderSvgToBitmapWithDynamicWebView(
                context = character.context,
                svgUrl = finalUrl,
                width = 770,
                height = 770
            ) { bitmap ->
                character.setImageBitmap(bitmap)
            }
        }
    }

    override fun getItemCount(): Int = stylesList.size

    private fun generatePreviewUrl(styleValue: String): String {
        val defaultHair = if (gender == "female") "LongHairStraight" else "ShortHairShortCurly"
        val topType = viewModel.selectedHair ?: defaultHair
        val clotheType = viewModel.selectedClothes ?: "Hoodie"
        val skinColor = viewModel.selectedSkinTone ?: "Light"
        val facialHairType = viewModel.selectedFacialHair ?: "Blank"
        val clotheColor = viewModel.selectedClothColor ?: "Blue03"
        val hairColor = viewModel.selectedHairColor ?: "Brown"

        val baseUrl = "https://avataaars.io/?avatarStyle=Transparent" +
                "&topType=$topType" +
                "&clotheType=$clotheType" +
                "&skinColor=$skinColor" +
                "&facialHairType=$facialHairType" +
                "&hairColor=$hairColor" +
                "&accessoriesType=Blank" +
                "&mouthType=Smile" +
                "&clotheColor=$clotheColor"

        return when (currentCategory.lowercase()) {
            "hair style" -> baseUrl.replace("topType=$topType", "topType=$styleValue")
            "clothes" -> baseUrl.replace("clotheType=$clotheType", "clotheType=$styleValue")
            "skin tone" -> baseUrl.replace("skinColor=$skinColor", "skinColor=$styleValue")
            "facial hair" -> baseUrl.replace("facialHairType=$facialHairType", "facialHairType=$styleValue")
            else -> baseUrl
        }
    }

}
