package com.habiba.habitopia

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.ImageButton

class onboardingone : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val nextButton: ImageButton = findViewById(R.id.nextButton)

        nextButton.setOnClickListener {
            val intent = Intent(this, onboardingone::class.java)
            startActivity(intent)
        }
    }
}