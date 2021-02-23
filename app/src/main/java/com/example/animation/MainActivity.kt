package com.example.animation

import android.animation.ObjectAnimator
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.view.animation.*
import androidx.appcompat.app.AppCompatActivity
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import com.example.animation.R
import com.example.animation.views.FidgetSpinnerView
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val animator = ObjectAnimator.ofFloat(central_spinner, View.ROTATION, 0f, 360f)
        play_button.apply {
            setOnClickListener {
                val animDuration = 600L
                animator.apply {
                    interpolator = FastOutSlowInInterpolator()
                    repeatCount = 0
                    duration = animDuration * 2
                    start()
                }
            }
        }
    }
}