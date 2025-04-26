package com.habiba.habitopia

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.habiba.habitopia.databinding.FragmentGenderBinding

class Gender : Fragment() {
    private var _viewBinding: FragmentGenderBinding? = null
    private val viewBinding get() = _viewBinding!!
    private var selectedGender:String="male"
    private var fisrtTime=true



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _viewBinding = FragmentGenderBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            requireActivity().finishAffinity()
        }
        viewBinding.maleCard.setOnClickListener {
            fisrtTime=false
            selectedGender="male"
            selectGender(selectedGender)

        }

        viewBinding.femaleCard.setOnClickListener {
            fisrtTime=false
            selectedGender="female"
            selectGender(selectedGender)

        }
        viewBinding.nextButton.setOnClickListener {
            if (!fisrtTime) {
                val action = GenderDirections.actionGenderToCharacter(selectedGender)
                findNavController().navigate(action)
            } else {
                Toast.makeText(requireContext(), "Please choose your gender first", Toast.LENGTH_SHORT).show()

            }
        }
    }

    private fun selectGender(gender: String) {
        val selectedColor = ContextCompat.getColor(requireContext(), R.color.green)
        val defaultColor = ContextCompat.getColor(requireContext(), android.R.color.white)

        if (gender=="male") {
            viewBinding.maleCard.setCardBackgroundColor(selectedColor)
            viewBinding.femaleCard.setCardBackgroundColor(defaultColor)
            viewBinding.maleText.setTextColor(defaultColor)
            viewBinding.femaleText.setTextColor(selectedColor)
            viewBinding.maleCard.cardElevation = 24f
            viewBinding.femaleCard.cardElevation = 4f
        } else {
            viewBinding.femaleCard.setCardBackgroundColor(selectedColor)
            viewBinding.maleCard.setCardBackgroundColor(defaultColor)
            viewBinding.femaleText.setTextColor(defaultColor)
            viewBinding.maleText.setTextColor(selectedColor)
            viewBinding.femaleCard.cardElevation = 24f
            viewBinding.maleCard.cardElevation = 4f

        }

        viewBinding.nextButton.setImageResource(R.drawable.next_arrow)
        viewBinding.nextButton.elevation=24f
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }
}
