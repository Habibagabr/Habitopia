package com.habiba.habitopia
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class FragmentQ5 : Fragment() {

    private var selectedFitnessGoal: String? = null // متغير عشان نحفظ فيه الإجابة مؤقتًا
    private var lastSelectedButton: Button? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_q5, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // العناصر الموجودة في الـ XML
        val optionWalk: Button = view.findViewById(R.id.optionWalk)
        val optionGym: Button = view.findViewById(R.id.optionGym)
        val optionYoga: Button = view.findViewById(R.id.optionYoga)
        val optionNoGoal: Button = view.findViewById(R.id.optionNoGoal)
        val continueButton: ImageButton = view.findViewById(R.id.continueButton)
        val skipButton: TextView = view.findViewById(R.id.skipButton)
        val backButton:ImageView=view.findViewById(R.id.backButton)

        // تعريف الألوان كـ ColorStateList
        val defaultColor =
            ContextCompat.getColorStateList(requireContext(), R.color.default_button_color)
        val selectedColor =
            ContextCompat.getColorStateList(requireContext(), R.color.selected_button_color)

        // لما المستخدم يضغط على كل زرار
        optionWalk.setOnClickListener {
            selectedFitnessGoal = "Walk for 30 Minutes"
            lastSelectedButton?.backgroundTintList = defaultColor
            optionWalk.backgroundTintList = selectedColor
            lastSelectedButton = optionWalk
            optionWalk.isSelected = true
            optionGym.isSelected = false
            optionYoga.isSelected = false
            optionNoGoal.isSelected = false
        }

        optionGym.setOnClickListener {
            selectedFitnessGoal = "Go to the gym"
            lastSelectedButton?.backgroundTintList = defaultColor
            optionGym.backgroundTintList = selectedColor
            lastSelectedButton = optionGym
            optionGym.isSelected = true
            optionWalk.isSelected = false
            optionYoga.isSelected = false
            optionNoGoal.isSelected = false
        }

        optionYoga.setOnClickListener {
            selectedFitnessGoal = "Do yoga or stretching"
            lastSelectedButton?.backgroundTintList = defaultColor
            optionYoga.backgroundTintList = selectedColor
            lastSelectedButton = optionYoga
            optionYoga.isSelected = true
            optionWalk.isSelected = false
            optionGym.isSelected = false
            optionNoGoal.isSelected = false
        }
        optionNoGoal.setOnClickListener {
            selectedFitnessGoal = "No fitness goal for now"
            lastSelectedButton?.backgroundTintList = defaultColor
            optionNoGoal.backgroundTintList = selectedColor
            lastSelectedButton = optionNoGoal
            optionNoGoal.isSelected = true
            optionWalk.isSelected = false
            optionGym.isSelected = false
            optionYoga.isSelected = false
        }
        // لما المستخدم يضغط على Continue (Finish)
        continueButton.setOnClickListener {
            // لو المستخدم اختار إجابة، نحفظها في SharedPreferences
            if (selectedFitnessGoal != null) {
                val sharedPref =
                    requireContext().getSharedPreferences("fitness_prefs", Context.MODE_PRIVATE)
                with(sharedPref.edit()) {
                    putString("habitName", "fitness")
                    putString("answer", selectedFitnessGoal)
                    putInt("habitCount", 0)
                    putString("emoji", "🏋️")
                    apply()
                }

            }

            // إضافة الهالة لـ continueButton
            continueButton.setBackgroundResource(R.drawable.glow_effect)
            // نتنقل لـ mainScreenFragment
            findNavController().navigate(R.id.action_fragmentQ5_to_gender)
        }

        // لما المستخدم يضغط على Skip
        skipButton.setOnClickListener{
            findNavController().navigate(R.id.action_fragmentQ5_to_gender)

        }
        backButton.setOnClickListener{
            findNavController().navigate(R.id.action_fragmentQ5_to_fragmentQ4)
        }

    }

}


