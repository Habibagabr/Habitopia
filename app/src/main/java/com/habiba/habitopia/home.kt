package com.habiba.habitopia

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.habiba.habitopia.CharactersData.homeViewModel
import com.habiba.habitopia.databinding.FragmentHomeBinding
import com.habiba.habitopia.utils.renderSvgToBitmapWithDynamicWebView

class home : Fragment() {
    private var _binding : FragmentHomeBinding? = null
    private val binding get()=_binding!!
    private lateinit var viewModel:homeViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding=FragmentHomeBinding.inflate(layoutInflater)
        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[homeViewModel::class.java]
        val sharedPref = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        var characterImage = sharedPref.getString("character",null)
        if (characterImage != null) {
            //for trying
            characterImage=viewModel.setAvater(characterImage,"Happy","Default")
            renderSvgToBitmapWithDynamicWebView(
                context = binding.avatarImage.context,
                svgUrl = characterImage,
                width = 700,
                height = 700
            ) { bitmap ->
                binding.avatarImage.setImageBitmap(bitmap)
            }
        }

        val name = viewModel.characterName
            binding.subtext.text = "Rise and shine! You and $name have things to check off today!"



    }


    }
