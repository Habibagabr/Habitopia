package com.habiba.habitopia

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.habiba.habitopia.Adapters.colorsAdapter
import com.habiba.habitopia.Adapters.stylesRecyclerAdapter
import com.habiba.habitopia.CharactersData.femaledata
import com.habiba.habitopia.CharactersData.maledata
import com.habiba.habitopia.CharactersData.styleColors
import com.habiba.habitopia.ViewModel.CharacterViewModel
import com.habiba.habitopia.databinding.FragmentCharacterBinding
import com.habiba.habitopia.utils.renderSvgToBitmapWithDynamicWebView

class Character : Fragment() {

    private lateinit var adapter: stylesRecyclerAdapter
    private lateinit var titles: List<String>
    private lateinit var usedData: List<List<String>>
    private var counter = 0

    private val viewModel: CharacterViewModel by activityViewModels()
    private val args: CharacterArgs by navArgs()

    private var _viewBinding: FragmentCharacterBinding? = null
    private val viewBinding get() = _viewBinding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _viewBinding = FragmentCharacterBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Handle physical back button on the phone
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            viewModel.reset()
            findNavController().navigate(R.id.action_character_to_gender)
        }


        val gender = args.gender

        titles = if (gender == "female") {
            viewBinding.charcter.setImageResource(R.drawable.customized_female)
            femaledata.totalListsTitles
        } else {
            viewBinding.charcter.setImageResource(R.drawable.customized_male)
            maledata.totalListsTitles
        }

        usedData = if (gender == "female") femaledata.totalLists else maledata.totalList

        viewBinding.backButton.setOnClickListener {
            viewModel.reset()
            findNavController().navigate(R.id.action_character_to_gender)
        }

        loadCurrentStyle(gender)

        viewBinding.nextStyleButton.setOnClickListener {
            if (counter < titles.lastIndex) {
                counter++
                loadCurrentStyle(gender)
            }
            if(counter==titles.lastIndex) {
                viewBinding.skipButton.visibility = View.GONE
                viewBinding.DoneButton.visibility=View.VISIBLE
            }

        }
        viewBinding.skipButton.setOnClickListener {
            val action=CharacterDirections.actionCharacterToNaming(gender)
            findNavController().navigate(action)
        }

        viewBinding.DoneButton.setOnClickListener {
            val action=CharacterDirections.actionCharacterToNaming(gender)
            findNavController().navigate(action)
        }


        viewBinding.backStyleButton.setOnClickListener {
            if (counter > 0) {
                counter--
                loadCurrentStyle(gender)
                viewBinding.skipButton.visibility = View.VISIBLE
                viewBinding.DoneButton.visibility=View.GONE
            }
        }



        viewBinding.characterStyleRecyclerview.layoutManager =
            GridLayoutManager(requireContext(), 3)

        viewBinding.colorRecycle.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
    }

    private fun loadCurrentStyle(gender: String) {
        val currentCategory = titles[counter]
        val currentList = usedData[counter]

        viewBinding.styleText.text = currentCategory

        val avatarUrl = viewModel.buildAvatarUrl(gender)

        renderSvgToBitmapWithDynamicWebView(
            context = viewBinding.charcter.context,
            svgUrl = avatarUrl,
            width = 770,
            height = 770
        ) { bitmap ->
            _viewBinding?.charcter?.setImageBitmap(bitmap)
        }


        // Set character style options
        adapter = stylesRecyclerAdapter(
            gender = gender,
            stylesList = currentList,
            currentCategory = currentCategory,
            character = viewBinding.charcter,
            viewModel = viewModel
        )
        viewBinding.characterStyleRecyclerview.adapter = adapter


        // Get color options only for Hair or Clothes
        val colorsList = styleColors.getColorsForCategory(currentCategory)

        if (colorsList != null) {
            viewBinding.colorRecycle.visibility = View.VISIBLE
            viewBinding.colorRecycle.adapter = colorsAdapter(
                colorsList = colorsList,
                category = currentCategory,
                gender = gender,
                viewModel = viewModel,
                character = viewBinding.charcter
            )

        } else {
            viewBinding.colorRecycle.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }
}
