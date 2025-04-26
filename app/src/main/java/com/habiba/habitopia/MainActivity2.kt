package com.habiba.habitopia

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.habiba.habitopia.CharactersData.homeViewModel
import com.habiba.habitopia.databinding.ActivityMain2Binding

class MainActivity2 : AppCompatActivity() {
    private lateinit var binding: ActivityMain2Binding
    private lateinit var viewModel: homeViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding=ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[homeViewModel::class.java]

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())

            view.setPadding(
                0,
                systemBars.top,
                0,
                systemBars.bottom
            )

            insets
        }
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController

        val name = intent.getStringExtra("name")
        viewModel.characterName=name


        binding.homeIcon.setOnClickListener {
            setIcons(binding.homeIcon,R.drawable.clickedhome)
            navController.navigate(R.id.home2)

        }
        binding.alarmIcon.setOnClickListener {
            setIcons(binding.alarmIcon, R.drawable.clickedalarm)
            navController.navigate(R.id.alarm)
        }
        binding.addIcon.setOnClickListener {
            setIcons(binding.addIcon,R.drawable.clickedadd)
            navController.navigate(R.id.addtask)

        }
        binding.analysisIcon.setOnClickListener {
            setIcons(binding.analysisIcon,R.drawable.clickedanalysis)
            navController.navigate(R.id.analysis)

        }
        binding.profileIcon.setOnClickListener {
            setIcons(binding.profileIcon,R.drawable.clickeduser)
            navController.navigate(R.id.profile)

        }

    }

    fun setIcons(clickedIcon:ImageView,clickedDirection:Int)
    {
        val IconList= listOf(
            binding.homeIcon to R.drawable.home,
            binding.alarmIcon to R.drawable.alarm,
            binding.addIcon to R.drawable.add,
            binding.analysisIcon to R.drawable.analysis,
            binding.profileIcon to R.drawable.profile,
        )

        IconList.forEach { (iconView, drawableResource) ->
            iconView.setImageResource(drawableResource)
        }

        clickedIcon.setImageResource(clickedDirection)

    }
}