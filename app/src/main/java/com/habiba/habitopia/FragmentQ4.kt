package com.habiba.habitopia

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class FragmentQ4 : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_q4, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // العناصر الموجودة في الـ XML
        val waterCupsInput: EditText = view.findViewById(R.id.waterCupsInput)
        val continueButton: Button = view.findViewById(R.id.continue_button)
        val skipButton: TextView = view.findViewById(R.id.skip_button)

        // لما المستخدم يضغط على Continue
        continueButton.setOnClickListener {
            // لو المستخدم دخّل عدد أكواب الماء، نحفظه في SharedPreferences
            val waterCups = waterCupsInput.text.toString()
            if (waterCups.isNotEmpty()) {
                val sharedPref =
                    requireContext().getSharedPreferences("drinking_prefs", Context.MODE_PRIVATE)
                with(sharedPref.edit()) {
                    putString("habitName", "drinking")
                    putString("answer", null)
                    putInt("habitCount", waterCups.toIntOrNull() ?: 0)
                    putString("emoji", "💧")
                    apply()
                }

            }

            // إضافة الهالة لـ continueButton
            continueButton.setBackgroundResource(R.drawable.glow_effect)
            // ننقل المستخدم لـ FragmentQ5 حتى لو ساب الحقل فاضي
            findNavController().navigate(R.id.action_fragmentQ4_to_fragmentQ5)
        }
        // لما المستخدم يضغط على Skip
        skipButton.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentQ4_to_fragmentQ5)
        }
    }




}