package com.habiba.habitopia.Adapters

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.habiba.habitopia.CharacterViewModel
import com.habiba.habitopia.R
import com.habiba.habitopia.utils.renderSvgToBitmapWithDynamicWebView

class colorsAdapter(
    private val colorsList: List<Pair<String, String>>, // (name, hex)
    private val category: String,
    private val gender: String, // ✅ أضفنا نوع الجنس هنا
    private val viewModel: CharacterViewModel,
    private val character: ImageView
) : RecyclerView.Adapter<colorsAdapter.ColorViewHolder>() {

    class ColorViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val colorCard: CardView = view.findViewById(R.id.colorCard)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.color_item, parent, false)
        return ColorViewHolder(view)
    }

    override fun onBindViewHolder(holder: ColorViewHolder, position: Int) {
        val (colorName, colorHex) = colorsList[position]
        holder.colorCard.setCardBackgroundColor(Color.parseColor(colorHex))

        holder.colorCard.setOnClickListener {
            // تحديث اللون المختار في الـ ViewModel
            when (category.trim().lowercase()) {
                "hair style" -> viewModel.selectedHairColor = colorName
                "clothes" -> viewModel.selectedClothColor = colorName
            }

            val url = viewModel.buildAvatarUrl(gender) // ✅ استخدم الجنس لبناء الرابط
            Log.d("AVATAR_URL", url)

            renderSvgToBitmapWithDynamicWebView(
                context = holder.itemView.context,
                svgUrl = url,
                width = 770,
                height = 770
            ) { bitmap ->
                character.setImageBitmap(bitmap)
            }
        }
    }

    override fun getItemCount(): Int = colorsList.size
}
