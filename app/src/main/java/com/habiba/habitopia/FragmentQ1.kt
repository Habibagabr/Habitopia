package com.habiba.habitopia

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class FragmentQ1 : Fragment() {

    private var selectedAnswer: String? = null
    private var lastSelectedButton: Button? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_q1, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val yesButton: Button = view.findViewById(R.id.yesButton)
        val noButton: Button = view.findViewById(R.id.noButton)
        val continueButton: Button = view.findViewById(R.id.continueButton)
        val skipText: TextView = view.findViewById(R.id.skipText)

        // تعريف ألوان افتراضية ومختارة
        val defaultColor =
            ContextCompat.getColorStateList(requireContext(), R.color.default_button_color)
        val selectedColor =
            ContextCompat.getColorStateList(requireContext(), R.color.selected_button_color)

        yesButton.setOnClickListener {
            selectedAnswer = "Yes"
            // إذا كان فيه زر مختار قبل كده، نرجّع لونه للون الافتراضي
            lastSelectedButton?.backgroundTintList = defaultColor
            // تغيير لون Yes بس
            yesButton.backgroundTintList = selectedColor
            lastSelectedButton = yesButton
            yesButton.isSelected = true
            noButton.isSelected = false

        }

        noButton.setOnClickListener {
            selectedAnswer = "No"
            // إذا كان فيه زر مختار قبل كده، نرجّع لونه للون الافتراضي
            lastSelectedButton?.backgroundTintList = defaultColor
            // تغيير لون No بس
            noButton.backgroundTintList = selectedColor
            lastSelectedButton = noButton
            yesButton.isSelected = false
            noButton.isSelected = true
        }


        continueButton.setOnClickListener {
            if (selectedAnswer == "Yes") {
                findNavController().navigate(R.id.action_fragmentQ1_to_fragmentQ2)
                }
            else
            {
                findNavController().navigate(R.id.action_fragmentQ1_to_fragmentQ3)
            }
            // إضافة الهالة لـ continueButton
            continueButton.setBackgroundResource(R.drawable.glow_effect)
        }

        skipText.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentQ1_to_fragmentQ2)
        }
    }
}
