package com.habiba.habitopia

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.habiba.habitopia.ViewModel.CharacterViewModel
import com.habiba.habitopia.utils.renderSvgToBitmapWithDynamicWebView

class Naming : Fragment() {
    private val args: NamingArgs by navArgs()
    private lateinit var character:ImageView
    private lateinit var nextButton:ImageView
    private lateinit var nameText:EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_naming, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val image:String
        super.onViewCreated(view, savedInstanceState)
         val viewModel: CharacterViewModel by activityViewModels()
        val gender=args.gender
        context?.deleteSharedPreferences("MasterPreference")

        image=viewModel.buildAvatarUrl(gender)
        character=view.findViewById(R.id.finalImage)
        renderSvgToBitmapWithDynamicWebView(
            context = character.context,
            svgUrl = image,
            width = 700,
            height = 700
        ) { bitmap ->
            character.setImageBitmap(bitmap)
        }
        nameText=view.findViewById(R.id.nameInput)
        nextButton=view.findViewById(R.id.nextnamingButton)
        nameText.addTextChangedListener {
            val name = it.toString().trim()
            if (name.isNotEmpty()) {
                nextButton.isEnabled = true
                nextButton.setImageResource(R.drawable.clickednext)
            } else {
                nextButton.isEnabled = false
                nextButton.setImageResource(R.drawable.next_arrow_unclicked)
            }
        }

        nextButton.setOnClickListener {
            val name = nameText.text.toString().trim()
            if (name.isNotEmpty()) {
                val sharedPref = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                sharedPref.edit()
                    .putString("character", image)
                    .apply()

                val intent = Intent(requireContext(), MainActivity2::class.java)
                val namSharedPref=requireContext().getSharedPreferences("name Preference",Context.MODE_PRIVATE)
                namSharedPref.edit()
                    .putString("character name",name)
                    .apply()
                startActivity(intent)
        }
        }


    }

}