package com.habiba.habitopia
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class splashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        enableEdgeToEdge()

        Handler(Looper.getMainLooper()).postDelayed({
            val user = FirebaseAuth.getInstance().currentUser

            if (user != null) {
                val intent = Intent(this, MainActivity2::class.java)
                startActivity(intent)
                finish()

            } else {
                val intent = Intent(this,onboardingone::class.java)
                startActivity(intent)
                finish()
            }

        }, 3500) // 3 and half seconds splash duration


    }
}
