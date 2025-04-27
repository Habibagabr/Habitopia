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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.habiba.habitopia.Adapters.TaskAdapter
import com.habiba.habitopia.Adapters.TaskItem
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

        val recyclerView = view.findViewById<RecyclerView>(R.id.tasksRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val taskItems = listOf(
            TaskItem.Header("Monday 1/5/2025"),
            TaskItem.Task("Meeting", "Discuss with client", "10:00 AM", "11:00 AM"),
            TaskItem.Task("Code Review", "Review team's code", "12:00 PM", "1:00 PM"),
            TaskItem.Task("Design Update", "Update app design", "2:00 PM", "3:00 PM"),

            TaskItem.Header("Tuesday 2/5/2025"),
            TaskItem.Task("Presentation", "Show project progress", "9:00 AM", "10:30 AM"),
            TaskItem.Task("Team Standup", "Daily team meeting", "11:00 AM", "11:30 AM"),
            TaskItem.Task("Client Feedback", "Review feedback notes", "1:00 PM", "2:00 PM"),

            TaskItem.Header("Wednesday 3/5/2025"),
            TaskItem.Task("Bug Fixing", "Fix reported issues", "10:00 AM", "12:00 PM"),
            TaskItem.Task("Research", "Explore new technologies", "1:00 PM", "2:30 PM"),
            TaskItem.Task("Documentation", "Update project docs", "3:00 PM", "4:00 PM"),

            TaskItem.Header("Thursday 4/5/2025"),
            TaskItem.Task("Deployment", "Deploy new version", "9:00 AM", "11:00 AM"),
            TaskItem.Task("Testing", "Run full test cases", "12:00 PM", "2:00 PM"),
            TaskItem.Task("Team Meeting", "Plan next sprint", "3:00 PM", "4:00 PM")
        )


        val adapter = TaskAdapter(taskItems)
        recyclerView.adapter = adapter



    }


    }
