package com.habiba.habitopia

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class FragmentQ2 : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_q2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // العناصر الموجودة في الـ XML
        val pagesInput: EditText = view.findViewById(R.id.pagesInput)
        val continueButton: ImageButton= view.findViewById(R.id.continueButton)
        val skipButton: TextView = view.findViewById(R.id.skipButton)
        val preButton:ImageView=view.findViewById(R.id.backButton)



        continueButton.setOnClickListener {

            val sharedPref = requireContext().getSharedPreferences("reading_prefs", Context.MODE_PRIVATE)
            with(sharedPref.edit()) {
                putString("habitName", "reading")
                putString("answer", null) // yes أو no
                putInt("habitCount", pagesInput.text.toString().toIntOrNull() ?: 0)
                putString("emoji", "📚")
                apply()
            }

            // إضافة الهالة لـ continueButton
            continueButton.setBackgroundResource(R.drawable.glow_effect)

            // الانتقال إلى Q3
            findNavController().navigate(R.id.action_fragmentQ2_to_fragmentQ3)
        }

        // لما المستخدم يضغط على Skip
        skipButton.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentQ2_to_character)
        }

    }
}
