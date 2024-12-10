package com.cs407.offthephone

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class Logo : AppCompatActivity() {

    private lateinit var database: TaskDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_logo)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val logo = findViewById<ImageView>(R.id.logoImage)
        lifecycleScope.launch {
            delay(1500)
            playPopOutAnimation(logo)

           // startActivity(Intent(this@Logo, Homepage::class.java))

        }
    }
    private fun playPopOutAnimation(logo: ImageView) {
        // Scale up for the pop out effect
        val scaleUpX = ObjectAnimator.ofFloat(logo, "scaleX", 1f, 1.5f)
        val scaleUpY = ObjectAnimator.ofFloat(logo, "scaleY", 1f, 1.5f)

        // Scale down animation for the pop in effect
        val scaleDownX = ObjectAnimator.ofFloat(logo, "scaleX", 1.5f, 1f)
        val scaleDownY = ObjectAnimator.ofFloat(logo, "scaleY", 1.5f, 1f)

        // Set durations in ms for each animation
        scaleUpX.duration = 300
        scaleUpY.duration = 300
        scaleDownX.duration = 300
        scaleDownY.duration = 300

        // Create the sequence
        val animatorSet = AnimatorSet()
        animatorSet.play(scaleUpX).with(scaleUpY)
        animatorSet.play(scaleDownX).with(scaleDownY).after(scaleUpX)

        animatorSet.start() // Start the animation

        // Call onComplete after animation finishes leave other methods default
        animatorSet.addListener(object : android.animation.Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
                TODO("Not yet implemented")
            }

            override fun onAnimationEnd(animation: Animator) {
                startActivity(Intent(this@Logo, Homepage::class.java))
            }

            override fun onAnimationCancel(animation: Animator) {
                TODO("Not yet implemented")
            }

            override fun onAnimationRepeat(animation: Animator) {
                TODO("Not yet implemented")
            }
        })
    }
}
