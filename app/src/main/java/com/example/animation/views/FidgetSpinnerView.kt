package com.example.animation.views

import android.content.Context
import android.content.res.Resources
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.VectorDrawable
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.AlphaAnimation
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.res.ResourcesCompat
import com.example.animation.R
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

class FidgetSpinnerView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private var image: Drawable

    init {
        val attributes: TypedArray =
            context.obtainStyledAttributes(attrs, R.styleable.FidgetSpinner)
        try {
            image = ResourcesCompat.getDrawable(
                context.resources,
                attributes.getResourceId(R.styleable.FidgetSpinner_image, 0),
                null
            )!!
        } finally {
            attributes.recycle()
        }
    }

    private fun getThrowSpinner(drawable: Drawable): Drawable {
        val arrayOfDrawable = arrayOf(drawable)
        return object : LayerDrawable(arrayOfDrawable) {
            override fun draw(canvas: Canvas) {
                canvas.save()
                canvas.translate(spinner.positionX.toFloat(), spinner.positionY.toFloat())
                canvas.rotate(spinner.angle)
                super.draw(canvas)
                canvas.restore()
            }
        }
    }

    private fun randomPositionY() : Int {
        return (75 +
                ((Math.abs(Random.nextInt()) % (this@FidgetSpinnerView.bottom - 151)))
                ) % (this@FidgetSpinnerView.bottom - 75)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        image.setBounds(
            0 - 75,
            0 - 75,
            0 + 75,
            0 + 75
        )
        spinner.increaseAttributes()
        if (spinner.angle >= 360F) {
            spinner.angle = 0F
        }
        if (spinner.positionX == this@FidgetSpinnerView.right + 150) {
            spinner.positionX = this@FidgetSpinnerView.left - 150
            spinner.positionY = randomPositionY()
        }
        getThrowSpinner(image).draw(canvas)
        invalidate()
    }

    class ThrowedSpinner(
        var positionX: Int,
        var positionY: Int,
        var angle: Float
    ) {
        fun increaseAttributes() {
            positionX += 10
            angle += 6F
        }
    }

    private var spinner = ThrowedSpinner(0, 0, 0F)

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        spinner.positionY = randomPositionY()
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        val savedState = state as SavedState
        super.onRestoreInstanceState(savedState.superState)
        spinner = savedState.spinner
    }

    override fun onSaveInstanceState(): Parcelable? {
        return SavedState(
            super.onSaveInstanceState(),
            spinner
        )
    }

    class SavedState : View.BaseSavedState {

        var spinner: ThrowedSpinner

        constructor(
            superState: Parcelable?,
            inputText: ThrowedSpinner
        ) : super(superState) {
            this.spinner = inputText
        }

        constructor(superState: Parcel) : super(superState) {
            this.spinner = ThrowedSpinner(0, 0, 0F)
        }

        override fun writeToParcel(out: Parcel?, flags: Int) {
            super.writeToParcel(out, flags)
            out?.writeInt(spinner.positionX)
            out?.writeInt(spinner.positionY)
            out?.writeFloat(spinner.angle)
        }

        companion object CREATOR : Parcelable.Creator<SavedState?> {
            override fun createFromParcel(source: Parcel): SavedState {
                return SavedState(source)
            }


            override fun newArray(size: Int): Array<SavedState?> {
                return Array(size) { null }
            }

        }
    }
}