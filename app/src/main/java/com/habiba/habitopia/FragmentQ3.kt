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

class FragmentQ3 : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_q3, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // العناصر الموجودة في الـ XML
        val sleepTimeInput: EditText = view.findViewById(R.id.sleepTimeInput)
        val continueButton: Button = view.findViewById(R.id.continue_button)
        val skipButton: TextView = view.findViewById(R.id.skip_button)

        // لما المستخدم يضغط على Continue
        continueButton.setOnClickListener {
            val sleepTime = sleepTimeInput.text.toString()
            if (sleepTime.isNotEmpty()) {
                val sharedPref =
                    requireContext().getSharedPreferences("sleeping_prefs", Context.MODE_PRIVATE)
                with(sharedPref.edit()) {
                    putString("habitName", "sleeping")
                    putString("answer", sleepTime) // مثلاً "8 hours" أو null
                    putString("habitCount", null)
                    putString("emoji", "😴")
                    apply()
                }

            }

            // إضافة الهالة لـ continueButton
            continueButton.setBackgroundResource(R.drawable.glow_effect)
            // ننقل المستخدم لـ FragmentQ4 حتى لو ساب الحقل فاضي
            findNavController().navigate(R.id.action_fragmentQ3_to_fragmentQ4)
        }

        // لما المستخدم يضغط على Skip
        skipButton.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentQ3_to_fragmentQ4)
        }

    }
}