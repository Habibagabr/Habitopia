package com.habiba.habitopia

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.habiba.habitopia.databinding.ActivityMain2Binding
import com.habiba.habitopia.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
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
    }

//    // Function to navigate to ForgotPasswordFragment
//    fun navigateToForgotPassword() {
//        supportFragmentManager.beginTransaction()
//            .replace(R.id.main, ForgotPasswordFragment())
//            .addToBackStack(null) // Add to back stack so user can navigate back
//        .commit()
//        }
}